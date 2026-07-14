import subprocess
from concurrent.futures import ProcessPoolExecutor, as_completed
from pathlib import Path
import shutil
import numpy as np

from utils import MAX_WORKERS, decode_rawvideo, ffprobe_info, find_input_files, run

COMPARE_SIZE = 256          # Frames are downscaled to this for comparison
MIN_LOOP_SECONDS = 1.0      # Loop segment must be at least this long
INDIR  = "0_raw"
OUTDIR = "1_trimmed"


TARGET_FILES = [
    "creative_tweaks/disable_fire_effect_off.mov",
    "creative_tweaks/disable_fire_effect_on.mov",
    "creative_tweaks/disable_bubble_column_drag_on.mov",
    "creative_tweaks/disable_bubble_column_drag_off.mov",
]






def ffprobe_fps(path: Path) -> float:
    return ffprobe_info(path)["fps"]




def decode_small_frames(path: Path, size: int) -> np.ndarray:
    # Downscale to a fixed square for cheap frame comparison.
    # This doesnt affect output resolution
    return decode_rawvideo(path, size, size, scale=f"scale={ size }:{ size }")



def copy_no_changes(path: Path, input_dir: Path, outdir: Path) -> str:
    rel = path.relative_to(input_dir)
    out_path = outdir / rel
    out_path.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(path, out_path)
    return f"copied without changes: {rel}"



def best_loop_segment(frames: np.ndarray, min_gap: int):
    """
    Find the pair of frames with start < end - min_gap that minimizes MSE between them
    ||a - b||^2 = ||a||^2 + ||b||^2 - 2 a.b
    """
    n = frames.shape[0]
    f = frames.reshape(n, -1).astype(np.float32)  # (n, pixels)
    pixels = f.shape[1]

    norms = np.einsum("ij,ij->i", f, f)           # (n,)
    dots = f @ f.T                                # (n, n) single BLAS matmul
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
        "-threads", "0",  # Enable multithreading
        str(out_path),
    ]
    run(cmd)




def process_file(path: Path, input_dir: Path, outdir: Path):
    rel = path.relative_to(input_dir)
    log = [ f"\n{ rel }" ]
    try:
        fps = ffprobe_fps(path)
    except Exception as e:
        log.append(f"  ffprobe failed, skipping: { e }")
        return "\n".join(log)

    try:
        frames = decode_small_frames(path, COMPARE_SIZE)
    except Exception as e:
        log.append(f"  decode failed, skipping: { e }")
        return "\n".join(log)

    n = frames.shape[0]
    min_gap = max(1, int(round(MIN_LOOP_SECONDS * fps)))
    if n < min_gap + 2:
        log.append(f"  Only { n } frames decoded, need at least { min_gap + 2 }, skipping.")
        return "\n".join(log)

    start, end, score = best_loop_segment(frames, min_gap)
    t0, t1 = start / fps, end / fps
    log.append(f"  fps={fps:.3f}  frames={ n }  best_loop=[{ start }:{ end }] "
               f"({t0:.3f}s-{t1:.3f}s, {t1-t0:.3f}s)  mse={score:.2f}")

    out_path = outdir / rel.parent / f"{ path.stem }{ path.suffix }"
    out_path.parent.mkdir(parents=True, exist_ok=True)
    try:
        cut_video(path, out_path, start, end, fps)
        log.append(f"  wrote {out_path}")
    except subprocess.CalledProcessError as e:
        log.append(f"  ffmpeg cut failed: { e.stderr.decode(errors='ignore') }")

    return "\n".join(log)



def main():
    cwd = Path(".")
    input_dir = cwd / INDIR

    mov_files = [input_dir / rel for rel in TARGET_FILES]
    missing = [p for p in mov_files if not p.exists()]
    if missing:
        for p in missing:
            print(f"Missing: {p}")
        mov_files = [p for p in mov_files if p.exists()]

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)

    all_files = find_input_files(input_dir)
    target_set = {p.resolve() for p in mov_files}
    other_files = [p for p in all_files if p.resolve() not in target_set]

    for path in other_files:
        print(copy_no_changes(path, input_dir, outdir))

    if not mov_files:
        print("No valid files found in TARGET_FILES.")
        return

    with ProcessPoolExecutor(max_workers=MAX_WORKERS) as ex:
        futures = { ex.submit(process_file, path, input_dir, outdir): path for path in mov_files }
        for fut in as_completed(futures):
            print(fut.result())


if __name__ == "__main__":
    main()