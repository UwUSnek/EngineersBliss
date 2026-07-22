import subprocess
from concurrent.futures import ProcessPoolExecutor, as_completed
from pathlib import Path
import numpy as np

from utils import MAX_WORKERS, decode_rawvideo, ffprobe_info, find_input_files, run, target_dimensions


COMPARE_SIZE = 256          # Frames are downscaled to this for comparison
MIN_LOOP_SECONDS = 1.0      # Loop segment must be at least this long
INDIR  = "0_raw"
OUTDIR = "1_trimmed"

# Size of the longer edge of the output frame
TARGET_RESOLUTION = 1080
# Output FPS
#! 12fps uses about ~20% less space but it looks very choppy. Not worth it. 24fps is smooth enough and it loads just as fast.
TARGET_FPS = 24

# ffmpeg scale filter flags
SCALE_FLAGS = "flags=lanczos+accurate_rnd+full_chroma_int"


# Pairs of files that must be trimmed to loops of the same duration
# Everything else is simply resized, resampled, and copied normally.
LOOP_TARGET_PAIRS = [
    (
        "creative_tweaks/disable_fire_effect_off.mov",
        "creative_tweaks/disable_fire_effect_on.mov"
    ),
    (
        "creative_tweaks/disable_bubble_column_drag_on.mov",
        "creative_tweaks/disable_bubble_column_drag_off.mov"
    ),
]




def decode_small_frames(path: Path, size: int) -> np.ndarray:
    # Downscale to a fixed square for cheap frame comparison.
    # This doesnt affect output resolution
    return decode_rawvideo(path, size, size, scale=f"scale={ size }:{ size }")



def compute_mse_matrix(frames: np.ndarray) -> np.ndarray:
    """||a - b||^2 = ||a||^2 + ||b||^2 - 2 a.b"""
    n = frames.shape[0]
    f = frames.reshape(n, -1).astype(np.float32)  # (n, pixels)
    pixels = f.shape[1]

    norms = np.einsum("ij,ij->i", f, f)           # (n,)
    dots = f @ f.T                                # (n, n) single BLAS matmul
    mse = (norms[:, None] + norms[None, :] - 2.0 * dots) / pixels
    return mse



def best_segment_for_gap(mse: np.ndarray, gap: int):
    n = mse.shape[0]
    if gap <= 0 or gap >= n:
        return None
    ends = np.arange(gap, n)
    starts = ends - gap
    scores = mse[starts, ends]
    i = int(np.argmin(scores))
    return int(starts[i]), int(ends[i]), float(scores[i])



def best_loop_segment(mse: np.ndarray, min_gap: int):
    n = mse.shape[0]
    idx = np.arange(n)
    invalid = (idx[None, :] - idx[:, None]) < min_gap
    masked = np.where(invalid, np.inf, mse)
    best_start, best_end = np.unravel_index(np.argmin(masked), masked.shape)
    best_score = float(masked[best_start, best_end])
    return int(best_start), int(best_end), best_score



def joint_loop_segments(mse_a: np.ndarray, fps_a: float, mse_b: np.ndarray, fps_b: float, min_seconds: float):
    n_a, n_b = mse_a.shape[0], mse_b.shape[0]
    if n_a < 2 or n_b < 2:
        return None  # nothing usable to compare in at least one clip

    max_duration_sec = min((n_a - 1) / fps_a, (n_b - 1) / fps_b)
    effective_min_seconds = min(min_seconds, max_duration_sec)

    min_gap_a = min(max(1, int(round(effective_min_seconds * fps_a))), n_a - 1)
    min_gap_b = min(max(1, int(round(effective_min_seconds * fps_b))), n_b - 1)

    if fps_a >= fps_b:
        ref_fps, ref_min_gap = fps_a, min_gap_a
    else:
        ref_fps, ref_min_gap = fps_b, min_gap_b

    max_ref_gap = max(ref_min_gap, int(np.floor(max_duration_sec * ref_fps)))

    best = None
    for gap_ref in range(ref_min_gap, max_ref_gap + 1):
        duration_sec = gap_ref / ref_fps
        gap_a = min(max(1, int(round(duration_sec * fps_a))), n_a - 1)
        gap_b = min(max(1, int(round(duration_sec * fps_b))), n_b - 1)

        seg_a = best_segment_for_gap(mse_a, gap_a)
        seg_b = best_segment_for_gap(mse_b, gap_b)
        if seg_a is None or seg_b is None:
            continue

        score = seg_a[2] + seg_b[2]
        if best is None or score < best[0]:
            best = (score, duration_sec, seg_a, seg_b)

    if best is not None:
        return best

    # If everything fails, compare adjacent frames in each clip
    seg_a = best_segment_for_gap(mse_a, 1)
    seg_b = best_segment_for_gap(mse_b, 1)
    duration_sec = 1 / ref_fps
    return (seg_a[2] + seg_b[2], duration_sec, seg_a, seg_b)



def cut_video(path: Path, out_path: Path, start_frame: int, end_frame: int, fps: float, out_w: int, out_h: int, target_fps: int):
    start_t = start_frame / fps
    duration = (end_frame - start_frame) / fps
    vf = f"scale={ out_w }:{ out_h }:{ SCALE_FLAGS },fps={ target_fps }"
    cmd = [
        "ffmpeg", "-y", "-v", "error", "-nostdin",
        "-ss", f"{start_t:.6f}",
        "-i", str(path),
        "-t", f"{duration:.6f}",
        "-vf", vf,
        "-an",
        "-c:v", "prores_ks", "-profile:v", "4444",
        "-pix_fmt", "yuva444p10le",
        "-alpha_bits", "16",
        "-vendor", "apl0",
        "-threads", "0",  # Enable multithreading
        str(out_path),
    ]
    run(cmd)




def scale_video(path: Path, out_path: Path, out_w: int, out_h: int, target_fps: int):
    """Scales + resamples a file without trimming."""
    vf = f"scale={ out_w }:{ out_h }:{ SCALE_FLAGS },fps={ target_fps }"
    cmd = [
        "ffmpeg", "-y", "-v", "error", "-nostdin",
        "-i", str(path),
        "-vf", vf,
        "-an",
        "-c:v", "prores_ks", "-profile:v", "4444",
        "-pix_fmt", "yuva444p10le",
        "-alpha_bits", "16",
        "-vendor", "apl0",
        "-threads", "0",  # Enable multithreading
        str(out_path),
    ]
    run(cmd)




def process_loop_pair(path_a: Path, path_b: Path, input_dir: Path, outdir: Path):
    rel_a = path_a.relative_to(input_dir)
    rel_b = path_b.relative_to(input_dir)
    log = [f"\n{ rel_a }  <->  { rel_b }"]

    info_a = ffprobe_info(path_a)
    info_b = ffprobe_info(path_b)
    fps_a, w_a, h_a = info_a["fps"], info_a["width"], info_a["height"]
    fps_b, w_b, h_b = info_b["fps"], info_b["width"], info_b["height"]

    frames_a = decode_small_frames(path_a, COMPARE_SIZE)
    frames_b = decode_small_frames(path_b, COMPARE_SIZE)

    if frames_a.shape[0] < 2 or frames_b.shape[0] < 2:
        log.append(f"  Only { frames_a.shape[0] } / { frames_b.shape[0] } frames decoded, falling back to plain scaling")
        for path, rel, w, h in ((path_a, rel_a, w_a, h_a), (path_b, rel_b, w_b, h_b)):
            out_w, out_h = target_dimensions(w, h, TARGET_RESOLUTION)
            out_path = outdir / rel.parent / f"{ path.stem }{ path.suffix }"
            out_path.parent.mkdir(parents=True, exist_ok=True)
            scale_video(path, out_path, out_w, out_h, TARGET_FPS)
        return "\n".join(log)

    mse_a = compute_mse_matrix(frames_a)
    mse_b = compute_mse_matrix(frames_b)

    # Always returns a result as long as both clips have >= 2 frames.
    result = joint_loop_segments(mse_a, fps_a, mse_b, fps_b, MIN_LOOP_SECONDS)

    _combined_score, duration_sec, (start_a, end_a, score_a), (start_b, end_b, score_b) = result
    if duration_sec < MIN_LOOP_SECONDS:
        log.append(f"  Clip was shorter than the { MIN_LOOP_SECONDS }s minimum, using the best available shared duration instead.")
    log.append(f"  shared loop duration = {duration_sec:.3f}s")

    for path, rel, fps, w, h, start, end, seg_score in (
        (path_a, rel_a, fps_a, w_a, h_a, start_a, end_a, score_a),
        (path_b, rel_b, fps_b, w_b, h_b, start_b, end_b, score_b),
    ):
        t0, t1 = start / fps, end / fps
        out_w, out_h = target_dimensions(w, h, TARGET_RESOLUTION)
        log.append(f"  { rel }: fps={fps:.3f}  frames=[{ start }:{ end }] "
                   f"({t0:.3f}s-{t1:.3f}s)  mse={seg_score:.2f}  "
                   f"{ w }x{ h } -> { out_w }x{ out_h }")

        out_path = outdir / rel.parent / f"{ path.stem }{ path.suffix }"
        out_path.parent.mkdir(parents=True, exist_ok=True)
        cut_video(path, out_path, start, end, fps, out_w, out_h, TARGET_FPS)

    return "\n".join(log)




def process_plain_file(path: Path, input_dir: Path, outdir: Path):
    rel = path.relative_to(input_dir)
    log = [ f"\n{ rel }" ]
    info = ffprobe_info(path)
    fps, width, height = info["fps"], info["width"], info["height"]

    out_w, out_h = target_dimensions(width, height, TARGET_RESOLUTION)
    log.append(f"  { width }x{ height } -> { out_w }x{ out_h }  fps {fps:.3f} -> { TARGET_FPS }")

    out_path = outdir / rel
    out_path.parent.mkdir(parents=True, exist_ok=True)
    scale_video(path, out_path, out_w, out_h, TARGET_FPS)

    return "\n".join(log)



def main():
    cwd = Path(".")
    input_dir = cwd / INDIR

    loop_pairs = [ (input_dir / a, input_dir / b) for a, b in LOOP_TARGET_PAIRS ]
    missing = [ p for pair in loop_pairs for p in pair if not p.exists() ]
    if missing:
        raise FileNotFoundError(f"Missing loop target files: { missing }")

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)

    all_files = find_input_files(input_dir)
    target_set = { p.resolve() for pair in loop_pairs for p in pair }
    other_files = [ p for p in all_files if p.resolve() not in target_set ]

    with ProcessPoolExecutor(max_workers=MAX_WORKERS) as ex:
        futures = { ex.submit(process_loop_pair, a, b, input_dir, outdir): (a, b) for a, b in loop_pairs }
        futures.update({ ex.submit(process_plain_file, path, input_dir, outdir): path for path in other_files })
        for fut in as_completed(futures):
            print(fut.result())


if __name__ == "__main__":
    main()