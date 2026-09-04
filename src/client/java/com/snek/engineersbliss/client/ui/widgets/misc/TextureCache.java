package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.ui.UiGraphics;
import com.snek.engineersbliss.client.ui.base.ScreenMixinAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;








/**
 * TextureCache allows expensive draw operations to be performed on an instance, storing the result in an off-screen texture.
 * The stored texture can then be drawn using the .blit operation, which only cosists of one GPU call and is much faster to process.
 */
public class TextureCache implements AutoCloseable {

    private static int nextId = 0;

    private final Identifier location;
    private @Nullable DynamicTexture texture;
    private int width  = -1;
    private int height = -1;
    private boolean dirty = false;


    /**
     * Creates a TextureCache that frees its memory on its own when the screen is closed.
     * @param screen The screen to track.
     */
    public TextureCache(final Screen screen) {
        this();
        ((ScreenMixinAccessor)screen).eb$registerTextureCacheForClose(this);
    }

    /**
     * Creates a TextureCache that doesn't free its memory on its own.
     * Call .close() when the texture is discarded to avoid memory leaks.
     */
    public TextureCache() {
        this.location = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "dynamic_texture_cache/_" + ++nextId);
    }




    /**
     * Repaints the texture if marked dirty or if it differs from the size of the currently cached texture.
     * @param width The width of the texture.
     * @param width The height of the texture.
     */
    public void update(final int width, final int height, final Consumer<NativeImage> painter) {
        if(texture == null || dirty || width != this.width || height != this.height) {

            // useCalloc=true initializes the buffer to 0 so unpainted pixels start transparent
            final NativeImage image = new NativeImage(NativeImage.Format.RGBA, Math.max(1, width), Math.max(1, height), true);
            painter.accept(image);

            final DynamicTexture newTexture = new DynamicTexture(() -> "", image);
            Minecraft.getInstance().getTextureManager().register(location, newTexture);

            if(texture != null) texture.close(); // Close the old GPU texture to avoid memory leaks
            texture = newTexture;
            this.width  = width;
            this.height = height;
            this.dirty = false;
        }
    }




    /**
     * Draws the cached texture at the provided coordinates.
     * The texture is stretched to fit the provided width and height.
     * @param graphics The UiGraphics to blit to.
     * @param x The X position of the texture.
     * @param y The Y position of the texture.
     * @param w The final width of the drawn texture.
     * @param h The final height of the drawn texture.
     */
    public void blit(final UiGraphics graphics, final float x, final float y, final float w, final float h) {
        if(texture == null) return;
        graphics.blit(RenderPipelines.GUI_TEXTURED, location, x, y, 0, 0, w, h, width, height);
    }


    @Override
    public void close() {
        if(texture != null) {
            texture.close();
            texture = null;
            width = height = -1;
        }
    }



    public void markDirty() {
        dirty = true;
    }
}