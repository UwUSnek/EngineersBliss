package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public enum AltTextureFeature {
    TRANSPARENT_SLIME_BLOCK     (true, "Transparent Slime Blocks",     List.of(Blocks.SLIME_BLOCK)),
    TRANSPARENT_HONEY_BLOCK     (true, "Transparent Honey Blocks",     List.of(Blocks.HONEY_BLOCK)),
    UNOBSTRUCTIVE_MANGROVE_ROOTS(true, "Unobstructive Mangrove Roots", List.of(Blocks.MANGROVE_ROOTS)),
    UNOBSTRUCTIVE_SCAFFOLDING   (true, "Unobstructive Scaffolding",    List.of(Blocks.SCAFFOLDING)),

    MINIMAL_REDSTONE_WIRE       (true, "Minimal Redstone Wire",        List.of(Blocks.REDSTONE_WIRE)),
    CONSISTENT_SLOPED_RAILS     (true, "Consistent sloped Rails",      List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL)),

    REDSTONE_WIRE_3D            (true, "3D Redstone Wire",             List.of(Blocks.REDSTONE_WIRE)),
    RAILS_3D                    (true, "3D Rails",                     List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL));




    // Feature name and properties
    String name;
    List<Block> affectedBlocks;
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
    private AltTextureFeature(final boolean _default, String name, List<Block> affectedBlocks) {
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
        for(AltTextureFeature feature : values()) {
            blocksWithFeatures.addAll(feature.affectedBlocks);
        }
    }
}
