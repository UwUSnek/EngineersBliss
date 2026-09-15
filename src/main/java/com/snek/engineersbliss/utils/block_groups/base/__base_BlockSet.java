package com.snek.engineersbliss.utils.block_groups.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.world.level.block.Block;




public class __base_BlockSet {
    private final List<Block> list;
    protected Block register(final Block block) {
        list.add(block);
        return block;
    }
    protected final Map<String, Block> byProperty;
    protected Block registerWithCustomData(final String property, final Block block) {
        byProperty.put(property, block);
        return register(block);
    }



    public __base_BlockSet() {
        this.list       = new ArrayList<>();
        this.byProperty = new HashMap<>();
    }



    public List<Block> asList() {
        return list;
    }
    public Stream<Block> asStream() {
        return asList().stream();
    }
}
