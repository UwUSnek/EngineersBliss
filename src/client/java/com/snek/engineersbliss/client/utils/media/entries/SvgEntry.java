package com.snek.engineersbliss.client.utils.media.entries;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2L;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.screens.pause_screen.PauseScreenContentAccessorInterface;
import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.media.support.SvgRasterizer;

import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
















// Specialized Entry for SVG media. Handles rasterization and caches images for each size request.
public class SvgEntry extends __base_MediaEntry {
    private final byte[] svgBytes;
    private final Map<Long, NativeImage> cached;




    private static long packSize(final int w, final int h) {
        return (((long)w) << 32) | (h & 0xFFFFFFFFL);
    }
    private static Vector2L unpackSize(final long packedSize) {
        final int w = (int)(packedSize >> 32);
        final int h = (int)(packedSize & 0xFFFFFFFFL);
        return new Vector2L(w, h);
    }
    private Identifier calcTextureIdFor(final long packedSize) {
        return getResourceId().withPrefix("media_tracker.svg." + packedSize + ".");
    }
    public void addCacheFor(final long key, final NativeImage data) {
        cached.put(key, data);
    }
    public byte[] getSvgBytes() {
        return svgBytes;
    }




    /**
     * Finds the cached image whose size is closest to (width, height) by squared distance.
     * This MUST NOT be called when no cached image is available.
     * @return The ID of the closest texture. Never null.
     */
    private @NotNull Identifier findClosest(final int desiredWidth, final int desiredHeight) {
        //TODO add debug checks. make sure there is at least one cached texture before this can run. This should always be the case but shold be checked for extra safety
        long best = 0;
        long bestDistance = Long.MAX_VALUE;
        for(final @NotNull var e : cached.entrySet()) {
            final long k = e.getKey();
            final int keyWidth  = (int)(k >> 32);
            final int keyHeight = (int)(k & 0xFFFFFFFFL);
            final long dw = Math.abs(keyWidth  - desiredWidth);
            final long dh = Math.abs(keyHeight - desiredHeight);
            final long distance = dw * dw + dh * dh;
            if(distance < bestDistance) {
                best = k;
                bestDistance = distance;
            }
        }
        return calcTextureIdFor(best);
    }




    private boolean isRasterizationAllowed() {
        final @Nullable Screen screen = MinecraftUtils.getScreen();
        /**/ if(screen instanceof @NotNull    UiScreen s) return !s.isGuiScaleTransitioning();
        else if(screen instanceof @NotNull PauseScreen s) return !((PauseScreenContentAccessorInterface)s).eb$getEmbedded().isGuiScaleTransitioning();
        return true;
    }




    @Override
    public @Nullable Identifier __internal_requestTextureFor(int desiredWidth, int desiredHeight) {

        // Try to retrieve the cached texture. Return an exact match if it exists.
        final long packedSize = packSize(desiredWidth, desiredHeight);
        final Identifier targetTextureId = calcTextureIdFor(packedSize);
        if(cached.containsKey(packedSize)) return targetTextureId;


        // If no exact cached texture is available.
        else {

            // If rasterization is allowed or required, rasterize a new cache image and return it.
            if(cached.isEmpty() || isRasterizationAllowed()) {
                final var rasterized = SvgRasterizer.rasterize(svgBytes, desiredWidth, desiredHeight);
                cached.put(packedSize, rasterized);
                return targetTextureId;
            }

            // Otherwise, return the cached image closest to the requested size.
            else {
                return findClosest(desiredWidth, desiredHeight);
            }
        }
    }




    //! The width and height are the SVG's declared view box.
    public SvgEntry(final Identifier id, final byte[] svgBytes) {
        super(id, 1, 1); //FIXME calculate width and height from view box data
        this.svgBytes = svgBytes;
        this.cached = new ConcurrentHashMap<>();
    }
}