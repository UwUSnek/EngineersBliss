package com.snek.engineersbliss.client.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Utils;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor.ScissorStack;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;








/**
 * A wrapper for Minecraft's GuiGraphicsExtractor which adds support for full resolution rendering, UiTxt rendering, and antialiased operations.
 */
public class UiGraphics {
    private static final double ALIGNMENT_THRESHOLD = 1d / 100; //  1/100th of a pixel
    GuiGraphicsExtractor raw;




    // Antialiasing generalization

    private record Split(int x0i, int x1i, int y0i, int y1i, float l, float r, float t, float b) {
        float tl() { return t * l; }
        float tr() { return t * r; }
        float bl() { return b * l; }
        float br() { return b * r; }
    }
    private static Split split(float x0, float y0, float x1, float y1) {
        final int x0i = (int)Math.ceil (x0);
        final int x1i = (int)Math.floor(x1) + 1;
        final int y0i = (int)Math.ceil (y0);
        final int y1i = (int)Math.floor(y1) + 1;
        return new Split(x0i, x1i, y0i, y1i, x0i - x0, x1 - x1i + 1, y0i - y0, y1 - y1i + 1);
    }
    @FunctionalInterface private interface RectEmitter {
        void emit(int x, int y, int w, int h, float weight);
    }
    private static void emitNine(Split s, RectEmitter e) {
        e.emit(s.x0i(),     s.y0i(),     s.x1i() - 1 - s.x0i(), s.y1i() - 1 - s.y0i(), 1f);
        e.emit(s.x0i() - 1, s.y0i() ,    1,                     s.y1i() - 1 - s.y0i(), s.l());
        e.emit(s.x1i() - 1, s.y0i() ,    1,                     s.y1i() - 1 - s.y0i(), s.r());
        e.emit(s.x0i(),     s.y0i()-1,   s.x1i() - 1 - s.x0i(), 1,                     s.t());
        e.emit(s.x0i(),     s.y1i()-1,   s.x1i() - 1 - s.x0i(), 1,                     s.b());
        e.emit(s.x0i() - 1, s.y0i() - 1, 1, 1,                                         s.tl());
        e.emit(s.x1i() - 1, s.y0i() - 1, 1, 1,                                         s.tr());
        e.emit(s.x0i() - 1, s.y1i() - 1, 1, 1,                                         s.bl());
        e.emit(s.x1i() - 1, s.y1i() - 1, 1, 1,                                         s.br());
    }
    private static boolean isPixelAligned(float x0, float y0, float x1, float y1) {
        return
            Utils.doubleEquals(x0, Math.floor(x0), ALIGNMENT_THRESHOLD) &&
            Utils.doubleEquals(x1, Math.floor(x1), ALIGNMENT_THRESHOLD) &&
            Utils.doubleEquals(y0, Math.floor(y0), ALIGNMENT_THRESHOLD) &&
            Utils.doubleEquals(y1, Math.floor(y1), ALIGNMENT_THRESHOLD)
        ;
    }




    public UiGraphics(final GuiGraphicsExtractor raw) {
        this.raw = raw;
    }



    public void requestCursor(final CursorType cursorType) {
        raw.requestCursor(cursorType);
    }






    // Scissors

    public void enableScissor(final int x0, final int y0, final int x1, final int y1) {
        raw.enableScissor(x0, y0, x1, y1);
    }
    public void disableScissor() {
        raw.disableScissor();
    }
    public boolean containsPointInScissor(final int x, final int y) {
        return raw.containsPointInScissor(x, y);
    }
    public ScissorStack getScissorStack() {
        return raw.scissorStack;
    }




    public void blurBeforeThisStratum() {
        raw.blurBeforeThisStratum();
    }








    // Floating point fills

    public void fill(final float x0, final float y0, final float x1, final float y1, final int col) {
        fill(RenderPipelines.GUI, x0, y0, x1, y1, col);
    }
    public void fill(final RenderPipeline pipeline, float x0, float y0, float x1, float y1, final int col) {
        if(x0 > x1) { final float tmp = x0; x0 = x1; x1 = tmp; }
        if(y0 > y1) { final float tmp = y0; y0 = y1; y1 = tmp; }
        raw.guiRenderState.addGuiElement(new AaRectRenderState(
            UiRenderPipelines.AA_RECT, TextureSetup.noTexture(), new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1, col, getScissorStack().peek()
        ));
    }
    public int alphaColor(final int baseARGB, final float alpha) {
        return (Math.round(((baseARGB & 0xFF000000) >>> 24) * alpha) << 24) | (baseARGB & 0x00FFFFFF);
    }








    // Text rendering

    public void extractTxt(
        final FormattedCharSequence text,
        final int textWidth,
        final ScaledFont scaledFont,
        final int x, final int y,
        final int color,
        final TextAlignment textAlignment,
        final int elmWidth,
        final float shiftX, final float shiftY //! Text shift in real screen pixels. This doesn't depend on the text size.
    ) {

        // Retrieve font and text scale
        final float textScale = scaledFont.getScale() * SettingsFeatureHandler.getCurrentGuiScale();

        // Compute x and y positions
        final int _x = (int)(switch(textAlignment) {
            case LEFT            -> x;
            case CENTER          -> x + (elmWidth - textWidth) / 2;
            case RIGHT           -> x + elmWidth - textWidth;
            case CENTER_ANCHORED -> x - textWidth / 2;
        } / textScale);
        final int _y = (int)(y / textScale);

        // Draw scaled text
        raw.pose().pushMatrix();
        raw.pose().translate(shiftX, shiftY);
        raw.pose().scale(textScale, textScale);
        raw.text(scaledFont.getFont(), text, _x, _y, color);
        raw.pose().popMatrix();
    }


    public void extractTxt(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth, final boolean dropShadow) {
        extractTxt(text, x, y, color, textAlignment, elmWidth, dropShadow, 0f, 0f);
    }
    public void extractTxt(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth, final boolean dropShadow, final float shiftX, final float shiftY) {
        //! All overloads go through this which calls the true extractTxt.
        //! Using toRawVisualOrder() is required in order to render '§' properly.
        final ScaledFont scaledFont = (text instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont();
        extractTxt((dropShadow ? text : text.noShadow()).toRawVisualOrder(), text.getWidth(), scaledFont, x, y, color, textAlignment, elmWidth, shiftX, shiftY);
    }
    public void extractTxt(final UiTxt text, final int x, final int y, final int color, final boolean dropShadow) {
        extractTxt(text, x, y, color, dropShadow, 0f, 0f);
    }
    public void extractTxt(final UiTxt text, final int x, final int y, final int color, final boolean dropShadow, final float shiftX, final float shiftY) {
        extractTxt(text, x, y, color, TextAlignment.LEFT, 0, dropShadow, shiftX, shiftY);
    }


    public void extractTxt(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth) {
        extractTxt(text, x, y, color, textAlignment, elmWidth, 0f, 0f);
    }
    public void extractTxt(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth, final float shiftX, final float shiftY) {
        extractTxt(text, x, y, color, textAlignment, elmWidth, false, shiftX, shiftY);
    }
    public void extractTxt(final UiTxt text, final int x, final int y, final int color) {
        extractTxt(text, x, y, color, 0f, 0f);
    }
    public void extractTxt(final UiTxt text, final int x, final int y, final int color, final float shiftX, final float shiftY) {
        extractTxt(text, x, y, color, false, shiftX, shiftY);
    }










    // Floating point blit

    public void blit(final RenderPipeline renderPipeline, final Identifier texture, final float x, final float y, final float u, final float v, final float width, final float height, final int textureWidth, final int textureHeight) {
        if(isPixelAligned(x, y, x + width, y + height)) {
            raw.blit(renderPipeline, texture, (int)x, (int)y, u, v, (int)width, (int)height, textureWidth, textureHeight);
        }
        else {
            final int col = 0xFFFFFFFF;
            emitNine(split(x, y, x + width, y + height), (px, py, pw, ph, w) -> {
                raw.blit(renderPipeline, texture, px, py, u + (px - x), v + (py - y), pw, ph, textureWidth, textureHeight, alphaColor(col, w));
            });
        }
    }





    public void blit(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float u0, final float u1, final float v0, final float v1) {
        raw.blit(location, Math.round(x0), Math.round(y0), Math.round(x1), Math.round(y1), u0, u1, v0, v1);
    }
    public void blit(final GpuTextureView textureView, final GpuSampler sampler, final float x0, final float y0, final float x1, final float y1, final float u0, final float u1, final float v0, final float v1) {
        raw.blit(textureView, sampler, Math.round(x0), Math.round(y0), Math.round(x1), Math.round(y1), u0, u1, v0, v1);
    }


    // Floating point blitSprite

    public void blitSprite(final RenderPipeline renderPipeline, final Identifier location, final float x, final float y, final float width, final float height) {
        blitSprite(renderPipeline, location, x, y, width, height, 1.0f);
    }
    public void blitSprite(final RenderPipeline renderPipeline, final Identifier location, final float x, final float y, final float width, final float height, final float alpha) {
        if(isPixelAligned(x, y, x + width, y + height)) {
            raw.blitSprite(renderPipeline, location, (int)x, (int)y, (int)width, (int)height, alpha);
        }
        else {
            emitNine(split(x, y, x + width, y + height), (px, py, pw, ph, w) -> {
                raw.blitSprite(renderPipeline, location, px, py, pw, ph, alpha * w);
            });
        }
    }


    public void blitSprite(final RenderPipeline renderPipeline, final Identifier location, final int spriteWidth, final int spriteHeight, final int textureX, final int textureY, final float x, final float y, final float width, final float height) {
        if(isPixelAligned(x, y, x + width, y + height)) {
            raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, (int)x, (int)y, (int)width, (int)height);
        }
        else {
            final int col = 0xFFFFFFFF;
            emitNine(split(x, y, x + width, y + height), (px, py, pw, ph, w) -> {
                raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, px, py, pw, ph, alphaColor(col, w));
            });
        }
    }


    public void blitSprite(final RenderPipeline renderPipeline, final TextureAtlasSprite sprite, final float x, final float y, final float width, final float height) {
        if(isPixelAligned(x, y, x + width, y + height)) {
            raw.blitSprite(renderPipeline, sprite, (int) x, (int) y, (int) width, (int) height);
        }
        else {
            final int col = 0xFFFFFFFF;
            emitNine(split(x, y, x + width, y + height), (px, py, pw, ph, w) -> {
                raw.blitSprite(renderPipeline, sprite, px, py, pw, ph, alphaColor(col, w));
            });
        }
    }
}
