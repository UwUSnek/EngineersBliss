package com.snek.engineersbliss.client.feature_handlers.overlays;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.snek.engineersbliss.client.mixin.accessors.PoweredRailBlockAccessor;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;




public class OverlaysHandler {
    private OverlaysHandler() {}


    private static Map<OverlayFeature, Boolean> features = new EnumMap<>(OverlayFeature.class);


    public static void init(){
        for(OverlayFeature feature : OverlayFeature.values()) {
            features.put(feature, true);
        }
    }


    public static void setFeature(final OverlayFeature feature, boolean value) {
        features.put(feature, value);
    }

    public static boolean getFeature(final OverlayFeature feature) {
        return features.get(feature);
    }






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
    public static int getRailLevel(BlockPos pos) {
        return powerLevelsCache.getOrDefault(pos, -1);
    }
    public static void depowerRail(final BlockPos pos) {
        powerLevelsCache.put(pos, 0);
    }
    public static void addRailSource(final BlockPos pos, final int level) {
        powerLevelsCache.merge(pos, level, Math::max);
    }




    /**
     * Populates the map with data from newly loaded chunks.
     * Call this from CHUNK_LOAD event.
     */
    public static void onChunkLoad(ClientLevel level, LevelChunk chunk) {
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
                            BlockState state = section.getBlockState(x, y, z);
                            if(state.getBlock() instanceof PoweredRailBlock rail) {
                                BlockPos pos = new BlockPos(minX + x, minY + y, minZ + z);

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
