package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.base.ClientFeature;
import com.snek.engineersbliss.client.feature_handlers.base.__base_ClientFeatureSet;

import java.util.List;
import java.util.function.Supplier;








@SuppressWarnings("java:S1905")
public class AltTexturesClientFeatureSet extends __base_ClientFeatureSet<AltTexturesServerFeatureSet> {
    public static final AltTexturesClientFeatureSet INSTANCE = new AltTexturesClientFeatureSet();
    private AltTexturesClientFeatureSet() {
        super(AltTexturesServerFeatureSet.INSTANCE, () -> new UiTxt("Alternative Textures"));
    }




    public static final ClientFeature<?> MINIMAL_REDSTONE_WIRE = new ClientFeature<>(
        AltTexturesServerFeatureSet.MINIMAL_REDSTONE_WIRE,
        s(() -> new UiTxt("Minimal Redstone Wire")),
        s(() -> new UiTxt("Replaces the messy dust-like Redstone Wire texture with a simple monochrome line to make circuits more readable."))
    );
    public static final ClientFeature<?> NO_REDSTONE_DUST_PARTICLES = new ClientFeature<>(
        AltTexturesServerFeatureSet.NO_REDSTONE_DUST_PARTICLES,
        s(() -> new UiTxt("Disable Redstone dust particles")),
        s(() -> new UiTxt("Stops Levers, Redstone Wire, Redstone Torches, Redstone Ores, and Redstone Repeaters from emitting dust particles when powered."))
    );
    public static final ClientFeature<?> NO_CAMPFIRE_PARTICLES = new ClientFeature<>(
        AltTexturesServerFeatureSet.NO_CAMPFIRE_PARTICLES,
        s(() -> new UiTxt("Disable Campfire particles")),
        s(() -> new UiTxt("Stops Campfires and Soul Campfires from emitting smoke and ember particles."))
    );
    public static final ClientFeature<?> NO_FIRE_PARTICLES = new ClientFeature<>(
        AltTexturesServerFeatureSet.NO_FIRE_PARTICLES,
        s(() -> new UiTxt("Disable Fire particles")),
        s(() -> new UiTxt("Stops Fire and Soul Fire from emitting smoke and ember particles."))
    );
    public static final ClientFeature<?> NO_LAVA_PARTICLES = new ClientFeature<>(
        AltTexturesServerFeatureSet.NO_LAVA_PARTICLES,
        s(() -> new UiTxt("Disable Lava particles")),
        s(() -> new UiTxt("Stops Lava from emitting ember particles."))
    );
    public static final ClientFeature<?> NO_WATER_STREAM_PARTICLES = new ClientFeature<>(
        AltTexturesServerFeatureSet.NO_WATER_STREAM_PARTICLES,
        s(() -> new UiTxt("Disable Water Stream particles")),
        s(() -> new UiTxt("Disables bubble and water splash particles emitted by items travelling in water streams."))
    );
    public static final ClientFeature<?> NO_DRIP_PARTICLES = new ClientFeature<>(
        AltTexturesServerFeatureSet.NO_DRIP_PARTICLES,
        s(() -> new UiTxt("Disable drip particles")),
        s(() -> new UiTxt("Disables dripping water and dripping lava particles emitted by full blocks with water or lava above them."))
    );
    public static final ClientFeature<?> TRANSPARENT_SLIME_BLOCK = new ClientFeature<>(
        AltTexturesServerFeatureSet.TRANSPARENT_SLIME_BLOCK,
        s(() -> new UiTxt("Transparent Slime Blocks")),
        s(() -> new UiTxt("Replaces the texture of Slime Blocks with a less opaque version."))
    );
    public static final ClientFeature<?> TRANSPARENT_HONEY_BLOCK = new ClientFeature<>(
        AltTexturesServerFeatureSet.TRANSPARENT_HONEY_BLOCK,
        s(() -> new UiTxt("Transparent Honey Blocks")),
        s(() -> new UiTxt("Replaces the texture of Honey Blocks with a less opaque version."))
    );
    public static final ClientFeature<?> UNOBSTRUCTIVE_MANGROVE_ROOTS = new ClientFeature<>(
        AltTexturesServerFeatureSet.UNOBSTRUCTIVE_MANGROVE_ROOTS,
        s(() -> new UiTxt("Unobstructive Mangrove Roots")),
        s(() -> new UiTxt("Removes the central diagonal textures present inside of Mangrove Roots to improve visibility."))
    );
    public static final ClientFeature<?> UNOBSTRUCTIVE_SCAFFOLDING = new ClientFeature<>(
        AltTexturesServerFeatureSet.UNOBSTRUCTIVE_SCAFFOLDING,
        s(() -> new UiTxt("Unobstructive Scaffolding")),
        s(() -> new UiTxt("Removes the woven bamboo part of the texture in the middle of Scaffolding blocks to improve visibility."))
    );








    public static final ClientFeature<?> CONSISTENT_SLOPED_RAILS = new ClientFeature<>(
        AltTexturesServerFeatureSet.CONSISTENT_SLOPED_RAILS,
        s(() -> new UiTxt("Consistent sloped Rails")),
        List.of(
            s(() -> new UiTxt("Minecraft Vanilla stretches the 1:1 textures of Rails, Powered Rails, Activator Rails and Detector Rails to 1:√2 to fit the length of sloped shapes.")),
            s(() -> new UiTxt("This toggle replaces the stretched textures with a properly sized model, keeping rails visually consistent across all shapes."))
        )
    );
    public static final ClientFeature<?> STATIC_CHESTS = new ClientFeature<>(
        AltTexturesServerFeatureSet.STATIC_CHESTS,
        s(() -> new UiTxt("Static Chest models")),
        s(() -> new UiTxt("Replaces the costly real-time rendering of Chests, Trapped Chests, and Ender Chests with a static model to improve performance.")),
        s(() -> (UiTxt)new UiTxt("This breaks the opening and closing animations.").Orange()),
        Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE,
        Notices.MOD_COMPATIBILITY_NOTICE
    );
    public static final ClientFeature<?> STATIC_SIGNS = new ClientFeature<>(
        AltTexturesServerFeatureSet.STATIC_SIGNS,
        s(() -> new UiTxt("Static Sign models")),
        s(() -> new UiTxt("Replaces the costly real-time rendering of Signs and Hanging Signs with a static model to improve performance.")),
        s(() -> (UiTxt)new UiTxt("The text must be rendered dynamically, so static signs are laggier than normal blocks, but way less than Vanilla's.").yellow()),
        Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE,
        Notices.MOD_COMPATIBILITY_NOTICE
    );
    public static final ClientFeature<?> STATIC_BANNERS = new ClientFeature<>(
        AltTexturesServerFeatureSet.STATIC_BANNERS,
        s(() -> new UiTxt("Static Banner models")),
        s(() -> new UiTxt("Replaces the costly real-time rendering of Banners with a static model to improve performance.")),
        s(() -> (UiTxt)new UiTxt("This breaks the fluttering animation banners have in Vanilla.").Orange()),
        s(() -> (UiTxt)new UiTxt("This also breaks custom banner patterns. Instead, only the base color is displayed.").Orange()),
        Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE,
        Notices.MOD_COMPATIBILITY_NOTICE
    );
    public static final ClientFeature<?> STATIC_DECORATED_POTS = new ClientFeature<>(
        AltTexturesServerFeatureSet.STATIC_DECORATED_POTS,
        s(() -> new UiTxt("Static Decorated Pot models")),
        s(() -> new UiTxt("Replaces the costly real-time rendering of Decorated Pots with a static model to improve performance.")),
        s(() -> (UiTxt)new UiTxt("This breaks the wobbling animation that plays in Vanilla when a Decorated Pot is right-clicked.").Orange()),
        s(() -> (UiTxt)new UiTxt("Sides customized with Sherds (not the default brick) must be rendered dynamically, so static decorated pots are laggier than normal blocks, but way less than Vanilla's.").yellow()),
        Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE,
        Notices.MOD_COMPATIBILITY_NOTICE
    );
    public static final ClientFeature<?> STATIC_BELLS = new ClientFeature<>(
        AltTexturesServerFeatureSet.STATIC_BELLS,
        s(() -> new UiTxt("Static Bell models")),
        s(() -> new UiTxt("Replaces the costly real-time rendering of Bells with a static model to improve performance.")),
        s(() -> (UiTxt)new UiTxt("This breaks the swinging animation that plays in Vanilla when a Bell is rung.").Orange()),
        Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE,
        Notices.MOD_COMPATIBILITY_NOTICE
    );
    public static final ClientFeature<?> STATIC_COPPER_GOLEM_STATUES = new ClientFeature<>(
        AltTexturesServerFeatureSet.STATIC_COPPER_GOLEM_STATUES,
        s(() -> new UiTxt("Static Copper Golem Statue models")),
        s(() -> new UiTxt("Replaces the costly real-time rendering of Copper Golem Statues with a static model to improve performance.")),
        Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE,
        Notices.MOD_COMPATIBILITY_NOTICE
    );
    public static final ClientFeature<?> STATIC_LECTERNS = new ClientFeature<>(
        AltTexturesServerFeatureSet.STATIC_LECTERNS,
        s(() -> new UiTxt("Static Lectern models")),
        s(() -> new UiTxt("Replaces the costly real-time rendering of Lecterns with a static model to improve performance.")),
        Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE,
        Notices.MOD_COMPATIBILITY_NOTICE
    );
    public static final ClientFeature<?> STATIC_BEDS = new ClientFeature<>(
        AltTexturesServerFeatureSet.STATIC_BEDS,
        s(() -> new UiTxt("Static Bed models")),
        s(() -> new UiTxt("Replaces the costly real-time rendering of Beds with a static model to improve performance.")),
        //? if <26.2 {
             /*Notices.RESOURCEPACK_INCOMPATIBILITY_NOTICE,
             Notices.MOD_COMPATIBILITY_NOTICE
        *///? } else {
            s(() -> (UiTxt)new UiTxt("This feature is disabled in 26.2+. Modern versions already use static models for beds.").green())
        //? }
    );
    public static final ClientFeature<?> OPTIMIZED_SHELVES = new ClientFeature<>(
        AltTexturesServerFeatureSet.OPTIMIZED_SHELVES,
        s(() -> new UiTxt("Optimized Shelf models")),
        s(() -> new UiTxt("Optimizes shelf rendering logic to improve performance.")),
        s(() -> new UiTxt("This doesn't cause visual changes.")),
        Notices.RESOURCEPACK_COMPATIBILITY_NOTICE
    );
    public static final ClientFeature<?> OPTIMIZED_CAMPFIRES = new ClientFeature<>(
        AltTexturesServerFeatureSet.OPTIMIZED_CAMPFIRES,
        s(() -> new UiTxt("Optimized Campfire models")),
        s(() -> new UiTxt("Optimizes Campfire and Soul Campfire rendering logic to improve performance.")),
        s(() -> new UiTxt("This doesn't cause visual changes.")),
        Notices.RESOURCEPACK_COMPATIBILITY_NOTICE
    );









    public static final ClientFeature<?> REDSTONE_WIRE_3D = new ClientFeature<>(
        AltTexturesServerFeatureSet.REDSTONE_WIRE_3D,
        s(() -> new UiTxt("3D Redstone Wire")),
        s(() -> new UiTxt("Replaces the default flat texture of Redstone Wire with a three-dimensional model.")),
        s(() -> (UiTxt)new UiTxt("Implies [Minimal Redstone Wire]").yellow())
    );
    public static final ClientFeature<?> RAILS_3D = new ClientFeature<>(
        AltTexturesServerFeatureSet.RAILS_3D,
        s(() -> new UiTxt("3D Rails")),
        s(() -> new UiTxt("Replaces the default flat texture of Rails, Powered Rails, Activator Rails, and Detector Rails with a three-dimensional model.")),
        s(() -> (UiTxt)new UiTxt("Implies [Consistent sloped Rails]").yellow())
    );
    public static final ClientFeature<?> LADDERS_3D = new ClientFeature<>(
        AltTexturesServerFeatureSet.LADDERS_3D,
        s(() -> new UiTxt("3D Ladders")),
        s(() -> new UiTxt("Replaces the default flat texture of Ladders with a three-dimensional model."))
    );
    public static final ClientFeature<?> CHAINS_3D = new ClientFeature<>(
        AltTexturesServerFeatureSet.CHAINS_3D,
        s(() -> new UiTxt("3D Chains")),
        s(() -> new UiTxt("Replaces the default flat texture of Iron Chains and Copper Chains with a three-dimensional model.")),
        s(() -> (UiTxt)new UiTxt("This also affects the chains in Lanterns, Copper Lanterns, and Hanging Signs.").yellow()),
        s(() -> (UiTxt)new UiTxt("3D Hanging Sign chains require [Static Sign Models]").yellow())
    );
    public static final ClientFeature<?> BARS_3D = new ClientFeature<>(
        AltTexturesServerFeatureSet.BARS_3D,
        s(() -> new UiTxt("3D Bars")),
        s(() -> new UiTxt("Replaces the default flat texture of Iron Bars and Copper Bars with a three-dimensional model."))
    );
    public static final ClientFeature<?> VINES_3D = new ClientFeature<>(
        AltTexturesServerFeatureSet.VINES_3D,
        s(() -> new UiTxt("3D Vines")),
        s(() -> new UiTxt("Replaces the default flat texture of Vines with a three-dimensional model."))
    );
    public static final ClientFeature<?> GLOW_LICHEN_3D = new ClientFeature<>(
        AltTexturesServerFeatureSet.GLOW_LICHEN_3D,
        s(() -> new UiTxt("3D Glow Lichen")),
        s(() -> new UiTxt("Replaces the default flat texture of Glow Lichen with a three-dimensional model."))
    );















    private static class Notices {
        private static Supplier<UiTxt> RESOURCEPACK_INCOMPATIBILITY_NOTICE = () -> (UiTxt)new UiTxt(
            "This breaks custom textures and models defined by standard Resource Packs.").red()
        ;
        private static Supplier<UiTxt> RESOURCEPACK_COMPATIBILITY_NOTICE = () -> (UiTxt)new UiTxt(
            "This feature is compatible with Resource Packs.").green()
        ;
        private static Supplier<UiTxt> MOD_COMPATIBILITY_NOTICE = () -> (UiTxt)new UiTxt(
            "This feature is not compatible with other static block entity model mods.").red()
        ;
    }
}



