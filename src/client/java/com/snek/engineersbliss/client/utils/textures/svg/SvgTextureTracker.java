package com.snek.engineersbliss.client.utils.textures.svg;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.system.MemoryUtil;




public final class SvgTextureTracker {
    private SvgTextureTracker() {}


    public static final class Entry {
        public final byte[] svgBytes;
        public final Map<Long, NativeImage> cached = new ConcurrentHashMap<>();
        public Entry(final byte[] svgBytes) {
            this.svgBytes = svgBytes;
        }
    }



    private static final Map<Identifier, Entry> REGISTRY   = new ConcurrentHashMap<>();
    private static final Set<Identifier>        REGISTERED = ConcurrentHashMap.newKeySet(); // sized ids bound to GPU textures

    public static Entry getOrRegister(final Identifier id, final byte[] bytes) {
        return REGISTRY.computeIfAbsent(id, k -> new Entry(bytes));
    }

    public static boolean isRegistered(final Identifier baseId) { return REGISTRY.containsKey(baseId); }
    public static Entry get(final Identifier id) { return REGISTRY.get(id); }
    public static Map<Identifier, Entry> all() { return REGISTRY; }

    private static long packSize(final int w, final int h) {
        return (((long)w) << 32) | (h & 0xFFFFFFFFL);
    }

    public static void retainOnly(final Set<Identifier> validIds) {
        REGISTRY.keySet().removeIf(id -> !validIds.contains(id));
        REGISTERED.removeIf(sizedId -> validIds.stream().noneMatch(base -> sizedId.getPath().startsWith(base.getPath() + "."))); //TODO optimize this. make it more realiable too
    }

    private static NativeImage copy(final NativeImage src) {
        final NativeImage out = new NativeImage(src.getWidth(), src.getHeight(), false);
        MemoryUtil.memCopy(src.getPointer(), out.getPointer(), src.getWidth() * src.getHeight() * 4L);
        return out;
    }

    /** Returns the texture at the requested pixel size. Rasterizes a new size if needed. Caller owns the returned copy. */
    public static NativeImage acquire(final Identifier id, final int width, final int height) {
        final Entry e = REGISTRY.get(id);
        if(e == null) return null;
        final long key = packSize(width, height);
        NativeImage cached = e.cached.get(key);
        if(cached == null) {
            cached = SvgRasterizer.rasterize(e.svgBytes, width, height);
            e.cached.put(key, cached);
        }
        return copy(cached);
    }

    /**
     * Returns an Identifier bound to a GPU texture rasterized at (width, height).
     */
    public static Identifier bindForSize(final Identifier baseId, final int width, final int height) {
        final Identifier sizedId = baseId.withSuffix("." + width + "x" + height);
        if(REGISTERED.add(sizedId)) {
            final NativeImage img = acquire(baseId, width, height);
            Minecraft.getInstance().getTextureManager().register(sizedId, new DynamicTexture(sizedId::toString, img));
        }
        return sizedId;
    }
}