import json
import subprocess
import shutil
from pathlib import Path

import numpy as np
from PIL import Image

TARGET_RESOLUTION = 480
INDIR = "trimmed"
OUTDIR = "converted"
TMPDIR = "converted/_tmp"

MAX_ATLAS_DIM = 16384
QUALITY = 16          # avif CRF, lower = better quality/bigger file
TARGET_FPS = 12

KEY_COLOR = np.array([0, 255, 0], dtype=np.int16)
KEY_THRESHOLD_LOW = 60
KEY_THRESHOLD_HIGH = 120


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
        "ffmpeg", "-v", "error", "-i", str(path),
        "-pix_fmt", "rgb24",
        "-f", "rawvideo", "-",
    ]
    raw = subprocess.run(cmd, check=True, capture_output=True).stdout
    frame_size = width * height * 3
    n = len(raw) // frame_size
    if n == 0:
        return np.zeros((0, height, width, 3), dtype=np.uint8)
    arr = np.frombuffer(raw[: n * frame_size], dtype=np.uint8)
    return arr.reshape(n, height, width, 3).copy()


def resample_to_target_fps(frames: np.ndarray, src_fps: int, target_fps: int) -> np.ndarray:
    n = frames.shape[0]
    if n == 0 or src_fps == target_fps:
        return frames
    duration = n / src_fps
    target_n = max(1, round(duration * target_fps))
    idx = np.linspace(0, n - 1, target_n).round().astype(int)
    return frames[idx]


def key_out_green(frames: np.ndarray) -> np.ndarray:
    n, h, w = frames.shape[:3]
    f = frames.astype(np.float32)
    r, g, b = f[..., 0], f[..., 1], f[..., 2]

    diff = f - KEY_COLOR
    dist = np.sqrt(np.sum(diff * diff, axis=-1))

    alpha = np.clip(
        (dist - KEY_THRESHOLD_LOW) / (KEY_THRESHOLD_HIGH - KEY_THRESHOLD_LOW),
        0.0, 1.0,
    )

    spill = np.clip(g - np.maximum(r, b), 0, None)
    g_fixed = g - spill * (1.0 - alpha)

    rgba = np.empty((n, h, w, 4), dtype=np.uint8)
    rgba[..., 0] = np.clip(r, 0, 255).astype(np.uint8)
    rgba[..., 1] = np.clip(g_fixed, 0, 255).astype(np.uint8)
    rgba[..., 2] = np.clip(b, 0, 255).astype(np.uint8)
    rgba[..., 3] = np.clip(alpha * 255, 0, 255).astype(np.uint8)
    return rgba


def resize_premultiplied(rgba: np.ndarray, size: int) -> np.ndarray:

    # Scaling before keying blends green into the edges so keying must be done first
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

        alpha_safe = np.where(alpha_r > 1.0, alpha_r, 255.0)  # avoid div-by-~0
        rgb_out = np.clip(premult_r * 255.0 / alpha_safe[..., None], 0, 255)

        out[i, ..., :3] = rgb_out.astype(np.uint8)
        out[i, ..., 3] = alpha_r.astype(np.uint8)

    return out


def atlas_capacity():
    per_side = MAX_ATLAS_DIM // TARGET_RESOLUTION
    return per_side, per_side * per_side


def grid_for(n: int, max_cols: int) -> int:
    """Pick a near-square column count for n frames, capped at max_cols."""
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

    for path in mov_files:
        print(f"\n{ path.name }")
        try:
            src_fps, width, height = ffprobe_info(path)
        except Exception as e:
            print(f"  ffprobe failed, skipping: { e }")
            continue

        try:
            frames = decode_frames(path, width, height)
        except Exception as e:
            print(f"  decode failed, skipping: { e }")
            continue

        n = frames.shape[0]
        if n == 0:
            print("  no frames decoded, skipping.")
            continue

        frames = resample_to_target_fps(frames, src_fps, TARGET_FPS)
        if frames.shape[0] != n:
            print(f"  resampled { n } -> { frames.shape[0] } frames ({ src_fps } -> { TARGET_FPS } fps)")
        n = frames.shape[0]

        rgba = key_out_green(frames)                          # key at original resolution
        rgba = resize_premultiplied(rgba, TARGET_RESOLUTION)   # downscale

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
                print(f"  wrote { avif_path } ({ chunk.shape[0] } frames, { cols } cols)")
                print(f"  mcmeta: { json.dumps(meta['avif_atlas']) }")
            except subprocess.CalledProcessError as e:
                print(f"  avif encode failed: { e.stderr.decode(errors='ignore') }")
            finally:
                png_path.unlink(missing_ok=True)

    shutil.rmtree(tmpdir, ignore_errors=True)


if __name__ == "__main__":
    main()