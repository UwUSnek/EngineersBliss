import re
import shutil
import subprocess
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path

from utils import ffprobe_info as ffprobe_stream_info
from utils import find_input_files, has_alpha, run
from utils import MAX_WORKERS

INDIR = "1_trimmed"
OUTDIR = "2_even"

# Matches "<name>_on" / "<name>_off" (case-insensitive)
PAIR_SUFFIX_RE = re.compile(r"^(?P<base>.+)_(?P<state>on|off)$", re.IGNORECASE)








def pad_video(src: Path, dst: Path, info: dict, target_duration: float):
    pad_seconds = max(0.0, target_duration - info["duration"])

    vf = f"tpad=stop_mode=clone:stop_duration={ pad_seconds:.6f}"

    alpha = has_alpha(info["pix_fmt"])
    pix_fmt = "yuva444p10le" if alpha else "yuv444p10le"

    cmd = [
        "ffmpeg", "-y", "-v", "error", "-nostdin",
        "-i", str(src),
        "-vf", vf,
        "-c:v", "prores_ks", "-profile:v", "4444" if alpha else "3",
        "-pix_fmt", pix_fmt,
        "-an",
        str(dst),
    ]
    run(cmd)




def copy_video(src: Path, dst: Path):
    shutil.copy2(src, dst)




def find_pairs(input_dir: Path):
    mov_files = find_input_files(input_dir)

    groups = {}
    unmatched = []
    for path in mov_files:
        rel_dir = path.relative_to(input_dir).parent
        m = PAIR_SUFFIX_RE.match(path.stem)
        if not m:
            unmatched.append(path)
            continue
        base = m.group("base")
        state = m.group("state").lower()
        key = (rel_dir, base)
        groups.setdefault(key, {})[state] = path

    pairs = {}
    for key, states in groups.items():
        if "on" in states and "off" in states:
            pairs[key] = states
        else:
            unmatched.extend(states.values())

    return pairs, unmatched




def process_pair(rel_dir: Path, base: str, states: dict, input_dir: Path, outdir: Path):
    on_path, off_path = states["on"], states["off"]
    label = str(rel_dir / base) if str(rel_dir) != "." else base
    log = [f"\n{ label }  ({ on_path.name } / { off_path.name })"]

    on_info = ffprobe_stream_info(on_path)
    off_info = ffprobe_stream_info(off_path)

    on_dur, off_dur = on_info["duration"], off_info["duration"]
    target_duration = max(on_dur, off_dur)
    log.append(f"  on={on_dur:.3f}s  off={off_dur:.3f}s  -> target={target_duration:.3f}s")

    out_subdir = outdir / rel_dir
    out_subdir.mkdir(parents=True, exist_ok=True)
    on_dst = out_subdir / on_path.name
    off_dst = out_subdir / off_path.name

    if abs(on_dur - off_dur) < 1e-3:
        copy_video(on_path, on_dst)
        copy_video(off_path, off_dst)
    elif on_dur < off_dur:
        pad_video(on_path, on_dst, on_info, target_duration)
        copy_video(off_path, off_dst)
        log.append(f"  padded { on_path.name } to match { off_path.name }")
    else:
        pad_video(off_path, off_dst, off_info, target_duration)
        copy_video(on_path, on_dst)
        log.append(f"  padded { off_path.name } to match { on_path.name }")

    return "\n".join(log)




def process_unmatched(path: Path, input_dir: Path, outdir: Path):
    rel = path.relative_to(input_dir)
    dst = outdir / rel
    dst.parent.mkdir(parents=True, exist_ok=True)
    copy_video(path, dst)
    return f"  { rel }"




def main():
    input_dir = Path(INDIR)
    outdir = Path(OUTDIR)
    outdir.mkdir(exist_ok=True)

    pairs, unmatched = find_pairs(input_dir)

    if not pairs and not unmatched:
        print(f"No .mov files found in { INDIR }/.")
        return

    if pairs:
        with ThreadPoolExecutor(max_workers=MAX_WORKERS) as ex:
            futures = {
                ex.submit(process_pair, rel_dir, base, states, input_dir, outdir): (rel_dir, base)
                for (rel_dir, base), states in sorted(pairs.items(), key=lambda kv: (str(kv[0][0]), kv[0][1]))
            }
            for fut in as_completed(futures):
                print(fut.result())

    if unmatched:
        print("\nUnpaired files:")
        with ThreadPoolExecutor(max_workers=MAX_WORKERS) as ex:
            futures = {
                ex.submit(process_unmatched, path, input_dir, outdir): path
                for path in unmatched
            }
            for fut in as_completed(futures):
                print(fut.result())


if __name__ == "__main__":
    main()