package com.snek.engineersbliss.client.ui.widgets.misc;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.gui.GuiGraphicsExtractor;




/**
 * An interface shared by any widget that implements a background cache system
 */
public interface BgCacheWidget {


    public int getX();
    public int getY();
    public int getWidth();
    public int getHeight();
    public boolean isGuiScaleTransitioning();
    public TextureCache getBgTextureCache();



    /**
     * Draws the cached background to the provided GuiGraphicsExtractor, recomputing it if needed.
     * This must be called from the widget's rendering function, before any foreground and overlay is drawn.
     * @param graphics The GuiGraphicsExtractor to draw to.
     * @param mouseX The X position of the mouse.
     * @param mouseY The Y position of the mouse.
     * @param a
     */
    public default void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        final int w = getWidth();
        final int h = getHeight();
        final float guiScale = SettingsFeatureHandler.getCurrentGuiScale()
        final int pixelW = Math.max(1, Math.round(w * guiScale));
        final int pixelH = Math.max(1, Math.round(h * guiScale));

        // Draw background cache if present
        final @Nullable TextureCache bgCache = getBgTextureCache();
        if(bgCache != null) {
            if(!isGuiScaleTransitioning()) {
                bgCache.update(pixelW, pixelH, image -> drawCachedBackground(image, pixelW, pixelH));
            }
            bgCache.blit(graphics, getX(), getY(), w, h);
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
        RenderingUtils.fillImageArea(img, 0, 0, w, h, getBgBaseColor());
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
