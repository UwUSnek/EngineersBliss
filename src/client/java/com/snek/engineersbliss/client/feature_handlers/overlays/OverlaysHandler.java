package com.snek.engineersbliss.client.feature_handlers.overlays;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.ComparatorAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.RailAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.data_types.Pair;
import com.snek.engineersbliss.client.utils.scheduler.LoopTaskHandler;
import com.snek.engineersbliss.client.utils.scheduler.Scheduler;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;




public class OverlaysHandler {
    private OverlaysHandler() {}


    // A map containing all available feature and their current state
    private static Map<OverlayFeature, Boolean> features = new EnumMap<>(OverlayFeature.class);



    /**
     * Initializes the overlay handler and registers any required event listener.
     * This must be called in the mod's initializer function.
     */
    public static void init(){
        for(final OverlayFeature feature : OverlayFeature.values()) {
            features.put(feature, true);
        }

        // Register listeners
        ClientChunkEvents.CHUNK_LOAD  .register((level, chunk) -> onChunkLoad(chunk));
        ClientChunkEvents.CHUNK_UNLOAD.register((level, chunk) -> onChunkUnload(chunk.getPos()));
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((client, level) -> onLevelChange());
        //! onFeatureToggle is called internally
        //! onBlockChanged is called by a mixin that intercepts ClientLevel.setBlock
    }



    /**
     * Sets a new value for the specified feature.
     * @param feature The feature.
     * @param value The new value.
     */
    public static void setFeature(final OverlayFeature feature, final boolean value) {
        features.put(feature, value);
    }

    /***
     * Fetches the current value of the specified feature.
     * @param feature The feature.
     * @return The current value.
     */
    public static boolean getFeature(final OverlayFeature feature) {
        return features.get(feature);
    }







    // A map containing all the states of all features on all currently visible blocks.
    //! Map is initially created with capacity 5000, while internal maps depend entirely on their contents.
    //! Outer map stays allocated for the entire lifetime of the client to improve performance on level changes.
    private static final Map<ChunkPos, Map<BlockPos, Pair<Long, @Nullable __base_OverlayAttachedData>>> featureMask = HashMap.newHashMap(5000);
    public static Map<ChunkPos, Map<BlockPos, Pair<Long, @Nullable __base_OverlayAttachedData>>> getFeatureMask() { return featureMask; }


    // A map that specifies the proper attached data type for each Block
    // ! This must be updated manually when new types of attached data are added
    private static final Map<Block, TriFunction<Level, BlockPos, BlockState, ? extends __base_OverlayAttachedData>> attachedDataSuppliers = Map.of(
        Blocks.REDSTONE_WIRE,  (a, b, c) -> null,
        Blocks.COMPARATOR,     ComparatorAttachedData::new,
        Blocks.POWERED_RAIL,   RailAttachedData::new,
        Blocks.ACTIVATOR_RAIL, RailAttachedData::new
    );

    public static __base_OverlayAttachedData createAttachedData(final Level level, final BlockPos pos, final BlockState state) {
        final var supplier = attachedDataSuppliers.get(state.getBlock());
        if(supplier == null) {
            EngineerSBliss.LOGGER.error("Missing attached data supplier function for block {}", state.getBlock().getDescriptionId());
            return null;
        }
        return supplier.apply(level, pos, state);
    }

    /**
     * Updates the data attached to the specified block position, only if it exists in the global map.
     * ! This should be used for external updates that aren't already handled by the automatic update detection system,
     * ! such as update packets received from the server or other unique update events.
     * @param pos The position of the block.
     * @param newData The new data to attach.
     */
    public static void updateAttachedData(final BlockPos pos, __base_OverlayAttachedData newData) {
        final var chunkFeatureMask = featureMask.get(MinecraftUtils.blockPosToChunk(pos));
        if(chunkFeatureMask != null) {
            final var pair = chunkFeatureMask.get(pos);
            if(pair != null) {
                pair.setSecond(newData);
            }
        }
    }




    /**
     * Calculates the flags of the specified block based on the current settings, the block's state and its surroundings.
     * @param state The current blockstate of the block. Redundant but helps performance.
     * @return A long value whose bits represent the features that are currently active on the block, or 0 if the block doesn't have any available feature.
     */
    public static long calcFeatureFlags(final BlockState state) {
        long r = 0;

        final Block block = state.getBlock();
        for(final OverlayFeature feature : OverlayFeature.values()) {
            if(feature.affects(block) && getFeature(feature)) {
                r |= feature.getFlagBit();
            }
        }
        return r;
    }
    public static long updateFeatureFlags(final long mask, final long flag, final boolean featureState) {
        return featureState ? mask | flag : mask & ~flag;
    }





    /**
     * Updates the runtime map. This must be called whenever a block is changed anywhere in the client's level.
     * @param level The level.
     * @param pos The position of the block.
     * @param newState The new blockstate.
     */
    public static void onBlockChanged(final Level level, final BlockPos pos, final BlockState newState) {
        if(!level.isClientSide()) return;
        final var chunkFeatureMask = featureMask.computeIfAbsent(MinecraftUtils.blockPosToChunk(pos), k -> new HashMap<>());

        // Calculate new flags and put/remove the entry depending on the value
        final long newFlags = calcFeatureFlags(newState);
        if(newFlags != 0) {
            chunkFeatureMask.put(pos, Pair.from(newFlags, createAttachedData(level, pos, newState)));
        }
        else {
            chunkFeatureMask.remove(pos);
        }
    }








    // These keep track of chunks that are currently waiting for their 8 neighbour to load in in order to calculate overlay data
    private static Map<ChunkPos, LoopTaskHandler> waitingForNeighbours = new HashMap<>();




    /**
     * Updates the runtime map. This must be called whenever a chunk is loaded into the client's level.
     * @param chunk The new chunk.
     */
    public static void onChunkLoad(final LevelChunk chunk) {
        if(!chunk.getLevel().isClientSide()) return;
        final ChunkPos chunkPos = chunk.getPos();
        final Level level = chunk.getLevel();


        // Wait for the chunk's neighbours to load in. Cancel the loop task and continue when they finally do
        waitingForNeighbours.put(chunkPos, Scheduler.loop(0, 4, () -> {
            if(!MinecraftUtils.areChunkNeighboursLoaded(level, chunkPos)) return;
            waitingForNeighbours.get(chunkPos).cancel();
            waitingForNeighbours.remove(chunkPos);

            // For each chunk section
            final var chunkFeatureMask = featureMask.computeIfAbsent(chunkPos, k -> new HashMap<>());
            final int minX = chunkPos.getMinBlockX();
            final int minZ = chunkPos.getMinBlockZ();
            final var sections = chunk.getSections();
            for(int i = 0; i < sections.length; ++i) {
                final LevelChunkSection section = sections[i];
                final int minY = chunk.getMinY() + (i * LevelChunkSection.SECTION_HEIGHT);

                // If the section contains blocks with features
                if(!section.hasOnlyAir() && section.maybeHas(state -> OverlayFeature.hasFeature(state.getBlock()))) {

                    // For each block in the section
                    for(int x = 0; x < LevelChunkSection.SECTION_WIDTH; x++) {
                        for(int y = 0; y < LevelChunkSection.SECTION_HEIGHT; y++) {
                            for(int z = 0; z < LevelChunkSection.SECTION_WIDTH; z++) {
                                final BlockPos pos = new BlockPos(minX + x, minY + y, minZ + z);
                                final BlockState state = level.getBlockState(pos);
                                final Block block = state.getBlock();

                                //If the block has features
                                if(OverlayFeature.hasFeature(block)) {

                                    // Calculate all flags and put them in the map if not empty
                                    final long newFlags = calcFeatureFlags(state);
                                    if(newFlags != 0) {
                                        chunkFeatureMask.put(pos, Pair.from(newFlags, createAttachedData(level, pos, state)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }));
    }




    /**
     * Updates the runtime map. This must be called whenever a chunk is unloaded from the client's level.
     * @param pos The chunk position of the chunk that was unloaded.
     */
    public static void onChunkUnload(final ChunkPos pos) {
        featureMask.remove(pos);
        final LoopTaskHandler handler = waitingForNeighbours.get(pos);
        if(handler != null) {
            handler.cancel();
            waitingForNeighbours.remove(pos);
        }
    }


    /**
     * Clears the runtime map. This must be called whenever the client changes level.
     */
    public static void onLevelChange() {
        featureMask.clear();
        waitingForNeighbours.forEach((k, v) -> {
            if(v != null) v.cancel();
        });
        waitingForNeighbours.clear();
    }
}