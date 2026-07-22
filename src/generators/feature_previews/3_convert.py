import json
import subprocess
from concurrent.futures import ProcessPoolExecutor, as_completed
from pathlib import Path

import numpy as np
from PIL import Image

from utils import PRODUCTION_RENDERING, decode_rawvideo, ffprobe_info, find_input_files, run


# input/output directories
INDIR = "2_even"
OUTDIR = "3_converted"


# oxipng optimization level, 0 to 6. Higher creates smaller files but is exponentially slower
OXIPNG_LEVEL = 6 if PRODUCTION_RENDERING else 2

# pngquant lossy quantization
PNGQUANT_QUALITY = "65-80"  # min-max quality range, 0-100

# Max atlas dimensions (the GPU refuses to load textures that are too big)
MAX_ATLAS_DIM = 16384








def decode_frames(path: Path, width: int, height: int) -> np.ndarray:
    return decode_rawvideo(path, width, height, copy=True)




def atlas_capacity(frame_w: int, frame_h: int):
    """Max cols/rows/frames-per-atlas for a given cell size."""
    max_cols = max(1, MAX_ATLAS_DIM // frame_w)
    max_rows = max(1, MAX_ATLAS_DIM // frame_h)
    return max_cols, max_rows, max_cols * max_rows




def grid_for(n, max_cols, max_rows, frame_w, frame_h):
    """Finds the column number that creates the most square grid for the provided frame dimensions."""
    best_cols, best_score = 1, float("inf")
    for cols in range(1, max_cols + 1):
        rows = int(np.ceil(n / cols))
        if rows > max_rows:
            continue
        sheet_w, sheet_h = cols * frame_w, rows * frame_h
        score = abs(sheet_w - sheet_h)
        if score < best_score:
            best_cols, best_score = cols, score
    return best_cols



def build_atlas(frames_chunk: np.ndarray, cols: int, frame_w: int, frame_h: int) -> Image.Image:
    n = frames_chunk.shape[0]
    rows = (n + cols - 1) // cols
    canvas = np.zeros((rows * frame_h, cols * frame_w, 4), dtype=np.uint8)
    for i in range(n):
        r, c = divmod(i, cols)
        y, x = r * frame_h, c * frame_w
        canvas[y:y + frame_h, x:x + frame_w] = frames_chunk[i]
    # Zero out RGB values for fully transparent pixels. This helps oxipng compute faster and get better results. --alpha is goofy and doesnt work well
    alpha = canvas[..., 3]
    canvas[alpha == 0, :3] = 0
    return Image.fromarray(canvas, mode="RGBA")




def write_mcmeta(png_path: Path, cols: int, rows: int, frame_count: int, fps: int, frame_w: int, frame_h: int) -> dict:
    meta = {
        "engineers-bliss.atlas": {
            "atlas_cols": 1,
            "atlas_rows": 1,
            "sheet_width": cols * frame_w,
            "sheet_height": rows * frame_h,
            "frame_width": frame_w,
            "frame_height": frame_h,
            "frame_count": frame_count,
            "fps": fps,
        }
    }
    mcmeta_path = png_path.with_suffix(png_path.suffix + ".mcmeta")
    mcmeta_path.write_text(json.dumps(meta))
    return meta




def quantize_png(png_path: Path):
    """Lossily quantizes the PNG to a palette with pngquant. --skip-if-larger keeps the original file if quantization is not smaller."""
    cmd = [
        "pngquant",
        "--quality", PNGQUANT_QUALITY,
        "--force",
        "--skip-if-larger",
        "--strip",
        "--output", str(png_path),
        str(png_path),
    ]
    try:
        run(cmd)
    except RuntimeError as e:
        # 99 means pngquant couldn't meet quality requirements. It's not a real error
        if "exit status 99" not in str(e):
            raise




def optimize_png(png_path: Path):
    """Optimizes the PNG with oxipng."""
    cmd = [
        "oxipng",
        "-o", str(OXIPNG_LEVEL),
        "--strip", "safe"
    ]
    cmd.append(str(png_path))
    run(cmd)




def process_file(path: Path, input_dir: Path, outdir: Path):
    rel = path.relative_to(input_dir)
    log = [f"\n{ rel }"]
    info = ffprobe_info(path)
    width, height, fps = info["width"], info["height"], round(info["fps"])

    frames = decode_frames(path, width, height)

    n = frames.shape[0]
    if n == 0:
        raise RuntimeError(f"no frames decoded for { path }")

    log.append(f"  { width }x{ height }  fps={ fps }  frames={ n }")

    max_cols, max_rows, capacity = atlas_capacity(width, height)

    out_subdir = outdir / rel.parent
    out_subdir.mkdir(parents=True, exist_ok=True)

    for chunk_idx, start in enumerate(range(0, n, capacity)):
        chunk = frames[start:start + capacity]
        cols = grid_for(chunk.shape[0], max_cols, max_rows, width, height)
        chunk_rows = (chunk.shape[0] + cols - 1) // cols
        atlas = build_atlas(chunk, cols, width, height)

        png_path = out_subdir / f"{ path.stem }_{ chunk_idx }.png"

        atlas.save(png_path, optimize=True)
        quantize_png(png_path)
        optimize_png(png_path)
        meta = write_mcmeta(png_path, cols, chunk_rows, chunk.shape[0], fps, width, height)
        size_kb = png_path.stat().st_size / 1024

    return "\n".join(log)




def main():
    input_dir = Path(INDIR)
    mov_files = find_input_files(input_dir)

    if not mov_files:
        print(f"No .mov files found in { INDIR }/")
        return

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)

    for path in mov_files:
        print(process_file(path, input_dir, outdir))





if __name__ == "__main__":
    main()