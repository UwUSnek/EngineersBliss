from concurrent.futures import ProcessPoolExecutor, as_completed
from pathlib import Path

from utils import PRODUCTION_RENDERING, ffprobe_info, find_input_files, run


# input/output directories
INDIR = "2_even"
OUTDIR = "3_converted"


# x264 encode settings. Slower preset = smaller file at the same CRF
CRF    = 16        if PRODUCTION_RENDERING else 28
PRESET = "placebo" if PRODUCTION_RENDERING else "ultrafast"






#! Input videos are RGB with a BW Alpha channel stacked below the real picture.

#! MP4 H264 is the best format for maximum compression, low ram usage, and low-ish decoding times.
#! It's also the most widely supported format so storing alpha this way is the best solution rn.

def encode_video(path: Path, out_path: Path):
    cmd = [
        "ffmpeg", "-y",                             # Call ffmped, overwrite output without asking.
        "-i", str(path),                            # Output path.
        "-r", "30",                                 #! Force 30fps. The mod's MP4 reader only allows 30fps videos.
        "-c:v", "libx264",                          #! Use H264.
        "-preset", PRESET,                          # Effort preset.
        "-crf", str(CRF),                           # Quality factor.
        "-filter_complex",                          #! Alpha workaround
        "[0:v]format=yuva420p,split=2[rgb][a];"         # Convert to YUVA
        "[rgb]format=yuv420p[rgbout];"                  # Extract RGB as a video
        "[a]alphaextract,format=yuv420p[aout];"         # Store Alpha as a grayscale video
        "[rgbout][aout]vstack=inputs=2[out]",           # Store the video with the grayscale video stacked below it
        "-map", "[out]",                                # Use the filter graph's output as the output stream
        "-profile:v", "baseline",                   # Baseline profile. Max compatibility.
        "-bf", "0",                                 # Strip B frames.
        "-refs", "1",                               # Only 1 reference frame for cheap decoding.
        "-movflags", "+faststart",                  # Store metadata at the start of the file so it can start playing instantly.
        "-an",                                      # Strip audio data.
        str(out_path),
    ]
    run(cmd)



def process_file(path: Path, input_dir: Path, outdir: Path) -> str:
    rel = path.relative_to(input_dir)
    out_subdir = outdir / rel.parent
    out_subdir.mkdir(parents=True, exist_ok=True)
    out_path = out_subdir / f"{ path.stem }.mp4"

    info = ffprobe_info(path)
    width, height, fps = info["width"], info["height"], round(info["fps"])

    encode_video(path, out_path)

    in_kb = path.stat().st_size / 1024
    out_kb = out_path.stat().st_size / 1024
    return f"\n{ rel }\n  { width }x{ height }  fps={ fps }  { in_kb:.0f}KB -> { out_kb:.0f}KB"




def main():
    input_dir = Path(INDIR)
    mov_files = find_input_files(input_dir)

    if not mov_files:
        print(f"No .mov files found in { INDIR }/")
        return

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)

    with ProcessPoolExecutor() as executor:
        futures = [executor.submit(process_file, path, input_dir, outdir) for path in mov_files]
        for future in as_completed(futures):
            print(future.result())




if __name__ == "__main__":
    main()