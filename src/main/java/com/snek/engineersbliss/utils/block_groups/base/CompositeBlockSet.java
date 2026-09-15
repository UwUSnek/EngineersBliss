package com.snek.engineersbliss.utils.block_groups.base;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.Block;




public class CompositeBlockSet extends __base_BlockSet {
    public CompositeBlockSet(__base_BlockSet... sets) {
        for(final @NotNull __base_BlockSet set : sets) {
            for(final Block b : set.asList()) {
                register(b);
            }
        }
    }
}
