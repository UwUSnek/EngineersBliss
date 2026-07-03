package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import com.snek.engineersbliss.utils.Txt;
import com.snek.engineersbliss.client.utils.UiTxt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;








public enum AltTextureFeature {


    MINIMAL_REDSTONE_WIRE(true,
        () -> new UiTxt("Minimal Redstone Wire"),
        () -> new UiTxt("Replaces the messy dust-like Redstone Wire texture with a simple monochrome line to make circuits more readable."),
        List.of(Blocks.REDSTONE_WIRE)
    ),
    TRANSPARENT_SLIME_BLOCK(true,
        () -> new UiTxt("Transparent Slime Blocks"),
        () -> new UiTxt("Replaces the texture of Slime Blocks with a less opaque version."),
        List.of(Blocks.SLIME_BLOCK)
    ),
    TRANSPARENT_HONEY_BLOCK(true,
        () -> new UiTxt("Transparent Honey Blocks"),
        () -> new UiTxt("Replaces the texture of Honey Blocks with a less opaque version."),
        List.of(Blocks.HONEY_BLOCK)
    ),
    UNOBSTRUCTIVE_MANGROVE_ROOTS(true,
        () -> new UiTxt("Unobstructive Mangrove Roots"),
        () -> new UiTxt("Removes the central diagonal textures present inside of Mangrove Roots to improve visibility."),
        List.of(Blocks.MANGROVE_ROOTS)
    ),
    UNOBSTRUCTIVE_SCAFFOLDING(true,
        () -> new UiTxt("Unobstructive Scaffolding"),
        () -> new UiTxt("Removes the woven bamboo part of the texture in the middle of Scaffolding blocks to improve visibility."),
        List.of(Blocks.SCAFFOLDING)
    ),




    CONSISTENT_SLOPED_RAILS(true,
        () -> new UiTxt("Consistent sloped Rails"),
        () -> new UiTxt(
            "Minecraft Vanilla stretches the 1:1 textures of Rails, Powered Rails, Activator Rails and Detector Rails to 1:√2 to fit the length of sloped shapes.\n" +
            "This toggle replaces the stretched textures with a properly sized model, keeping rails visually consistent across all shapes."
        ),
        List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL)
    ),
    STATIC_CHESTS(true,
        () -> new UiTxt("Static Chest models"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the costly real-time rendering of Chests, Trapped Chests, and Ender Chests with a static model to improve performance.\n"))
            .cat(new UiTxt("This breaks the opening and closing animations.\n").Orange())
            .cat(new UiTxt("This breaks custom textures and models defined by standard Resource Packs.\n").red())
            .cat(new UiTxt("This feature is not compatible with other static block entity model mods.").red())
        ,
        Groups.ALL_CHESTS
    ),
    STATIC_SIGNS(true,
        () -> new UiTxt("Static Sign models"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the costly real-time rendering of Signs and Hanging Signs with a static model to improve performance.\n"))
            .cat(new UiTxt("The text will still be rendered dynamically, so signs will still be laggier than normal blocks, but way less than Vanilla's.\n").Orange())
            .cat(new UiTxt("This breaks custom textures and models defined by standard Resource Packs.\n").red())
            .cat(new UiTxt("This feature is not compatible with other static block entity model mods.").red())
        ,
        Stream.of(Groups.ALL_SIGNS.stream(), Groups.ALL_HANGING_SIGNS.stream()).flatMap(s -> s).toList()
    ),




    REDSTONE_WIRE_3D(true,
        () -> new UiTxt("3D Redstone Wire"),
        () -> new UiTxt()
            .cat(new UiTxt("Replaces the default flat texture of Redstone Wire with a three-dimensional model.\n"))
            .cat(new UiTxt("Implies [Minimal Redstone Wire]").yellow())
        ,
        List.of(Blocks.REDSTONE_WIRE)
    ),
    RAILS_3D(true,
        () -> new UiTxt("3D Rails"),
        () -> new UiTxt()
            .cat(new UiTxt("Replaces the default flat texture of Rails, Powered Rails, Activator Rails, and Detector Rails with a three-dimensional model.\n"))
            .cat(new UiTxt("Implies [Consistent sloped Rails]").yellow())
        ,
        List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL)
    ),
    LADDERS_3D(true, //TODO IMPLEMENT
        () -> new UiTxt("3D Ladders"),
        () -> new UiTxt("Replaces the default flat texture of Ladders with a three-dimensional model."),
        List.of(Blocks.LADDER)
    ),
    CHAINS_3D(true, //TODO IMPLEMENT //FIXME change 3d model of hanging signs and lanterns too (and all copper lanterns)
        () -> new UiTxt("3D Chains"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the default flat texture of Iron Chains and Copper Chains with a three-dimensional model.\n"))
            .cat(new UiTxt("This also affects the chains in Lanterns, Copper Lanterns, and Hanging Signs.\n").yellow())
            .cat(new UiTxt("3D Hanging Sign chains require [Static Sign Models]").yellow())
        ,
        Stream.of(Stream.of(Blocks.IRON_CHAIN), Blocks.COPPER_CHAIN.asList().stream(), Groups.ALL_LANTERNS.stream(), Groups.ALL_SIGNS.stream()).flatMap(s -> s).toList()
    ),
    BARS_3D(true, //TODO IMPLEMENT
        () -> new UiTxt("3D Bars"),
        () -> new UiTxt("Replaces the default flat texture of Iron Bars and Copper Bars with a three-dimensional model."),
        Stream.concat(Stream.of(Blocks.IRON_BARS), Blocks.COPPER_BARS.asList().stream()).toList()
    ),
    VINES_3D(true, //TODO IMPLEMENT
        () -> new UiTxt("3D Vines"),
        () -> new UiTxt("Replaces the default flat texture of Vines with a three-dimensional model."),
        List.of(Blocks.VINE)
    ),
    GLOW_LICHEN_3D(true, //TODO IMPLEMENT
        () -> new UiTxt("3D Glow Lichen"),
        () -> new UiTxt("Replaces the default flat texture of Glow Lichen with a three-dimensional model."),
        List.of(Blocks.GLOW_LICHEN)
    );








    /**
     * A class containing groups of blocks used by AltTextureFeature as lists.
     * ! This is required because Minecraft's tag registries are not available during static initialization.
     * ! The inner class pattern is also required:
     * !     Defining these before enum constants is not allowed and defining them after them creates forward references, which are forbidden.
     */
    private static class Groups {
        private static final List<Block> ALL_LANTERNS = List.of(
            Blocks.LANTERN,
            Blocks.SOUL_LANTERN,
            Blocks.COPPER_LANTERN.unaffected(),
            Blocks.COPPER_LANTERN.exposed(),
            Blocks.COPPER_LANTERN.weathered(),
            Blocks.COPPER_LANTERN.oxidized(),
            Blocks.COPPER_LANTERN.waxed(),
            Blocks.COPPER_LANTERN.waxedExposed(),
            Blocks.COPPER_LANTERN.waxedWeathered(),
            Blocks.COPPER_LANTERN.waxedOxidized()
        );
        private static final List<Block> ALL_SIGNS = List.of(
            Blocks.OAK_SIGN,
            Blocks.SPRUCE_SIGN,
            Blocks.BIRCH_SIGN,
            Blocks.JUNGLE_SIGN,
            Blocks.ACACIA_SIGN,
            Blocks.DARK_OAK_SIGN,
            Blocks.MANGROVE_SIGN,
            Blocks.CHERRY_SIGN,
            Blocks.PALE_OAK_SIGN,
            Blocks.BAMBOO_SIGN,
            Blocks.CRIMSON_SIGN,
            Blocks.WARPED_SIGN,
            Blocks.OAK_WALL_SIGN,
            Blocks.SPRUCE_WALL_SIGN,
            Blocks.BIRCH_WALL_SIGN,
            Blocks.JUNGLE_WALL_SIGN,
            Blocks.ACACIA_WALL_SIGN,
            Blocks.DARK_OAK_WALL_SIGN,
            Blocks.MANGROVE_WALL_SIGN,
            Blocks.CHERRY_WALL_SIGN,
            Blocks.PALE_OAK_WALL_SIGN,
            Blocks.BAMBOO_WALL_SIGN,
            Blocks.CRIMSON_WALL_SIGN,
            Blocks.WARPED_WALL_SIGN
        );
        private static final List<Block> ALL_HANGING_SIGNS = List.of(
            Blocks.OAK_HANGING_SIGN,
            Blocks.SPRUCE_HANGING_SIGN,
            Blocks.BIRCH_HANGING_SIGN,
            Blocks.JUNGLE_HANGING_SIGN,
            Blocks.ACACIA_HANGING_SIGN,
            Blocks.DARK_OAK_HANGING_SIGN,
            Blocks.MANGROVE_HANGING_SIGN,
            Blocks.CHERRY_HANGING_SIGN,
            Blocks.PALE_OAK_HANGING_SIGN,
            Blocks.BAMBOO_HANGING_SIGN,
            Blocks.CRIMSON_HANGING_SIGN,
            Blocks.WARPED_HANGING_SIGN,
            Blocks.OAK_WALL_HANGING_SIGN,
            Blocks.SPRUCE_WALL_HANGING_SIGN,
            Blocks.BIRCH_WALL_HANGING_SIGN,
            Blocks.JUNGLE_WALL_HANGING_SIGN,
            Blocks.ACACIA_WALL_HANGING_SIGN,
            Blocks.DARK_OAK_WALL_HANGING_SIGN,
            Blocks.MANGROVE_WALL_HANGING_SIGN,
            Blocks.CHERRY_WALL_HANGING_SIGN,
            Blocks.PALE_OAK_WALL_HANGING_SIGN,
            Blocks.BAMBOO_WALL_HANGING_SIGN,
            Blocks.CRIMSON_WALL_HANGING_SIGN,
            Blocks.WARPED_WALL_HANGING_SIGN
        );
        private static final List<Block> ALL_CHESTS = List.of(
            Blocks.COPPER_CHEST,
            Blocks.EXPOSED_COPPER_CHEST,
            Blocks.WEATHERED_COPPER_CHEST,
            Blocks.OXIDIZED_COPPER_CHEST,
            Blocks.WAXED_COPPER_CHEST,
            Blocks.WAXED_EXPOSED_COPPER_CHEST,
            Blocks.WAXED_WEATHERED_COPPER_CHEST,
            Blocks.WAXED_OXIDIZED_COPPER_CHEST,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.ENDER_CHEST
        );
    }








    // Feature name and properties
    //! Txt values are computed lazily as they depend on the Minecraft window and cannot be calculated during static initialization
    private final Supplier<Txt> nameSupplier;
    private final Supplier<Txt> detailsSupplier;
    private Txt name    = null;
    private Txt details = null;
    List<Block> affectedBlocks;
    private final long flagBit; //! Flag bit index is calculated from the order of declaration
    private final boolean _default;


    // Getters and checks
    public Txt getName   () { return name    == null ? (name    =    nameSupplier.get()) :    name; }
    public Txt getDetails() { return details == null ? (details = detailsSupplier.get()) : details; }
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
    private AltTextureFeature(final boolean _default, final Supplier<Txt> nameSupplier, final Supplier<Txt> detailsSupplier, final List<Block> affectedBlocks) {
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
        for(final AltTextureFeature feature : values()) {
            blocksWithFeatures.addAll(feature.affectedBlocks);
        }
    }
}
