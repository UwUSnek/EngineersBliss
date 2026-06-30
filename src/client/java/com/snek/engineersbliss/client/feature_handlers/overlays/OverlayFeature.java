package com.snek.engineersbliss.client.feature_handlers.overlays;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public enum OverlayFeature {

    REDSTONE_WIRE_POWER_LEVELS    (true,  "Redstone Wire power levels",    List.of(Blocks.REDSTONE_WIRE)),
    RAIL_POWER_LEVELS             (true,  "Rail power levels",             List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)),
    COMPARATOR_POWER_LEVELS       (true,  "Comparator power levels",       List.of(Blocks.COMPARATOR)),

    COMPARATOR_LOGIC_SNIPPET      (false, "Comparator logic snippet",      List.of(Blocks.COMPARATOR)), //TODO implement these as custom arrows
    REDSTONE_WIRE_POWER_SOURCE    (false, "Redstone wire power source",    List.of(Blocks.REDSTONE_WIRE)), //TODO implement these as custom arrows
    RAIL_POWER_SOURCE             (false, "Rail power source",             List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)), //TODO implement these as custom arrows

    BETTER_BARRIER_DISPLAY        (true,  "Better Barrier display",        List.of(Blocks.BARRIER)),
    BETTER_STRUCTURE_VOID_DISPLAY (true,  "Better Structure Void display", List.of(Blocks.STRUCTURE_VOID)),
    BETTER_LIGHT_BLOCK_DISPLAY    (true,  "Better Light Block display",    List.of(Blocks.LIGHT));




    // Feature name and properties
    private String name;
    private List<Block> affectedBlocks;
    private long flagBit; //! Flag bit index is calculated from the order of declaration
    private boolean _default;


    // Getters and checks
    public String getName() { return name; }
    public boolean affects(final Block block) { return affectedBlocks.contains(block); }
    public List<Block> getAffectedBlocks() { return affectedBlocks; }
    public long getFlagBit() { return flagBit; }
    public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }



    public static long DEFAULT_FLAGS = 0;
    static {
        for(var feature : values()) {
            if(feature._default) DEFAULT_FLAGS |= feature.getFlagBit();
        }
    }


    // Constructor
    private OverlayFeature(final boolean _default, String name, List<Block> affectedBlocks) {
        this._default = _default;
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
