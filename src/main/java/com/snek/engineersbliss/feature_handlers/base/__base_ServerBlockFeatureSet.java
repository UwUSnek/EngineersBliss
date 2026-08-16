package com.snek.engineersbliss.feature_handlers.base;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.Block;




//TODO REMOVE IF NOT USED
//TODO ServerFeatureSync should already handle this



/**
 * A __base_ServerFeatureSet that keeps track of the blocks it affects the rendering of.
 * ! This is used to refresh chunk sections and optimize branches in hot paths.
 */
public class __base_ServerBlockFeatureSet extends __base_ServerFeatureSet {
    // private final Set<Block> affectedBlocks;


    public __base_ServerBlockFeatureSet(final String id) {
        super(id);
        // affectedBlocks = new HashSet<>();
    }


    // @Override
    // protected <F extends __base_ServerFeature<?>> F registerFeature(@NotNull F feature) {
    //     if(feature instanceof @NotNull __base_BlockFeatureInterface blockFeature) {
    //         affectedBlocks.addAll(blockFeature.getAffectedBlocks());
    //     }
    //     return super.registerFeature(feature);
    // }


    // public boolean affects(final Block block) {
    //     return affectedBlocks.contains(block);
    // }
}
