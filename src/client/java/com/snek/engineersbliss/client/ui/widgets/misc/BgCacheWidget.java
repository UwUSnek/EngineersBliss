package com.snek.engineersbliss.client.ui.widgets.misc;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.ui.UiGraphics;
import com.snek.engineersbliss.client.utils.Layout;




/**
 * An interface shared by any widget that implements a background cache system
 */
public interface BgCacheWidget {


    public float getXF();
    public float getYF();
    public float getWidthF();
    public float getHeightF();
    public boolean isGuiScaleTransitioning();
    public TextureCache getBgTextureCache();



    /**
     * Draws the cached background to the provided UiGraphics, recomputing it if needed.
     * This must be called from the widget's rendering function, before any foreground and overlay is drawn.
     * @param graphics The UiGraphics to draw to.
     * @param mouseX The X position of the mouse.
     * @param mouseY The Y position of the mouse.
     * @param a
     */
    public default void extractBackground(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        final float w = getWidthF();
        final float h = getHeightF();
        final float guiScale = SettingsFeatureHandler.getCurrentGuiScale();
        final int pixelW = Math.max(1, Math.round(w * guiScale));
        final int pixelH = Math.max(1, Math.round(h * guiScale));


        // Draw background color if needed
        final int bgColor = getBgBaseColor();
        if((bgColor & 0xFF000000) != 0) {
            graphics.fill(getXF(), getYF(), getXF() + getWidthF(), getYF() + getHeightF(), bgColor);
        }


        // Draw background cache if present
        final @Nullable TextureCache bgCache = getBgTextureCache();
        if(bgCache != null) {
            if(!isGuiScaleTransitioning()) {
                bgCache.update(pixelW, pixelH, image -> drawCachedBackground(image, pixelW, pixelH));
            }
            bgCache.blit(graphics, getXF(), getYF(), w, h);
        }
    }




    /**
     * Redraws the background of the widget to update the texture cache. Uses local coordinates.
     * Defaults to filling the widget's box with getBgBaseColor().
     * @param img The output image to draw to.
     * @param w The width of the image and widget.
     * @param h The height of the image and widget.
     */
    public default void drawCachedBackground(final NativeImage img, final int w, final int h) {
        // Empty by default
    }




    /**
     * Marks the background texture cache as dirty, forcing it to be redrawn before the next frame.
     */
    public default void markBgDirty() {
        final @Nullable TextureCache bgCache = getBgTextureCache();
        if(bgCache != null) {
            getBgTextureCache().markDirty();
        }
    }



    public default int getBgBaseColor() {
        return Layout.bgColor;
    }
}
