import json
import subprocess
import shutil
from pathlib import Path

import numpy as np
from PIL import Image

SOURCE_SIZE = 1080
INDIR = "trimmed"
OUTDIR = "converted"
TMPDIR = "converted/_tmp"

MAX_ATLAS_DIM = 16384
QUALITY = 30          # avif CRF, lower = better quality/bigger file

KEY_COLOR = np.array([0, 255, 0], dtype=np.int16)
KEY_THRESHOLD = 90


def run(cmd):
    return subprocess.run(cmd, check=True, capture_output=True)


def ffprobe_fps(path: Path) -> int:
    cmd = [
        "ffprobe", "-v", "error", "-select_streams", "v:0",
        "-show_entries", "stream=r_frame_rate",
        "-of", "json", str(path),
    ]
    data = json.loads(run(cmd).stdout)
    num, den = data["streams"][0]["r_frame_rate"].split("/")
    return round(float(num) / float(den))


def decode_frames(path: Path) -> np.ndarray:
    cmd = [
        "ffmpeg", "-v", "error", "-i", str(path),
        "-vf", f"scale={ SOURCE_SIZE }:{ SOURCE_SIZE }",
        "-pix_fmt", "rgb24",
        "-f", "rawvideo", "-",
    ]
    raw = subprocess.run(cmd, check=True, capture_output=True).stdout
    frame_size = SOURCE_SIZE * SOURCE_SIZE * 3
    n = len(raw) // frame_size
    if n == 0:
        return np.zeros((0, SOURCE_SIZE, SOURCE_SIZE, 3), dtype=np.uint8)
    arr = np.frombuffer(raw[: n * frame_size], dtype=np.uint8)
    return arr.reshape(n, SOURCE_SIZE, SOURCE_SIZE, 3).copy()


def key_out_green(frames: np.ndarray) -> np.ndarray:
    n = frames.shape[0]
    rgba = np.empty((n, SOURCE_SIZE, SOURCE_SIZE, 4), dtype=np.uint8)
    rgba[..., :3] = frames
    diff = frames.astype(np.int32) - KEY_COLOR
    dist = np.sqrt(np.sum(diff * diff, axis=-1))
    alpha = np.where(dist < KEY_THRESHOLD, 0, 255).astype(np.uint8)
    rgba[..., 3] = alpha
    return rgba


def atlas_capacity():
    per_side = MAX_ATLAS_DIM // SOURCE_SIZE
    return per_side, per_side * per_side


def build_atlas(frames_chunk: np.ndarray, cols: int) -> Image.Image:
    n = frames_chunk.shape[0]
    rows = (n + cols - 1) // cols
    canvas = np.zeros((rows * SOURCE_SIZE, cols * SOURCE_SIZE, 4), dtype=np.uint8)
    for i in range(n):
        r, c = divmod(i, cols)
        y, x = r * SOURCE_SIZE, c * SOURCE_SIZE
        canvas[y:y + SOURCE_SIZE, x:x + SOURCE_SIZE] = frames_chunk[i]
    return Image.fromarray(canvas, mode="RGBA")

def write_mcmeta(avif_path: Path, cols: int, rows: int, frame_count: int, fps: int) -> dict:
    meta = {
        "avif_atlas": {
            "atlas_cols": 1,
            "atlas_rows": 1,
            "sheet_width": cols * SOURCE_SIZE,
            "sheet_height": rows * SOURCE_SIZE,
            "frame_width": SOURCE_SIZE,
            "frame_height": SOURCE_SIZE,
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

    cols, capacity = atlas_capacity()

    for path in mov_files:
        print(f"\n{ path.name }")
        try:
            fps = ffprobe_fps(path)
        except Exception as e:
            print(f"  ffprobe failed, skipping: { e }")
            continue

        try:
            frames = decode_frames(path)
        except Exception as e:
            print(f"  decode failed, skipping: { e }")
            continue

        n = frames.shape[0]
        if n == 0:
            print("  no frames decoded, skipping.")
            continue

        rgba = key_out_green(frames)

        for chunk_idx, start in enumerate(range(0, n, capacity)):
            chunk = rgba[start:start + capacity]
            chunk_rows = (chunk.shape[0] + cols - 1) // cols
            atlas = build_atlas(chunk, cols)

            png_path = tmpdir / f"{ path.stem }_{ chunk_idx }.png"
            avif_path = outdir / f"{ path.stem }_{ chunk_idx }.avif"

            atlas.save(png_path)
            try:
                encode_avif(png_path, avif_path)
                meta = write_mcmeta(avif_path, cols, chunk_rows, chunk.shape[0], fps)
                print(f"  wrote { avif_path } ({ chunk.shape[0] } frames, { cols } cols)")
                print(f"  mcmeta: { json.dumps(meta['avif_atlas']) }")
            except subprocess.CalledProcessError as e:
                print(f"  avif encode failed: { e.stderr.decode(errors='ignore') }")
            finally:
                png_path.unlink(missing_ok=True)

    shutil.rmtree(tmpdir, ignore_errors=True)


if __name__ == "__main__":
    main()
