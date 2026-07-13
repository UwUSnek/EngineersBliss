package com.snek.engineersbliss.client.utils;

import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;








public class RenderingUtils {
    private RenderingUtils() {}




    public static void extractTxt(
        final GuiGraphicsExtractor graphics,
        final Txt text,
        final int x, final int y,
        final int color,
        final TextAlignment textAlignment,
        final int elmWidth, //! Can safely be 0 if textAlignment is LEFT or CENTER_ANCHORED
        final boolean dropShadow
    ) {

        // Retrieve font and text scale, apply drop shadow option
        final Font font = Minecraft.getInstance().font;
        final float textScale = (text instanceof UiTxt uiTxt) ? uiTxt.getTextScale() : 1f;
        final Component componentText = (dropShadow ? text : text.copy().noShadow()).get();
        final float stringWidth = font.width(componentText) * textScale; //! Width of the string in screen space


        // Compute x and y positions (calculate in screen space, resize to scaled coords)
        final int _x = (int)(switch(textAlignment) {
            case LEFT            -> x;
            case CENTER          -> x + (elmWidth - stringWidth) / 2;
            case RIGHT           -> x + elmWidth - stringWidth;
            case CENTER_ANCHORED -> x - stringWidth / 2; //! Vanilla's .centeredText
        } / textScale);
        final int _y = (int)(y / textScale);

        // Draw scaled text
        graphics.pose().pushMatrix();
        graphics.pose().scale(textScale, textScale);
        graphics.text(font, componentText, _x, _y, color);
        graphics.pose().popMatrix();
    }


    public static void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color, final boolean dropShadow) {
        extractTxt(graphics, text, x, y, color, TextAlignment.LEFT, 0, dropShadow);
    }
    public static void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth) {
        extractTxt(graphics, text, x, y, color, textAlignment, elmWidth, false);
    }
    public static void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color) {
        extractTxt(graphics, text, x, y, color, TextAlignment.LEFT, 0, false);
    }
}
