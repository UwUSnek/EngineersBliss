package com.snek.engineersbliss.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.rendering.PixelFiller;
import com.snek.engineersbliss.client.utils.rendering.PixelSetter;
import com.snek.engineersbliss.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
















public class RenderingUtils {
    private RenderingUtils() {}


    private static int clampX(final int n, final NativeImage image) { return Math.clamp(n, 0, image.getWidth()  - 1); }
    private static int clampY(final int n, final NativeImage image) { return Math.clamp(n, 0, image.getHeight() - 1); }








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
     * Overlays the specified pixel of the provided NativeImage with the given color.
     * @param img The image to draw to.
     * @param x The X position of the pixel.
     * @param y The Y position of the pixel.
     * @param c The color to draw.
     */
    public static void fillImagePixel(final NativeImage img, final int x, final int y, final int c) {

        // Custom logic to blend alpha. setPixel doesn't do that by default.
        final int px = clampX(x, img);
        final int py = clampY(y, img);
        img.setPixel(px, py, over(c, img.getPixel(px, py)));
    }


    /**
     * Overlays the specified area of the provided NativeImage with the given color.
     * Does nothing if the color's alpha channel is 0.
     * @param img The image to draw to.
     * @param x0 The X position of the top-left corner of the area to fill.
     * @param y0 The Y position of the top-left corner of the area to fill.
     * @param x1 The X position of the bottom-right corner of the area to fill.
     * @param y1 The Y position of the bottom-right corner of the area to fill.
     * @param c The color to draw.
     */
    public static void fillImageArea(final NativeImage img, final int x0, final int y0, final int x1, final int y1, final int c) {
        if((c & 0xFF000000) != 0) {
            // Fill one pixel at a time using the same method. fillRect can't blend.
            final int cx0 = clampX(x0, img);
            final int cy0 = clampY(y0, img);
            final int cx1 = clampX(x1 - 1, img);
            final int cy1 = clampY(y1 - 1, img);
            for(int yy = cy0; yy <= cy1; yy++) {
                for(int xx = cx0; xx <= cx1; xx++) {
                    fillImagePixel(img, xx, yy, c);
                }
            }
        }
    }


    /**
     * Blits a sprite onto a NativeImage.
     * ! NOTICE: This runs on the CPU, it gets very laggy very quickly. Cache drawn images whenever possible.
     * ! This method only supports PNG sprites. Other formats go through Minecraft's loading system and must be blitted from the GPU.
     * @param img The image to blit the sprite to.
     * @param spriteId The Identifier of the sprite to blit.
     * @param x The X position of the sprite in image-local coords.
     * @param y The Y position of the sprite in image-local coords.
     * @param w The final width of the sprite.
     * @param h The final height of the sprite.
     */
    public static void blitSpriteToImage(final NativeImage img, final Identifier spriteId, final int x, final int y, final int w, final int h) {
        final Identifier textureId = spriteId.withPath(p -> "textures/gui/sprites/" + p + ".png");

        try(final InputStream in = Minecraft.getInstance().getResourceManager().open(textureId);
            final NativeImage src = NativeImage.read(in)) {

            final int srcW = src.getWidth();
            final int srcH = src.getHeight();

            for(int yy = 0; yy < h; yy++) {
                final int sy = Math.clamp((yy * srcH) / h, 0, srcH - 1);
                for(int xx = 0; xx < w; xx++) {
                    final int sx = Math.clamp((xx * srcW) / w, 0, srcW - 1);
                    fillImagePixel(img, x + xx, y + yy, src.getPixel(sx, sy));
                }
            }
        }
        catch(final IOException e) {
            EngineerSBliss.LOGGER.error("Missing GUI sprite: {}", spriteId, e);
        }
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

            if(scaledFont.calcWidth(raw.substring(lineStart, i + 1)) > maxWidth) { //TODO this is prob inefficient
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
     * Fills a row of pixels, interpolating its ends based on how much of the final pixel they cover.
     * @param pixelSetter The PixelSetter to use.
     * @param pixelFiller The PixelFiller to use.
     * @param y The Y position of the column, in pixels.
     * @param fractionalLeft The X position of the left of the row, in pixels.
     * @param fractionalRight The X position of the right of the row, in pixels.
     * @param color The color of the line.
     */
    public static void extractHorizontalSpan(
        final PixelSetter pixelSetter,
        final PixelFiller pixelFiller,
        final int y,
        final double fractionalLeft, final double fractionalRight,
        final int color
    ) {
        final int baseAlpha = (color >>> 24) & 0xFF;
        if(baseAlpha == 0 || fractionalRight <= fractionalLeft) return;

        final int left  = (int)Math.floor(fractionalLeft);
        final int right = (int)Math.floor(fractionalRight);

        // Draw a single pixel if the line is 1px wide
        if(left == right) {
            blendPixel(pixelSetter, left, y, color, baseAlpha, fractionalRight - fractionalLeft);
            return;
        }

        // Partial coverage on the left column
        blendPixel(pixelSetter, left, y, color, baseAlpha, (left + 1) - fractionalLeft);

        // Fully-covered rows in between
        if(right > left + 1) {
            pixelFiller.fill(left + 1, y, right, y + 1, color);
        }

        // Partial coverage on the right column
        final double rightCoverage = fractionalRight - right;
        if(rightCoverage > 0) blendPixel(pixelSetter, right, y, color, baseAlpha, rightCoverage);
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
        extractLine((x, y, c) -> graphics.fill(x, y, x + 1, y + 1, c), xs, ys, thickness, color);
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
        extractLine((x, y, c) -> fillImagePixel(img, x,  y, c), xs, ys, thickness, color);
    }




    private static void extractLine(final PixelSetter pixelSetter, final double[] xs, final double[] ys, final float thickness, final int color) {
        if(xs.length < 2) return;
        final double halfThickness = thickness / 2.0;
        final int baseAlpha = (color >>> 24) & 0xFF;
        if(baseAlpha == 0) return;


        // Calculate bounding box of the segment, includes thickness and AA
        final double pad = halfThickness + 1.0;
        double minX = xs[0];
        double maxX = xs[0];
        double minY = ys[0];
        double maxY = ys[0];
        for(int i = 1; i < xs.length; i++) {
            minX = Math.min(minX, xs[i]); maxX = Math.max(maxX, xs[i]);
            minY = Math.min(minY, ys[i]); maxY = Math.max(maxY, ys[i]);
        }
        final int left   = (int)Math.floor(minX - pad);
        final int right  = (int)Math.ceil (maxX + pad);
        final int top    = (int)Math.floor(minY - pad);
        final int bottom = (int)Math.ceil (maxY + pad);
        final int w = right - left + 1;
        final int h = bottom - top + 1;
        if(w <= 0 || h <= 0) return;


        // Calculate max coverage per pixel across all segments
        final double[] coverage = new double[w * h];
        for(int i = 0; i < xs.length - 1; i++) {
            final double x0 = xs[i];
            final double y0 = ys[i];
            final double x1 = xs[i + 1];
            final double y1 = ys[i + 1];
            final double dx = x1 - x0;
            final double dy = y1 - y0;
            final double lenSq = dx * dx + dy * dy;

            // skip degenerate segments
            if(lenSq == 0) continue;

            final int segLeft   = Math.max(left,   (int)Math.floor(Math.min(x0, x1) - pad));
            final int segRight  = Math.min(right,  (int)Math.ceil (Math.max(x0, x1) + pad));
            final int segTop    = Math.max(top,    (int)Math.floor(Math.min(y0, y1) - pad));
            final int segBottom = Math.min(bottom, (int)Math.ceil (Math.max(y0, y1) + pad));

            for(int py = segTop; py <= segBottom; py++) {
                for(int px = segLeft; px <= segRight; px++) {
                    final double cx = px + 0.5;
                    final double cy = py + 0.5;
                    final double t = Math.clamp(((cx - x0) * dx + (cy - y0) * dy) / lenSq, 0.0, 1.0);
                    final double projX = x0 + dx * t;
                    final double projY = y0 + dy * t;
                    final double dist = Math.sqrt((cx - projX) * (cx - projX) + (cy - projY) * (cy - projY));
                    final double c = Math.clamp(halfThickness + 0.5 - dist, 0.0, 1.0);

                    final int idx = (py - top) * w + (px - left);
                    if(c > coverage[idx]) coverage[idx] = c;
                }
            }
        }


        // Blend each pixel using its accumulated coverage
        for(int py = top; py <= bottom; py++) {
            for(int px = left; px <= right; px++) {
                final double c = coverage[(py - top) * w + (px - left)];
                if(c > 0) blendPixel(pixelSetter, px, py, color, baseAlpha, c);
            }
        }
    }




    /**
     * Fills the area between a base y and the given polyline using the provided color.
     * ! NOTICE: This runs on the CPU, it gets very laggy very quickly. Cache drawn images whenever possible.
     * @param img The image to draw on.
     * @param xs The local X coordinates of the points.
     * @param ys The local Y coordinates of the points.
     * @param baseY The Y coordinate in whihc the area to fill starts.
     * @param from The X coordinate to start drawing from (inclusive).
     * @param to The Y coordinate to stop drawing at (exclusive).
     * @param color The color to use.
     */
    public static void extractLineArea(
        final NativeImage img,
        final double[] xs,
        final double[] ys,
        final int baseY,
        final int from,
        final int to,
        final int color
    ) {
        extractLineArea(
            (x,  y,          c) -> fillImagePixel(img, x,  y,          c),
            (x0, y0, x1, y1, c) -> fillImageArea (img, x0, y0, x1, y1, c),
            xs, ys, baseY, from, to, color
        );
    }




    /**
     * Fills the area between a base y and the given polyline using the provided color.
     * ! NOTICE: This runs on the CPU, it gets very laggy very quickly. Cache drawn images whenever possible.
     * @param graphics The GuiGraphicsExtractor to draw on.
     * @param xs The local X coordinates of the points.
     * @param ys The local Y coordinates of the points.
     * @param baseY The Y coordinate in whihc the area to fill starts.
     * @param from The X coordinate to start drawing from (inclusive).
     * @param to The Y coordinate to stop drawing at (exclusive).
     * @param color The color to use.
     */
    public static void extractLineArea(
        final GuiGraphicsExtractor graphics,
        final double[] xs,
        final double[] ys,
        final int baseY,
        final int from,
        final int to,
        final int color
    ) {
        extractLineArea(
            (x,  y,          c) -> graphics.fill(x,  y,  x + 1, y + 1, c),
            (x0, y0, x1, y1, c) -> graphics.fill(x0, y0, x1,    y1,    c),
            xs, ys, baseY, from, to, color
        );
    }




    private static void extractLineArea(
        final PixelSetter setter,
        final PixelFiller filler,
        final double[] xs,
        final double[] ys,
        final int baseY,
        final int from,
        final int to,
        final int color
    ) {
        final int n = xs.length;
        int segmentIndex = 0;
        for(int x = from; x < to; x++) {
            while(segmentIndex < n - 2 && x > xs[segmentIndex + 1]) segmentIndex++;

            // Calculate coordinates
            final double x0 = xs[segmentIndex];
            final double x1 = xs[segmentIndex + 1];
            final double y0 = ys[segmentIndex];
            final double y1 = ys[segmentIndex + 1];
            final double t = (x1 == x0) ? 0.0 : Mth.clamp((x - x0) / (x1 - x0), 0.0, 1.0);
            final double lineY = Utils.interpolateF(y0, y1, t);

            // Draw line
            extractVerticalSpan(setter, filler, x, lineY - 0.5, baseY, color);
        }
    }
}
