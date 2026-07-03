from PIL import Image, ImageDraw, ImageFont
from fontTools.ttLib import TTFont
from concurrent.futures import ProcessPoolExecutor
import json, math, os




SIZE = 8
CELL = 10
COLS = 20  # atlas width in glyphs
SCALES = [ 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5 ]
#! Scales go up to 5 to minimize jar file.
#! Font atlases are huge and higher resolutions are exponentially larger.


SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
MAIN_DIR = os.path.join(SCRIPT_DIR, "main")
FALLBACK_DIR = os.path.join(SCRIPT_DIR, "fallback")


OUTPUT_PNG_DIR  = os.path.join(SCRIPT_DIR, "..", "main", "resources", "assets", "engineers-bliss", "textures", "font")
OUTPUT_JSON_DIR = os.path.join(SCRIPT_DIR, "..", "main", "resources", "assets", "engineers-bliss", "font")
os.makedirs(OUTPUT_PNG_DIR, exist_ok=True)
os.makedirs(OUTPUT_JSON_DIR, exist_ok=True)




def is_visible(ch, font):
    if ch in (0x20,):  # Manually skip standard spaces
        return False
    mask = font.getmask(chr(ch))
    bbox = mask.getbbox()
    return bbox is not None




def build_atlas(name, font_path, fallback_path, upscale):
    ttf = TTFont(font_path, lazy=True)
    cmap = ttf.getBestCmap()
    fallback_ttf = TTFont(fallback_path, lazy=True)
    fallback_cmap = fallback_ttf.getBestCmap()


    glyph_source = {}
    for cp in fallback_cmap:
        glyph_source[cp] = fallback_path
    for cp in cmap:  # overwrite with main font where available
        glyph_source[cp] = font_path
    codepoints = sorted(glyph_source)


    scaled_size = round(upscale * SIZE)
    scaled_cell = round(upscale * CELL)
    png_name  = f"{name}_{upscale}x.png"
    json_name = f"{name}_{upscale}x.json"
    png_path  = os.path.join(OUTPUT_PNG_DIR, png_name)
    json_path = os.path.join(OUTPUT_JSON_DIR, json_name)


    # Fetch fonts and store chars
    font_main = ImageFont.truetype(font_path, scaled_size)
    font_fallback = ImageFont.truetype(fallback_path, scaled_size)
    def font_for(cp):
        return font_main if glyph_source[cp] == font_path else font_fallback
    chars = [c for c in codepoints if is_visible(c, font_for(c))]


    # Calculate font ascents
    main_ascent, _ = font_main.getmetrics()
    fallback_ascent, _ = font_fallback.getmetrics()
    y_offset = main_ascent - fallback_ascent  # add this when drawing fallback glyphs


    # Pad to full rows (required by minecraft)
    rows = math.ceil(len(chars) / COLS)
    chars_padded = chars + [ 0 ] * (rows * COLS - len(chars))
    grid = [ chars_padded[r * COLS:(r + 1) * COLS ] for r in range(rows) ]
    grid_str = [ "".join(chr(c) for c in row) for row in grid ]


    # Render atlas
    img = Image.new("RGBA", (COLS * scaled_cell, rows * scaled_cell), (0, 0, 0, 0))
    for row, line in enumerate(grid_str):
        for col, ch in enumerate(line):
            codepoint = ord(ch)
            if codepoint != 0:
                font = font_for(codepoint)
                x = col * scaled_cell
                y = row * scaled_cell
                glyph_local_y = y_offset if font is font_fallback else 0

                glyph_img = Image.new("RGBA", (scaled_cell, scaled_cell), (0, 0, 0, 0))
                ImageDraw.Draw(glyph_img).text((0, glyph_local_y), ch, font=font, fill=(255, 255, 255, 255))
                img.paste(glyph_img, (x, y), glyph_img)

    # Glyphs are pure white. Converting to LA (grayscale + alpha) allows for better compression
    img = img.convert("LA")
    img.save(png_path, optimize=True)


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
    with open(json_path, "w", encoding="utf-8") as f:
        json.dump(provider, f, ensure_ascii=False, indent=2)


    print(f"[{ name } { upscale }x] { len(codepoints) } codepoints, { len(chars) } rendered, atlas is { COLS }x{ rows } cells -> { png_name }, { json_name }")








if __name__ == "__main__":
    # Create all atlas/provider pairs
    main_fonts = {
        os.path.splitext(f)[0]: os.path.join(MAIN_DIR, f)
        for f in os.listdir(MAIN_DIR) if f.lower().endswith(".ttf")
    }

    # Find font pairs by matching filename pairs
    # ! This uses true multithreading to speed up the process
    jobs = []
    for name, font_path in sorted(main_fonts.items()):
        fallback_path = os.path.join(FALLBACK_DIR, f"{ name }.ttf")
        if not os.path.isfile(fallback_path):
            print(f"No fallback found for '{ name }' (expected { fallback_path }), skipping")
            continue
        for scale in SCALES:
            jobs.append((name, font_path, fallback_path, scale))

    with ProcessPoolExecutor() as executor:
        list(executor.map(build_atlas, *zip(*jobs)))