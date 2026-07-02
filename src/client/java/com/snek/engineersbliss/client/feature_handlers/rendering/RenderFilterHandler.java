package com.snek.engineersbliss.client.feature_handlers.rendering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.mixin.accessors.BlockEntityRenderersAccessor;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.utils.scheduler.LoopTaskHandler;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
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





    private static final Set<Block> blocksWithBlockEntityRendering = new HashSet<>();
    private static void findBlocksWithBlockEntityRendering() {

        // For each block, for each block entity type
        BuiltInRegistries.BLOCK.forEach(block -> {
            BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {

                // If the block has a block entity
                //! isValid takes a blockstate but only checks the Block stored in it so we can just pass the default one
                if(blockEntityType.isValid(block.defaultBlockState())) {

                    // If the block entity has custom rendering
                    if(BlockEntityRenderersAccessor.getProviders().containsKey(blockEntityType)) {
                        blocksWithBlockEntityRendering.add(block);
                    }
                }
            });
        });
    }




    /**
     * Init function. Must be called during the mod's initialization.
     * This function can be called again to reset the filter back to the default state.
     */
    public static void init(
        final boolean defaultTargetHiddenBlocks,
        final boolean defaultRenderBlockOutlines,
        final boolean defaultRenderBlocks,
        final boolean defaultRenderBlockEntities,
        final boolean defaultRenderFluids
    ) {
        findBlocksWithBlockEntityRendering();

        targetHiddenBlocks = defaultTargetHiddenBlocks;
        renderBlockOutlines = defaultRenderBlockOutlines;
        renderBlocks        = defaultRenderBlocks;
        renderBlockEntities = defaultRenderBlockEntities;
        renderFluids        = defaultRenderFluids;

        //! This inclused the 3 air blocks but it doesn't really matter, they only ned to be hidden in the UI
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




    /**
     * Recalculates the light in all loaded chunks.
     * This will freeze the game for a few ticks/seconds/minutes depending on machine's specs.
     */
    public static void recalculateLight() {
        final @NotNull Minecraft minecraft = Minecraft.getInstance();
        final @NotNull ClientLevel level = minecraft.level;
        if(level == null) return;

        final LevelLightEngine lightEngine = level.getChunkSource().getLightEngine();
        final int minY = level.getMinY();
        final int maxY = level.getMaxY();
        final List<LevelChunk> chunks = MinecraftUtils.getLoadedChunks();
        lightRecalcMax = chunks.size();
        lightRecalcProgress = 0;

        final int[] index = { 0 };
        final LoopTaskHandler[] handle = { null };
        handle[0] = ClientScheduler.loop(1, 1, () -> {
            if(index[0] >= chunks.size()) {
                handle[0].cancel();
                return;
            }

            final LevelChunk chunk = chunks.get(index[0]++);
            final int baseX = chunk.getPos().getMinBlockX();
            final int baseZ = chunk.getPos().getMinBlockZ();

            for(int x = 0; x < LevelChunkSection.SECTION_WIDTH; x++) {
                for(int z = 0; z < LevelChunkSection.SECTION_WIDTH; z++) {
                    for(int y = maxY; y >= minY; y--) {
                        lightEngine.checkBlock(new BlockPos(baseX + x, y, baseZ + z));
                    }
                }
            }

            lightEngine.runLightUpdates();
            ++lightRecalcProgress;
        });
    }




    /**
     * Checks if the specified block state should render, based on the current rendering filter settings.
     * @param state The blockstate of the block to check.
     * @return True if the block should render, false ot
     */
    public static boolean shouldBlockRender(final BlockState state) {
        if(state == null) return false;


        //Check category rendering. Return false if disabled
        final boolean hasBlockEntityRendering = state.hasBlockEntity() && blocksWithBlockEntityRendering.contains(state.getBlock());
        if(
            !RenderFilterHandler.getRenderFluids()        && !state.getFluidState().isEmpty()  ||
            !RenderFilterHandler.getRenderBlockEntities() && hasBlockEntityRendering           ||
            !RenderFilterHandler.getRenderBlocks()        && state.getFluidState().isEmpty() && !hasBlockEntityRendering
        ) {
            return false;
        }


        // If rendering of the block category is enabled, check the individual filters
        return RenderFilterHandler.getActiveBlocks().contains(state.getBlock());
    }
}