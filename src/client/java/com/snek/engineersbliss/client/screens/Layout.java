package com.snek.engineersbliss.client.screens;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;








public class Layout {
    private Layout() { }

    public static final int BORDER_WIDTH = 10;
    public static final int BORDER_HEIGHT = 4;
    public static final int LIST_TOP = 32;
    public static final int BUTTON_HEIGHT = 20;

    public static final String FONT_NAME_UI_LIGHT  = "ui_light";
    public static final String FONT_NAME_UI_MEDIUM = "ui_medium";
    public static final String FONT_NAME_UI_BOLD   = "ui_bold";
    // public static final String FONT_NAME_CODE_LIGHT //TODO
    // public static final String FONT_NAME_CODE_MEDIUM //TODO
    // public static final String FONT_NAME_CODE_BOLD //TODO

    private static int FONT_MAX_SCALE = 5;




    /**
     * Fetches the ID of the font provider of the specified font that is most optimal for rendering text of default size.
     * This takes into account the current GUI Scale option.
     * ! Available providers are the ones bundled with the mod. Specifying a non-existent font will cause the client to crash.
     * @param baseName The name of the font to fetch. This doesn't include the scale or the file extension.
     * @return The ID of the font provider.
     */
    public static Identifier getFontIdForScale(final String baseName) {
        return getFontIdForScale(baseName, 1);
    }

    /**
     * Fetches the ID of the font provider of the specified font that is most optimal for rendering text of the specified size.
     * This takes into account the current GUI Scale option.
     * ! Available providers are the ones bundled with the mod. Specifying a non-existent font will cause the client to crash.
     * @param baseName The name of the font to fetch. This doesn't include the scale or the file extension.
     * @param scale The scale factor. This should match the size of the text you intend to display relative to the default size (scale 1).
     *              This is clamped between 0.5 and 5 and rounded to the nearest multiple of 0.5 units.
     * @return The ID of the font provider.
     */
    public static Identifier getFontIdForScale(final String baseName, final float scale) {
        float guiScale = Minecraft.getInstance().getWindow().getGuiScale() * scale;

        // Snap to nearest 0.5 increment, clamped between 0.5 and FONT_MAX_SCALE
        float snapped = Math.round(guiScale * 2f) / 2f;
        snapped = Math.clamp(snapped, 0.5f, FONT_MAX_SCALE);

        // Format as "1" for whole numbers, "1.5" for half-steps, to matches atlas filenames
        String scaleStr = (snapped == Math.floor(snapped))
            ? String.valueOf((int)snapped)
            : String.valueOf(snapped)
        ;

        return Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("%s_%sx", baseName, scaleStr));
    }




    /**
     * Creates a style with the variant of the specified font that is most optimal for rendering text of default size.
     * This takes into account the current GUI Scale option.
     * ! Available fonts are the ones bundled with the mod. Specifying a non-existent font will cause the client to crash.
     * @param baseName The name of the font to fetch. This doesn't include the scale or the file extension.
     * @return The style.
     */
    public static final Style textStyleForScale(final String fontName) {
        return textStyleForScale(fontName, 1);
    }

    /**
     * Creates a style with the variant of the specified font that is most optimal for rendering text of the specified size.
     * This takes into account the current GUI Scale option.
     * ! Available fonts are the ones bundled with the mod. Specifying a non-existent font will cause the client to crash.
     * @param baseName The name of the font to fetch. This doesn't include the scale or the file extension.
     * @param scale The scale factor. This should match the size of the text you intend to display relative to the default size (scale 1).
     *              This is clamped between 0.5 and 5 and rounded to the nearest multiple of 0.5 units.
     * @return The style.
     */
    public static final Style textStyleForScale(final String fontName, final float scale) {
        final FontDescription textFont = new FontDescription.Resource(getFontIdForScale(fontName));
        return Style.EMPTY.withFont(textFont);
    }
}
