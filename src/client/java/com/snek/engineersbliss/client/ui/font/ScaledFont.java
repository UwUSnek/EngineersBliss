package com.snek.engineersbliss.client.ui.font;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;








/**
 * A Font wrapper that knows its scale factor, glyph size and FontDescription.
 * This is used to easily calculate height and witdth of text without having to manually factor in the scale.
 *
 * ! The scale doesn't depend on the current GUI Scale option.
 * ! The glyph size is static and represents the absolute size of the glyphs. This also doesn't depend on the GUI Scale.
 *
 * ! The calculated width and height does however depend on the GUI Scale.
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








    /**
     * Calculates the width of the provided FormattedCharSequence for the current GUI Scale.
     * ! This function properly counts the '§' character. For legacy Vanilla behaviour ('§' measures 0) use calcLegacyWidth(s).
     * @param s The text to measure.
     * @return The width of the text in pixels.
     */
    public int calcWidth(Txt s) {
        return __internal_calcWidth(s.toRawVisualOrder(), SettingsFeatureHandler.getCurrentGuiScale());
    }
    /**
     * Calculates the width of the provided FormattedCharSequence for the current GUI Scale.
     * ! This function properly counts the '§' character. For legacy Vanilla behaviour ('§' measures 0) use calcLegacyWidth(s).
     * @param s The text to measure.
     * @return The width of the text in pixels.
     */
    public int calcWidth(Component s) {
        return calcWidth(new Txt(s));
    }
    /**
     * Calculates the width of the provided string for the current GUI Scale.
     * ! This function properly counts the '§' character. For legacy Vanilla behaviour ('§' measures 0) use calcLegacyWidth(s).
     * @param s The string to measure.
     * @return The width of the string in pixels.
     */
    public int calcWidth(String s) {
        return __internal_calcWidth(Txt.toRawSequence(s, Style.EMPTY), SettingsFeatureHandler.getCurrentGuiScale());
    }


    /**
     * Calculates the width of the provided FormattedCharSequence for the specified GUI Scale.
     * ! This function properly counts the '§' character. For legacy Vanilla behaviour ('§' measures 0) use calcLegacyWidth(s).
     * @param s The text to measure.
     * @param scale The GUI Scale to calculate the width for.
     * @return The width of the text in pixels.
     */
    public int calcWidthForGuiScale(Txt s, final int scale) {
        return __internal_calcWidth(s.toRawVisualOrder(), scale);
    }
    /**
     * Calculates the width of the provided FormattedCharSequence for the specified GUI Scale.
     * ! This function properly counts the '§' character. For legacy Vanilla behaviour ('§' measures 0) use calcLegacyWidth(s).
     * @param s The text to measure.
     * @param scale The GUI Scale to calculate the width for.
     * @return The width of the text in pixels.
     */
    public int calcWidthForGuiScale(Component s, final int scale) {
        return calcWidthForGuiScale(new Txt(s), scale);
    }
    /**
     * Calculates the width of the provided string for the specified GUI Scale.
     * ! This function properly counts the '§' character. For legacy Vanilla behaviour ('§' measures 0) use calcLegacyWidth(s).
     * @param s The string to measure.
     * @param scale The GUI Scale to calculate the width for.
     * @return The width of the string in pixels.
     */
    public int calcWidthForGuiScale(String s, final int scale) {
        return __internal_calcWidth(Txt.toRawSequence(s, Style.EMPTY), scale);
    }


    private int __internal_calcWidth(final FormattedCharSequence s, final float scale) {
        return (int)(font.width(s) * getScale() * scale);
    }








    /**
     * Calculates the legacy width of the provided FormattedCharSequence.
     * ! This function considers the '§' character to be of 0 length. To include '§' in the width, use calcWidth() and pass a Txt or String.
     * ! This also doesn't take into consideration the current GUI Scale setting.
     * @param s The text to measure.
     * @return The width of the text in pixels.
     */
    public int calcLegacyWidth(FormattedCharSequence s) {
        return (int)(font.width(s) * getScale());
    }
    /**
     * Calculates the legacy width of the provided FormattedCharSequence.
     * ! This function considers the '§' character to be of 0 length. To include '§' in the width, use calcWidth(s).
     * ! This also doesn't take into consideration the current GUI Scale setting.
     * @param s The text to measure.
     * @return The width of the text in pixels.
     */
    public int calcLegacyWidth(Txt s) {
        return calcLegacyWidth(s.get());
    }
    /**
     * Calculates the legacy width of the provided FormattedCharSequence.
     * ! This function considers the '§' character to be of 0 length. To include '§' in the width, use calcWidth(s).
     * ! This also doesn't take into consideration the current GUI Scale setting.
     * @param s The text to measure.
     * @return The width of the text in pixels.
     */
    public int calcLegacyWidth(Component s) {
        return (int)(font.width(s) * getScale());
    }
    /**
     * Calculates the legacy width of the provided string.
     * ! This function considers the '§' character to be of 0 length. To include '§' in the width, use calcWidth(s).
     * ! This also doesn't take into consideration the current GUI Scale setting.
     * @param s The string to measure.
     * @return The width of the string in pixels.
     */
    public int calcLegacyWidth(String s) {
        return (int)(font.width(s) * getScale());
    }








    public int getLineHeight() {
        return getLineHeightForGuiScale(SettingsFeatureHandler.getCurrentGuiScale());
    }
    public int getLineHeightForGuiScale(final float scale) {
        return (int)(font.lineHeight * getScale() * scale);
    }
}
