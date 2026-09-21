package com.snek.engineersbliss.client.utils.textures.mp4;

import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;








public final class Mp4TextureTracker {
    private static final long STALE_MS = 500L;
    private Mp4TextureTracker() {}


    private static final class Entry {
        final Path filePath;
        Mp4Player  videoPlayer;
        long       lastRequestTime;
        boolean    loading;
        //! Loading TRUE means "The player doesn't exist yet and a thread is currently taking care of that. Don't try to create a new one."

        Entry(final Path path) {
            this.filePath = path;
            this.videoPlayer = null;
            this.lastRequestTime = 0;
            this.loading = false;
        }
    }




    private static final Map<Identifier, Entry> REGISTRY = new ConcurrentHashMap<>();


    public static void getOrRegister(final Identifier id, final Path path) {
        REGISTRY.compute(id, (k, existing) -> {
            if(existing != null && existing.videoPlayer != null) existing.videoPlayer.close();
            return new Entry(path);
        });
    }


    public static boolean isRegistered(final Identifier id) { return REGISTRY.containsKey(id); }


    public static void clearAllBut(final Set<Identifier> ids) {
        REGISTRY.forEach((id, e) -> {
            if(!ids.contains(id) && e.videoPlayer != null) e.videoPlayer.close();
        });
        REGISTRY.keySet().removeIf(id -> !ids.contains(id));
    }




    /**
     * Advances playback and returns the current GPU texture.
     * Starts decoding on first call, stops it after STALE_MS without requests.
     *
     * Returns a placeholder texture while the first frame is loading.
     */
    public static synchronized @Nullable Identifier getCurrentTexture(final Identifier id) {
        final long now = System.currentTimeMillis();
        final Entry e = REGISTRY.get(id);
        if(e == null) {
            return null; //TODO return a "missing media file" texture instead of null?
            //TODO null works but it just displays Minecraft's default missing texture checkerboard which is rly ugly
        }
        else {
            e.lastRequestTime = now;

            // Create video player asynchronously if needed
            if(e.videoPlayer == null) {
                if(!e.loading) {
                    e.loading = true;
                    CompletableFuture.supplyAsync(() -> new Mp4Player(id, e.filePath)).thenAccept(player -> {
                        synchronized(Mp4TextureTracker.class) {
                            e.videoPlayer = player;
                            e.loading = false;
                        }
                    });
                }
                return null; //TODO return placeholder texture
            }

            // If player is available, advance its frame and close stale players
            else {
                e.videoPlayer.tick();
                for(final @NotNull Entry other : REGISTRY.values()) {
                    if(other.videoPlayer != null && now - other.lastRequestTime > STALE_MS) {
                        other.videoPlayer.close();
                        other.videoPlayer = null;
                    }
                }
                return e.videoPlayer.getTextureId();
            }
        }
    }
}