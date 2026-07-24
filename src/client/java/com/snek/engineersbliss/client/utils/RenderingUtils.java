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




    /**
     * Draws formatted text at the specified location.
     * @param graphics The GuiGraphicsExtractor to draw on.
     * @param text The text to draw.
     * @param scaledFont The ScaledFont instance used to determine the Font Family and text scale.
     * @param x The X position, in pixels.
     * @param y The Y position, in pixels.
     * @param color The default text color. Individual styled text segments can override this.
     * @param textAlignment The horizontal alignment of the text.
     * @param elmWidth The width of the element. This is used for alignment calculations. Can safely be 0 if textAlignment is LEFT or CENTER_ANCHORED.
     * @param dropShadow Whether to draw a shadow behind the rendered text.
     */
    public static void extractTxt(
        final GuiGraphicsExtractor graphics,
        final Component text,
        final ScaledFont scaledFont,
        final int x, final int y,
        final int color,
        final TextAlignment textAlignment,
        final int elmWidth,
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








    /**
     * Draws a pixel of the specified color with the given coverage, scaling the existing base alpha value.
     */
    public static void blendPixel(final GuiGraphicsExtractor graphics, final int x, final int y, final int color, final int baseAlpha, final double coverage) {
        final int alpha = (int)Math.round(baseAlpha * Math.clamp(coverage, 0.0, 1.0));
        if(alpha <= 0) return;
        graphics.fill(x, y, x + 1, y + 1, (color & 0x00FFFFFF) | (alpha << 24));
    }




    /**
     * Fills a column of pixels, interpolating its ends based on how much of the final pixel they cover.
     * @param graphics The GuiGraphicsExtractor to draw on.
     * @param x The X position of the column, in pixels.
     * @param fractionalTop The Y position of the top of the column, in pixels.
     * @param fractionalBottom The Y position of the bottom of the column, in pixels.
     * @param color The color of the line.
     */
    public static void extractVerticalSpan(final GuiGraphicsExtractor graphics, final int x, final double fractionalTop, final double fractionalBottom, final int color) {
        final int baseAlpha = (color >>> 24) & 0xFF;
        if(baseAlpha == 0 || fractionalBottom <= fractionalTop) return;

        final int top    = (int)Math.floor(fractionalTop);
        final int bottom = (int)Math.floor(fractionalBottom);

        // Draw a single pixel if the line is 1px tall
        if(top == bottom) {
            blendPixel(graphics, x, top, color, baseAlpha, fractionalBottom - fractionalTop);
            return;
        }

        // Partial coverage on the top row
        blendPixel(graphics, x, top, color, baseAlpha, (top + 1) - fractionalTop);

        // Fully-covered rows in between
        if(bottom > top + 1) {
            graphics.fill(x, top + 1, x + 1, bottom, color);
        }

        // Partial coverage on the bottom row
        final double bottomCoverage = fractionalBottom - bottom;
        if(bottomCoverage > 0) blendPixel(graphics, x, bottom, color, baseAlpha, bottomCoverage);
    }




    /**
     * Draws a polyline through the provided coordinates, antialiasing pixel columns.
     * @param graphics The GuiGraphicsExtractor to draw on.
     * @param xs The X coordinates of the points.
     * @param ys The Y coordinates of the points.
     * @param thickness The thickness of the polyline, in pixels.
     * @param color The color of the polyline.
     */
    public static void extractLine(
        final GuiGraphicsExtractor graphics,
        final double[] xs, final double[] ys,
        final float thickness, final int color
    ) {
        if(xs.length < 2) return;

        final int left  = (int)Math.floor(xs[0]);
        final int right = (int)Math.ceil(xs[xs.length - 1]);
        final double halfThickness = thickness / 2.0;

        final double[] colY = new double[right - left + 1];
        int segIdx = 0;
        for(int col = left; col <= right; col++) {
            while(segIdx < xs.length - 2 && col > xs[segIdx + 1]) segIdx++;
            final double x0 = xs[segIdx], x1 = xs[segIdx + 1];
            final double y0 = ys[segIdx], y1 = ys[segIdx + 1];
            final double t = (x1 == x0) ? 0 : Math.clamp((col - x0) / (x1 - x0), 0.0, 1.0);
            colY[col - left] = y0 + (y1 - y0) * t;
        }

        for(int col = left; col <= right; col++) {
            final double yHere = colY[col - left];
            final double yNext = (col < right) ? colY[col - left + 1] : yHere;
            final double top    = Math.min(yHere, yNext) - halfThickness;
            final double bottom = Math.max(yHere, yNext) + halfThickness;
            extractVerticalSpan(graphics, col, top, bottom, color);
        }
    }
}
