package com.snek.engineersbliss.feature_handlers.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.world.level.block.Block;




/**
 * A ServerAnalogueFeature that keeps track of the blocks it affects the rendering of.
 * ! This is used to refresh chunk sections and optimize branches in hot paths.
 */
public class ServerBlockAnalogueFeature<T> extends ServerAnalogueFeature<T> implements __base_BlockFeatureInterface {
    private final Set<Block> affectedBlocks = new HashSet<>();


    public Set<Block> getAffectedBlocks() {
        return affectedBlocks;
    }


    public ServerBlockAnalogueFeature(final String id, final T min, final T max, final T defaultValue, final List<Block> affectedBlocks) {
        super(id, min, max, defaultValue);
        this.affectedBlocks.addAll(affectedBlocks);
    }

}
