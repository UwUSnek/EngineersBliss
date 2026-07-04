package com.snek.engineersbliss.client.feature_handlers.overlays;

import com.snek.engineersbliss.utils.Txt;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.UiTxt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public enum OverlayFeature {

    REDSTONE_WIRE_POWER_LEVELS(true,
        () -> new UiTxt("Redstone Wire power levels"),
        () -> new UiTxt("Displays the power level of powered Redstone Wires."),
        List.of(Blocks.REDSTONE_WIRE)
    ),
    RAIL_POWER_LEVELS(true,
        () -> new UiTxt("Rail power levels"),
        () -> new Txt()
            .cat(new UiTxt("Displays the power level of powered Activator Rails and Powered Rails.\n"))
            .cat(new UiTxt("This follows Minecraft Vanilla's quirky rail update logic, so the displayed power levels might at times seem counterintuitive.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE)
        ,
        List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)
    ),
    COMPARATOR_POWER_LEVELS(true,
        () -> new UiTxt("Comparator power levels"),
        () -> new Txt()
            .cat(new UiTxt("Displays the output power level of Comparators.\n"))
            .cat(new UiTxt("Unlike other power level overlays, this is also shown on Comparators with output 0.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE)
        ,
        List.of(Blocks.COMPARATOR)
    ),




    COMPARATOR_LOGIC_SNIPPET(false,
        () -> new UiTxt("Comparator logic snippet"),
        () -> new Txt()
            .cat(new UiTxt("Displays the logic Comparators use to calculate their output signal as an expression.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE)
        ,
        List.of(Blocks.COMPARATOR)
    ), //TODO implement these as custom arrows
    REDSTONE_WIRE_POWER_SOURCE(false,
        () -> new UiTxt("Redstone Wire power source"),
        () -> new Txt()
            .cat(new UiTxt("Shows arrows connecting each Redstone Wire to the blocks that are currently powering it.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE)
        ,
        List.of(Blocks.REDSTONE_WIRE)
    ), //TODO implement these as custom arrows
    RAIL_POWER_SOURCE(false,
        () -> new UiTxt("Rail power source"),
        () -> new Txt()
            .cat(new UiTxt("Shows arrows connecting each Activator Rail and Powered Rail to the block that is currently powering it.\n"))
            .cat(Notices.MULTIPLAYER_NOTICE)
        ,
        List.of(Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL)
    ), //TODO implement these as custom arrows




    BETTER_BARRIER_DISPLAY(true,
        () -> new UiTxt("Better Barrier display"),
        () -> new Txt()
            .cat(new UiTxt("Removes the Vanilla Barrier particles that spawn when holding a Barrier item, replacing them with a proper overlay.\n"))
            .cat(Notices.OVERLAY_PROS_NOTICE)
        ,
        List.of(Blocks.BARRIER)
    ),
    BETTER_STRUCTURE_VOID_DISPLAY(true,
        () -> new UiTxt("Better Structure Void display"),
        () -> new Txt()
            .cat(new UiTxt("Displays placed Structure Void blocks while holding a Structure Void item, similarly to how Barriers work in Minecraft Vanilla, "))
            .cat(new UiTxt("but instead of particles, this uses a proper overlay.\n"))
            .cat(Notices.OVERLAY_PROS_NOTICE)
        ,
        List.of(Blocks.STRUCTURE_VOID)
    ),
    BETTER_LIGHT_BLOCK_DISPLAY(true,
        () -> new UiTxt("Better Light Block display"),
        () -> new Txt()
            .cat(new UiTxt("Removes the Vanilla Light particles that spawn when holding a Light item, replacing them with a proper overlay.\n"))
            .cat(Notices.OVERLAY_PROS_NOTICE)
        ,
        List.of(Blocks.LIGHT)
    );




    private class Notices {
        public static final Txt OVERLAY_PROS_NOTICE = new UiTxt(
            "Overlays are shown and removed instantly, don't have a view distance limit and can be seen through walls."
        ).green();

        public static final Txt MULTIPLAYER_NOTICE = new UiTxt(
            "This overlay isn't available on servers without the " + EngineerSBliss.MOD_NAME + " mod installed."
        ).red();
    }








    // Feature name and properties
    //! Txt values are computed lazily as they depend on the Minecraft window and cannot be calculated during static initialization
    private final Supplier<Txt> nameSupplier;
    private final Supplier<Txt> detailsSupplier;
    private Txt name    = null;
    private Txt details = null;
    private final List<Block> affectedBlocks;
    private final long flagBit; //! Flag bit index is calculated from the order of declaration
    private final boolean _default;


    // Getters and checks
    public Txt getName   () { return name    == null ? (name    =    nameSupplier.get()).copy() :    name.copy(); }
    public Txt getDetails() { return details == null ? (details = detailsSupplier.get()).copy() : details.copy(); }
    public boolean affects(final Block block) { return affectedBlocks.contains(block); }
    public List<Block> getAffectedBlocks() { return affectedBlocks; }
    public long getFlagBit() { return flagBit; }
    public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }



    public static long DEFAULT_FLAGS = 0;
    static {
        for(final var feature : values()) {
            if(feature._default) DEFAULT_FLAGS |= feature.getFlagBit();
        }
    }


    // Constructor
    private OverlayFeature(final boolean _default, final Supplier<Txt> nameSupplier, final Supplier<Txt> detailsSupplier, final List<Block> affectedBlocks) {
        this._default = _default;
        this.nameSupplier    = nameSupplier;
        this.detailsSupplier = detailsSupplier;
        this.affectedBlocks = affectedBlocks;
        this.flagBit = 1 << ordinal();
    }


    // List of blocks with features - used during block model registration
    private static final Set<Block> blocksWithFeatures = new HashSet<>();
    public static Set<Block> getBlocksWithFeatures() { return blocksWithFeatures; }
    public static boolean hasFeature(final Block block) { return blocksWithFeatures.contains(block); }
    static {
        for(final OverlayFeature feature : values()) {
            blocksWithFeatures.addAll(feature.affectedBlocks);
        }
    }
}
