package com.snek.engineersbliss.client.utils.rendering;



/**
 * An functional interface used to fill square areas of a texture or render target.
 * This can be useful to support multiple targets without duplicating the core rendering logic.
 */
@FunctionalInterface
public interface PixelFiller {
    public void fill(final int x0, final int y0, final int x1, final int y1, final int color);
}
