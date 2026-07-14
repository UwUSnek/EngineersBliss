import json
import os
import subprocess
import shutil
from concurrent.futures import ProcessPoolExecutor, as_completed
from pathlib import Path

import numpy as np
from PIL import Image

TARGET_RESOLUTION = 480
INDIR = "2_even"
OUTDIR = "3_converted"
TMPDIR = "3_converted/_tmp"

MAX_ATLAS_DIM = 16384
QUALITY = 16          # avif CRF, lower = better quality/bigger file
TARGET_FPS = 12
MAX_WORKERS = max(1, (os.cpu_count() or 4) - 1)


def run(cmd):
    return subprocess.run(cmd, check=True, capture_output=True)


def ffprobe_info(path: Path):
    cmd = [
        "ffprobe", "-v", "error", "-select_streams", "v:0",
        "-show_entries", "stream=r_frame_rate,width,height",
        "-of", "json", str(path),
    ]
    data = json.loads(run(cmd).stdout)
    stream = data["streams"][0]
    num, den = stream["r_frame_rate"].split("/")
    fps = round(float(num) / float(den))
    return fps, stream["width"], stream["height"]


def decode_frames(path: Path, width: int, height: int) -> np.ndarray:
    cmd = [
        "ffmpeg", "-v", "error", "-nostdin", "-i", str(path),
        "-pix_fmt", "rgba",
        "-f", "rawvideo", "-",
    ]
    raw = subprocess.run(cmd, check=True, capture_output=True).stdout
    frame_size = width * height * 4
    n = len(raw) // frame_size
    if n == 0:
        return np.zeros((0, height, width, 4), dtype=np.uint8)
    arr = np.frombuffer(raw[: n * frame_size], dtype=np.uint8)
    return arr.reshape(n, height, width, 4).copy()


def resample_to_target_fps(frames: np.ndarray, src_fps: int, target_fps: int) -> np.ndarray:
    n = frames.shape[0]
    if n == 0 or src_fps == target_fps:
        return frames
    duration = n / src_fps
    target_n = max(1, round(duration * target_fps))
    idx = np.linspace(0, n - 1, target_n).round().astype(int)
    return frames[idx]


def resize_premultiplied(rgba: np.ndarray, size: int) -> np.ndarray:
    n = rgba.shape[0]
    out = np.empty((n, size, size, 4), dtype=np.uint8)
    rgb = rgba[..., :3].astype(np.float32)
    a = rgba[..., 3:4].astype(np.float32) / 255.0
    premult = rgb * a

    for i in range(n):
        premult_img = Image.fromarray(np.clip(premult[i], 0, 255).astype(np.uint8))
        alpha_img = Image.fromarray(rgba[i, ..., 3])

        premult_r = np.asarray(
            premult_img.resize((size, size), Image.LANCZOS)
        ).astype(np.float32)
        alpha_r = np.asarray(
            alpha_img.resize((size, size), Image.LANCZOS)
        ).astype(np.float32)

        alpha_safe = np.where(alpha_r > 1.0, alpha_r, 255.0)  # avoid div by ~0
        rgb_out = np.clip(premult_r * 255.0 / alpha_safe[..., None], 0, 255)

        out[i, ..., :3] = rgb_out.astype(np.uint8)
        out[i, ..., 3] = alpha_r.astype(np.uint8)

    return out


def atlas_capacity():
    per_side = MAX_ATLAS_DIM // TARGET_RESOLUTION
    return per_side, per_side * per_side


def grid_for(n: int, max_cols: int) -> int:
    cols = min(max_cols, max(1, int(np.ceil(np.sqrt(n)))))
    return cols


def build_atlas(frames_chunk: np.ndarray, cols: int) -> Image.Image:
    n = frames_chunk.shape[0]
    rows = (n + cols - 1) // cols
    canvas = np.zeros((rows * TARGET_RESOLUTION, cols * TARGET_RESOLUTION, 4), dtype=np.uint8)
    for i in range(n):
        r, c = divmod(i, cols)
        y, x = r * TARGET_RESOLUTION, c * TARGET_RESOLUTION
        canvas[y:y + TARGET_RESOLUTION, x:x + TARGET_RESOLUTION] = frames_chunk[i]
    return Image.fromarray(canvas, mode="RGBA")


def write_mcmeta(avif_path: Path, cols: int, rows: int, frame_count: int, fps: int) -> dict:
    meta = {
        "avif_atlas": {
            "atlas_cols": 1,
            "atlas_rows": 1,
            "sheet_width": cols * TARGET_RESOLUTION,
            "sheet_height": rows * TARGET_RESOLUTION,
            "frame_width": TARGET_RESOLUTION,
            "frame_height": TARGET_RESOLUTION,
            "frame_count": frame_count,
            "fps": fps,
        }
    }
    mcmeta_path = avif_path.with_suffix(avif_path.suffix + ".mcmeta")
    mcmeta_path.write_text(json.dumps(meta))
    return meta


def encode_avif(png_path: Path, avif_path: Path):
    cmd = [
        "avifenc",
        "--min", str(QUALITY), "--max", str(QUALITY),
        "--speed", "4",
        "--yuv", "444",
        str(png_path), str(avif_path),
    ]
    run(cmd)


def process_file(path: Path, outdir: Path, tmpdir: Path, max_cols: int, capacity: int):
    log = [f"\n{ path.name }"]
    try:
        src_fps, width, height = ffprobe_info(path)
    except Exception as e:
        log.append(f"  ffprobe failed, skipping: { e }")
        return "\n".join(log)

    try:
        frames = decode_frames(path, width, height)
    except Exception as e:
        log.append(f"  decode failed, skipping: { e }")
        return "\n".join(log)

    n = frames.shape[0]
    if n == 0:
        log.append("  no frames decoded, skipping.")
        return "\n".join(log)

    frames = resample_to_target_fps(frames, src_fps, TARGET_FPS)
    if frames.shape[0] != n:
        log.append(f"  resampled { n } -> { frames.shape[0] } frames ({ src_fps } -> { TARGET_FPS } fps)")
    n = frames.shape[0]

    rgba = resize_premultiplied(frames, TARGET_RESOLUTION)   # downscale, alpha from source

    for chunk_idx, start in enumerate(range(0, n, capacity)):
        chunk = rgba[start:start + capacity]
        cols = grid_for(chunk.shape[0], max_cols)
        chunk_rows = (chunk.shape[0] + cols - 1) // cols
        atlas = build_atlas(chunk, cols)

        png_path = tmpdir / f"{ path.stem }_{ chunk_idx }.png"
        avif_path = outdir / f"{ path.stem }_{ chunk_idx }.avif"

        atlas.save(png_path)
        try:
            encode_avif(png_path, avif_path)
            meta = write_mcmeta(avif_path, cols, chunk_rows, chunk.shape[0], TARGET_FPS)
            log.append(f"  wrote { avif_path } ({ chunk.shape[0] } frames, { cols } cols)")
            log.append(f"  mcmeta: { json.dumps(meta['avif_atlas']) }")
        except subprocess.CalledProcessError as e:
            log.append(f"  avif encode failed: { e.stderr.decode(errors='ignore') }")
        finally:
            png_path.unlink(missing_ok=True)

    return "\n".join(log)


def main():
    input_dir = Path(INDIR)
    mov_files = sorted({ *input_dir.glob("*.mov"), *input_dir.glob("*.MOV") })

    if not mov_files:
        print("No .mov files found in trimmed/.")
        return

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)
    tmpdir = Path(TMPDIR)
    tmpdir.mkdir(parents=True, exist_ok=True)

    max_cols, capacity = atlas_capacity()

    with ProcessPoolExecutor(max_workers=MAX_WORKERS) as ex:
        futures = {
            ex.submit(process_file, path, outdir, tmpdir, max_cols, capacity): path
            for path in mov_files
        }
        for fut in as_completed(futures):
            print(fut.result())

    shutil.rmtree(tmpdir, ignore_errors=True)


if __name__ == "__main__":
    main()