package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import com.snek.engineersbliss.utils.Txt;
import com.snek.engineersbliss.client.utils.UiTxt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;








public enum AltTextureFeature {


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




    MINIMAL_REDSTONE_WIRE(true,
        () -> new UiTxt("Minimal Redstone Wire"),
        () -> new UiTxt("Replaces the messy dust-like Redstone Wire texture with a simple monochrome line to make circuits more readable."),
        List.of(Blocks.REDSTONE_WIRE)
    ),
    CONSISTENT_SLOPED_RAILS(true,
        () -> new UiTxt("Consistent sloped Rails"),
        () -> new UiTxt(
            "Minecraft Vanilla stretches the 1:1 textures of Rails, Powered Rails, Activator Rails and Detector Rails to 1:√2 to fit the length of sloped shapes.\n" +
            "This toggle replaces the stretched textures with a properly sized model, keeping rails visually consistent across all shapes."
        ),
        List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL)
    ),




    REDSTONE_WIRE_3D(true,
        () -> new UiTxt("3D Redstone Wire"),
        () -> new UiTxt()
            .cat(new UiTxt("Replaces the default flat texture of Redstone Wire with a three-dimensional model.\n"))
            .cat(new UiTxt("Implies [Minimal Redstone Wire]").lightGray())
        ,
        List.of(Blocks.REDSTONE_WIRE)
    ),
    RAILS_3D(true,
        () -> new UiTxt("3D Rails"),
        () -> new UiTxt()
            .cat(new UiTxt("Replaces the default flat texture of Rails, Powered Rails, Activator Rails, and Detector Rails with a three-dimensional model.\n"))
            .cat(new UiTxt("Implies [Consistent sloped Rails]").lightGray())
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
            .cat(new UiTxt("This also affects the chains in Lanterns, Copper Lanterns and all Hanging Signs.").lightGray())
        ,
        Stream.of(
            Stream.of(Blocks.IRON_CHAIN),
            Blocks.COPPER_CHAIN.asList().stream(),
            StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(BlockTags.ALL_HANGING_SIGNS).spliterator(), false).map(Holder::value),
            StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(BlockTags.LANTERNS).spliterator(), false).map(Holder::value)
        ).flatMap(s -> s).toList()
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
