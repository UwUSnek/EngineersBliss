package com.snek.engineersbliss.client.ui;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;








/**
 * A wrapper for Minecraft's GuiGraphicsExtractor which adds support for full resolution rendering, UiTxt rendering, and antialiased operations.
 */
public class UiGraphics {
    GuiGraphicsExtractor raw;

    public UiGraphics(final GuiGraphicsExtractor raw) {
        this.raw = raw;
    }



	public void requestCursor(final CursorType cursorType) {
		raw.requestCursor(cursorType);
	}








    /** //TODO prob not needed? bake the math into dedicated methods?
     * Forces the GuiGraphicsExtractor to render at full resolution instead of whatever the GUI Scale option decides.
     * This allows for sharper edges and proper antialiasing at high GUI Scales.
     * ! Use the returned scale factor to multiply coordinates and dimensions.
     * ! Call popFullResRendering(GuiGraphicsExtractor) after rendering is done to revert the custom transform.
     * @return The current scale factor. Use this for coordinate calculations.
     */
    public double pushFullResRendering() {
        final var window = Minecraft.getInstance().getWindow();
        final float guiScale = window.getWidth() / SettingsFeatureHandler.getCurrentGuiScale();
        raw.pose().pushMatrix();
        raw.pose().scale(1.0f / guiScale, 1.0f / guiScale);
        return guiScale;
    }


    /** //TODO prob not needed? bake the math into dedicated methods?
     * Reverts the transform pushed by pushFullResRendering(GuiGraphicsExtractor).
     */
    public void popFullResRendering() {
        raw.pose().popMatrix();
    }








	public void enableScissor(final int x0, final int y0, final int x1, final int y1) {
        raw.enableScissor(x0, y0, x1, y1);
	}
	public void disableScissor() {
        raw.disableScissor();
	}
	public boolean containsPointInScissor(final int x, final int y) {
        return raw.containsPointInScissor(x, y);
	}




	public void blurBeforeThisStratum() {
		raw.blurBeforeThisStratum();
	}



    // Integer fills

    public void fill(final int x0, final int y0, final int x1, final int y1, final int col) {
        fill(RenderPipelines.GUI, x0, y0, x1, y1, col);
    }
    public void fill(final RenderPipeline pipeline, int x0, int y0, int x1, int y1, final int col) {
        raw.fill(pipeline, x0, y0, x1, y1, col);
    }


    // Floating point fills

    public void fill(final float x0, final float y0, final float x1, final float y1, final int col) {
        fill(RenderPipelines.GUI, x0, y0, x1, y1, col);
    }
    public void fill(final RenderPipeline pipeline, float x0, float y0, float x1, float y1, final int col) {

        // Normalize coord order, make x0/y0 the lower values. //! Minecraft does this on its own but its needed for the next steps.
        if(x0 > x1) { float tmp = x0; x0 = x1; x1 = tmp; }
        if(y0 > y1) { float tmp = y0; y0 = y1; y1 = tmp; }

        // Calculate integer boundaries //! Integer x1 and y1 are not inclusive as for Minecraft Vanilla's behaviour.
        final int x0i = (int)Math.ceil (x0);
        final int x1i = (int)Math.floor(x1) + 1;
        final int y0i = (int)Math.ceil (y0);
        final int y1i = (int)Math.floor(y1) + 1;

        // Calculate antialiased weights (Alpha 0-1)
        final float lWeight =  x0i - x0;
        final float rWeight = -x1  + x1i; //FIXME this might be inverted. prob needs 1 - n
        final float tWeight =  y0i - y0;
        final float bWeight = -y1  + y1i; //FIXME this might be inverted. prob needs 1 - n
        final float tlWeight = (tWeight + lWeight) / 2f;
        final float trWeight = (tWeight + rWeight) / 2f;
        final float blWeight = (bWeight + lWeight) / 2f;
        final float brWeight = (bWeight + rWeight) / 2f;

        // Fill the geometry
        raw.fill(pipeline, x0i,     y0i,     x1i - 1, y1i - 1,            col           ); // Solid square
        raw.fill(pipeline, x0i - 1, y0i,     x0i,     y1i - 1, alphaColor(col, lWeight )); // Left   edge
        raw.fill(pipeline, x1i - 1, y0i,     x1i,     y1i - 1, alphaColor(col, rWeight )); // Right  edge
        raw.fill(pipeline, x0i,     y0i - 1, x1i - 1, y0i,     alphaColor(col, tWeight )); // Top    edge
        raw.fill(pipeline, x0i,     y1i - 1, x1i - 1, y1i,     alphaColor(col, bWeight )); // Bottom edge
        raw.fill(pipeline, x0i - 1, y0i - 1, x0i,     y0i,     alphaColor(col, tlWeight)); // Top-left     corner
        raw.fill(pipeline, x1i - 1, y0i - 1, x1i,     y0i,     alphaColor(col, trWeight)); // Top-right    corner
        raw.fill(pipeline, x0i - 1, y1i - 1, x0i,     y1i,     alphaColor(col, blWeight)); // Bottom-left  corner
        raw.fill(pipeline, x1i - 1, y1i - 1, x1i,     y1i,     alphaColor(col, brWeight)); // Bottom-right corner
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
        final float textScale = scaledFont.getScale();

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








    // Textures

	public void blit(final RenderPipeline renderPipeline, final Identifier texture, final int x, final int y, final float u, final float v, final int width, final int height, final int textureWidth, final int textureHeight) {
        raw.blit(renderPipeline, texture, x, y, u, v, width, height, textureWidth, textureHeight);
    }
	public void blit(final RenderPipeline renderPipeline, final Identifier texture, final int x, final int y, final float u, final float v, final int width, final int height, final int srcWidth, final int srcHeight, final int textureWidth, final int textureHeight) {
        raw.blit(renderPipeline, texture, x, y, u, v, width, height, srcWidth, srcHeight, textureWidth, textureHeight);
    }
	public void blit(final Identifier location, final int x0, final int y0, final int x1, final int y1, final float u0, final float u1, final float v0, final float v1) {
        raw.blit(location, x0, y0, x1, y1, u0, u1, v0, v1);
    }
	public void blit(final GpuTextureView textureView, final GpuSampler sampler, final int x0, final int y0, final int x1, final int y1, final float u0, final float u1, final float v0, final float v1) {
        raw.blit(textureView, sampler, x0, y0, x1, y1, u0, u1, v0, v1);
    }
	public void blitSprite(final RenderPipeline renderPipeline, final Identifier location, final int x, final int y, final int width, final int height) {
        raw.blitSprite(renderPipeline, location, x, y, width, height);
    }
	public void blitSprite(final RenderPipeline renderPipeline, final Identifier location, final int x, final int y, final int width, final int height, final float alpha) {
        raw.blitSprite(renderPipeline, location, x, y, width, height, alpha);
    }
	public void blitSprite(final RenderPipeline renderPipeline, final Identifier location, final int spriteWidth, final int spriteHeight, final int textureX, final int textureY, final int x, final int y, final int width, final int height) {
        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x, y, width, height);
    }
	public void blitSprite(final RenderPipeline renderPipeline, final TextureAtlasSprite sprite, final int x, final int y, final int width, final int height) {
        raw.blitSprite(renderPipeline, sprite, x, y, width, height);
    }






    // Floating point blit

    public void blit(final RenderPipeline renderPipeline, final Identifier texture, final float x, final float y, final float u, final float v, final float width, final float height, final int textureWidth, final int textureHeight) {
        final int col = 0xFFFFFFFF;
        final float x0 = x, y0 = y, x1 = x + width, y1 = y + height;
        final float u0 = u, v0 = v;

        // Calculate integer boundaries //! Integer x1 and y1 are not inclusive as for Minecraft Vanilla's behaviour.
        final int x0i = (int)Math.ceil (x0);
        final int x1i = (int)Math.floor(x1) + 1;
        final int y0i = (int)Math.ceil (y0);
        final int y1i = (int)Math.floor(y1) + 1;

        // Calculate antialiased weights (Alpha 0-1)
        final float lWeight =  x0i - x0;
        final float rWeight = -x1  + x1i; //FIXME this might be inverted. prob needs 1 - n
        final float tWeight =  y0i - y0;
        final float bWeight = -y1  + y1i; //FIXME this might be inverted. prob needs 1 - n
        final float tlWeight = (tWeight + lWeight) / 2f;
        final float trWeight = (tWeight + rWeight) / 2f;
        final float blWeight = (bWeight + lWeight) / 2f;
        final float brWeight = (bWeight + rWeight) / 2f;

        // Blit geometry
        raw.blit(renderPipeline, texture, x0i,     y0i,     u0 + (x0i     - x0), v0 + (y0i     - y0), x1i - 1 - x0i, y1i - 1 - y0i, textureWidth, textureHeight, col                      ); // Solid square
        raw.blit(renderPipeline, texture, x0i - 1, y0i,     u0 + (x0i - 1 - x0), v0 + (y0i     - y0), 1,             y1i - 1 - y0i, textureWidth, textureHeight, alphaColor(col, lWeight )); // Left   edge
        raw.blit(renderPipeline, texture, x1i - 1, y0i,     u0 + (x1i - 1 - x0), v0 + (y0i     - y0), 1,             y1i - 1 - y0i, textureWidth, textureHeight, alphaColor(col, rWeight )); // Right  edge
        raw.blit(renderPipeline, texture, x0i,     y0i - 1, u0 + (x0i     - x0), v0 + (y0i - 1 - y0), x1i - 1 - x0i, 1,             textureWidth, textureHeight, alphaColor(col, tWeight )); // Top    edge
        raw.blit(renderPipeline, texture, x0i,     y1i - 1, u0 + (x0i     - x0), v0 + (y1i - 1 - y0), x1i - 1 - x0i, 1,             textureWidth, textureHeight, alphaColor(col, bWeight )); // Bottom edge
        raw.blit(renderPipeline, texture, x0i - 1, y0i - 1, u0 + (x0i - 1 - x0), v0 + (y0i - 1 - y0), 1,             1,             textureWidth, textureHeight, alphaColor(col, tlWeight)); // Top-left     corner
        raw.blit(renderPipeline, texture, x1i - 1, y0i - 1, u0 + (x1i - 1 - x0), v0 + (y0i - 1 - y0), 1,             1,             textureWidth, textureHeight, alphaColor(col, trWeight)); // Top-right    corner
        raw.blit(renderPipeline, texture, x0i - 1, y1i - 1, u0 + (x0i - 1 - x0), v0 + (y1i - 1 - y0), 1,             1,             textureWidth, textureHeight, alphaColor(col, blWeight)); // Bottom-left  corner
        raw.blit(renderPipeline, texture, x1i - 1, y1i - 1, u0 + (x1i - 1 - x0), v0 + (y1i - 1 - y0), 1,             1,             textureWidth, textureHeight, alphaColor(col, brWeight)); // Bottom-right corner
    }


    public void blit(final RenderPipeline renderPipeline, final Identifier texture, final float x, final float y, final float u, final float v, final float width, final float height, final int srcWidth, final int srcHeight, final int textureWidth, final int textureHeight) {
        final int col = 0xFFFFFFFF;

        final float x0 = x, y0 = y, x1 = x + width, y1 = y + height;
        final float u0 = u, v0 = v;

        final int x0i = (int)Math.ceil (x0);
        final int x1i = (int)Math.floor(x1) + 1;
        final int y0i = (int)Math.ceil (y0);
        final int y1i = (int)Math.floor(y1) + 1;

        final float lWeight =  x0i - x0;
        final float rWeight = -x1  + x1i; //FIXME this might be inverted. prob needs 1 - n
        final float tWeight =  y0i - y0;
        final float bWeight = -y1  + y1i; //FIXME this might be inverted. prob needs 1 - n
        final float tlWeight = (tWeight + lWeight) / 2f;
        final float trWeight = (tWeight + rWeight) / 2f;
        final float blWeight = (bWeight + lWeight) / 2f;
        final float brWeight = (bWeight + rWeight) / 2f;

        final float scaleX = srcWidth  / width;
        final float scaleY = srcHeight / height;
        final int edgeSrcW = Math.max(1, Math.round(scaleX));
        final int edgeSrcH = Math.max(1, Math.round(scaleY));

        raw.blit(renderPipeline, texture, x0i,     y0i,     u0 + (x0i     - x0) * scaleX, v0 + (y0i     - y0) * scaleY, x1i - 1 - x0i, y1i - 1 - y0i, srcWidth,  srcHeight, textureWidth, textureHeight, col                      ); // Solid square
        raw.blit(renderPipeline, texture, x0i - 1, y0i,     u0 + (x0i - 1 - x0) * scaleX, v0 + (y0i     - y0) * scaleY, 1,             y1i - 1 - y0i, edgeSrcW,  srcHeight, textureWidth, textureHeight, alphaColor(col, lWeight )); // Left   edge
        raw.blit(renderPipeline, texture, x1i - 1, y0i,     u0 + (x1i - 1 - x0) * scaleX, v0 + (y0i     - y0) * scaleY, 1,             y1i - 1 - y0i, edgeSrcW,  srcHeight, textureWidth, textureHeight, alphaColor(col, rWeight )); // Right  edge
        raw.blit(renderPipeline, texture, x0i,     y0i - 1, u0 + (x0i     - x0) * scaleX, v0 + (y0i - 1 - y0) * scaleY, x1i - 1 - x0i, 1,             srcWidth,   edgeSrcH, textureWidth, textureHeight, alphaColor(col, tWeight )); // Top    edge
        raw.blit(renderPipeline, texture, x0i,     y1i - 1, u0 + (x0i     - x0) * scaleX, v0 + (y1i - 1 - y0) * scaleY, x1i - 1 - x0i, 1,             srcWidth,   edgeSrcH, textureWidth, textureHeight, alphaColor(col, bWeight )); // Bottom edge
        raw.blit(renderPipeline, texture, x0i - 1, y0i - 1, u0 + (x0i - 1 - x0) * scaleX, v0 + (y0i - 1 - y0) * scaleY, 1,             1,             edgeSrcW,   edgeSrcH, textureWidth, textureHeight, alphaColor(col, tlWeight)); // Top-left     corner
        raw.blit(renderPipeline, texture, x1i - 1, y0i - 1, u0 + (x1i - 1 - x0) * scaleX, v0 + (y0i - 1 - y0) * scaleY, 1,             1,             edgeSrcW,   edgeSrcH, textureWidth, textureHeight, alphaColor(col, trWeight)); // Top-right    corner
        raw.blit(renderPipeline, texture, x0i - 1, y1i - 1, u0 + (x0i - 1 - x0) * scaleX, v0 + (y1i - 1 - y0) * scaleY, 1,             1,             edgeSrcW,   edgeSrcH, textureWidth, textureHeight, alphaColor(col, blWeight)); // Bottom-left  corner
        raw.blit(renderPipeline, texture, x1i - 1, y1i - 1, u0 + (x1i - 1 - x0) * scaleX, v0 + (y1i - 1 - y0) * scaleY, 1,             1,             edgeSrcW,   edgeSrcH, textureWidth, textureHeight, alphaColor(col, brWeight)); // Bottom-right corner
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
        final float x0 = x, y0 = y, x1 = x + width, y1 = y + height;

        final int x0i = (int)Math.ceil (x0);
        final int x1i = (int)Math.floor(x1) + 1;
        final int y0i = (int)Math.ceil (y0);
        final int y1i = (int)Math.floor(y1) + 1;

        final float lWeight =  x0i - x0;
        final float rWeight = -x1  + x1i;
        final float tWeight =  y0i - y0;
        final float bWeight = -y1  + y1i;
        final float tlWeight = (tWeight + lWeight) / 2f;
        final float trWeight = (tWeight + rWeight) / 2f;
        final float blWeight = (bWeight + lWeight) / 2f;
        final float brWeight = (bWeight + rWeight) / 2f;

        raw.blitSprite(renderPipeline, location, x0i,     y0i,     x1i - 1 - x0i, y1i - 1 - y0i, alpha             );
        raw.blitSprite(renderPipeline, location, x0i - 1, y0i,     1,             y1i - 1 - y0i, alpha * lWeight  );
        raw.blitSprite(renderPipeline, location, x1i - 1, y0i,     1,             y1i - 1 - y0i, alpha * rWeight  );
        raw.blitSprite(renderPipeline, location, x0i,     y0i - 1, x1i - 1 - x0i, 1,             alpha * tWeight  );
        raw.blitSprite(renderPipeline, location, x0i,     y1i - 1, x1i - 1 - x0i, 1,             alpha * bWeight  );
        raw.blitSprite(renderPipeline, location, x0i - 1, y0i - 1, 1,             1,             alpha * tlWeight );
        raw.blitSprite(renderPipeline, location, x1i - 1, y0i - 1, 1,             1,             alpha * trWeight );
        raw.blitSprite(renderPipeline, location, x0i - 1, y1i - 1, 1,             1,             alpha * blWeight );
        raw.blitSprite(renderPipeline, location, x1i - 1, y1i - 1, 1,             1,             alpha * brWeight );
    }


    public void blitSprite(final RenderPipeline renderPipeline, final Identifier location, final int spriteWidth, final int spriteHeight, final int textureX, final int textureY, final float x, final float y, final float width, final float height) {
        final int col = 0xFFFFFFFF;
        final float x0 = x, y0 = y, x1 = x + width, y1 = y + height;

        final int x0i = (int)Math.ceil (x0);
        final int x1i = (int)Math.floor(x1) + 1;
        final int y0i = (int)Math.ceil (y0);
        final int y1i = (int)Math.floor(y1) + 1;

        final float lWeight =  x0i - x0;
        final float rWeight = -x1  + x1i;
        final float tWeight =  y0i - y0;
        final float bWeight = -y1  + y1i;
        final float tlWeight = (tWeight + lWeight) / 2f;
        final float trWeight = (tWeight + rWeight) / 2f;
        final float blWeight = (bWeight + lWeight) / 2f;
        final float brWeight = (bWeight + rWeight) / 2f;

        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x0i,     y0i,     x1i - 1 - x0i, y1i - 1 - y0i, col                      );
        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x0i - 1, y0i,     1,             y1i - 1 - y0i, alphaColor(col, lWeight ));
        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x1i - 1, y0i,     1,             y1i - 1 - y0i, alphaColor(col, rWeight ));
        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x0i,     y0i - 1, x1i - 1 - x0i, 1,             alphaColor(col, tWeight ));
        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x0i,     y1i - 1, x1i - 1 - x0i, 1,             alphaColor(col, bWeight ));
        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x0i - 1, y0i - 1, 1,             1,             alphaColor(col, tlWeight));
        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x1i - 1, y0i - 1, 1,             1,             alphaColor(col, trWeight));
        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x0i - 1, y1i - 1, 1,             1,             alphaColor(col, blWeight));
        raw.blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x1i - 1, y1i - 1, 1,             1,             alphaColor(col, brWeight));
    }


    public void blitSprite(final RenderPipeline renderPipeline, final TextureAtlasSprite sprite, final float x, final float y, final float width, final float height) {
        final int col = 0xFFFFFFFF;
        final float x0 = x, y0 = y, x1 = x + width, y1 = y + height;

        final int x0i = (int)Math.ceil (x0);
        final int x1i = (int)Math.floor(x1) + 1;
        final int y0i = (int)Math.ceil (y0);
        final int y1i = (int)Math.floor(y1) + 1;

        final float lWeight =  x0i - x0;
        final float rWeight = -x1  + x1i;
        final float tWeight =  y0i - y0;
        final float bWeight = -y1  + y1i;
        final float tlWeight = (tWeight + lWeight) / 2f;
        final float trWeight = (tWeight + rWeight) / 2f;
        final float blWeight = (bWeight + lWeight) / 2f;
        final float brWeight = (bWeight + rWeight) / 2f;

        raw.blitSprite(renderPipeline, sprite, x0i,     y0i,     x1i - 1 - x0i, y1i - 1 - y0i, col                      );
        raw.blitSprite(renderPipeline, sprite, x0i - 1, y0i,     1,             y1i - 1 - y0i, alphaColor(col, lWeight ));
        raw.blitSprite(renderPipeline, sprite, x1i - 1, y0i,     1,             y1i - 1 - y0i, alphaColor(col, rWeight ));
        raw.blitSprite(renderPipeline, sprite, x0i,     y0i - 1, x1i - 1 - x0i, 1,             alphaColor(col, tWeight ));
        raw.blitSprite(renderPipeline, sprite, x0i,     y1i - 1, x1i - 1 - x0i, 1,             alphaColor(col, bWeight ));
        raw.blitSprite(renderPipeline, sprite, x0i - 1, y0i - 1, 1,             1,             alphaColor(col, tlWeight));
        raw.blitSprite(renderPipeline, sprite, x1i - 1, y0i - 1, 1,             1,             alphaColor(col, trWeight));
        raw.blitSprite(renderPipeline, sprite, x0i - 1, y1i - 1, 1,             1,             alphaColor(col, blWeight));
        raw.blitSprite(renderPipeline, sprite, x1i - 1, y1i - 1, 1,             1,             alphaColor(col, brWeight));
    }
}
