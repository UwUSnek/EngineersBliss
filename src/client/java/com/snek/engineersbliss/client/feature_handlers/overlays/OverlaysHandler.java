package com.snek.engineersbliss.client.feature_handlers.overlays;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.ComparatorAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.RailAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.feature_handlers.ServerFeatureSync;
import com.snek.engineersbliss.feature_handlers.overlays.OverlaysServerFeatureSet;
import com.snek.engineersbliss.utils.scheduler.LoopTaskHandler;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;

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



    /**
     * Initializes the overlay handler and registers any required event listener.
     * This must be called in the mod's initializer function.
     */
    public static void init() {

        // Register listeners
        ClientChunkEvents.CHUNK_LOAD  .register((level, chunk) -> onChunkLoad(chunk));
        ClientChunkEvents.CHUNK_UNLOAD.register((level, chunk) -> onChunkUnload(chunk.getPos()));
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((client, level) -> onLevelChange());
        //! onFeatureToggle is called internally
        //! onBlockChanged is called by a mixin that intercepts ClientLevel.setBlock
    }








    // A map containing all the computed custom data of all features on all currently visible blocks.
    //! Map is initially created with capacity 5000, while internal maps depend entirely on their contents.
    //! Outer map stays allocated for the entire lifetime of the client to improve performance on level changes.
    private static final Map<ChunkPos, Map<BlockPos, @Nullable __base_OverlayAttachedData>> worldFeatureDataMap = HashMap.newHashMap(5000);
    public  static       Map<ChunkPos, Map<BlockPos, @Nullable __base_OverlayAttachedData>> getWorldFeatureDataMap() { return worldFeatureDataMap; }


    // A map that specifies the proper attached data type for each Block
    // ! This must be updated manually when new types of attached data are added
    private static final Map<Block, TriFunction<Level, BlockPos, BlockState, ? extends __base_OverlayAttachedData>> attachedDataSuppliers = Map.of(
        Blocks.REDSTONE_WIRE,  (a, b, c) -> null,
        Blocks.COMPARATOR,     ComparatorAttachedData::new,
        Blocks.POWERED_RAIL,   RailAttachedData::new,
        Blocks.ACTIVATOR_RAIL, RailAttachedData::new,
        Blocks.STRUCTURE_VOID, (a, b, c) -> null,
        Blocks.BARRIER,        (a, b, c) -> null,
        Blocks.LIGHT,          (a, b, c) -> null
    );

    public static __base_OverlayAttachedData createAttachedData(final Level level, final BlockPos pos, final BlockState state) {
        final var supplier = attachedDataSuppliers.get(state.getBlock());
        if(supplier == null) {
            EngineerSBliss.LOGGER.error("Missing attached data supplier function for block {}", state.getBlock().getDescriptionId());
            return null;
        }
        else {
            return supplier.apply(level, pos, state);
        }
    }

    /**
     * Updates the data attached to the specified block position, only if it exists in the global map.
     * ! This should be used for external updates that aren't already handled by the automatic update detection system,
     * ! such as update packets received from the server or other unique update events.
     * @param pos The position of the block.
     * @param newData The new data to attach.
     */
    public static void updateAttachedData(final BlockPos pos, final __base_OverlayAttachedData newData) {
        final var chunkFeatureDataMap = worldFeatureDataMap.get(MinecraftUtils.blockPosToChunk(pos));
        if(chunkFeatureDataMap != null) {
            chunkFeatureDataMap.put(pos, newData);
        }
    }



    /**
     * Updates the runtime map. This must be called whenever a block is changed anywhere in the client's level.
     * @param level The level.
     * @param pos The position of the block.
     * @param newState The new blockstate.
     */
    public static void onBlockChanged(final Level level, final BlockPos pos, final BlockState newState) {
        if(!level.isClientSide()) return;
        final @NotNull var chunkFeatureDataMap = worldFeatureDataMap.computeIfAbsent(MinecraftUtils.blockPosToChunk(pos), k -> new HashMap<>());

        // Calculate new data and put/remove the entry depending on the value
        if(ServerFeatureSync.stateHasFeatures(newState)) {
            chunkFeatureDataMap.put(pos, createAttachedData(level, pos, newState));
        }
        else {
            chunkFeatureDataMap.remove(pos);
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
        final @NotNull ChunkPos chunkPos = chunk.getPos();
        final @NotNull Level level = chunk.getLevel();


        // Wait for the chunk's neighbours to load in. Cancel the loop task and continue when they finally do
        waitingForNeighbours.put(chunkPos, ClientScheduler.loop(0, 4, () -> {
            if(!MinecraftUtils.areChunkNeighboursLoaded(level, chunkPos)) return;
            final LoopTaskHandler handler = waitingForNeighbours.get(chunkPos);
            if(handler != null) {
                handler.cancel();
                waitingForNeighbours.remove(chunkPos);
            }

            // For each chunk section
            final @NotNull var chunkFeatureDataMap = worldFeatureDataMap.computeIfAbsent(chunkPos, k -> new HashMap<>());
            final int minX = chunkPos.getMinBlockX();
            final int minZ = chunkPos.getMinBlockZ();
            final @NotNull var sections = chunk.getSections();
            for(int i = 0; i < sections.length; ++i) {
                final @NotNull LevelChunkSection section = sections[i];
                final int minY = chunk.getMinY() + (i * LevelChunkSection.SECTION_HEIGHT);

                // If the section contains blocks with features
                if(!section.hasOnlyAir() && section.maybeHas(state -> ServerFeatureSync.stateHasFeaturesFromSet(state, OverlaysServerFeatureSet.INSTANCE))) {

                    // For each block in the section
                    for(int x = 0; x < LevelChunkSection.SECTION_WIDTH; x++) {
                        for(int y = 0; y < LevelChunkSection.SECTION_HEIGHT; y++) {
                            for(int z = 0; z < LevelChunkSection.SECTION_WIDTH; z++) {
                                final @NotNull BlockPos pos = new BlockPos(minX + x, minY + y, minZ + z);
                                final @NotNull BlockState state = level.getBlockState(pos);

                                //If the block has features, compute the attached data and update the map
                                if(ServerFeatureSync.stateHasFeaturesFromSet(state, OverlaysServerFeatureSet.INSTANCE)) {
                                    chunkFeatureDataMap.put(pos, createAttachedData(level, pos, state));
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
        worldFeatureDataMap.remove(pos);
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
        worldFeatureDataMap.clear();
        waitingForNeighbours.forEach((k, v) -> {
            if(v != null) v.cancel();
        });
        waitingForNeighbours.clear();
    }
}