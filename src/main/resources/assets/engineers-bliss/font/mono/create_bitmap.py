from PIL import Image, ImageDraw, ImageFont
from fontTools.ttLib import TTFont
import json, math


FONT_PATH = "medium.ttf"
FALLBACK_FONT_PATH = "fallback_light.ttf"
SIZE = 8
CELL = 10
COLS = 20  # atlas width in glyphs
SCALES = [1, 2, 3, 4]

OUTPUT_PNG_NAME  = "mono"
OUTPUT_JSON_NAME = "ui_font"



# Find all codepoints in the font
ttf = TTFont(FONT_PATH, lazy=True)
cmap = ttf.getBestCmap()
fallback_ttf = TTFont(FALLBACK_FONT_PATH, lazy=True)
fallback_cmap = fallback_ttf.getBestCmap()
codepoints = sorted(set(cmap) | set(fallback_cmap))

glyph_source = {}
for cp in fallback_cmap:
    glyph_source[cp] = FALLBACK_FONT_PATH
for cp in cmap:  # overwrite with main font where available
    glyph_source[cp] = FONT_PATH
codepoints = sorted(glyph_source)




def is_visible(ch, font):
    if ch in (0x20,):  # Manually skip standard spaces
        return False
    mask = font.getmask(chr(ch))
    bbox = mask.getbbox()
    return bbox is not None




def build_atlas(upscale):
    scaled_size = upscale * SIZE
    scaled_cell = upscale * CELL
    png_name  = f"{ OUTPUT_PNG_NAME  }_{ upscale }x.png"
    json_name = f"{ OUTPUT_JSON_NAME }_{ upscale }x.json"


    # Fetch fonts and store chars
    font_main = ImageFont.truetype(FONT_PATH, scaled_size)
    font_fallback = ImageFont.truetype(FALLBACK_FONT_PATH, scaled_size)
    def font_for(cp):
        return font_main if glyph_source[cp] == FONT_PATH else font_fallback
    chars = [c for c in codepoints if is_visible(c, font_for(c))]


    # Calculate font ascents
    main_ascent, _ = font_main.getmetrics()
    fallback_ascent, _ = font_fallback.getmetrics()
    y_offset = main_ascent - fallback_ascent  # add this when drawing fallback glyphs


    # Pad to full rows (required by minecraft)
    rows = math.ceil(len(chars) / COLS)
    chars_padded = chars + [0] * (rows * COLS - len(chars))
    grid = [ chars_padded[r * COLS:(r + 1) * COLS] for r in range(rows) ]
    grid_str = [ "".join(chr(c) for c in row) for row in grid ]


    # Render atlas
    img = Image.new("RGBA", (COLS * scaled_cell, rows * scaled_cell), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    for row, line in enumerate(grid_str):
        for col, ch in enumerate(line):
            codepoint = ord(ch)
            if codepoint != 0:
                font = font_for(codepoint)
                x = col * scaled_cell
                y = row * scaled_cell + (y_offset if font is font_fallback else 0)
                draw.text((x, y), ch, font=font, fill=(255,255,255,255))
    img.save(png_name)


    # Write JSON
    provider = {
        "providers": [
            {
                "type": "bitmap",
                "file": f"engineers-bliss:font/{ png_name }",
                "height": CELL,
                "ascent": round(main_ascent / scaled_cell * CELL),
                "chars": grid_str
            },
            {"type": "space", "advances": {" ": 5}}
        ]
    }
    with open(json_name, "w", encoding="utf-8") as f:
        json.dump(provider, f, ensure_ascii=False, indent=2)


    # Print output message
    print(f"[{ upscale }x] {len(codepoints)} codepoints, {len(chars)} rendered, atlas is { COLS }x{ rows } cells -> { png_name }, { json_name }")




for scale in SCALES:
    build_atlas(scale)