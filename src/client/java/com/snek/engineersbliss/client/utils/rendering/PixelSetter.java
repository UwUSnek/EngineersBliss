package com.snek.engineersbliss.client.utils.rendering;



/**
 * An functional interface used to set individual pixels of a texture or render target.
 * This can be useful to support multiple targets without duplicating the core rendering logic.
 */
@FunctionalInterface
public interface PixelSetter {

    /**
     * Implementations must overlay the provided pixel on top of the existing color.
     * @param x The X position of the pixel.
     * @param y The Y position of the pixel.
     * @param color The color of the pixel to overlay.
     */
    public void set(final int x, final int y, final int color);
}
