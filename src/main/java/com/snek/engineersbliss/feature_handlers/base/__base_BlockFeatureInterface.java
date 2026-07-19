package com.snek.engineersbliss.feature_handlers.base;

import java.util.Set;

import net.minecraft.world.level.block.Block;




public interface __base_BlockFeatureInterface {


    public Set<Block> getAffectedBlocks();


    public default boolean affects(final Block block) {
        return getAffectedBlocks().contains(block);
    }
}
