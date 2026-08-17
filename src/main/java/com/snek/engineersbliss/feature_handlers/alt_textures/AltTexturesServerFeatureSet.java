package com.snek.engineersbliss.feature_handlers.alt_textures;

import java.util.List;
import java.util.stream.Stream;

import com.snek.engineersbliss.feature_handlers.BlockGroups;
import com.snek.engineersbliss.feature_handlers.base.ServerBlockToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.ServerToggleFeature;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeatureSet;

import net.minecraft.world.level.block.Blocks;








public class AltTexturesServerFeatureSet extends __base_ServerFeatureSet {
    public static final AltTexturesServerFeatureSet INSTANCE = new AltTexturesServerFeatureSet();
    private AltTexturesServerFeatureSet() { super("alt_textures"); }


    //! For whatever reason, Vanilla's Shelves and Campfires are rendered properly: The block model is static, while held items are rendered dynamically.
    //! However, their rendering logic does a lot of extra preparation steps even when there are no items to render.
    //! Campfires are also extremely laggy because of their particles.
    //! These all get simple logic optimizations, while Campfires also get particle suppression.


    //! Vanilla Bells are rendered dynamically but the supports are static.
    //! STATIC_BELLS simply replaces the bell part and keeps the Vanilla support.




    public static final ServerBlockToggleFeature MINIMAL_REDSTONE_WIRE = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "minimal_redstone_wire", true,
        List.of(Blocks.REDSTONE_WIRE)
    ));
    public static final ServerToggleFeature NO_REDSTONE_DUST_PARTICLES = INSTANCE.registerFeature(new ServerToggleFeature(
        "no_redstone_dust_particles", true
        //! Feature doesn't affect block models. No need for block tracking
    ));
    public static final ServerToggleFeature NO_CAMPFIRE_PARTICLES = INSTANCE.registerFeature(new ServerToggleFeature(
        "no_campfire_particles", true
        //! Feature doesn't affect block models. No need for block tracking
    ));
    public static final ServerToggleFeature NO_FIRE_PARTICLES = INSTANCE.registerFeature(new ServerToggleFeature(
        "no_fire_particles", true
        //! Feature doesn't affect block models. No need for block tracking
    ));
    public static final ServerToggleFeature NO_LAVA_PARTICLES = INSTANCE.registerFeature(new ServerToggleFeature(
        "no_lava_particles", true
        //! Feature doesn't affect block models. No need for block tracking
    ));
    public static final ServerToggleFeature NO_WATER_STREAM_PARTICLES = INSTANCE.registerFeature(new ServerToggleFeature(
        "no_water_stream_particles", true
        //! Feature doesn't affect block models. No need for block tracking
    ));
    public static final ServerToggleFeature NO_DRIP_PARTICLES = INSTANCE.registerFeature(new ServerToggleFeature(
        "no_drip_particles", true
        //! Feature doesn't affect block models. No need for block tracking
    ));
    public static final ServerBlockToggleFeature TRANSPARENT_SLIME_BLOCK = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "transparent_slime_block", true,
        List.of(Blocks.SLIME_BLOCK)
    ));
    public static final ServerBlockToggleFeature TRANSPARENT_HONEY_BLOCK = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "transparent_honey_block", true,
        List.of(Blocks.HONEY_BLOCK)
    ));
    public static final ServerBlockToggleFeature UNOBSTRUCTIVE_MANGROVE_ROOTS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "unobstructive_mangrove_roots", true,
        List.of(Blocks.MANGROVE_ROOTS)
    ));
    public static final ServerBlockToggleFeature UNOBSTRUCTIVE_SCAFFOLDING = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "unobstructive_scaffolding", true,
        List.of(Blocks.SCAFFOLDING)
    ));




    public static final ServerBlockToggleFeature CONSISTENT_SLOPED_RAILS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "consistent_sloped_rails", true,
        List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL)
    ));
    public static final ServerBlockToggleFeature STATIC_CHESTS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "static_chests", true,
        BlockGroups.ALL_CHESTS
    ));
    public static final ServerBlockToggleFeature STATIC_SIGNS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "static_signs", true,
        Stream.of(BlockGroups.ALL_SIGNS.stream(), BlockGroups.ALL_HANGING_SIGNS.stream()).flatMap(s -> s).toList()
    ));
    public static final ServerBlockToggleFeature STATIC_BANNERS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "static_banners", true,
        Stream.of(BlockGroups.ALL_BANNERS.stream()).flatMap(s -> s).toList()
    ));
    public static final ServerBlockToggleFeature STATIC_DECORATED_POTS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "static_decorated_pots", true,
        List.of(Blocks.DECORATED_POT)
    ));
    public static final ServerBlockToggleFeature STATIC_BELLS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "static_bells", true,
        List.of(Blocks.BELL)
    ));
    public static final ServerBlockToggleFeature STATIC_COPPER_GOLEM_STATUES = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "static_copper_golem_statues", true,
        Stream.of(BlockGroups.ALL_COPPER_GOLEM_STATUES.stream()).flatMap(s -> s).toList()
    ));
    public static final ServerBlockToggleFeature STATIC_LECTERNS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "static_lecterns", true,
        List.of(Blocks.LECTERN)
    ));
    public static final ServerBlockToggleFeature STATIC_BEDS = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "static_beds", true,
        BlockGroups.ALL_BEDS
    ));
    public static final ServerBlockToggleFeature OPTIMIZED_SHELVES = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "optimized_shelves", true,
        List.of() //! Feature doesn't change the model. No section refresh
    ));
    public static final ServerBlockToggleFeature OPTIMIZED_CAMPFIRES = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "optimized_campfires", true,
        List.of() //! Feature doesn't change the model. No section refresh
    ));




    public static final ServerBlockToggleFeature REDSTONE_WIRE_3D = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "redstone_wire_3d", true,
        List.of(Blocks.REDSTONE_WIRE)
    ));
    public static final ServerBlockToggleFeature RAILS_3D = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "rails_3d", true,
        List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL)
    ));
    public static final ServerBlockToggleFeature LADDERS_3D = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "ladders_3d", true,
        List.of(Blocks.LADDER)
    ));
    public static final ServerBlockToggleFeature CHAINS_3D = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "chains_3d", true,
        Stream.of(Stream.of(Blocks.IRON_CHAIN), Blocks.COPPER_CHAIN.asList().stream(), BlockGroups.ALL_LANTERNS.stream(), BlockGroups.ALL_HANGING_SIGNS.stream()).flatMap(s -> s).toList()
    ));
    public static final ServerBlockToggleFeature BARS_3D = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "bars_3d", true,
        Stream.concat(Stream.of(Blocks.IRON_BARS), Blocks.COPPER_BARS.asList().stream()).toList()
    ));
    public static final ServerBlockToggleFeature VINES_3D = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "vines_3d", true,
        List.of(Blocks.VINE)
    ));
    public static final ServerBlockToggleFeature GLOW_LICHEN_3D = INSTANCE.registerFeature(new ServerBlockToggleFeature(
        "glow_lichen_3d", true,
        List.of(Blocks.GLOW_LICHEN)
    ));
}



