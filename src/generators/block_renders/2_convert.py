import json
from concurrent.futures import ProcessPoolExecutor
from pathlib import Path

import numpy as np
from PIL import Image

from utils import MAX_WORKERS, PRODUCTION_RENDERING, decode_rawvideo, ffprobe_info, find_input_files, run








# input/output directories
INDIR = "1_trimmed"
OUTDIR = "2_converted"


# oxipng optimization level, 0 to 6. Higher creates smaller files but is exponentially slower
OXIPNG_LEVEL = 6 if PRODUCTION_RENDERING else 2

# pngquant lossy quantization
PNGQUANT_QUALITY = "10-80"  # min-max quality range, 0-100

# Max atlas dimensions (the GPU refuses to load textures that are too big)
MAX_ATLAS_DIM = 16384








def decode_frames(path: Path, width: int, height: int) -> np.ndarray:
    return decode_rawvideo(path, width, height, copy=True)




def grid_capacity(cell_w: int, cell_h: int):
    """Max cols/rows/cells per canvas for a given cell size."""
    max_cols = max(1, MAX_ATLAS_DIM // cell_w)
    max_rows = max(1, MAX_ATLAS_DIM // cell_h)
    return max_cols, max_rows, max_cols * max_rows




def grid_for(n, max_cols, max_rows, cell_w, cell_h):
    """Finds the column count that creates the most square grid for the provided cell dimensions."""
    best_cols, best_score = 1, float("inf")
    for cols in range(1, max_cols + 1):
        rows = int(np.ceil(n / cols))
        if rows > max_rows:
            continue
        canvas_w, canvas_h = cols * cell_w, rows * cell_h
        score = abs(canvas_w - canvas_h)
        if score < best_score:
            best_cols, best_score = cols, score
    return best_cols




def build_grid(cells: np.ndarray, cols: int, cell_w: int, cell_h: int) -> np.ndarray:
    """Tiles a stack of same-sized RGBA cells (frames or sheets) into one canvas array."""
    n = cells.shape[0]
    rows = (n + cols - 1) // cols
    canvas = np.zeros((rows * cell_h, cols * cell_w, 4), dtype=np.uint8)
    for i in range(n):
        r, c = divmod(i, cols)
        y, x = r * cell_h, c * cell_w
        canvas[y:y + cell_h, x:x + cell_w] = cells[i]
    return canvas




def canvas_to_image(canvas: np.ndarray) -> Image.Image:
    # Zero out RGB values for fully transparent pixels. This helps oxipng compute faster and get better results. --alpha is goofy and doesnt work well
    alpha = canvas[..., 3]
    canvas[alpha == 0, :3] = 0
    return Image.fromarray(canvas, mode="RGBA")




def write_mcmeta(png_path: Path, atlas_cols: int, atlas_rows: int, sheet_w: int, sheet_h: int, frame_w: int, frame_h: int, frame_count: int, fps: int) -> dict:
    meta = {
        "engineers-bliss.atlas": {
            "atlas_cols": atlas_cols,
            "atlas_rows": atlas_rows,
            "sheet_width": sheet_w,
            "sheet_height": sheet_h,
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
    run(cmd)




def optimize_png(png_path: Path):
    """Optimizes the PNG with oxipng."""
    cmd = [
        "oxipng",
        "-o", str(OXIPNG_LEVEL),
        "--strip", "safe"
    ]
    cmd.append(str(png_path))
    run(cmd)




def build_video_sheet(args):
    """Decodes one video and tiles its frames into a sprite sheet."""
    path, input_dir, frame_w, frame_h, n_frames, sheet_cols = args
    rel = path.relative_to(input_dir)

    info = ffprobe_info(path)
    w, h = info["width"], info["height"]
    if (w, h) != (frame_w, frame_h):
        raise RuntimeError(f"{ rel }: { w }x{ h } does not match expected { frame_w }x{ frame_h }")

    frames = decode_frames(path, w, h)
    if frames.shape[0] < n_frames:
        raise RuntimeError(f"{ rel }: only { frames.shape[0] } frames decoded, expected { n_frames }")
    frames = frames[:n_frames]  # Drop any stray extra frame

    sheet = build_grid(frames, sheet_cols, frame_w, frame_h)
    return str(rel), sheet




def flush_atlas(buffer_sheets, buffer_names, atlas_idx, outdir, max_cols, max_rows, sheet_w, sheet_h, frame_w, frame_h, n_frames, fps):
    cols = grid_for(len(buffer_sheets), max_cols, max_rows, sheet_w, sheet_h)
    rows = (len(buffer_sheets) + cols - 1) // cols

    canvas = build_grid(np.stack(buffer_sheets), cols, sheet_w, sheet_h)
    image = canvas_to_image(canvas)

    out_path = outdir / f"atlas_{ atlas_idx }.png"
    image.save(out_path, optimize=True)
    quantize_png(out_path)
    optimize_png(out_path)
    meta = write_mcmeta(out_path, cols, rows, sheet_w, sheet_h, frame_w, frame_h, n_frames, fps)

    size_kb = out_path.stat().st_size / 1024
    log = [f"\n{ out_path } ({ len(buffer_sheets) } sheets, { cols }x{ rows }, {size_kb:.1f} KiB)"]
    log.append(f"  mcmeta: { json.dumps(meta['engineers-bliss.atlas']) }")
    log.append("  contains: " + ", ".join(buffer_names))
    return "\n".join(log)




def main():
    input_dir = Path(INDIR)
    files = find_input_files(input_dir)

    if not files:
        print(f"No .mov files found in { INDIR }/")
        return

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)

    # All source videos share fps/dimensions/frame count
    first_path = files[0]
    info = ffprobe_info(first_path)
    frame_w, frame_h, fps = info["width"], info["height"], round(info["fps"])

    first_frames = decode_frames(first_path, frame_w, frame_h)
    n_frames = first_frames.shape[0]
    if n_frames == 0:
        raise RuntimeError(f"no frames decoded for { first_path }")

    sheet_cols = grid_for(n_frames, n_frames, n_frames, frame_w, frame_h)
    sheet_rows = (n_frames + sheet_cols - 1) // sheet_cols
    sheet_w, sheet_h = sheet_cols * frame_w, sheet_rows * frame_h

    max_cols, max_rows, capacity = grid_capacity(sheet_w, sheet_h)

    print(f"frame { frame_w }x{ frame_h }  count={ n_frames }  fps={ fps }")
    print(f"per-video sheet { sheet_cols }x{ sheet_rows } = { sheet_w }x{ sheet_h }")
    print(f"atlas capacity: { capacity } sheets ({ max_cols }x{ max_rows } grid)  { len(files) } videos total")

    jobs = [
        (path, input_dir, frame_w, frame_h, n_frames, sheet_cols)
        for path in files
    ]

    atlas_idx = 0
    buffer_sheets, buffer_names = [], []

    with ProcessPoolExecutor(max_workers=MAX_WORKERS) as ex:
        for rel, sheet in ex.map(build_video_sheet, jobs):
            buffer_sheets.append(sheet)
            buffer_names.append(rel)

            if len(buffer_sheets) == capacity:
                print(flush_atlas(buffer_sheets, buffer_names, atlas_idx, outdir, max_cols, max_rows, sheet_w, sheet_h, frame_w, frame_h, n_frames, fps))
                atlas_idx += 1
                buffer_sheets, buffer_names = [], []

    if buffer_sheets:
        print(flush_atlas(buffer_sheets, buffer_names, atlas_idx, outdir, max_cols, max_rows, sheet_w, sheet_h, frame_w, frame_h, n_frames, fps))




if __name__ == "__main__":
    main()