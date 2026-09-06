package com.snek.engineersbliss.client.ui.renderer;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor.ScissorStack;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;








/**
 * A wrapper for Minecraft's GuiGraphicsExtractor which adds support for UiTxt rendering and antialiased operations.
 */
public class UiGraphics {
    GuiGraphicsExtractor raw;
    __base_UiScreen screen;







    public UiGraphics(final GuiGraphicsExtractor raw, __base_UiScreen screen) {
        this.raw = raw;
        this.screen = screen;
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
        raw.guiRenderState.addGuiElement(new AaFillRenderState(
            UiRenderPipelines.AA_FILL, TextureSetup.noTexture(), new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1, col, getScissorStack().peek()
        ));
    }








    // Text rendering

    public void text(
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
        final float textScale = scaledFont.getScale() * screen.getGuiScale();

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


    public void text(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth, final boolean dropShadow) {
        text(text, x, y, color, textAlignment, elmWidth, dropShadow, 0f, 0f);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth, final boolean dropShadow, final float shiftX, final float shiftY) {
        //! All overloads go through this which calls the true extractTxt.
        //! Using toRawVisualOrder() is required in order to render '§' properly.
        final ScaledFont scaledFont = (text instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont();
        text((dropShadow ? text : text.noShadow()).toRawVisualOrder(), text.getWidth(), scaledFont, x, y, color, textAlignment, elmWidth, shiftX, shiftY);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final boolean dropShadow) {
        text(text, x, y, color, dropShadow, 0f, 0f);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final boolean dropShadow, final float shiftX, final float shiftY) {
        text(text, x, y, color, TextAlignment.LEFT, 0, dropShadow, shiftX, shiftY);
    }


    public void text(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth) {
        text(text, x, y, color, textAlignment, elmWidth, 0f, 0f);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final int elmWidth, final float shiftX, final float shiftY) {
        text(text, x, y, color, textAlignment, elmWidth, false, shiftX, shiftY);
    }
    public void text(final UiTxt text, final int x, final int y, final int color) {
        text(text, x, y, color, 0f, 0f);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final float shiftX, final float shiftY) {
        text(text, x, y, color, false, shiftX, shiftY);
    }








    // Internal blit methods
    private static TextureSetup textureSetupFor(final Identifier location) {
        final @NotNull AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location);

        return TextureSetup.singleTexture(texture.getTextureView(), texture.getSampler());
    }
    private void __internal_blit(final TextureSetup setup, final float x0, final float y0, final float x1, final float y1, final float u0, final float v0, final float u1, final float v1, final float alpha) {
        raw.guiRenderState.addGuiElement(new AaBlitRenderState(
            UiRenderPipelines.AA_BLIT, setup, new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1,
            u0, v0, u1, v1,
            alpha, getScissorStack().peek()
        ));
    }


    // Floating point blit
    public void blit(final Identifier texture, final float x, final float y, final float u, final float v, final float width, final float height, final int textureWidth, final int textureHeight) {
        blit(texture, x, y, u, v, width, height, textureWidth, textureHeight, 1.0f);
    }
    public void blit(final Identifier texture, final float x, final float y, final float u, final float v, final float width, final float height, final int textureWidth, final int textureHeight, final float alpha) {
        __internal_blit(textureSetupFor(texture), x, y, x + width, y + height, u / textureWidth, v / textureHeight, (u + width) / textureWidth, (v + height) / textureHeight, alpha);
    }
    public void blit(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float u0, final float u1, final float v0, final float v1) {
        blit(location, x0, y0, x1, y1, u0, u1, v0, v1, 1.0f);
    }
    public void blit(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float u0, final float u1, final float v0, final float v1, final float alpha) {
        __internal_blit(textureSetupFor(location), x0, y0, x1, y1, u0, v0, u1, v1, alpha);
    }


    // Floating point blitSprite
    public void blitSprite(final Identifier location, final float x, final float y, final float width, final float height) {
        blitSprite(location, x, y, width, height, 1.0f);
    }
    public void blitSprite(final Identifier location, final float x, final float y, final float width, final float height, final float alpha) {
        final TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.GUI).getSprite(location);
        blitSprite(sprite, x, y, width, height, alpha);
    }
    public void blitSprite(final TextureAtlasSprite sprite, final float x, final float y, final float width, final float height) {
        blitSprite(sprite, x, y, width, height, 1.0f);
    }
    public void blitSprite(final TextureAtlasSprite sprite, final float x, final float y, final float width, final float height, final float alpha) {
        __internal_blit(textureSetupFor(sprite.atlasLocation()), x, y, x + width, y + height, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), alpha);
    }








    // Multilines
    public void multiLine(final float x0, final float y0, final float x1, final float y1, float[] xs, float[] ys, float thickness, int color) {
        raw.guiRenderState.addGuiElement(new AaMultilineRenderState(
            UiRenderPipelines.AA_MULTILINE, new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1,
            xs, ys, thickness, color,
            getScissorStack().peek()
        ));
    }
    public void multiLineArea(final float x0, final float y0, final float x1, final float y1, float[] xs, float[] ys, int color) {
        raw.guiRenderState.addGuiElement(new MultilineAreaRenderState(
            UiRenderPipelines.MULTILINE_AREA, new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1,
            xs, ys, color,
            getScissorStack().peek()
        ));
    }
}
