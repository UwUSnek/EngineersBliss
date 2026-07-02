from PIL import Image, ImageDraw, ImageFont
from fontTools.ttLib import TTFont
import json, math

FONT_PATH = "light.ttf"
UPSCALE = 4
SIZE = 10
CELL = 12
SCALED_SIZE = UPSCALE * SIZE
SCALED_CELL = UPSCALE * CELL
COLS = 20  # atlas width in glyphs


# Find all codepoints in the font
ttf = TTFont(FONT_PATH, lazy=True)
cmap = ttf.getBestCmap()
codepoints = sorted(cmap.keys())

font = ImageFont.truetype(FONT_PATH, SCALED_SIZE)




# Skip blank glyphs
def has_ink(ch):
    if ch in (0x20,):  # Manually skip standard spaces
        return False
    mask = font.getmask(chr(ch))
    bbox = mask.getbbox()
    return bbox is not None

chars = [c for c in codepoints if has_ink(c)]




# Pad to full rows (required by minecraft)
ROWS = math.ceil(len(chars) / COLS)
chars += [0] * (ROWS * COLS - len(chars))

grid = [chars[r*COLS:(r+1)*COLS] for r in range(ROWS)]
grid_str = ["".join(chr(c) for c in row) for row in grid]




# Render atlas
img = Image.new("RGBA", (COLS * SCALED_CELL, ROWS * SCALED_CELL), (0, 0, 0, 0))
draw = ImageDraw.Draw(img)
for row, line in enumerate(grid_str):
    for col, ch in enumerate(line):
        if ord(ch) != 0:
            draw.text((col * SCALED_CELL, row * SCALED_CELL), ch, font=font, fill=(255, 255, 255, 255))
img.save("mono.png")




# Create provider JSON
provider = {
    "providers": [
        {
            "type": "bitmap",
            "file": "engineers-bliss:font/mono.png",
            "height": SIZE,
            "ascent": SIZE - 1,
            "chars": grid_str
        },
        {"type": "space", "advances": {" ": 5}}
    ]
}
with open("font.json", "w", encoding="utf-8") as f:
    json.dump(provider, f, ensure_ascii=False, indent=2)

print(f"{len(codepoints)} codepoints, {len([c for c in chars if c])} rendered, atlas is {COLS}x{ROWS} cells")