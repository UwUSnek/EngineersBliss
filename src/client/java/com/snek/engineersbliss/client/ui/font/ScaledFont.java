package com.snek.engineersbliss.client.ui.font;

import org.jetbrains.annotations.Nullable;

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
    private final FontDescription description;


    // Getters
    public Font getFont() { return font; }
    public float getScale() { return scale; }
    public FontDescription getDescription() { return description; }




    @SuppressWarnings("java:S1172")
    public ScaledFont(final @Nullable Object __unused, Font font, final float scale, final FontDescription description) {
        this.font = font;
        this.scale = scale;
        this.description = description;
    }
    public ScaledFont(Font.Provider provider, final float scale, final FontDescription description) {
        this(null, new Font(provider), scale, description);
    }
    public ScaledFont() {
        this(null, Minecraft.getInstance().font, 1f, Style.EMPTY.getFont());
    }
    public ScaledFont(ScaledFont scaledFont, final float scale) {
        this(null, scaledFont.getFont(), scale, scaledFont.getDescription());
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
