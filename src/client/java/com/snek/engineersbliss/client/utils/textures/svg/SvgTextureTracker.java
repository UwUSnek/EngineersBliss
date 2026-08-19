package com.snek.engineersbliss.client.utils.textures.svg;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;




public final class SvgTextureTracker {
    private SvgTextureTracker() {}


    public static final class Entry {
        public final byte[] svgBytes;
        public final SvgMetadataSection meta;
        public final NativeImage[] cached = new NativeImage[SettingsServerFeatureSet.GUI_SCALE.getValues().size()];

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







    private static NativeImage copy(final NativeImage src) {
        final NativeImage out = new NativeImage(src.getWidth(), src.getHeight(), false);
        MemoryUtil.memCopy(src.getPointer(), out.getPointer(), src.getWidth() * src.getHeight() * 4L);
        return out;
    }


    /**
     * Creates a copy of the requested texture.
     * This also rasterizes and caches the texture on first request.
     */
    public static NativeImage acquire(final Identifier id, final int scaleIndex) {
        final @NotNull Entry e = REGISTRY.get(id);
        if(e == null) return null;
        NativeImage cached = e.cached[scaleIndex];
        if(cached == null) {
            final float scale = SettingsServerFeatureSet.GUI_SCALE.getValues().get(scaleIndex);
            cached = SvgRasterizer.rasterize(e.svgBytes, (int)(e.meta.width() * scale), (int)(e.meta.height() * scale));
            e.cached[scaleIndex] = cached;
        }
        return copy(cached);
    }







    /**
     * Finds the version of the specified sprite rasterized for the current GUI scale.
     * @param baseId The ID of the sprite, without scale suffix.
     * @return The complete sprite ID.
     */
    public static Identifier getOptimalSprite(final Identifier baseId) {
        return getOptimalSprite(baseId, ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.GUI_SCALE));
    }


    /**
     * Finds the version of the specified sprite rasterized for the provided GUI scale.
     * @param baseId The ID of the sprite, without scale suffix.
     * @param scale The target GUI Scale.
     * @return The complete sprite ID.
     */
    public static Identifier getOptimalSprite(final Identifier baseId, final int scaleIndex) {
        final int clamped = Math.clamp(scaleIndex, 0, SettingsServerFeatureSet.GUI_SCALE.getValues().size() - 1);
        return baseId.withSuffix(".x" + clamped);
    }
}