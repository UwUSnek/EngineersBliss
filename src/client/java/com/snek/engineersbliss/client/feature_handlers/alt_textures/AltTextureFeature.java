package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public enum AltTextureFeature {
    TRANSPARENT_SLIME_BLOCK     ("Transparent Slime Blocks",     List.of(Blocks.SLIME_BLOCK)),
    TRANSPARENT_HONEY_BLOCK     ("Transparent Honey Blocks",     List.of(Blocks.HONEY_BLOCK)),
    UNOBSTRUCTIVE_MANGROVE_ROOTS("Unobstructive Mangrove Roots", List.of(Blocks.MANGROVE_ROOTS)),
    UNOBSTRUCTIVE_SCAFFOLDING   ("Unobstructive Scaffolding",    List.of(Blocks.SCAFFOLDING)),
    LINE_REDSTONE_WIRE          ("Minimal Redstone Wire",        List.of(Blocks.REDSTONE_WIRE)),
    REDSTONE_WIRE_POWER_LEVELS  ("Redstone Wire power levels",   List.of(Blocks.REDSTONE_WIRE)),
    COMPARATOR_POWER_LEVELS     ("Comparator power levels",      List.of(Blocks.COMPARATOR)), //TODO also add input power levels, including sides
    CONSISTENT_SLOPED_RAILS     ("Consistent sloped Rails",      List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL));


    // Feature name and properties
    String name;
    List<Block> affectedBlocks;
    public String getName() { return name; }
    public boolean affects(final Block block) { return affectedBlocks.contains(block); }
    public List<Block> getAffectedBlocks() { return affectedBlocks; }


    // Constructor
    private AltTextureFeature(String name, List<Block> affectedBlocks) {
        this.name = name;
        this.affectedBlocks = affectedBlocks;
    }


    // List of blocks with features - used during block model registration
    private static final Set<Block> blocksWithFeatures = new HashSet<>();
    public static boolean hasFeature(final Block block) { return blocksWithFeatures.contains(block); }
    static {
        for(AltTextureFeature feature : values()) {
            blocksWithFeatures.addAll(feature.affectedBlocks);
        }
    }
}
