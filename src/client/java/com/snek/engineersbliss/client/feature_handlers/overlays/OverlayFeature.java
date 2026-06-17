package com.snek.engineersbliss.client.feature_handlers.overlays;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public enum OverlayFeature {

    REDSTONE_WIRE_POWER_LEVELS  ("Redstone Wire power levels",   List.of(Blocks.REDSTONE_WIRE)),
    RAIL_POWER_LEVELS           ("Rail power levels",            List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL)),
    COMPARATOR_POWER_LEVELS     ("Comparator power levels",      List.of(Blocks.COMPARATOR)); //TODO also add input power levels, including sides


    // Feature name and properties
    String name;
    List<Block> affectedBlocks;
    public String getName() { return name; }
    public boolean affects(final Block block) { return affectedBlocks.contains(block); }
    public List<Block> getAffectedBlocks() { return affectedBlocks; }


    // Constructor
    private OverlayFeature(String name, List<Block> affectedBlocks) {
        this.name = name;
        this.affectedBlocks = affectedBlocks;
    }


    // List of blocks with features - used during block model registration
    private static final Set<Block> blocksWithFeatures = new HashSet<>();
    public static boolean hasFeature(final Block block) { return blocksWithFeatures.contains(block); }
    static {
        for(OverlayFeature feature : values()) {
            blocksWithFeatures.addAll(feature.affectedBlocks);
        }
    }
}
