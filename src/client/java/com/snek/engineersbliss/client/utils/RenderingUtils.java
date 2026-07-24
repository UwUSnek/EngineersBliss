package com.snek.engineersbliss.client.utils;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.rendering.PixelFiller;
import com.snek.engineersbliss.client.utils.rendering.PixelSetter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;








public class RenderingUtils {
    private RenderingUtils() {}


    private static int clampX(final int n, final NativeImage image) { return Math.clamp(n, 0, image.getWidth()  - 1); }
    private static int clampY(final int n, final NativeImage image) { return Math.clamp(n, 0, image.getHeight() - 1); }












    /**
     * Forces the GuiGraphicsExtractor to render at full resolution instead of whatever the GUI Scale option decides.
     * This allows for sharper edges and proper antialiasing at high GUI Scales.
     * ! Use the returned scale factor to multiply coordinates and dimensions.
     * ! Call popFullResRendering(GuiGraphicsExtractor) after rendering is done to revert the custom transform.
     * @param graphics The GuiGraphicsExtractor to modify.
     * @return The current scale factor. Use this for coordinate calculations.
     */
    public static double pushFullResRendering(final GuiGraphicsExtractor graphics) {
        final var window = Minecraft.getInstance().getWindow();
        final double guiScale = window.getWidth() / (double) window.getGuiScaledWidth();
        graphics.pose().pushMatrix();
        graphics.pose().scale((float)(1.0 / guiScale), (float)(1.0 / guiScale));
        return guiScale;
    }




    /**
     * Reverts the transform pushed by pushFullResRendering(GuiGraphicsExtractor).
     * @param graphics The GuiGraphicsExtractor to modify.
     */
    public static void popFullResRendering(final GuiGraphicsExtractor graphics) {
        graphics.pose().popMatrix();
    }




    /**
     * Overlays the provided color on top of the source color.
     * @param source The source color.
     * @param overlay The color to overlay on top of the source.
     * @return The resulting color.
     */
    public static int over(final int source, final int overlay) {
        final double srcA = ((source  >>> 24) & 0xFF) / 255.0;
        final double dstA = ((overlay >>> 24) & 0xFF) / 255.0;
        final double outA = srcA + dstA * (1 - srcA);
        if(outA <= 0) return 0;

        int result = (int)Math.round(outA * 255) << 24;
        for(int shift = 0; shift < 24; shift += 8) {
            final int c = (int)Math.round((
                ((source  >>> shift) & 0xFF) * srcA +
                ((overlay >>> shift) & 0xFF) * dstA * (1 - srcA)) / outA
            );
            result |= Math.clamp(c, 0, 255) << shift;
        }
        return result;
    }
















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
    private static void blendPixel(
        final PixelSetter pixelSetter,
        final int x, final int y,
        final int color,
        final int baseAlpha, final double coverage
    ) {
        final int alpha = (int)Math.round(baseAlpha * Math.clamp(coverage, 0.0, 1.0));
        if(alpha <= 0) return;
        pixelSetter.set(x, y, (color & 0x00FFFFFF) | (alpha << 24));
    }


    /**
     * Fills a column of pixels, interpolating its ends based on how much of the final pixel they cover.
     * @param pixelSetter The PixelSetter to use.
     * @param pixelFiller The PixelFiller to use.
     * @param x The X position of the column, in pixels.
     * @param fractionalTop The Y position of the top of the column, in pixels.
     * @param fractionalBottom The Y position of the bottom of the column, in pixels.
     * @param color The color of the line.
     */
    public static void extractVerticalSpan(
        final PixelSetter pixelSetter,
        final PixelFiller pixelFiller,
        final int x,
        final double fractionalTop, final double fractionalBottom,
        final int color
    ) {
        final int baseAlpha = (color >>> 24) & 0xFF;
        if(baseAlpha == 0 || fractionalBottom <= fractionalTop) return;

        final int top    = (int)Math.floor(fractionalTop);
        final int bottom = (int)Math.floor(fractionalBottom);

        // Draw a single pixel if the line is 1px tall
        if(top == bottom) {
            blendPixel(pixelSetter, x, top, color, baseAlpha, fractionalBottom - fractionalTop);
            return;
        }

        // Partial coverage on the top row
        blendPixel(pixelSetter, x, top, color, baseAlpha, (top + 1) - fractionalTop);

        // Fully-covered rows in between
        if(bottom > top + 1) {
            pixelFiller.fill(x, top + 1, x + 1, bottom, color);
        }

        // Partial coverage on the bottom row
        final double bottomCoverage = fractionalBottom - bottom;
        if(bottomCoverage > 0) blendPixel(pixelSetter, x, bottom, color, baseAlpha, bottomCoverage);
    }




    /**
     * Draws a polyline through the provided coordinates, antialiasing pixel columns.
     * ! NOTICE: This runs on the CPU, it gets very laggy very quickly. Cache drawn images whenever possible.
     * @param graphics The GuiGraphicsExtractor to draw on.
     * @param xs The absolute X coordinates of the points.
     * @param ys The absolute Y coordinates of the points.
     * @param thickness The thickness of the polyline, in pixels.
     * @param color The color of the polyline.
     */
    public static void extractLine(
        final GuiGraphicsExtractor graphics,
        final double[] xs, final double[] ys,
        final float thickness, final int color
    ) {
        extractLine(
            (x,  y,          c) -> graphics.fill(x,  y,  x + 1, y + 1, c),
            (x0, y0, x1, y1, c) -> graphics.fill(x0, y0, x1,    y1,    c),
            xs, ys, thickness, color
        );
    }




    /**
     * Draws a polyline through the provided coordinates, antialiasing pixel columns.
     * ! NOTICE: This runs on the CPU, it gets very laggy very quickly. Cache drawn images whenever possible.
     * @param img The image to draw on.
     * @param xs The local X coordinates of the points.
     * @param ys The local Y coordinates of the points.
     * @param thickness The thickness of the polyline, in pixels.
     * @param color The color of the polyline.
     */
    public static void extractLine(
        final NativeImage img,
        final double[] xs, final double[] ys,
        final float thickness, final int color
    ) {
        extractLine(

            // Custom logic to blend alpha. setPixel doesn't do that by default.
            (x, y, c) -> {
                final int px = clampX(x, img);
                final int py = clampY(y, img);
                img.setPixel(px, py, over(c, img.getPixel(px, py)));
            },

            // Fille one pixel at a time using the same method. fillRect can't blend.
            (x0, y0, x1, y1, c) -> {
                final int cx0 = clampX(x0, img);
                final int cy0 = clampY(y0, img);
                final int cx1 = clampX(x1 - 1, img);
                final int cy1 = clampY(y1 - 1, img);
                for(int yy = cy0; yy <= cy1; yy++) {
                    for(int xx = cx0; xx <= cx1; xx++) {
                        img.setPixel(xx, yy, over(c, img.getPixel(xx, yy)));
                    }
                }
            },

            xs, ys, thickness, color
        );
    }




    private static void extractLine(
        final PixelSetter pixelSetter,
        final PixelFiller pixelFiller,
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
            final double x0 = xs[segIdx];
            final double x1 = xs[segIdx + 1];
            final double y0 = ys[segIdx];
            final double y1 = ys[segIdx + 1];
            final double t = (x1 == x0) ? 0 : Math.clamp((col - x0) / (x1 - x0), 0.0, 1.0);
            colY[col - left] = y0 + (y1 - y0) * t;
        }

        for(int col = left; col <= right; col++) {
            final double yHere = colY[col - left];
            final double yNext = (col < right) ? colY[col - left + 1] : yHere;
            final double top    = Math.min(yHere, yNext) - halfThickness;
            final double bottom = Math.max(yHere, yNext) + halfThickness;
            extractVerticalSpan(pixelSetter, pixelFiller, col, top, bottom, color);
        }
    }
}
