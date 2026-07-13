package com.snek.engineersbliss.client.utils.avif_textures;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.resources.Identifier;








/**
 * A class that keeps track of loaded AVIF textures.
 * This can be used by other classes to know if the returned texture is a placeholder or the one they requested.
 * ! This class depends on the AvifTextureReader mixin.
 */
public class AvifTextureTracker {
    private AvifTextureTracker() {}

    private static final Set<Identifier> loadedTextures = ConcurrentHashMap.newKeySet();
    private static final Map<Identifier, AvifAtlasMetadataSection> loadedTexturesMeta = new ConcurrentHashMap<>();




    public static void markLoaded(final Identifier id) {
        loadedTextures.add(id);
    }
    public static boolean isTextureReady(final Identifier id) {
        return loadedTextures.contains(id);
    }


    public static void registerAtlas(final Identifier id, final AvifAtlasMetadataSection meta) {
        loadedTexturesMeta.put(id, meta);
    }
    public static AvifAtlasMetadataSection getAtlasMeta(final Identifier id) {
        return loadedTexturesMeta.get(id);
    }




    public static float[] getUV(Identifier atlasId, int localSheetIdx, long timeMillis) {
        final AvifAtlasMetadataSection meta = getAtlasMeta(atlasId);
        if(meta == null) return new float[]{0f, 1f, 0f, 1f};

        final int sheetCol = localSheetIdx % meta.atlasCols();
        final int sheetRow = localSheetIdx / meta.atlasCols();

        final int frame = (int)((timeMillis / (1000L / meta.fps())) % meta.frameCount());
        final int sheetColsInFrames = meta.sheetWidth() / meta.frameWidth();
        final int frameCol = frame % sheetColsInFrames;
        final int frameRow = frame / sheetColsInFrames;

        final float atlasW = meta.atlasCols() * (float) meta.sheetWidth();
        final float atlasH = meta.atlasRows() * (float) meta.sheetHeight();

        final float sheetOffsetX = sheetCol * (float) meta.sheetWidth();
        final float sheetOffsetY = sheetRow * (float) meta.sheetHeight();

        final float u0 = (sheetOffsetX +  frameCol      * meta.frameWidth())  / atlasW;
        final float u1 = (sheetOffsetX + (frameCol + 1) * meta.frameWidth())  / atlasW;
        final float v0 = (sheetOffsetY +  frameRow      * meta.frameHeight()) / atlasH;
        final float v1 = (sheetOffsetY + (frameRow + 1) * meta.frameHeight()) / atlasH;

        return new float[]{u0, u1, v0, v1};
    }
}