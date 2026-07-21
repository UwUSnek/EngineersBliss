package com.snek.engineersbliss.client.ui.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;








/**
 * A Font wrapper that knows its scale factor, glyph size and FontDescription.
 * This is used to easily calculate height and witdth of text without having to manually factor in the scale.
 *
 * ! The scale doesn't depend on the current GUI Scale option.
 * ! The glyph size is static and represents the absolute size of the glyphs. This also doesn't depend on the GUI Scale.
 */
public class ScaledFont {
    private final Font font;
    private final float scale;
    private final float glyphSize;
    private final FontDescription description;


    // Getters
    public Font getFont() { return font; }
    public float getScale() { return scale; }
    public float getGlyphSize() { return glyphSize; }
    public FontDescription getDescription() { return description; }




    private ScaledFont(final Object __unused, Font font, final float scale, final float glyphSize, final FontDescription description) {
        this.font = font;
        this.scale = scale;
        this.glyphSize = glyphSize;
        this.description = description;
    }
    public ScaledFont(Font.Provider provider, final float scale, final float glyphSize, final FontDescription description) {
        this(null, new Font(provider), scale, glyphSize, description);
    }
    public ScaledFont() {
        this(null, Minecraft.getInstance().font, 1f, 1f, Style.EMPTY.getFont());
    }
    public ScaledFont(ScaledFont scaledFont, final float scale) {
        this(null, scaledFont.getFont(), scale, scaledFont.getGlyphSize(), scaledFont.getDescription());
    }




    public int calcWidth(FormattedCharSequence text) {
        return (int)(font.width(text) * getScale());
    }
    public int calcWidth(FormattedText text) {
        return (int)(font.width(text) * getScale());
    }
    public int calcWidth(String str) {
        return (int)(font.width(str) * getScale());
    }


    public int getLineHeight() {
        return (int)(font.lineHeight * getScale());
    }
}
