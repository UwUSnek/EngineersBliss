from PIL import Image, ImageDraw, ImageFont
from fontTools.ttLib import TTFont
from concurrent.futures import ProcessPoolExecutor
import json, math, os, subprocess, shutil
import numpy as np
import freetype







PRODUCTION_RENDERING = True     # Enables high res supersampling and png optimization when True. Drastically increases rendering time
MINECRAFT_SIZE = 7              # The font size used by minecraft. #! Values lower than 8 are allowed but glyphs will look smaller than Vanilla's font
CELL = 10                       # The size of the cell containing a glyph that's MINECRAFT_SIZE pixels tall
COLS = 20                       # Atlas PNG width in glyphs
SCALES = [
    0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 2.25, 2.5, 2.75, 3, 3.25, 3.5, 3.75, 4, 4.25, 4.5, 4.75, 5,
    5.25, 5.5, 5.75, 6, 6.25, 6.5, 6.75, 7, 7.25, 7.5, 7.75, 8, 8.25, 8.5, 8.75, 9, 9.25, 9.5, 9.75, 10,
]

#! Scales go up to 10 to minimize jar file size.
#! Font atlases are huge and higher resolutions are exponentially larger.

#! Scale 0.25 is essentially just single pixels but that's expected with such a small font size.
#! It looks right, it's just expectedly not readable. No need to remove it. The PNG is tiny anyway.




SCRIPT_DIR    = os.path.dirname(os.path.abspath(__file__))
MAIN_DIR     = os.path.join(SCRIPT_DIR, "main")
FALLBACK_DIR = os.path.join(SCRIPT_DIR, "fallback")


OUTPUT_PNG_DIR  = os.path.join(SCRIPT_DIR, "..", "..", "main", "resources", "assets", "engineers-bliss", "textures", "font")
OUTPUT_JSON_DIR = os.path.join(SCRIPT_DIR, "..", "..", "main", "resources", "assets", "engineers-bliss", "font")
os.makedirs(OUTPUT_PNG_DIR, exist_ok=True)
os.makedirs(OUTPUT_JSON_DIR, exist_ok=True)




def is_visible(ch, face):
    if ch in (0x20,):
        return False
    face.load_char(ch, freetype.FT_LOAD_RENDER | freetype.FT_LOAD_NO_HINTING | freetype.FT_LOAD_TARGET_NORMAL)
    bmp = face.glyph.bitmap
    return bmp.width > 0 and bmp.rows > 0


# Checks if a font is monospace by checking the fon'ts isFixedPitch flag
def is_monospace(ttf):
    try:
        if ttf["post"].isFixedPitch:
            return True
    except Exception:
        pass
    try:
        hmtx = ttf["hmtx"]
        cmap = ttf.getBestCmap()
        widths = {
            hmtx[cmap[cp]][0]
            for cp in range(0x21, 0x7F)
            if cp in cmap
        }
        return len(widths) == 1
    except Exception:
        return False



# This custom resize improves alpha in lower resolutions by normalizing it to 255 before rendering.
# This stops lighter fonts from becoming transparent when rendered at lower resolutions
# The strength is controlled by the curve_strength parameter. Value 1 does nothing. Higher values create sharper corners.
# def resize_atlas(img, target_size, curve_strength=2.5):
def resize_glyph(glyph_img, target_size, curve_strength=2.5):
    a = glyph_img.split()[-1]
    a = a.resize(target_size, Image.LANCZOS)
    arr = np.array(a, dtype=np.float32) / 255.0
    arr = 1 - (1 - arr) ** curve_strength
    arr = (arr * 255).clip(0, 255).astype(np.uint8)
    a = Image.fromarray(arr, "L")
    white = Image.new("L", target_size, 255)
    return Image.merge("RGBA", (white, white, white, a))
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




def load_face(font_path, px_size):
    face = freetype.Face(font_path)
    face.set_pixel_sizes(0, px_size)
    return face

def get_ascent(face):
    return face.size.ascender / 64.0

def render_glyph_ft(face, ch):
    face.load_char(
        ch,
        freetype.FT_LOAD_RENDER | freetype.FT_LOAD_NO_HINTING | freetype.FT_LOAD_TARGET_NORMAL
    )
    slot = face.glyph
    bmp = slot.bitmap
    if bmp.width == 0 or bmp.rows == 0:
        return None, 0, 0
    arr = np.array(bmp.buffer, dtype=np.uint8).reshape(bmp.rows, bmp.width)
    return arr, slot.bitmap_left, slot.bitmap_top

def font_advance_ratio(font_path):
    ttf = TTFont(font_path, lazy=True)
    upm = ttf["head"].unitsPerEm
    hmtx = ttf["hmtx"]
    cmap = ttf.getBestCmap()
    probe_cp = next((cp for cp in (0x4D, 0x30, 0x41) if cp in cmap), next(iter(cmap)))
    glyph_name = cmap[probe_cp]
    advance = hmtx[glyph_name][0]
    return advance / upm




def build_atlas(name, font_path, fallback_path, scale):

    # Supersampling multiplier. This improves antialiasing
    #! It also makes some character not aligned to pixel boundaries so this is disabled in lower resolutions
    #! 64 is overkill but its fine it doesnt rly matter
    SUPER_SAMPLING = 16 if PRODUCTION_RENDERING else 2
    if scale <= 1:
        SUPER_SAMPLING = 1


    ttf = TTFont(font_path, lazy=True)
    cmap = ttf.getBestCmap()
    main_is_monospace = is_monospace(ttf)
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

    png_name  = f"{ name }_{ scale }x.png"
    json_name = f"{ name }_{ scale }x.json"
    png_path  = os.path.join(OUTPUT_PNG_DIR, png_name)
    json_path = os.path.join(OUTPUT_JSON_DIR, json_name)


    # Fetch fonts and store chars. Render at higher resolution for supersampling
    face_main     = load_face(font_path, scaled_size * SUPER_SAMPLING)
    face_fallback = load_face(fallback_path, scaled_size * SUPER_SAMPLING)
    def face_for(cp):
        return face_main if glyph_source[cp] == font_path else face_fallback
    chars = [c for c in codepoints if is_visible(c, face_for(c))]


    # Calculate font ascents
    main_ascent     = get_ascent(face_main)
    fallback_ascent = get_ascent(face_fallback)
    y_offset = main_ascent - fallback_ascent


    # Pad to full rows (required by minecraft)
    rows = math.ceil(len(chars) / COLS)
    chars_padded = chars + [ 0 ] * (rows * COLS - len(chars))
    grid = [ chars_padded[r * COLS:(r + 1) * COLS ] for r in range(rows) ]
    grid_str = [ "".join(chr(c) for c in row) for row in grid ]


    # Render atlas
    print(f"Rendering { len(chars) }x{ scaled_cell * SUPER_SAMPLING }² pixels  ", end="")
    print(f"[{ name } { scale }x], atlas is { COLS }x{ rows } cells -> { png_name }, { json_name }")
    curve_strength = max(1.0, 2.5 - (scale - 1) * 0.2)
    img = Image.new("RGBA", (COLS * scaled_cell, rows * scaled_cell), (0, 0, 0, 0))
    for row, line in enumerate(grid_str):
        for col, ch in enumerate(line):
            codepoint = ord(ch)
            if codepoint != 0:
                x = col * scaled_cell
                y = row * scaled_cell

                # Render supersampled glyph image, then scale it down
                face = face_for(codepoint)
                alpha, left, top = render_glyph_ft(face, ch)
                glyph_img = Image.new("RGBA", (scaled_cell * SUPER_SAMPLING, scaled_cell * SUPER_SAMPLING), (0, 0, 0, 0))
                if alpha is not None:
                    pen_y = main_ascent if face is face_main else fallback_ascent + y_offset
                    px = round(                   left / SUPER_SAMPLING) * SUPER_SAMPLING # Snap to nearest pixel boundary in supersampled space
                    py = round(int(round(pen_y - top)) / SUPER_SAMPLING) * SUPER_SAMPLING # Snap to nearest pixel boundary in supersampled space
                    h, w = alpha.shape
                    canvas = np.array(glyph_img)
                    x0, y0 = max(px, 0), max(py, 0)
                    x1, y1 = min(px + w, canvas.shape[1]), min(py + h, canvas.shape[0])
                    if x1 > x0 and y1 > y0:
                        src = alpha[y0 - py:y1 - py, x0 - px:x1 - px]
                        canvas[y0:y1, x0:x1, 0] = 255
                        canvas[y0:y1, x0:x1, 1] = 255
                        canvas[y0:y1, x0:x1, 2] = 255
                        canvas[y0:y1, x0:x1, 3] = src
                    glyph_img = Image.fromarray(canvas, "RGBA")

                # Scale glyphs individually
                glyph_img = resize_glyph(glyph_img, (scaled_cell, scaled_cell), curve_strength)
                img.paste(glyph_img, (x, y), glyph_img)

    # Glyphs are pure white. Converting to LA (grayscale + alpha) allows for better compression
    # Optimization step further reduces the png's file using Oxipng
    img = img.convert("LA")


    # By default, Minecraft clips font glyphs to the visible bitmap pixels.
    # Monospace fonts needs additional almost transparent pixels in opposite corners to render with the proper width.
    mono_width = None
    if main_is_monospace:
        MARKER_ALPHA = 1
        arr = np.array(img)

        # Measure the advance. Monospace characters share the same advance.
        advance_ratio = font_advance_ratio(font_path)
        mono_width = max(1, min(scaled_cell, round(advance_ratio * scaled_size)))

        # For each character glyph
        for idx, cp in enumerate(chars):
            if glyph_source[cp] != font_path:
                continue

            # Clip glyphs to advance width
            row, col = divmod(idx, COLS)
            x0, y0 = col * scaled_cell, row * scaled_cell
            if mono_width < scaled_cell:
                arr[y0:y0 + scaled_cell, x0 + mono_width:x0 + scaled_cell, 1] = 0

            # Draw corner pixels
            row, col = divmod(idx, COLS)
            x0, y0 = col * scaled_cell, row * scaled_cell
            x1, y1 = x0 + mono_width - 1, y0 + scaled_cell - 1
            if arr[y0, x0, 1] == 0:
                arr[y0, x0] = (255, MARKER_ALPHA)   # top-left corner
            if arr[y1, x1, 1] == 0:
                arr[y1, x1] = (255, MARKER_ALPHA)   # right edge at the true advance width
        img = Image.fromarray(arr, "LA")


    img.save(png_path, optimize=True)

    #! This halves the final size of the PNGs, but it also makes the process some ~1500 times slower.
    #! Only enable the optimize_png step if rendering for production. Testing fonts can be done without it.
    if PRODUCTION_RENDERING:
        optimize_png(png_path)




    # Write JSON
    glyph_ascent = round(main_ascent / (scaled_cell * SUPER_SAMPLING) * CELL)
    space_width  = round(mono_width  /  scaled_cell                   * CELL) + 1 if main_is_monospace else 5 #! Add +1px of spacing manually.
    provider = {                                         #! Minecraft does this automatically with all glyphs ^
        "providers": [

            # Precomputed glyphs
            {
                "type": "bitmap",
                "file": f"engineers-bliss:font/{ png_name }",
                "height": CELL,
                "ascent": glyph_ascent,
                "chars": grid_str
            },

            #! Specify space width manually (5px) for non-monospace fonts. Minecraft's font system clips glyphs to their visible pixels.
            ## #! Spaces in non-monospace fonts have no visible pixels.
            ## #! Spaces in monospace fonts have the same corner pixels as all other fonts, so they don't need custom width.
            { "type": "space", "advances": { " ": space_width } },

            #! Use Vanilla's default font and unicode font as fallback
            { "type": "reference", "id": "minecraft:include/default" },
            { "type": "reference", "id": "minecraft:include/unifont" }
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