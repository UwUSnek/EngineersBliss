package com.snek.engineersbliss.client.utils;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;








public class Layout {
    private Layout() { }


    public static int bgColor = 0x80000000;
    public static int fgColor = 0xFFEEEEEE;
    public static int bgColorActive = 0x80777777;
    public static int fgColorActive = 0xFFFFFFFF;
    public static int textMarginPx = 4;

    public static final int BORDER_WIDTH = 10;
    public static final int BORDER_HEIGHT = 2;
    public static final int LIST_TOP = 32;
    public static final int BUTTON_HEIGHT = 16;

    public static final int HEADER_HEIGHT = 24;

    public static final String FONT_NAME_UI_LIGHT  = "ui_light";
    public static final String FONT_NAME_UI_MEDIUM = "ui_medium";
    public static final String FONT_NAME_UI_BOLD   = "ui_bold";
    // public static final String FONT_NAME_CODE_LIGHT //TODO
    // public static final String FONT_NAME_CODE_MEDIUM //TODO
    // public static final String FONT_NAME_CODE_BOLD //TODO

    private static int FONT_MAX_SCALE = 4;




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
     *              This is clamped between 0.25 and 4 and rounded to the nearest multiple of 0.25 units.
     *              For pixel-perfect rendering, ensure the text size is a multiple of 0.25 units (4px minecraft text height)
     * @return The ID of the font provider.
     */
    public static Identifier getFontIdForScale(final String baseName, final float scale) {
        final float guiScale = Minecraft.getInstance().getWindow().getGuiScale() * scale;

        // Snap to nearest 0.25 increment, clamped between 0.25 and FONT_MAX_SCALE
        float snapped = Math.round(guiScale * 4f) / 4f;
        snapped = Math.clamp(snapped, 0.25f, FONT_MAX_SCALE);

        // Format as "1" for whole numbers, "1.5" for decimal steps, to matches atlas filenames
        final String scaleStr = (snapped == Math.floor(snapped))
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
     *              This is clamped between 0.25 and 4 and rounded to the nearest multiple of 0.25 units.
     *              For pixel-perfect rendering, ensure the text size is a multiple of 0.25 units (4px minecraft text height)
     * @return The style.
     */
    public static final Style textStyleForScale(final String fontName, final float scale) {
        final FontDescription textFont = new FontDescription.Resource(getFontIdForScale(fontName));
        return Style.EMPTY.withFont(textFont);
    }
}
