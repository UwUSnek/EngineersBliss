from PIL import Image, ImageDraw, ImageFont
from fontTools.ttLib import TTFont
from concurrent.futures import ProcessPoolExecutor
import json, math, os, subprocess, shutil
import numpy as np







PRODUCTION_RENDERING = False    # Enables high res supersampling and png optimization when True. Drastically increases rendering time
MINECRAFT_SIZE = 8              # The font size used by minecraft
CELL = 10                       # The size of the cell containing a glyph that's MINECRAFT_SIZE pixels tall
COLS = 20                       # Atlas PNG width in glyphs
SCALES = [ 0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 2.25, 2.5, 2.75, 3, 3.25, 3.5, 3.75, 4 ]

#! Scales go up to 4 to minimize jar file size.
#! Font atlases are huge and higher resolutions are exponentially larger.

#! Scale 0.25 is essentially just single pixels but that's expected with such a small font size.
#! It looks right, it's just expectedly not readable. No need to remove it. The PNG is tiny anyway.




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



#! Custom resize improves alpha in lower resolutions. Sometimes. Marginally. Kind of.
def resize_glyph(glyph_img, target_size):
    a = glyph_img.split()[-1]
    a_resized = a.resize(target_size, Image.LANCZOS)
    white = Image.new("L", target_size, 255)
    return Image.merge("RGBA", (white, white, white, a_resized))
# NEAREST
# BOX
# BILINEAR
# HAMMING
# BICUBIC
# LANCZOS




if shutil.which("oxipng") is None:
    print("Oxipng not found on PATH")
    exit(0)

def optimize_png(path):
    subprocess.run(
        ["oxipng", "-o", "max", "--zopfli", "--strip", "safe", path],
        check=True,
        capture_output=True
    )




def build_atlas(name, font_path, fallback_path, scale):

    # Supersampling multiplier. This improves antialiasing
    #! It also makes some character not aligned to pixel boundaries so this is disabled in lower resolutions
    SUPER_SAMPLING = 32 if PRODUCTION_RENDERING else 4
    if scale <= 1:
        SUPER_SAMPLING = 1


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


    #! Weird calculations keep the ratio constant
    scaled_cell = round(scale * CELL)
    scaled_size = round(scaled_cell * (MINECRAFT_SIZE / CELL))

    png_name  = f"{name}_{scale}x.png"
    json_name = f"{name}_{scale}x.json"
    png_path  = os.path.join(OUTPUT_PNG_DIR, png_name)
    json_path = os.path.join(OUTPUT_JSON_DIR, json_name)


    # Fetch fonts and store chars. Render at higher resolution for supersampling
    font_main     = ImageFont.truetype(    font_path, scaled_size * SUPER_SAMPLING)
    font_fallback = ImageFont.truetype(fallback_path, scaled_size * SUPER_SAMPLING)
    def font_for(cp):
        return font_main if glyph_source[cp] == font_path else font_fallback
    chars = [c for c in codepoints if is_visible(c, font_for(c))]


    # Calculate font ascents
    main_ascent, _ = font_main.getmetrics()
    fallback_ascent, _ = font_fallback.getmetrics()
    y_offset = main_ascent - fallback_ascent


    # Pad to full rows (required by minecraft)
    rows = math.ceil(len(chars) / COLS)
    chars_padded = chars + [ 0 ] * (rows * COLS - len(chars))
    grid = [ chars_padded[r * COLS:(r + 1) * COLS ] for r in range(rows) ]
    grid_str = [ "".join(chr(c) for c in row) for row in grid ]


    # Render atlas
    print(f"Rendering { len(chars) }x{ scaled_cell * SUPER_SAMPLING }² pixels  ", end="")
    print(f"[{ name } { scale }x], atlas is { COLS }x{ rows } cells -> { png_name }, { json_name }")
    img = Image.new("RGBA", (COLS * scaled_cell, rows * scaled_cell), (0, 0, 0, 0))
    for row, line in enumerate(grid_str):
        for col, ch in enumerate(line):
            codepoint = ord(ch)
            if codepoint != 0:
                font = font_for(codepoint)
                x = col * scaled_cell
                y = row * scaled_cell
                glyph_local_y = y_offset if font is font_fallback else 0

                # Render supersampled glyph image, then scale it down
                glyph_img = Image.new("RGBA", (scaled_cell * SUPER_SAMPLING, scaled_cell * SUPER_SAMPLING), (0, 0, 0, 0))
                ImageDraw.Draw(glyph_img).text((0, glyph_local_y), ch, font=font, fill=(255,255,255,255))
                glyph_img = resize_glyph(glyph_img, (scaled_cell, scaled_cell))
                img.paste(glyph_img, (x, y), glyph_img)

    # Glyphs are pure white. Converting to LA (grayscale + alpha) allows for better compression
    # Optimization step further reduces the png's file using Oxipng
    img = img.convert("LA")
    img.save(png_path, optimize=True)

    #! This halves the final size of the PNGs, but it also makes the process some ~1500 times slower.
    #! Only enable the optimize_png step if rendering for production. Testing fonts can be done without it.
    if PRODUCTION_RENDERING:
        optimize_png(png_path)




    # Write JSON
    provider = {
        "providers": [
            {
                "type": "bitmap",
                "file": f"engineers-bliss:font/{ png_name }",
                "height": CELL,
                "ascent": round(main_ascent / (scaled_cell * SUPER_SAMPLING) * CELL),
                "chars": grid_str
            },
            {"type": "space", "advances": {" ": 5}},
            {"type": "reference", "id": "minecraft:include/default"},
            {"type": "reference", "id": "minecraft:include/unifont"}
        ]
    }
    with open(json_path, "w", encoding="utf-8") as f:
        json.dump(provider, f, ensure_ascii=False, indent=2)








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



