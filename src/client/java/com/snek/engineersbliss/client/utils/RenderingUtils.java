package com.snek.engineersbliss.client.utils;

import java.util.ArrayList;
import java.util.List;

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
        final Component text,
        final float textScale,
        final int x, final int y,
        final int color,
        final TextAlignment textAlignment,
        final int elmWidth //! Can safely be 0 if textAlignment is LEFT or CENTER_ANCHORED
    ) {

        // Retrieve font and text scale, apply drop shadow option
        final Font font = Minecraft.getInstance().font;
        final float stringWidth = font.width(text) * textScale; //! Width of the string in screen space


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
        graphics.text(font, text, _x, _y, color);
        graphics.pose().popMatrix();
    }
    public static void extractTxt(GuiGraphicsExtractor graphics, final Component text, final float textScale, final int x, final int y, final int color) {
        extractTxt(graphics, text, textScale, x, y, color, TextAlignment.LEFT, 0);
    }




    public static void extractTxt(final GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth, final boolean dropShadow) {
        final float textScale = (text instanceof UiTxt uiTxt) ? uiTxt.getTextScale() : 1f;
        final Component componentText = (dropShadow ? text : text.copy().noShadow()).get();
        extractTxt(graphics, componentText, textScale, x, y, color, textAlignment, elmWidth);
    }
    public static void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color, final boolean dropShadow) {
        final float textScale = (text instanceof UiTxt uiTxt) ? uiTxt.getTextScale() : 1f;
        final Component componentText = (dropShadow ? text : text.copy().noShadow()).get();
        extractTxt(graphics, componentText, textScale, x, y, color);
    }




    public static void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth) {
        extractTxt(graphics, text, x, y, color, textAlignment, elmWidth, false);
    }
    public static void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color) {
        extractTxt(graphics, text, x, y, color, false);
    }










    /**
     * Wraps the provided Txt so each line never goes past the width limit.
     * @param text The text to wrap.
     * @param maxWidth The maximum width of a line.
     * @return A list of Txt, each containing the formatted characters in a line.
     */
    public static List<Txt> wrapLines(Txt text, int maxWidth) {
        final Font font = Minecraft.getInstance().font;
        List<Txt> lines = new ArrayList<>();
        String raw = text.getString();
        int len = raw.length();
        int lineStart = 0;
        int lastSpace = -1;

        for(int i = 0; i < len; i++) {
            char c = raw.charAt(i);

            if(c == '\n') {
                lines.add(text.substring(lineStart, i));
                lineStart = i + 1;
                lastSpace = -1;
                continue;
            }

            if(c == ' ') {
                lastSpace = i;
            }

            if(font.width(raw.substring(lineStart, i + 1)) > maxWidth) {
                if (lastSpace >= lineStart) {
                    lines.add(text.substring(lineStart, lastSpace));
                    lineStart = lastSpace + 1;
                } else {
                    lines.add(text.substring(lineStart, i));
                    lineStart = i;
                }
                lastSpace = -1;
            }
        }

        if(lineStart < len) {
            lines.add(text.substring(lineStart, len));
        }

        return lines;
    }
}
