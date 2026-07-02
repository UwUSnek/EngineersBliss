from PIL import Image, ImageDraw, ImageFont


FONT_PATH = "medium.ttf"
UPSCALE = 4           # How much to updscale the rendered atlas. This lets Minecraft render at higher resolution when GUI Scale > 1
SIZE = UPSCALE * 8    # In-game character height in px
CELL = UPSCALE * 10   # PX per cell (bigger than SIZE for padding)


chars_grid = [
    "\u0000!\"#$%&'()*+,-./0123",
    "456789:;<=>?@ABCDEFG",
    "HIJKLMNOPQRSTUVWXYZ[",
    "\\]^_`abcdefghijklmno",
    "pqrstuvwxyz{|}~\u0000\u0000\u0000\u0000\u0000\u0000"
]
COLS = len(chars_grid[0])
ROWS = len(chars_grid)


img = Image.new("RGBA", (COLS * CELL, ROWS * CELL), (0, 0, 0, 0))
draw = ImageDraw.Draw(img)
font = ImageFont.truetype(FONT_PATH, SIZE)


for row, line in enumerate(chars_grid):
    for col, ch in enumerate(line):
        if ch != " ":
            draw.text((col * CELL, row * CELL), ch, font=font, fill=(255, 255, 255, 255))


img.save("mono.png")
print(chars_grid)