import os
import json
from pathlib import Path
from PIL import Image




SCRIPT_DIR = Path(__file__).resolve().parent
PATTERN_INPUT_DIR   = SCRIPT_DIR / "source_masks"
OUTPUT_DIR_TEXTURES = SCRIPT_DIR / "../../main/resources/assets/engineers-bliss/textures" / "block/vanilla/banners/static"


CREDIT = "UwU_Snek"
PARENT_MODEL = "engineers.bliss::block/banners/static/templates/sheet"
TEXTURE_ALIAS = "#pattern"








PATTERNS = [
    "sheet"
]


# The names of all existing dyes in Minecraft
#! This should technically be updated manually but they are never adding new colors :skull: no updating needed here.
COLORS = {
    "white":      (249, 255, 254),
    "light_gray": (157, 157, 151),
    "gray":       (71, 79, 82),
    "black":      (29, 29, 33),
    "brown":      (131, 84, 50),
    "red":        (176, 46, 38),
    "orange":     (249, 128, 29),
    "yellow":     (254, 216, 61),
    "lime":       (128, 199, 31),
    "green":      (94, 124, 22),
    "cyan":       (22, 156, 156),
    "light_blue": (58, 175, 217),
    "blue":       (60, 68, 170),
    "purple":     (137, 50, 184),
    "magenta":    (199, 78, 189),
    "pink":       (243, 139, 170),
}




def bake_texture(pattern_name, color_name, rgb):
    src_path = PATTERN_INPUT_DIR / f"{ pattern_name }.png"
    img = Image.open(src_path).convert("RGBA")
    r, g, b = rgb
    pixels = img.load()
    for y in range(img.height):
        for x in range(img.width):
            pr, pg, pb, a = pixels[x, y]
            pixels[x, y] = (pr * r // 255, pg * g // 255, pb * b // 255, a)

    OUTPUT_DIR_TEXTURES.mkdir(parents=True, exist_ok=True)
    img.save(OUTPUT_DIR_TEXTURES / f"{ color_name }.png")




def main():
    OUTPUT_DIR_TEXTURES.mkdir(parents=True, exist_ok=True)

    for pattern_name in PATTERNS:
        src_path = PATTERN_INPUT_DIR / f"{ pattern_name }.png"
        if not src_path.exists():
            print(f"Pattern mask not found: { src_path }")
            exit(1)

        for color_name, rgb in COLORS.items():
            bake_texture(pattern_name, color_name, rgb)

    print(f"Done: { len(PATTERNS) } patterns x { len(COLORS) } colors")




if __name__ == "__main__":
    main()
