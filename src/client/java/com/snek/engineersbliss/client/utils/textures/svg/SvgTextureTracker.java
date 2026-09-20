package com.snek.engineersbliss.client.utils.textures.svg;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.utils.data_types.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;








public final class SvgTextureTracker {
    private SvgTextureTracker() {}


    public static final class Entry {
        private final byte[] svgBytes;
        private final Map<Long, Pair<NativeImage, Identifier>> cached;

        public void addCacheFor(final long key, final NativeImage data, final Identifier sizedId) {
            cached.put(key, Pair.from(data, sizedId));
        }
        public byte[] getSvgBytes() {
            return svgBytes;
        }
        public NativeImage getDataFor(final long key) {
            final @Nullable var sizedImageCache = cached.get(key);
            return sizedImageCache == null ? null : sizedImageCache.getFirst();
        }
        public Identifier getIdFor(final long key) {
            final @Nullable var sizedImageCache = cached.get(key);
            return sizedImageCache == null ? null : sizedImageCache.getSecond();
        }

        public Entry(final byte[] svgBytes) {
            this.svgBytes = svgBytes;
            this.cached = new ConcurrentHashMap<>();
        }
    }








    private static final Map<Identifier, Entry> REGISTRY   = new ConcurrentHashMap<>();     // IDs containing texture data. Not sized.
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


    public static void clearAllBut(final Set<Identifier> baseIds) {
        REGISTRY.keySet().removeIf(id -> !baseIds.contains(id));
        REGISTERED.removeIf(sizedId -> baseIds.stream().noneMatch(base -> sizedId.getPath().startsWith(base.getPath() + "."))); //TODO optimize this. make it more realiable too
    }


    private static NativeImage copy(final NativeImage src) {
        final NativeImage out = new NativeImage(src.getWidth(), src.getHeight(), false);
        MemoryUtil.memCopy(src.getPointer(), out.getPointer(), src.getWidth() * src.getHeight() * 4L);
        return out;
    }




    /** Returns the texture at the requested pixel size. Rasterizes a new size if needed. Caller owns the returned copy. */
    public static NativeImage acquire(final Identifier id, final Identifier targetSizedId, final int width, final int height) {
        final Entry e = REGISTRY.get(id);
        if(e == null) {
            return null;
        }
        else {
            final long key = packSize(width, height);
            NativeImage cached = e.getDataFor(key);
            if(cached == null) {
                cached = SvgRasterizer.rasterize(e.svgBytes, width, height);
                e.addCacheFor(key, cached, targetSizedId);
            }
            return copy(cached);
        }
    }




    /** Finds the cached entry whose size is closest to (width, height) by squared distance. */
    private static Identifier findClosest(final @NotNull Identifier baseId, final int width, final int height) {
        final Entry entry = REGISTRY.get(baseId);
        if(entry == null) {
            return null;
        }
        else {
            Identifier best = null;
            long bestDistance = Long.MAX_VALUE;
            for(final @NotNull var e : entry.cached.entrySet()) {
                final long k = e.getKey();
                final int keyWidth  = (int)(k >> 32);
                final int keyHeight = (int)(k & 0xFFFFFFFFL);
                final long dw = Math.abs(keyWidth  - width);
                final long dh = Math.abs(keyHeight - height);
                final long distance = dw * dw + dh * dh;
                if(distance < bestDistance) {
                    best = e.getValue().getSecond();
                    bestDistance = distance;
                }
            }
            return best;
        }
    }

    /**
     * Returns an Identifier bound to a GPU texture rasterized at (width, height).
     * @param baseId The base ID of the texture.
     * @param width The target width of the texture.
     * @param height The target height of the texture.
     * @param allowRasterization Whether to allow synchronous rasterization of the SVG file when a cached image isn't available for the requested size.
     *     Passing false will make the system use the cached image closest in size to the requested width and height.
     */
    public static Identifier requestForSize(final @NotNull Identifier baseId, final int width, final int height, final boolean allowRasterization) {
        final Identifier targetSizedId = baseId.withSuffix("." + width + "x" + height);
        if(!REGISTERED.contains(targetSizedId)) {
            if(allowRasterization || (REGISTRY.containsKey(baseId) && REGISTRY.get(baseId).cached.isEmpty())) {
                final @Nullable NativeImage img = acquire(baseId, targetSizedId, width, height);
                if(img == null) {
                    return null;
                }
                else {
                    Minecraft.getInstance().getTextureManager().register(targetSizedId, new DynamicTexture(targetSizedId::toString, img));
                    REGISTERED.add(targetSizedId);
                    return targetSizedId;
                }
            }
            else {
                return findClosest(baseId, width, height);
            }
        }
        else {
            return targetSizedId;
        }
    }
}