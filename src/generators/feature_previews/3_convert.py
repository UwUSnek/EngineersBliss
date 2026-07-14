import json
import shutil
import subprocess
from concurrent.futures import ProcessPoolExecutor, as_completed
from pathlib import Path

import numpy as np
from PIL import Image

from utils import MAX_WORKERS, decode_rawvideo, find_input_files, run
from utils import ffprobe_info as _ffprobe_info



TARGET_RESOLUTION = 1080     # Size of the longer edge of the output frame
INDIR = "2_even"
OUTDIR = "3_converted"
TMPDIR = "3_converted/_tmp"

MAX_ATLAS_DIM = 16384
QUALITY = 16                # AVIF CRF, lower = better quality/bigger file
TARGET_FPS = 12








def ffprobe_info(path: Path):
    info = _ffprobe_info(path)
    return round(info["fps"]), info["width"], info["height"]




def target_dimensions(width: int, height: int, long_edge: int):
    if width >= height:
        out_w = long_edge
        out_h = max(1, round(long_edge * height / width))
    else:
        out_h = long_edge
        out_w = max(1, round(long_edge * width / height))
    return out_w, out_h




def decode_frames(path: Path, width: int, height: int) -> np.ndarray:
    return decode_rawvideo(path, width, height, copy=True)




def resample_to_target_fps(frames: np.ndarray, src_fps: int, target_fps: int) -> np.ndarray:
    n = frames.shape[0]
    if n == 0 or src_fps == target_fps:
        return frames
    duration = n / src_fps
    target_n = max(1, round(duration * target_fps))
    idx = np.linspace(0, n - 1, target_n).round().astype(int)
    return frames[idx]




def resize_premultiplied(rgba: np.ndarray, out_w: int, out_h: int) -> np.ndarray:
    n = rgba.shape[0]
    out = np.empty((n, out_h, out_w, 4), dtype=np.uint8)
    rgb = rgba[..., :3].astype(np.float32)
    a = rgba[..., 3:4].astype(np.float32) / 255.0
    premult = rgb * a

    for i in range(n):
        premult_img = Image.fromarray(np.clip(premult[i], 0, 255).astype(np.uint8))
        alpha_img = Image.fromarray(rgba[i, ..., 3])

        premult_r = np.asarray(
            premult_img.resize((out_w, out_h), Image.LANCZOS)
        ).astype(np.float32)
        alpha_r = np.asarray(
            alpha_img.resize((out_w, out_h), Image.LANCZOS)
        ).astype(np.float32)

        alpha_safe = np.where(alpha_r > 1.0, alpha_r, 255.0)  # avoid div by ~0
        rgb_out = np.clip(premult_r * 255.0 / alpha_safe[..., None], 0, 255)

        out[i, ..., :3] = rgb_out.astype(np.uint8)
        out[i, ..., 3] = alpha_r.astype(np.uint8)

    return out




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
    return Image.fromarray(canvas, mode="RGBA")




def write_mcmeta(avif_path: Path, cols: int, rows: int, frame_count: int, fps: int, frame_w: int, frame_h: int) -> dict:
    meta = {
        "avif_atlas": {
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




def safe_tmp_stem(rel_no_ext: Path) -> str:
    # Flatten relative path into a collision safe filename for the tmp directory
    return "__".join(rel_no_ext.parts)




def process_file(path: Path, input_dir: Path, outdir: Path, tmpdir: Path):
    rel = path.relative_to(input_dir)
    log = [f"\n{ rel }"]
    try:
        src_fps, width, height = ffprobe_info(path)
    except Exception as e:
        log.append(f"  ffprobe failed, skipping: { e }")
        return "\n".join(log)

    try:
        frames = decode_frames(path, width, height)
    except Exception as e:
        log.append(f"  decode failed, skipping: { e }")
        return "\n".join(log)

    n = frames.shape[0]
    if n == 0:
        log.append("  no frames decoded, skipping.")
        return "\n".join(log)

    frames = resample_to_target_fps(frames, src_fps, TARGET_FPS)
    if frames.shape[0] != n:
        log.append(f"  resampled { n } -> { frames.shape[0] } frames ({ src_fps } -> { TARGET_FPS } fps)")
    n = frames.shape[0]

    out_w, out_h = target_dimensions(width, height, TARGET_RESOLUTION)
    log.append(f"  { width }x{ height } -> { out_w }x{ out_h } (aspect ratio preserved)")
    rgba = resize_premultiplied(frames, out_w, out_h)   # downscale, alpha from source

    max_cols, max_rows, capacity = atlas_capacity(out_w, out_h)

    out_subdir = outdir / rel.parent
    out_subdir.mkdir(parents=True, exist_ok=True)
    tmp_stem = safe_tmp_stem(rel.with_suffix(""))

    for chunk_idx, start in enumerate(range(0, n, capacity)):
        chunk = rgba[start:start + capacity]
        cols = grid_for(chunk.shape[0], max_cols, max_rows, out_w, out_h)
        chunk_rows = (chunk.shape[0] + cols - 1) // cols
        atlas = build_atlas(chunk, cols, out_w, out_h)

        png_path = tmpdir / f"{ tmp_stem }_{ chunk_idx }.png"
        avif_path = out_subdir / f"{ path.stem }_{ chunk_idx }.avif"

        atlas.save(png_path)
        try:
            encode_avif(png_path, avif_path)
            meta = write_mcmeta(avif_path, cols, chunk_rows, chunk.shape[0], TARGET_FPS, out_w, out_h)
            log.append(f"  wrote { avif_path } ({ chunk.shape[0] } frames, { cols } cols)")
            log.append(f"  mcmeta: { json.dumps(meta['avif_atlas']) }")
        except subprocess.CalledProcessError as e:
            log.append(f"  avif encode failed: { e.stderr.decode(errors='ignore') }")
        finally:
            png_path.unlink(missing_ok=True)

    return "\n".join(log)




def main():
    input_dir = Path(INDIR)
    mov_files = find_input_files(input_dir)

    if not mov_files:
        print(f"No .mov files found in { INDIR }/")
        return

    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)
    tmpdir = Path(TMPDIR)
    tmpdir.mkdir(parents=True, exist_ok=True)

    with ProcessPoolExecutor(max_workers=MAX_WORKERS) as ex:
        futures = {
            ex.submit(process_file, path, input_dir, outdir, tmpdir): path
            for path in mov_files
        }
        for fut in as_completed(futures):
            print(fut.result())

    shutil.rmtree(tmpdir, ignore_errors=True)




if __name__ == "__main__":
    main()