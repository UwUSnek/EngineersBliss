package com.snek.engineersbliss.utils.block_groups.base;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.world.level.block.Block;




public class __base_BlockSet {
    private final List<Block> list;
    protected Block register(final Block block) {
        list.add(block);
        return block;
    }

    public __base_BlockSet() {
        this.list = new ArrayList<>();
    }

    public List<Block> asList() {
        return list;
    }
    public Stream<Block> asStream() {
        return asList().stream();
    }
}
