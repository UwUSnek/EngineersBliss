package com.snek.engineersbliss.client.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.lighting.LevelLightEngine;




public class RenderFilterHandler {
    private RenderFilterHandler() { }




    private static final Map<Block, Boolean> enabledStates  = new HashMap<>();
    private static final Map<Block, Boolean> isolatedStates = new HashMap<>();
    private static boolean targetHiddenBlocks;

    public static void init(final boolean defaultTargetHiddenBlocks) {
        targetHiddenBlocks = defaultTargetHiddenBlocks;
        BuiltInRegistries.BLOCK.forEach(block -> {
            setEnabled(block, true);
            setIsolated(block, false);
        });
        recalculate();
    }

    public static void  setEnabled(final Block block, final boolean  enabled) { enabledStates.put(block, enabled); }
    public static void setIsolated(final Block block, final boolean isolated) { isolatedStates.put(block, isolated); }
    public static void setTargetHiddenBlocks(final boolean newTargetHiddenBlocks) { targetHiddenBlocks = newTargetHiddenBlocks; }
    public static boolean  getEnabled(final Block block) { return enabledStates.get(block); }
    public static boolean getIsolated(final Block block) { return isolatedStates.get(block); }
    public static boolean getTargetHiddenBlocks() { return targetHiddenBlocks; }




    private static final List<Block> activeBlocks = new ArrayList<>(); //FIXME replace with hash something so lookup is quick
    public static List<Block> getActiveBlocks() { return activeBlocks; }

    public static void recalculate() {
        activeBlocks.clear();
        for(final Entry<Block, Boolean> entry : isolatedStates.entrySet()) {
            if(entry.getValue().booleanValue()) activeBlocks.add(entry.getKey());
        }
        if(activeBlocks.isEmpty()) for(final Entry<Block, Boolean> entry : enabledStates.entrySet()) {
            if(entry.getValue().booleanValue()) activeBlocks.add(entry.getKey());
        }
    }





    public static void refreshRendering() {

        // Mark all chunks for re-rendering (doesn't include light updates)
        Minecraft.getInstance().levelRenderer.allChanged();
    }




    public static void recalculateLight() {

        final Minecraft minecraft = Minecraft.getInstance();
        final ClientLevel level = minecraft.level;
        if(level == null) return;

        final int viewDist = minecraft.options.renderDistance().get() + 1;
        final int camX = SectionPos.blockToSectionCoord(minecraft.player.blockPosition().getX());
        final int camZ = SectionPos.blockToSectionCoord(minecraft.player.blockPosition().getZ());
        final ClientChunkCache cache = level.getChunkSource();
        final LevelLightEngine lightEngine = cache.getLightEngine();

        for(int radius = 0; radius <= viewDist; radius++) {
            for(int cx = camX - radius; cx <= camX + radius; cx++) {
                for(int cz = camZ - radius; cz <= camZ + radius; cz++) {
                    if(Math.abs(cx - camX) != radius && Math.abs(cz - camZ) != radius) continue;
                    final LevelChunk chunk = cache.getChunk(cx, cz, ChunkStatus.FULL, false);
                    if(chunk == null) continue;
                    for(int x = 0; x < 16; x++) {
                        for(int z = 0; z < 16; z++) {
                            final int worldX = chunk.getPos().getMinBlockX() + x;
                            final int worldZ = chunk.getPos().getMinBlockZ() + z;
                            for(int y = level.getMaxY() - 1; y >= level.getMinY(); y--) {
                                lightEngine.checkBlock(new BlockPos(worldX, y, worldZ));
                            }
                            lightEngine.runLightUpdates();
                        }
                    }
                }
            }
        }
    }
    //FIXME optimize light refresh
    //FIXME optimize light refresh
    //FIXME optimize light refresh
}