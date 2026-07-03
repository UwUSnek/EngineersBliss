package com.snek.engineersbliss.client.screens;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.resources.Identifier;



/**
 * A class that keeps track of loaded AVIF textures.
 * This can be used by other classes to know if the returned texture is a placeholder or the one they requested.
 */
public class AvifTextureTracker {
    private AvifTextureTracker() {}

    private static final Set<Identifier> loadedTextures = ConcurrentHashMap.newKeySet();


    public static void markLoaded(Identifier id) {
        loadedTextures.add(id);
    }
    public static boolean isTextureReady(Identifier id) {
        return loadedTextures.contains(id);
    }
}