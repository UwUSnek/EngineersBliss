package com.snek.engineersbliss.client.feature_handlers.overlays;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.OverlayAttachedDataComparator;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;
import com.snek.engineersbliss.client.mixin.accessors.PoweredRailBlockAccessor;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.data_types.Pair;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
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
        onFeatureToggle(feature);
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


    // A map that specified the proper attached data type for each Block
    // This must be updated manually when new types of attached data are added
    private static final Map<Block, Class<? extends __base_OverlayAttachedData>> attachedDataClasses = Map.of(
        Blocks.COMPARATOR, OverlayAttachedDataComparator.class
        // Blocks.COMPARATOR, OverlayAttachedDataComparator.class,
    );

    public static __base_OverlayAttachedData createAttachedData(final Level level, final BlockPos pos, final BlockState state) {
        try {
            final @Nullable var classType = attachedDataClasses.get(state.getBlock());
            if(classType == null) return null;
            return classType.getDeclaredConstructor(Level.class, BlockPos.class, BlockState.class).newInstance(level, pos, state);
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the data attached to the specified block position, only if it exists in the global map.
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
     * @param level The level the block is in. //TODO remove if unused
     * @param pos The position of the block.
     * @param state The current blockstate of the block. Redundant but helps performance.
     * @return A long value whose bits represent the features that are currently active on the block, or 0 if the block doesn't have any available feature.
     */
    public static long calcFeatureFlags(final Level level, final BlockPos pos, final BlockState state) {
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
        final long newFlags = calcFeatureFlags(level, pos, newState);
        if(newFlags != 0) {
            chunkFeatureMask.put(pos, Pair.from(newFlags, createAttachedData(level, pos, newState)));
        }
        else {
            chunkFeatureMask.remove(pos);
        }
    }




    /**
     * Updates the runtime map. This must be called whenever a chunk is loaded into the client's level.
     * @param chunk The new chunk.
     */
    public static void onChunkLoad(final LevelChunk chunk) {
        final ChunkPos chunkPos = chunk.getPos();
        final Level level = chunk.getLevel();
        final var chunkFeatureMask = featureMask.computeIfAbsent(chunkPos, k -> new HashMap<>());
        final int minX = chunkPos.getMinBlockX();
        final int minZ = chunkPos.getMinBlockZ();

        // For each chunk section
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
                                final long newFlags = calcFeatureFlags(level, pos, state);
                                if(newFlags != 0) {
                                    chunkFeatureMask.put(pos, Pair.from(newFlags, createAttachedData(level, pos, state)));
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Updates the runtime map. This must be called whenever a chunk is unloaded from the client's level.
     * @param pos The chunk position of the chunk that was unloaded.
     */
    public static void onChunkUnload(final ChunkPos pos) {
        featureMask.remove(pos);
    }


    /**
     * Clears the runtime map. This must be called whenever the client changes level.
     */
    public static void onLevelChange() {
        featureMask.clear();
    }




    /**
     * Updates the feature mask of all chunks containing affected blocks. This must be called when a feature is toggled.
     * @param feature The feature that was toggled.
     */
    public static void onFeatureToggle(final OverlayFeature feature) {

        // For each loaded chunk
        for(final LevelChunk chunk : MinecraftUtils.getLoadedChunks()) {
            final ChunkPos chunkPos = chunk.getPos();
            final Level level = chunk.getLevel();
            final var chunkFeatureMask = featureMask.computeIfAbsent(chunkPos, k -> new HashMap<>());
            final int minX = chunkPos.getMinBlockX();
            final int minZ = chunkPos.getMinBlockZ();

            // For each chunk section
            final var sections = chunk.getSections();
            for(int i = 0; i < sections.length; ++i) {
                final LevelChunkSection section = sections[i];
                final int minY = chunk.getMinY() + (i * LevelChunkSection.SECTION_HEIGHT);

                // If the section contains an affected block
                if(!section.hasOnlyAir() && section.maybeHas(state -> feature.affects(state.getBlock()))) {

                    // For each block in the section
                    for(int x = 0; x < LevelChunkSection.SECTION_WIDTH; x++) {
                        for(int y = 0; y < LevelChunkSection.SECTION_HEIGHT; y++) {
                            for(int z = 0; z < LevelChunkSection.SECTION_WIDTH; z++) {

                                // Recalculate the state
                                final BlockPos pos = new BlockPos(minX + x, minY + y, minZ + z);
                                final BlockState state = level.getBlockState(pos);
                                chunkFeatureMask.compute(
                                    pos,
                                    (k, v) -> {

                                        // Calculate new flags
                                        final long newFlags = v == null ?
                                            calcFeatureFlags(level, pos, level.getBlockState(pos)) :
                                            updateFeatureFlags(v.getFirst(), feature.getFlagBit(), getFeature(feature))
                                        ;

                                        // put/remove the entry depending on the value
                                        return newFlags != 0 ? Pair.from(newFlags, createAttachedData(level, pos, state)) : null;
                                    }
                                );
                            }
                        }
                    }
                }
            }
        }
    }









































    //TODO actually implement this stuff, maybe in a dedicated class?
    //TODO actually implement this stuff, maybe in a dedicated class?
    //TODO actually implement this stuff, maybe in a dedicated class?
    //TODO actually implement this stuff, maybe in a dedicated class?
    //TODO actually implement this stuff, maybe in a dedicated class?
    //TODO actually implement this stuff, maybe in a dedicated class?


    // A map that stores the current power level of each powered and activator rail block in the currently loaded client level
    //! Changing dimension makes all of the new rails that load it replace the previous values.
    //! These mix with the previous dimensions' data, but that's not as issue since the blockstate resolver
    //! only ever checks rails that actually exist in the current level.
    //FIXME this can cause bad numbers in case of outdated block palettes.
    //FIXME clear the map when changing dimension
    private static final Map<BlockPos, Integer> powerLevelsCache = new HashMap<>();


    /**
     * Returns the cached power level of the rail block at the specified position.
     * @param pos The position of the rail block to check.
     * @return The power level (0 to 9), or -1 if the block isn't a powerable rail or the value hasn't been cached yet.
     */
    public static int getRailLevel(final BlockPos pos) {
        return powerLevelsCache.getOrDefault(pos, -1);
    }
    public static void depowerRail(final BlockPos pos) {
        powerLevelsCache.put(pos, 0);
    }
    public static void addRailSource(final BlockPos pos, final int level) {
        powerLevelsCache.merge(pos, level, Math::max);
    }



//FIXME make this a generic "world load populator" function instead of checking for rail levels only
    /**
     * Populates the map with data from newly loaded chunks.
     * Call this from CHUNK_LOAD event.
     */
    public static void onChunkLoad(final ClientLevel level, final LevelChunk chunk) {
        final ChunkPos chunkPos = chunk.getPos();
        final int minX = chunkPos.getMinBlockX();
        final int minZ = chunkPos.getMinBlockZ();

        // For each chunk section
        final var sections = chunk.getSections();
        for(int i = 0; i < sections.length; ++i) {
            final LevelChunkSection section = sections[i];
            final int minY = chunk.getMinY() + (i * LevelChunkSection.SECTION_HEIGHT);

            // If it contains powered or activator rails
            if(!section.hasOnlyAir() && section.maybeHas(state -> state.getBlock() instanceof PoweredRailBlock)) {

                // For each block in the section
                for(int x = 0; x < 16; x++) {
                    for(int y = 0; y < 16; y++) {
                        for(int z = 0; z < 16; z++) {

                            // Force it to store its power level in the map by calling PoweredRailBlock.findPoweredRailSignal on it
                            final BlockState state = section.getBlockState(x, y, z);
                            if(state.getBlock() instanceof final PoweredRailBlock rail) {
                                final BlockPos pos = new BlockPos(minX + x, minY + y, minZ + z);

                                //! PoweredRailBlock.findPoweredRailSignal needs to be called on the current block type as the check also tests for that.
                                //! Boolean parameter defines the direction in which the checks move, so calling this twice is required (forwards and backwards)

                                //! Checks start at depth 0 and end at depth 8 (9 powered blocks on each direction, including the source)
                                //! Checks start at depth 0 and end at depth 8 (9 powered blocks)

                                ((PoweredRailBlockAccessor)state.getBlock()).invokeFindPoweredRailSignal(level, pos, state, true, 0);
                                ((PoweredRailBlockAccessor)state.getBlock()).invokeFindPoweredRailSignal(level, pos, state, false, 0);
                                //TODO this might need a rendering refresh
                            }
                        }
                    }
                }
            }
        }
    }



    //TODO remove after implementing the stuff
    public void todo (){


            //FIXME move to overlays
            // //! Rail power level isn't stored by Minecraft so this needs custom power source lookup logic
            // if(force || AltTexturesHandler.getFeature(AltTextureFeature.RAIL_POWER_LEVELS)) {
            //     ret.add("rails/power_levels/" + AltTexturesHandler.getRailLevel(blockPos));
            // }



            //FIXME move to overlays
            // if(force || AltTexturesHandler.getFeature(AltTextureFeature.REDSTONE_WIRE_POWER_LEVELS)) {
            //     ret.add("redstone_wire/power_levels/" + state.getValue(RedStoneWireBlock.POWER));
            // }
    }
}
