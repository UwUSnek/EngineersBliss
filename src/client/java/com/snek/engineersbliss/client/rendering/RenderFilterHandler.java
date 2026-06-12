package com.snek.engineersbliss.client.rendering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.scheduler.LoopTaskHandler;
import com.snek.engineersbliss.client.utils.scheduler.Scheduler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.lighting.LevelLightEngine;




public class RenderFilterHandler {
    private RenderFilterHandler() { }

    private static int lightRecalcMax = 0;
    private static int lightRecalcProgress = 0;
    public static int getLightRecalcMax() { return lightRecalcMax; }
    public static int getLightRecalcProgress() { return lightRecalcProgress; }




    private static final Map<Block, Boolean> enabledStates  = new HashMap<>();
    private static final Map<Block, Boolean> isolatedStates = new HashMap<>();
    private static boolean targetHiddenBlocks;
    public static void  setEnabled(final Block block, final boolean  enabled) { enabledStates.put(block, enabled); }
    public static void setIsolated(final Block block, final boolean isolated) { isolatedStates.put(block, isolated); }
    public static void setTargetHiddenBlocks(final boolean newTargetHiddenBlocks) { targetHiddenBlocks = newTargetHiddenBlocks; }
    public static boolean  getEnabled(final Block block) { return enabledStates.get(block); }
    public static boolean getIsolated(final Block block) { return isolatedStates.get(block); }
    public static boolean getTargetHiddenBlocks() { return targetHiddenBlocks; }




    private static boolean renderBlockOutlines;
    private static boolean renderBlocks;
    private static boolean renderBlockEntities;
    private static boolean renderFluids;
    public static void setRenderBlockOutlines(final boolean newRenderBlockOutlines) { renderBlockOutlines = newRenderBlockOutlines; }
    public static void setRenderBlocks       (final boolean newRenderBlocks)        { renderBlocks        = newRenderBlocks;        }
    public static void setRenderBlockEntities(final boolean newRenderBlockEntities) { renderBlockEntities = newRenderBlockEntities; }
    public static void setRenderFluids       (final boolean newRenderFluids)        { renderFluids        = newRenderFluids;        }
    public static boolean getRenderBlockOutlines() { return renderBlockOutlines; }
    public static boolean getRenderBlocks       () { return renderBlocks;        }
    public static boolean getRenderBlockEntities() { return renderBlockEntities; }
    public static boolean getRenderFluids       () { return renderFluids;        }




    // Init function. Must be called during the mod's initialization
    public static void init(
        final boolean defaultTargetHiddenBlocks,
        final boolean defaultRenderBlockOutlines,
        final boolean defaultRenderBlocks,
        final boolean defaultRenderBlockEntities,
        final boolean defaultRenderFluids
    ) {
        targetHiddenBlocks = defaultTargetHiddenBlocks;
        renderBlockOutlines = defaultRenderBlockOutlines;
        renderBlocks        = defaultRenderBlocks;
        renderBlockEntities = defaultRenderBlockEntities;
        renderFluids        = defaultRenderFluids;

        BuiltInRegistries.BLOCK.forEach(block -> {
            setEnabled(block, true);
            setIsolated(block, false);
        });
        recalculate();
    }




    private static final Set<Block> activeBlocks = new HashSet<>();
    public static Set<Block> getActiveBlocks() { return activeBlocks; }

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




    /**
     * Recalculates the light in all loaded chunks.
     * This will freeze the game for a few ticks/seconds/minutes depending on machine's specs.
     */
    public static void recalculateLight() {
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientLevel level = minecraft.level;
        if(level == null) return;

        final LevelLightEngine lightEngine = level.getChunkSource().getLightEngine();
        final int minY = level.getMinY();
        final int maxY = level.getMaxY();
        final List<LevelChunk> chunks = MinecraftUtils.getLoadedChunks();
        lightRecalcMax = chunks.size();
        lightRecalcProgress = 0;

        final int[] index = { 0 };
        final LoopTaskHandler[] handle = { null };
        handle[0] = Scheduler.loop(1, 1, () -> {
            if(index[0] >= chunks.size()) {
                handle[0].cancel();
                return;
            }

            final LevelChunk chunk = chunks.get(index[0]++);
            final int baseX = chunk.getPos().getMinBlockX();
            final int baseZ = chunk.getPos().getMinBlockZ();

            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    for(int y = maxY; y >= minY; y--) {
                        lightEngine.checkBlock(new BlockPos(baseX + x, y, baseZ + z));
                    }
                }
            }

            lightEngine.runLightUpdates();
            ++lightRecalcProgress;
        });
    }
}