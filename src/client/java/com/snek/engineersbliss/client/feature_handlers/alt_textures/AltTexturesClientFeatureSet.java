package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import com.snek.engineersbliss.utils.Txt;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.base.ClientBlockFeature;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;








public class AltTexturesClientFeatureSet extends __base_ClientFeatureSet<AltTexturesServerFeatureSet> {
    public static final AltTexturesClientFeatureSet INSTANCE = new AltTexturesClientFeatureSet();
    private AltTexturesClientFeatureSet() {
        super(AltTexturesServerFeatureSet.INSTANCE, () -> new UiTxt("Alternative Textures"));
    }




    public static final ClientBlockFeature<?> MINIMAL_REDSTONE_WIRE = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.MINIMAL_REDSTONE_WIRE,
        () -> new UiTxt("Minimal Redstone Wire"),
        () -> new UiTxt("Replaces the messy dust-like Redstone Wire texture with a simple monochrome line to make circuits more readable."),
        List.of(Blocks.REDSTONE_WIRE)
    );
    public static final ClientBlockFeature<?> NO_REDSTONE_DUST_PARTICLES = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.NO_REDSTONE_DUST_PARTICLES,
        () -> new UiTxt("Disable Redstone dust particles"),
        () -> new UiTxt("Stops Levers, Redstone Wire, Redstone Torches, Redstone Ores, and Redstone Repeaters from emitting dust particles when powered."),
        List.of() //! Feature doesn't change the model. No section refresh
    );
    public static final ClientBlockFeature<?> NO_CAMPFIRE_PARTICLES = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.NO_CAMPFIRE_PARTICLES,
        () -> new UiTxt("Disable Campfire particles"),
        () -> new UiTxt("Stops Campfires and Soul Campfires from emitting smoke and ember particles."),
        List.of() //! Feature doesn't change the model. No section refresh
    );
    public static final ClientBlockFeature<?> NO_FIRE_PARTICLES = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.NO_FIRE_PARTICLES,
        () -> new UiTxt("Disable Fire particles"),
        () -> new UiTxt("Stops Fire and Soul Fire from emitting smoke and ember particles."),
        List.of() //! Feature doesn't change the model. No section refresh
    );
    public static final ClientBlockFeature<?> NO_LAVA_PARTICLES = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.NO_LAVA_PARTICLES,
        () -> new UiTxt("Disable Lava particles"),
        () -> new UiTxt("Stops Lava from emitting ember particles."),
        List.of() //! Feature doesn't change the model. No section refresh
    );
    public static final ClientBlockFeature<?> NO_WATER_STREAM_PARTICLES = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.NO_WATER_STREAM_PARTICLES,
        () -> new UiTxt("Disable Water Stream particles"),
        () -> new UiTxt("Disables bubble and water splash particles emitted by items travelling in water streams."),
        List.of() //! Feature doesn't change the model. No section refresh
    );
    public static final ClientBlockFeature<?> NO_DRIP_PARTICLES = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.NO_DRIP_PARTICLES,
        () -> new UiTxt("Disable drip particles"),
        () -> new UiTxt("Disables dripping water and dripping lava particles emitted by full blocks with water or lava above them."),
        List.of() //! Feature doesn't change the model. No section refresh
    );
    public static final ClientBlockFeature<?> TRANSPARENT_SLIME_BLOCK = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.TRANSPARENT_SLIME_BLOCK,
        () -> new UiTxt("Transparent Slime Blocks"),
        () -> new UiTxt("Replaces the texture of Slime Blocks with a less opaque version."),
        List.of(Blocks.SLIME_BLOCK)
    );
    public static final ClientBlockFeature<?> TRANSPARENT_HONEY_BLOCK = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.TRANSPARENT_HONEY_BLOCK,
        () -> new UiTxt("Transparent Honey Blocks"),
        () -> new UiTxt("Replaces the texture of Honey Blocks with a less opaque version."),
        List.of(Blocks.HONEY_BLOCK)
    );
    public static final ClientBlockFeature<?> UNOBSTRUCTIVE_MANGROVE_ROOTS = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.UNOBSTRUCTIVE_MANGROVE_ROOTS,
        () -> new UiTxt("Unobstructive Mangrove Roots"),
        () -> new UiTxt("Removes the central diagonal textures present inside of Mangrove Roots to improve visibility."),
        List.of(Blocks.MANGROVE_ROOTS)
    );
    public static final ClientBlockFeature<?> UNOBSTRUCTIVE_SCAFFOLDING = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.UNOBSTRUCTIVE_SCAFFOLDING,
        () -> new UiTxt("Unobstructive Scaffolding"),
        () -> new UiTxt("Removes the woven bamboo part of the texture in the middle of Scaffolding blocks to improve visibility."),
        List.of(Blocks.SCAFFOLDING)
    );








    public static final ClientBlockFeature<?> CONSISTENT_SLOPED_RAILS = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.CONSISTENT_SLOPED_RAILS,
        () -> new UiTxt("Consistent sloped Rails"),
        () -> new UiTxt(
            "Minecraft Vanilla stretches the 1:1 textures of Rails, Powered Rails, Activator Rails and Detector Rails to 1:√2 to fit the length of sloped shapes.\n" +
            "This toggle replaces the stretched textures with a properly sized model, keeping rails visually consistent across all shapes."
        ),
        List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL)
    );
    public static final ClientBlockFeature<?> STATIC_CHESTS = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.STATIC_CHESTS,
        () -> new UiTxt("Static Chest models"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the costly real-time rendering of Chests, Trapped Chests, and Ender Chests with a static model to improve performance.\n"))
            .cat(new UiTxt("This breaks the opening and closing animations.\n").Orange())
            .cat(Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE.get()).cat("\n")
            .cat(Notices.MOD_COMPATIBILITY_NOTICE.get())
        ,
        Groups.ALL_CHESTS
    );
    public static final ClientBlockFeature<?> STATIC_SIGNS = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.STATIC_SIGNS,
        () -> new UiTxt("Static Sign models"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the costly real-time rendering of Signs and Hanging Signs with a static model to improve performance.\n"))
            .cat(new UiTxt("The text must be rendered dynamically, so static signs are laggier than normal blocks, but way less than Vanilla's.\n").yellow())
            .cat(Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE.get()).cat("\n")
            .cat(Notices.MOD_COMPATIBILITY_NOTICE.get())
        ,
        Stream.of(Groups.ALL_SIGNS.stream(), Groups.ALL_HANGING_SIGNS.stream()).flatMap(s -> s).toList()
    );
    public static final ClientBlockFeature<?> STATIC_BANNERS = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.STATIC_BANNERS,
        () -> new UiTxt("Static Banner models"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the costly real-time rendering of Banners with a static model to improve performance.\n"))
            .cat(new UiTxt("This breaks the fluttering animation banners have in Vanilla.\n").Orange())
            .cat(new UiTxt("This also breaks custom banner patterns. Instead, only the base color is displayed.\n").Orange())
            .cat(Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE.get()).cat("\n")
            .cat(Notices.MOD_COMPATIBILITY_NOTICE.get())
        ,
        Stream.of(Groups.ALL_BANNERS.stream()).flatMap(s -> s).toList()
    );
    public static final ClientBlockFeature<?> STATIC_DECORATED_POTS = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.STATIC_DECORATED_POTS,
        () -> new UiTxt("Static Decorated Pot models"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the costly real-time rendering of Decorated Pots with a static model to improve performance.\n"))
            .cat(new UiTxt("This breaks the wobbling animation that plays in Vanilla when a Decorated Pot is right-clicked.\n").Orange())
            .cat(new UiTxt("Sides customized with Sherds (not the default brick) must be rendered dynamically, so static decorated pots are laggier than normal blocks, but way less than Vanilla's.\n").yellow())
            .cat(Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE.get()).cat("\n")
            .cat(Notices.MOD_COMPATIBILITY_NOTICE.get())
        ,
        List.of(Blocks.DECORATED_POT)
    );
    public static final ClientBlockFeature<?> STATIC_BELLS = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.STATIC_BELLS,
        () -> new UiTxt("Static Bell models"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the costly real-time rendering of Bells with a static model to improve performance.\n"))
            .cat(new UiTxt("This breaks the swinging animation that plays in Vanilla when a Bell is rung.\n").Orange())
            .cat(Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE.get()).cat("\n")
            .cat(Notices.MOD_COMPATIBILITY_NOTICE.get())
        ,
        List.of(Blocks.BELL)
    );
    public static final ClientBlockFeature<?> STATIC_COPPER_GOLEM_STATUES = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.STATIC_COPPER_GOLEM_STATUES,
        () -> new UiTxt("Static Copper Golem Statue models"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the costly real-time rendering of Copper Golem Statues with a static model to improve performance.\n"))
            .cat(Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE.get()).cat("\n")
            .cat(Notices.MOD_COMPATIBILITY_NOTICE.get())
        ,
        Stream.of(Groups.ALL_COPPER_GOLEM_STATUES.stream()).flatMap(s -> s).toList()
    );
    public static final ClientBlockFeature<?> STATIC_LECTERNS = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.STATIC_LECTERNS,
        () -> new UiTxt("Static Lectern models"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the costly real-time rendering of Lecterns with a static model to improve performance.\n"))
            .cat(Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE.get()).cat("\n")
            .cat(Notices.MOD_COMPATIBILITY_NOTICE.get())
        ,
        List.of(Blocks.LECTERN)
    );
    public static final ClientBlockFeature<?> OPTIMIZED_SHELVES = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.OPTIMIZED_SHELVES,
        () -> new UiTxt("Optimized Shelf models"),
        () -> new Txt()
            .cat(new UiTxt("Optimizes shelf rendering logic to improve performance.\n"))
            .cat(new UiTxt("This doesn't cause visual changes.\n"))
            .cat(Notices.RESOURCEPACK_COMPATIBILITY_NOTICE.get())
        ,
        List.of() //! Feature doesn't change the model. No section refresh
    );
    public static final ClientBlockFeature<?> OPTIMIZED_CAMPFIRES = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.OPTIMIZED_CAMPFIRES,
        () -> new UiTxt("Optimized Campfire models"),
        () -> new Txt()
            .cat(new UiTxt("Optimizes Campfire and Soul Campfire rendering logic to improve performance.\n"))
            .cat(new UiTxt("This doesn't cause visual changes.\n"))
            .cat(Notices.RESOURCEPACK_COMPATIBILITY_NOTICE.get())
        ,
        List.of() //! Feature doesn't change the model. No section refresh
    );









    public static final ClientBlockFeature<?> REDSTONE_WIRE_3D = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.REDSTONE_WIRE_3D,
        () -> new UiTxt("3D Redstone Wire"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the default flat texture of Redstone Wire with a three-dimensional model.\n"))
            .cat(new UiTxt("Implies [Minimal Redstone Wire]").yellow())
        ,
        List.of(Blocks.REDSTONE_WIRE)
    );
    public static final ClientBlockFeature<?> RAILS_3D = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.RAILS_3D,
        () -> new UiTxt("3D Rails"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the default flat texture of Rails, Powered Rails, Activator Rails, and Detector Rails with a three-dimensional model.\n"))
            .cat(new UiTxt("Implies [Consistent sloped Rails]").yellow())
        ,
        List.of(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL)
    );
    public static final ClientBlockFeature<?> LADDERS_3D = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.LADDERS_3D,
        () -> new UiTxt("3D Ladders"),
        () -> new UiTxt("Replaces the default flat texture of Ladders with a three-dimensional model."),
        List.of(Blocks.LADDER)
    );
    public static final ClientBlockFeature<?> CHAINS_3D = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.CHAINS_3D,
        () -> new UiTxt("3D Chains"),
        () -> new Txt()
            .cat(new UiTxt("Replaces the default flat texture of Iron Chains and Copper Chains with a three-dimensional model.\n"))
            .cat(new UiTxt("This also affects the chains in Lanterns, Copper Lanterns, and Hanging Signs.\n").yellow())
            .cat(new UiTxt("3D Hanging Sign chains require [Static Sign Models]").yellow())
        ,
        Stream.of(Stream.of(Blocks.IRON_CHAIN), Blocks.COPPER_CHAIN.asList().stream(), Groups.ALL_LANTERNS.stream(), Groups.ALL_HANGING_SIGNS.stream()).flatMap(s -> s).toList()
    );
    public static final ClientBlockFeature<?> BARS_3D = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.BARS_3D,
        () -> new UiTxt("3D Bars"),
        () -> new UiTxt("Replaces the default flat texture of Iron Bars and Copper Bars with a three-dimensional model."),
        Stream.concat(Stream.of(Blocks.IRON_BARS), Blocks.COPPER_BARS.asList().stream()).toList()
    );
    public static final ClientBlockFeature<?> VINES_3D = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.VINES_3D,
        () -> new UiTxt("3D Vines"),
        () -> new UiTxt("Replaces the default flat texture of Vines with a three-dimensional model."),
        List.of(Blocks.VINE)
    );
    public static final ClientBlockFeature<?> GLOW_LICHEN_3D = new ClientBlockFeature<>(
        AltTexturesServerFeatureSet.GLOW_LICHEN_3D,
        () -> new UiTxt("3D Glow Lichen"),
        () -> new UiTxt("Replaces the default flat texture of Glow Lichen with a three-dimensional model."),
        List.of(Blocks.GLOW_LICHEN)
    );









    /**
     * A class containing groups of blocks used by AltTextureFeature as lists.
     * ! This MUST be updated manually as new blocks are added to affected categories.
     *
     * ! This is required because Minecraft's tag registries are not available during static initialization.
     * ! The inner class pattern is also required:
     * !     Defining these before enum constants is not allowed and defining them after creates forward references, which are forbidden.
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
        private static final List<Block> ALL_BANNERS = List.of(
            Blocks.WHITE_BANNER,
            Blocks.ORANGE_BANNER,
            Blocks.MAGENTA_BANNER,
            Blocks.LIGHT_BLUE_BANNER,
            Blocks.YELLOW_BANNER,
            Blocks.LIME_BANNER,
            Blocks.PINK_BANNER,
            Blocks.GRAY_BANNER,
            Blocks.LIGHT_GRAY_BANNER,
            Blocks.CYAN_BANNER,
            Blocks.PURPLE_BANNER,
            Blocks.BLUE_BANNER,
            Blocks.BROWN_BANNER,
            Blocks.GREEN_BANNER,
            Blocks.RED_BANNER,
            Blocks.BLACK_BANNER,
            Blocks.WHITE_WALL_BANNER,
            Blocks.ORANGE_WALL_BANNER,
            Blocks.MAGENTA_WALL_BANNER,
            Blocks.LIGHT_BLUE_WALL_BANNER,
            Blocks.YELLOW_WALL_BANNER,
            Blocks.LIME_WALL_BANNER,
            Blocks.PINK_WALL_BANNER,
            Blocks.GRAY_WALL_BANNER,
            Blocks.LIGHT_GRAY_WALL_BANNER,
            Blocks.CYAN_WALL_BANNER,
            Blocks.PURPLE_WALL_BANNER,
            Blocks.BLUE_WALL_BANNER,
            Blocks.BROWN_WALL_BANNER,
            Blocks.GREEN_WALL_BANNER,
            Blocks.RED_WALL_BANNER,
            Blocks.BLACK_WALL_BANNER
        );
        private static final List<Block> ALL_COPPER_GOLEM_STATUES = List.of(
            Blocks.COPPER_GOLEM_STATUE,
            Blocks.EXPOSED_COPPER_GOLEM_STATUE,
            Blocks.WEATHERED_COPPER_GOLEM_STATUE,
            Blocks.OXIDIZED_COPPER_GOLEM_STATUE,
            Blocks.WAXED_COPPER_GOLEM_STATUE,
            Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE,
            Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE,
            Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE
        );
    }






    private static class Notices {
        private static Supplier<Txt> RESOURCEPACK_INCOMPATIBILITY_NOTICE = () -> new UiTxt(
            "This breaks custom textures and models defined by standard Resource Packs.").red()
        ;
        private static Supplier<Txt> RESOURCEPACK_COMPATIBILITY_NOTICE = () -> new UiTxt(
            "This feature is compatible with Resource Packs.").green()
        ;
        private static Supplier<Txt> MOD_COMPATIBILITY_NOTICE = () -> new UiTxt(
            "This feature is not compatible with other static block entity model mods.").red()
        ;
    }
}



