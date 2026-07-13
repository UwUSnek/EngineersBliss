#!/usr/bin/env python3

import json
import subprocess
from pathlib import Path

import numpy as np

SOURCE_SIZE = 1080          # Source video is 1080x1080
COMPARE_SIZE = 256          # Frames are downscaled to this for comparison
MIN_SECONDS = 1.0           # Ignore candidate frames before this many seconds in
INDIR  = "raw"
OUTDIR = "trimmed"
SUFFIX = ""


def run(cmd):
    return subprocess.run(cmd, check=True, capture_output=True)


def ffprobe_fps(path: Path) -> float:
    cmd = [
        "ffprobe", "-v", "error", "-select_streams", "v:0",
        "-show_entries", "stream=r_frame_rate",
        "-of", "json", str(path),
    ]
    data = json.loads(run(cmd).stdout)
    num, den = data["streams"][0]["r_frame_rate"].split("/")
    return float(num) / float(den)


def decode_small_frames(path: Path, size: int) -> np.ndarray:
    cmd = [
        "ffmpeg", "-v", "error", "-i", str(path),
        "-vf", f"scale={ size }:{ size }",
        "-pix_fmt", "rgb24",
        "-f", "rawvideo", "-",
    ]
    raw = subprocess.run(cmd, check=True, capture_output=True).stdout
    frame_size = size * size * 3
    n = len(raw) // frame_size
    if n == 0:
        return np.zeros((0, size, size, 3), dtype=np.uint8)
    arr = np.frombuffer(raw[: n * frame_size], dtype=np.uint8)
    return arr.reshape(n, size, size, 3)


def best_loop_frame(frames: np.ndarray, min_frame: int):
    first = frames[0].astype(np.float32)
    candidates = frames[min_frame:].astype(np.float32)
    diff = candidates - first
    mse = np.mean(diff * diff, axis=(1, 2, 3))
    rel_idx = int(np.argmin(mse))
    return min_frame + rel_idx, float(mse[rel_idx])


def cut_video(path: Path, out_path: Path, n_frames: int):
    cmd = [
        "ffmpeg", "-y", "-v", "error", "-i", str(path),
        "-frames:v", str(n_frames),
        "-an",
        "-c:v", "copy",
        str(out_path),
    ]
    run(cmd)


def main():
    cwd = Path(".")
    input_dir = cwd / INDIR
    mov_files = sorted({*input_dir.glob("*.mov"), *input_dir.glob("*.MOV")})

    if not mov_files:
        print("No .mov files found in current directory.")
        return

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)

    for path in mov_files:
        print(f"\n{ path.name }")
        try:
            fps = ffprobe_fps(path)
        except Exception as e:
            print(f"  ffprobe failed, skipping: { e }")
            continue

        try:
            frames = decode_small_frames(path, COMPARE_SIZE)
        except Exception as e:
            print(f"  decode failed, skipping: { e }")
            continue

        n = frames.shape[0]
        if n < 3:
            print(f"  Only {n} frames decoded, skipping.")
            continue

        min_frame = max(1, int(round(MIN_SECONDS * fps)))
        if min_frame >= n:
            min_frame = 1

        idx, score = best_loop_frame(frames, min_frame)
        t = idx / fps
        print(f"  fps={fps:.3f}  frames={ n }  best_match=frame { idx } ({ t:.3f }s)  mse={ score:.2f }")

        out_path = outdir / f"{ path.stem }{ SUFFIX }{ path.suffix }"
        try:
            cut_video(path, out_path, idx)
            print(f"  wrote { out_path }")
        except subprocess.CalledProcessError as e:
            print(f"  ffmpeg cut failed: { e.stderr.decode(errors='ignore') }")


if __name__ == "__main__":
    main()
