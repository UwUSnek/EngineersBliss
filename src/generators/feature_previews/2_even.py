import json
import re
import shutil
import subprocess
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path

INDIR = "1_trimmed"
OUTDIR = "2_even"
MAX_WORKERS = 8

# Matches "<name>_on" / "<name>_off" (case-insensitive)
PAIR_SUFFIX_RE = re.compile(r"^(?P<base>.+)_(?P<state>on|off)$", re.IGNORECASE)


def run(cmd):
    return subprocess.run(cmd, check=True, capture_output=True)


def ffprobe_stream_info(path: Path):
    cmd = [
        "ffprobe", "-v", "error", "-select_streams", "v:0",
        "-show_entries", "stream=codec_name,pix_fmt,r_frame_rate,width,height",
        "-show_entries", "format=duration",
        "-of", "json", str(path),
    ]
    data = json.loads(run(cmd).stdout)
    stream = data["streams"][0]
    duration = float(data["format"]["duration"])
    num, den = stream["r_frame_rate"].split("/")
    fps = float(num) / float(den)
    return {
        "duration": duration,
        "fps": fps,
        "width": stream["width"],
        "height": stream["height"],
        "codec": stream.get("codec_name", ""),
        "pix_fmt": stream.get("pix_fmt", ""),
    }


def has_alpha(pix_fmt: str) -> bool:
    return "a" in pix_fmt and pix_fmt.startswith(("yuva", "rgba", "bgra", "argb"))


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
    mov_files = sorted({ *input_dir.glob("*.mov"), *input_dir.glob("*.MOV") })

    groups = {}
    unmatched = []
    for path in mov_files:
        m = PAIR_SUFFIX_RE.match(path.stem)
        if not m:
            unmatched.append(path)
            continue
        base = m.group("base")
        state = m.group("state").lower()
        groups.setdefault(base, {})[state] = path

    pairs = {}
    for base, states in groups.items():
        if "on" in states and "off" in states:
            pairs[base] = states
        else:
            unmatched.extend(states.values())

    return pairs, unmatched


def process_pair(base: str, states: dict, outdir: Path):
    on_path, off_path = states["on"], states["off"]
    log = [f"\n{base}  ({ on_path.name } / { off_path.name })"]

    try:
        on_info = ffprobe_stream_info(on_path)
        off_info = ffprobe_stream_info(off_path)
    except Exception as e:
        log.append(f"  ffprobe failed, skipping pair: {e}")
        return "\n".join(log)

    on_dur, off_dur = on_info["duration"], off_info["duration"]
    target_duration = max(on_dur, off_dur)
    log.append(f"  on={ on_dur:.3f}s  off={ off_dur:.3f}s  -> target={ target_duration:.3f}s")

    on_dst = outdir / on_path.name
    off_dst = outdir / off_path.name

    try:
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
    except subprocess.CalledProcessError as e:
        log.append(f"  ffmpeg failed: {e.stderr.decode(errors='ignore')}")

    return "\n".join(log)


def process_unmatched(path: Path, outdir: Path):
    dst = outdir / path.name
    copy_video(path, dst)
    return f"  {path.name}"


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
                ex.submit(process_pair, base, states, outdir): base
                for base, states in sorted(pairs.items())
            }
            for fut in as_completed(futures):
                print(fut.result())

    if unmatched:
        print("\nUnpaired files (copied through unchanged):")
        with ThreadPoolExecutor(max_workers=MAX_WORKERS) as ex:
            futures = {
                ex.submit(process_unmatched, path, outdir): path
                for path in unmatched
            }
            for fut in as_completed(futures):
                print(fut.result())


if __name__ == "__main__":
    main()