package com.snek.engineersbliss.client.utils.textures;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;




public final class SvgTextureTracker {
    private SvgTextureTracker() {}


    public static final class Entry {
        public final byte[] svgBytes;
        public final SvgMetadataSection meta;
        // Index 0 = 1x, 1 = 2x, 2 = 3x, 3 = 4x. null until that scale has been requested
        public final NativeImage[] cached = new NativeImage[4];

        public Entry(final byte[] svgBytes, final SvgMetadataSection meta) {
            this.svgBytes = svgBytes;
            this.meta = meta;
        }
    }


    private static final Map<Identifier, Entry> REGISTRY = new ConcurrentHashMap<>();

    public static Entry getOrRegister(final Identifier id, final byte[] bytes, final SvgMetadataSection meta) {
        return REGISTRY.computeIfAbsent(id, k -> new Entry(bytes, meta));
    }

    public static boolean isRegistered(final Identifier baseId) {
        return REGISTRY.containsKey(baseId);
    }


    public static Entry get(final Identifier id) { return REGISTRY.get(id); }
    public static Map<Identifier, Entry> all() { return REGISTRY; }


    /**
     * Creates a copy of the requested texture.
     * This also rasterizes and caches the texture on first request.
     */
    public static NativeImage acquire(final Identifier id, final int scale) {
        final Entry e = REGISTRY.get(id);
        if(e == null) return null;
        final int idx = scale - 1;
        NativeImage master = e.cached[idx];
        if(master == null) {
            master = SvgRasterizer.rasterize(e.svgBytes, e.meta.width() * scale, e.meta.height() * scale);
            e.cached[idx] = master;
        }
        return SvgRasterizer.copy(master);
    }







    /**
     * Finds the version of the specified sprite rasterized for the current GUI scale.
     * @param baseId The ID of the sprite, without scale suffix.
     * @return The complete sprite ID.
     */
    public static Identifier getOptimalSprite(final Identifier baseId) {
        return getOptimalSprite(baseId, Minecraft.getInstance().getWindow().getGuiScale());
    }


    /**
     * Finds the version of the specified sprite rasterized for the provided GUI scale.
     * @param baseId The ID of the sprite, without scale suffix.
     * @param scale The target GUI Scale.
     * @return The complete sprite ID.
     */
    public static Identifier getOptimalSprite(final Identifier baseId, final int scale) {
        final int clamped = Math.clamp(scale, 1, 4);
        return baseId.withSuffix(".x" + clamped);
    }
}