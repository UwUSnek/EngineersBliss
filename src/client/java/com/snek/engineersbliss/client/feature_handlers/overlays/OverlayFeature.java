package com.snek.engineersbliss.client.feature_handlers.overlays;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public enum OverlayFeature {

    REDSTONE_WIRE_POWER_LEVELS  ("Redstone Wire power levels",   List.of(Blocks.REDSTONE_WIRE)),
    RAIL_POWER_LEVELS           ("Rail power levels",            List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)),
    COMPARATOR_POWER_LEVELS     ("Comparator power levels",      List.of(Blocks.COMPARATOR)); //TODO also add input power levels, including sides


    // Feature name and properties
    private String name;
    private List<Block> affectedBlocks;
    private long flagBit; //! Flag bit index is calculated from the order of declaration

    // Getters and checks
    public String getName() { return name; }
    public boolean affects(final Block block) { return affectedBlocks.contains(block); }
    public List<Block> getAffectedBlocks() { return affectedBlocks; }
    public long getFlagBit() { return flagBit; }


    // Constructor
    private OverlayFeature(String name, List<Block> affectedBlocks) {
        this.name = name;
        this.affectedBlocks = affectedBlocks;
        this.flagBit = 1 << ordinal();
    }


    // List of blocks with features - used during block model registration
    private static final Set<Block> blocksWithFeatures = new HashSet<>();
    public static Set<Block> getBlocksWithFeatures() { return blocksWithFeatures; }
    public static boolean hasFeature(final Block block) { return blocksWithFeatures.contains(block); }
    static {
        for(OverlayFeature feature : values()) {
            blocksWithFeatures.addAll(feature.affectedBlocks);
        }
    }
}
