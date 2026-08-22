package com.snek.engineersbliss.client.feature_handlers.rendering;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.mixin.accessors.BlockEntityRenderersAccessor;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.feature_handlers.rendering.RenderingServerFeatureSet;
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




public class RenderingFilterHandler {
    private RenderingFilterHandler() {}


    private static final Map<BlockState, Boolean> stateShouldRenderCache = new ConcurrentHashMap<>();


    private static int lightRecalcMax = 0;
    private static int lightRecalcProgress = 0;
    public static int getLightRecalcMax() { return lightRecalcMax; }
    public static int getLightRecalcProgress() { return lightRecalcProgress; }


    private static final Map<Block, Boolean> enabledBlocks  = new ConcurrentHashMap<>();
    private static final Map<Block, Boolean> isolatedBlocks = new ConcurrentHashMap<>();
    public static void  setEnabled(final Block block, final boolean  enabled) { enabledBlocks.put(block, enabled); }
    public static void setIsolated(final Block block, final boolean isolated) { isolatedBlocks.put(block, isolated); }
    public static boolean  getEnabled(final Block block) { return enabledBlocks.get(block); }
    public static boolean getIsolated(final Block block) { return isolatedBlocks.get(block); }




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
    public static void init() {
        findBlocksWithBlockEntityRendering();
        resetStateCache();

        //! This inclused the 3 air blocks but it doesn't really matter, they only need to be hidden in the UI
        BuiltInRegistries.BLOCK.forEach(block -> {
            setEnabled(block, true);
            setIsolated(block, false);
        });
    }




    public static void resetStateCache() {
        stateShouldRenderCache.clear();
    }




    public void resetFilters() {
        RenderingFilterHandler.init(); //! This calls resetStatetCache()
        MinecraftUtils.refreshRendering();
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
     * ! This doesn't takes into account the static block entity model system for performance and maintainability reasons.
     * @param state The blockstate to check.
     * @return True if the block state should render, false otherwise.
     */
    public static boolean shouldStateRender(final BlockState state) {
        return stateShouldRenderCache.computeIfAbsent(state, k -> calculateRenderStateForState(k));
    }


    @SuppressWarnings("java:S1126")
    private static boolean calculateRenderStateForState(final BlockState state) {
        if(state == null) return false;
        final Block block = state.getBlock();


        // Check isolated blocks and enabled blocks lists
        final boolean isAnyIsolated = !isolatedBlocks.isEmpty();
        final boolean r = isAnyIsolated ? isolatedBlocks.containsKey(block) : enabledBlocks.containsKey(block);
        if(!r) return false;


        // Check category rendering
        final boolean hasFluid                = !state.getFluidState().isEmpty();
        final boolean hasBlockEntityRendering = state.hasBlockEntity() && blocksWithBlockEntityRendering.contains(block);
        final boolean isPlainBlock            = !hasFluid && !hasBlockEntityRendering;
        if(hasFluid                && !ClientFeatureSync.getFeatureB(RenderingServerFeatureSet.RENDER_FLUIDS))         return false;
        if(hasBlockEntityRendering && !ClientFeatureSync.getFeatureB(RenderingServerFeatureSet.RENDER_BLOCK_ENTITIES)) return false;
        if(isPlainBlock            && !ClientFeatureSync.getFeatureB(RenderingServerFeatureSet.RENDER_BLOCKS))         return false;
        return true;
    }
}