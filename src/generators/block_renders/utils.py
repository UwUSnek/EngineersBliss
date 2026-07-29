import json
import os
import subprocess
from pathlib import Path

import numpy as np




# Activates extremely expensive optimizations when True. Never use for testing
PRODUCTION_RENDERING = True

# The number of worker threads to use for each step of the rendering pipeline (build script does one step at a time)
MAX_WORKERS = max(1, (os.cpu_count() or 4) - 1)








def run(cmd):
    try:
        return subprocess.run(cmd, check=True, capture_output=True)
    except subprocess.CalledProcessError as e:
        stderr = e.stderr.decode(errors="ignore") if e.stderr else ""
        raise RuntimeError(f"Command failed ({ ' '.join(map(str, cmd)) }):\n{ stderr }") from e




def ffprobe_info(path: Path) -> dict:
    """Probe fps, width, height, codec, pix_fmt. Also probe duration if available."""
    cmd = [
        "ffprobe", "-v", "error", "-select_streams", "v:0",
        "-show_entries", "stream=codec_name,pix_fmt,r_frame_rate,width,height",
        "-show_entries", "format=duration",
        "-of", "json", str(path),
    ]
    data = json.loads(run(cmd).stdout)
    stream = data["streams"][0]
    num, den = stream["r_frame_rate"].split("/")

    info = {
        "fps": float(num) / float(den),
        "width": stream["width"],
        "height": stream["height"],
        "codec": stream.get("codec_name", ""),
        "pix_fmt": stream.get("pix_fmt", ""),
    }
    duration = data.get("format", {}).get("duration")
    if duration is not None:
        info["duration"] = float(duration)
    return info




def decode_rawvideo(path: Path, width: int, height: int, scale: str = None, copy: bool = False) -> np.ndarray:
    """Decodes a video to a uint8 RGBA array."""
    cmd = ["ffmpeg", "-v", "error", "-nostdin", "-i", str(path)]
    if scale:
        cmd += ["-vf", scale]
    cmd += ["-pix_fmt", "rgba", "-f", "rawvideo", "-"]

    raw = subprocess.run(cmd, check=True, capture_output=True).stdout
    frame_size = width * height * 4
    n = len(raw) // frame_size
    if n == 0:
        return np.zeros((0, height, width, 4), dtype=np.uint8)

    arr = np.frombuffer(raw[: n * frame_size], dtype=np.uint8)
    arr = arr.reshape(n, height, width, 4)
    return arr.copy() if copy else arr




def find_input_files(input_dir: Path, ext: str = "mov"):
    """Recursively finds files with the given extension."""
    return sorted({*input_dir.rglob(f"*.{ ext }"), *input_dir.rglob(f"*.{ ext.upper() }")})




def has_alpha(pix_fmt: str) -> bool:
    """Checks if the ffmpeg pixel format supports transparency."""
    return "a" in pix_fmt and pix_fmt.startswith(("yuva", "rgba", "bgra", "argb"))




def target_dimensions(width: int, height: int, long_edge: int):
    """Scales width/height so the longer edge equals long_edge, preserves aspect ratio."""
    if width >= height:
        out_w = long_edge
        out_h = max(1, round(long_edge * height / width))
    else:
        out_h = long_edge
        out_w = max(1, round(long_edge * width / height))
    return out_w, out_h