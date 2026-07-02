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




    public static Identifier getFontIdForScale(final String baseName) {
        return getFontIdForScale(baseName, 1);
    }
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


    public static final Style textStyleForScale(final String fontName) {
        return textStyleForScale(fontName, 1);
    }
    public static final Style textStyleForScale(final String fontName, final float scale) {
        final FontDescription textFont = new FontDescription.Resource(getFontIdForScale(fontName));
        return Style.EMPTY.withoutShadow().withFont(textFont);
    }
}
