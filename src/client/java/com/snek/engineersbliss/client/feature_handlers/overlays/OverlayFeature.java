package com.snek.engineersbliss.client.feature_handlers.overlays;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public enum OverlayFeature {

    REDSTONE_WIRE_POWER_LEVELS(
        true, "Redstone Wire power levels",
        "Displays the power level of powered Redstone Wires.",
        List.of(Blocks.REDSTONE_WIRE)
    ),
    RAIL_POWER_LEVELS(
        true, "Rail power levels",
        "Displays the power level of powered Activator Rails and Powered Rails.\n" +
        "This follows Minecraft Vanilla's quirky rail update logic, so the displayed power levels might at times seem counterintuitive.",
        List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)
    ),
    COMPARATOR_POWER_LEVELS(
        true, "Comparator power levels",
        "Displays the output power level of Comparators.\n" +
        "Unlike other power level overlays, this is also shown on Comparators with output 0.",
        List.of(Blocks.COMPARATOR)
    ),




    COMPARATOR_LOGIC_SNIPPET(
        false, "Comparator logic snippet",
        "Displays the logic Comparators use to calculate their output signal as an expression.",
        List.of(Blocks.COMPARATOR)
    ), //TODO implement these as custom arrows
    REDSTONE_WIRE_POWER_SOURCE(
        false, "Redstone Wire power source",
        "Shows arrows connecting each Redstone Wire to the blocks that are currently powering it.",
        List.of(Blocks.REDSTONE_WIRE)
    ), //TODO implement these as custom arrows
    RAIL_POWER_SOURCE(
        false, "Rail power source",
        "Shows arrows connecting each Activator Rail and Powered Rail to the block that is currently powering it.",
        List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)
    ), //TODO implement these as custom arrows




    BETTER_BARRIER_DISPLAY(
        true, "Better Barrier display",
        "Removes the Vanilla Barrier particles that spawn when holding a Barrier item, replacing them with a proper overlay.\n" +
        Notices.OVERLAY_PROS_NOTICE,
        List.of(Blocks.BARRIER)
    ),
    BETTER_STRUCTURE_VOID_DISPLAY(
        true, "Better Structure Void display",
        "Displays placed Structure Void blocks while holding a Structure Void item, similarly to how Barriers work in Minecraft Vanilla, " +
        "but instead of particles, this uses a proper overlay.\n" +
        Notices.OVERLAY_PROS_NOTICE,
        List.of(Blocks.STRUCTURE_VOID)
    ),
    BETTER_LIGHT_BLOCK_DISPLAY(
        true, "Better Light Block display",
        "Removes the Vanilla Light particles that spawn when holding a Light item, replacing them with a proper overlay.\n" +
        Notices.OVERLAY_PROS_NOTICE,
        List.of(Blocks.LIGHT)
    );




    private class Notices {
        public static final String OVERLAY_PROS_NOTICE = "Overlays are shown and removed instantly, don't have a view distance limit and can be seen through walls.";
    }








    // Feature name and properties
    private String name;
    private String details;
    private List<Block> affectedBlocks;
    private long flagBit; //! Flag bit index is calculated from the order of declaration
    private boolean _default;


    // Getters and checks
    public String getName() { return name; }
    public String getDetails() { return details; }
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
    private OverlayFeature(final boolean _default, String name, String details, List<Block> affectedBlocks) {
        this._default = _default;
        this.name = name;
        this.details = details;
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
