package com.snek.engineersbliss.client.utils.textures.mp4;

import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Nullable;








public final class Mp4TextureTracker {
    private Mp4TextureTracker() {}

    private static final long STALE_NANOS = 500_000_000L;


    private static final class Entry {
        final Path  path;
        Mp4Player player;
        long        lastUsedNanos;

        Entry(final Path path) {
            this.path = path;
        }
    }




    private static final Map<Identifier, Entry> REGISTRY = new ConcurrentHashMap<>();


    public static void getOrRegister(final Identifier id, final Path path) {
        REGISTRY.compute(id, (k, existing) -> {
            if(existing != null && existing.player != null) existing.player.close();
            return new Entry(path);
        });
    }


    public static boolean isRegistered(final Identifier id) { return REGISTRY.containsKey(id); }


    public static void clearAllBut(final Set<Identifier> ids) {
        REGISTRY.forEach((id, e) -> {
            if(!ids.contains(id) && e.player != null) e.player.close();
        });
        REGISTRY.keySet().removeIf(id -> !ids.contains(id));
    }




    /** Advances playback and returns the current GPU texture. Starts decoding on first call, stops it after STALE_NANOS of disuse. */
    public static synchronized @Nullable Identifier getCurrentTexture(final Identifier id) {
        final long  now = System.nanoTime();
        final Entry e   = REGISTRY.get(id);
        if(e == null) return null;

        e.lastUsedNanos = now;
        if(e.player == null) e.player = new Mp4Player(id, e.path);
        e.player.tick();

        for(final Entry other : REGISTRY.values()) {
            if(other.player != null && now - other.lastUsedNanos > STALE_NANOS) {
                other.player.close();
                other.player = null;
            }
        }

        return e.player.getTextureId();
    }
}