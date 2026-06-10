package com.snek.engineersbliss.client.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.lighting.LevelLightEngine;




public class RenderFilterHandler {
    private RenderFilterHandler() { }




    private static final Map<Block, Boolean> enabledStates  = new HashMap<>();
    private static final Map<Block, Boolean> isolatedStates = new HashMap<>();
    public static void init() {
        BuiltInRegistries.BLOCK.forEach(block -> {
            setEnabled(block, true);
            setIsolated(block, false);
        });
        recalculate();
    }

    public static void  setEnabled(final Block block, final boolean  enabled) { enabledStates.put(block, enabled); }
    public static void setIsolated(final Block block, final boolean isolated) { isolatedStates.put(block, isolated); }
    public static boolean  getEnabled(final Block block) { return enabledStates.get(block); }
    public static boolean getIsolated(final Block block) { return isolatedStates.get(block); }




    private static final List<Block> activeBlocks = new ArrayList<>();
    public static List<Block> getActiveBlocks() { return activeBlocks; }

    public static void recalculate() {
        activeBlocks.clear();
        for(Entry<Block, Boolean> entry : isolatedStates.entrySet()) {
            if(entry.getValue().booleanValue()) activeBlocks.add(entry.getKey());
        }
        if(activeBlocks.isEmpty()) for(Entry<Block, Boolean> entry : enabledStates.entrySet()) {
            if(entry.getValue().booleanValue()) activeBlocks.add(entry.getKey());
        }
    }




    public static void refreshRendering() {

        // Force reload light on all loaded chunks
        ClientLevel level = Minecraft.getInstance().level;
        Minecraft minecraft = Minecraft.getInstance();
        int viewDist = minecraft.options.renderDistance().get() + 1;
        int camX = SectionPos.blockToSectionCoord(minecraft.player.blockPosition().getX());
        int camZ = SectionPos.blockToSectionCoord(minecraft.player.blockPosition().getZ());
        ClientChunkCache cache = level.getChunkSource();
        LevelLightEngine lightEngine = cache.getLightEngine();
        for (int cx = camX - viewDist; cx <= camX + viewDist; cx++) {
            for (int cz = camZ - viewDist; cz <= camZ + viewDist; cz++) {
                LevelChunk chunk = cache.getChunk(cx, cz, ChunkStatus.FULL, false);
                if (chunk == null) continue;
                ChunkPos pos = chunk.getPos();
                lightEngine.setLightEnabled(pos, false);
                lightEngine.setLightEnabled(pos, true);
                lightEngine.propagateLightSources(pos);
            }
        }
        lightEngine.runLightUpdates();

        // Mark all chunks for re-rendering (doesn't include light updates)
        Minecraft.getInstance().levelRenderer.allChanged();
    }
}
