
from concurrent.futures import ProcessPoolExecutor, as_completed
from pathlib import Path

from utils import MAX_WORKERS, ffprobe_info, find_input_files, run, target_dimensions








INDIR  = "0_raw"
OUTDIR = "1_trimmed"

# Size of the longer edge of the output frame
TARGET_RESOLUTION = 64
# Output FPS
TARGET_FPS = 12

# ffmpeg scale filter flags
SCALE_FLAGS = "flags=lanczos+accurate_rnd+full_chroma_int"


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


def process_file(path: Path, input_dir: Path, outdir: Path):
    rel = path.relative_to(input_dir)
    log = [f"\n{ rel }"]

    info = ffprobe_info(path)
    fps, width, height = info["fps"], info["width"], info["height"]

    out_w, out_h = target_dimensions(width, height, TARGET_RESOLUTION)
    log.append(f"  { width }x{ height } -> { out_w }x{ out_h }  fps {fps:.3f} -> { TARGET_FPS }")

    out_path = outdir / rel
    out_path.parent.mkdir(parents=True, exist_ok=True)
    scale_video(path, out_path, out_w, out_h, TARGET_FPS)
    log.append(f"  wrote { out_path }")

    return "\n".join(log)


def main():
    cwd = Path(".")
    input_dir = cwd / INDIR

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)

    all_files = find_input_files(input_dir)
    print(f"Found { len(all_files) } files to process")

    with ProcessPoolExecutor(max_workers=MAX_WORKERS) as ex:
        futures = { ex.submit(process_file, path, input_dir, outdir): path for path in all_files }
        for fut in as_completed(futures):
            print(fut.result())


if __name__ == "__main__":
    main()