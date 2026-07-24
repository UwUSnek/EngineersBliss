package com.snek.engineersbliss.client.utils;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;








public class RenderingUtils {
    private RenderingUtils() {}




    public static void extractTxt(
        final GuiGraphicsExtractor graphics,
        final Component text,
        final ScaledFont scaledFont,
        final int x, final int y,
        final int color,
        final TextAlignment textAlignment,
        final int elmWidth, //! Can safely be 0 if textAlignment is LEFT or CENTER_ANCHORED
        final boolean dropShadow
    ) {

        // Retrieve font and text scale, apply drop shadow option
        final float textScale = scaledFont.getScale();
        final float stringWidth = scaledFont.calcWidth(text); //! Width of the string in screen space


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
        graphics.text(scaledFont.getFont(), text, _x, _y, color, dropShadow);
        graphics.pose().popMatrix();
    }
    public static void extractTxt(final GuiGraphicsExtractor graphics, final Component text, final ScaledFont scaledFont, final int x, final int y, final int color) {
        extractTxt(graphics, text, scaledFont, x, y, color, TextAlignment.LEFT, 0, true);
    }




    public static void extractTxt(final GuiGraphicsExtractor graphics, final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth, final boolean dropShadow) {
        final ScaledFont scaledFont = (text instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont();
        extractTxt(graphics, text.get(), scaledFont, x, y, color, textAlignment, elmWidth, dropShadow);
    }
    public static void extractTxt(final GuiGraphicsExtractor graphics, final UiTxt text, final int x, final int y, final int color, final boolean dropShadow) {
        extractTxt(graphics, text, x, y, color, TextAlignment.LEFT, 0, dropShadow);
    }




    public static void extractTxt(final GuiGraphicsExtractor graphics, final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth) {
        extractTxt(graphics, text, x, y, color, textAlignment, elmWidth, false);
    }
    public static void extractTxt(final GuiGraphicsExtractor graphics, final UiTxt text, final int x, final int y, final int color) {
        extractTxt(graphics, text, x, y, color, false);
    }










    /**
     * Wraps the provided UiTxt so each line never goes past the width limit.
     * @param text The text to wrap.
     * @param maxWidth The maximum width of a line.
     * @return A list of UiTxt, each containing the formatted characters in a line.
     */
    public static List<UiTxt> wrapLines(final UiTxt text, final int maxWidth) {


        // Create line list and calculate data
        final @NotNull ScaledFont scaledFont = text.getScaledFont();
        final @NotNull List<UiTxt> lines = new ArrayList<>();
        final @NotNull String raw = text.getString();
        final int len = raw.length();
        int lineStart = 0;
        int lastSpace = -1;


        // Split lines
        for(int i = 0; i < len; i++) {
            final char c = raw.charAt(i);

            if(c == '\n') {
                lines.add((UiTxt)text.substring(lineStart, i));
                lineStart = i + 1;
                lastSpace = -1;
                continue;
            }

            if(c == ' ') {
                lastSpace = i;
            }

            if(scaledFont.calcWidth(raw.substring(lineStart, i + 1)) > maxWidth) {
                if(lastSpace >= lineStart) {
                    lines.add((UiTxt)text.substring(lineStart, lastSpace));
                    lineStart = lastSpace + 1;
                }
                else {
                    lines.add((UiTxt)text.substring(lineStart, i));
                    lineStart = i;
                }
                lastSpace = -1;
            }
        }

        if(lineStart < len) {
            lines.add((UiTxt)text.substring(lineStart, len));
        }


        return lines;
    }
}
