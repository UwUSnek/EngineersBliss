import json
import os
import subprocess
from concurrent.futures import ProcessPoolExecutor, as_completed
from pathlib import Path

import numpy as np

COMPARE_SIZE = 256          # Frames are downscaled to this for comparison
MIN_LOOP_SECONDS = 1.0      # Loop segment must be at least this long
INDIR  = "0_raw"
OUTDIR = "1_trimmed"
SUFFIX = ""
MAX_WORKERS = max(1, (os.cpu_count() or 4) - 1)


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
        "ffmpeg", "-v", "error", "-nostdin", "-i", str(path),
        "-vf", f"scale={size}:{size}",
        "-pix_fmt", "rgba",
        "-f", "rawvideo", "-",
    ]
    raw = subprocess.run(cmd, check=True, capture_output=True).stdout
    frame_size = size * size * 4
    n = len(raw) // frame_size
    if n == 0:
        return np.zeros((0, size, size, 4), dtype=np.uint8)
    arr = np.frombuffer(raw[: n * frame_size], dtype=np.uint8)
    return arr.reshape(n, size, size, 4)


def best_loop_segment(frames: np.ndarray, min_gap: int):
    """
    Find the pair of frames with start < end - min_gap that minimizes MSE between them
    ||a - b||^2 = ||a||^2 + ||b||^2 - 2 a.b
    """
    n = frames.shape[0]
    f = frames.reshape(n, -1).astype(np.float32)  # (n, pixels)
    pixels = f.shape[1]

    norms = np.einsum("ij,ij->i", f, f)           # (n,)
    dots = f @ f.T                                 # (n, n) single BLAS matmul
    mse = (norms[:, None] + norms[None, :] - 2.0 * dots) / pixels

    idx = np.arange(n)
    invalid = (idx[None, :] - idx[:, None]) < min_gap
    mse[invalid] = np.inf

    best_start, best_end = np.unravel_index(np.argmin(mse), mse.shape)
    best_score = float(mse[best_start, best_end])
    return int(best_start), int(best_end), best_score


def cut_video(path: Path, out_path: Path, start_frame: int, end_frame: int, fps: float):
    start_t = start_frame / fps
    duration = (end_frame - start_frame) / fps
    cmd = [
        "ffmpeg", "-y", "-v", "error", "-nostdin",
        "-ss", f"{start_t:.6f}",
        "-i", str(path),
        "-t", f"{duration:.6f}",
        "-an",
        "-c:v", "prores_ks", "-profile:v", "4444",
        "-pix_fmt", "yuva444p10le",
        "-alpha_bits", "16",
        "-vendor", "apl0",
        "-threads", "0",  # Multithreading
        str(out_path),
    ]
    run(cmd)


def process_file(path: Path, outdir: Path):
    log = [f"\n{path.name}"]
    try:
        fps = ffprobe_fps(path)
    except Exception as e:
        log.append(f"  ffprobe failed, skipping: {e}")
        return "\n".join(log)

    try:
        frames = decode_small_frames(path, COMPARE_SIZE)
    except Exception as e:
        log.append(f"  decode failed, skipping: {e}")
        return "\n".join(log)

    n = frames.shape[0]
    min_gap = max(1, int(round(MIN_LOOP_SECONDS * fps)))
    if n < min_gap + 2:
        log.append(f"  Only {n} frames decoded, need at least {min_gap + 2}, skipping.")
        return "\n".join(log)

    start, end, score = best_loop_segment(frames, min_gap)
    t0, t1 = start / fps, end / fps
    log.append(f"  fps={fps:.3f}  frames={ n }  best_loop=[{ start }:{ end }] "
               f"({t0:.3f}s-{t1:.3f}s, {t1-t0:.3f}s)  mse={score:.2f}")

    out_path = outdir / f"{ path.stem }{ SUFFIX }{ path.suffix }"
    try:
        cut_video(path, out_path, start, end, fps)
        log.append(f"  wrote {out_path}")
    except subprocess.CalledProcessError as e:
        log.append(f"  ffmpeg cut failed: {e.stderr.decode(errors='ignore')}")

    return "\n".join(log)


def main():
    cwd = Path(".")
    input_dir = cwd / INDIR
    mov_files = sorted({*input_dir.glob("*.mov"), *input_dir.glob("*.MOV")})

    if not mov_files:
        print("No .mov files found in current directory.")
        return

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)

    with ProcessPoolExecutor(max_workers=MAX_WORKERS) as ex:
        futures = { ex.submit(process_file, path, outdir): path for path in mov_files }
        for fut in as_completed(futures):
            print(fut.result())


if __name__ == "__main__":
    main()