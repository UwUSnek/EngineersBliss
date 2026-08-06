package com.snek.engineersbliss.custom.items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.custom.blocks.CustomBlockHandler;
import com.snek.engineersbliss.custom.items.base.CustomBlockItem;
import com.snek.engineersbliss.custom.items.base.CustomWaterPlaceableBlockItem;
import com.snek.engineersbliss.custom.items.special.ArmorStandWithArmsItem;
import com.snek.engineersbliss.custom.items.special.CustomBedHalfBlockItem;
import com.snek.engineersbliss.custom.items.special.CustomBeehiveItem;
import com.snek.engineersbliss.custom.items.special.CustomCauldronItem;
import com.snek.engineersbliss.custom.items.special.CustomCaveVinesItem;
import com.snek.engineersbliss.custom.items.special.CustomDoorHalfBlockItem;
import com.snek.engineersbliss.custom.items.special.CustomHalfBlockItem;
import com.snek.engineersbliss.custom.items.special.HeadlessPistonItem;
import com.snek.engineersbliss.custom.items.special.NetherPortalItem;
import com.snek.engineersbliss.custom.items.special.PistonHeadItem;
import com.snek.engineersbliss.custom.items.special.UnlitCampfireItem;
import com.snek.engineersbliss.custom.items.special.YourPlayerHeadItem;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Blocks;



















public class CustomItemHandler {
    private CustomItemHandler() {}



    public static final Item GREEN_SCREEN = register(
        "green_screen",
        p -> new CustomBlockItem(CustomBlockHandler.GREEN_SCREEN, p, List.of(CustomBlockHandler.GREEN_SCREEN)),
        new Txt("A perfectly green block with no shading."),
        Notices.CUSTOM_BLOCK
    );
    public static final Item BLUE_SCREEN = register(
        "blue_screen",
        p -> new CustomBlockItem(CustomBlockHandler.BLUE_SCREEN, p, List.of(CustomBlockHandler.BLUE_SCREEN)),
        new Txt("A perfectly blue block with no shading."),
        Notices.CUSTOM_BLOCK
    );




    public static final Item FRICTIONLESS_BLOCK = register(
        "frictionless_block",
        p -> new CustomBlockItem(CustomBlockHandler.FRICTIONLESS_BLOCK, p, List.of(CustomBlockHandler.FRICTIONLESS_BLOCK)),
        List.of(
            new Txt("A block with no friction. Everything slides on it. Forever.").lightGray(),
            new Txt("The friction of whatever fluid you are travelling in still applies.").lightGray()
        ),
        Notices.FRICTIONLESS_BLOCK_MOVEMENT_ISSUE, Notices.CUSTOM_BLOCK
    );
    public static final Item FRICTIONFUL_BLOCK = register(
        "frictionful_block",
        p -> new CustomBlockItem(CustomBlockHandler.FRICTIONFUL_BLOCK, p, List.of(CustomBlockHandler.FRICTIONFUL_BLOCK)),
        new Txt("A block with infinite friction. Nothing can walk or slide on it. At all."),
        Notices.FRICTIONFUL_BLOCK_MOVEMENT_ISSUE, Notices.CUSTOM_BLOCK
    );




    public static final Item ITEM_SOURCE = register(
        "item_source",
        p -> new CustomBlockItem(CustomBlockHandler.ITEM_SOURCE, p, List.of(CustomBlockHandler.ITEM_SOURCE)),
        List.of(
            new Txt("A configurable container that can never be emptied.").lightGray(),
            new Txt("It can only hold one stack at a time.").lightGray()
        ),
        Notices.CUSTOM_BLOCK
    );
    public static final Item ITEM_SINK = register(
        "item_sink",
        p -> new CustomBlockItem(CustomBlockHandler.ITEM_SINK, p, List.of(CustomBlockHandler.ITEM_SINK)),
        new Txt("A container with unlimited capacity."),
        Notices.CUSTOM_BLOCK
    );
















    public static final Item FULL_BEE_NEST = register(
        "full_bee_nest", "beehives",
        p -> new CustomBeehiveItem(Blocks.BEE_NEST, p, 3, 5, null),
        new Txt("A Bee Nest with 5 levels of Honey and 3 Bees."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item FULL_BEE_NEST_NO_BEES = register(
        "full_bee_nest_no_bees", "beehives",
        p -> new CustomBeehiveItem(Blocks.BEE_NEST, p, 0, 5, null),
        new Txt("A Bee Nest with 5 levels of Honey but no Bees."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item FULL_BEEHIVE = register(
        "full_beehive", "beehives",
        p -> new CustomBeehiveItem(Blocks.BEEHIVE,  p, 3, 5, null),
        new Txt("A Beehive with 5 levels of Honey and 3 Bees."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item FULL_BEEHIVE_NO_BEES = register(
        "full_beehive_no_bees", "beehives",
        p -> new CustomBeehiveItem(Blocks.BEEHIVE,  p, 0, 5, null),
        new Txt("A Beehive with 5 levels of Honey but no Bees."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item YOUR_PLAYER_HEAD = register(
        "your_player_head",
        p -> new YourPlayerHeadItem(Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD, Direction.DOWN, p, null),
        new Txt("Your Player Head."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );



    public static final Item ARMOR_STAND_WITH_ARMS = register(
        "armor_stand_with_arms",
        ArmorStandWithArmsItem::new,
        new Txt("It comes with arms!"),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_ENTITY
    );



    public static final Item HEADLESS_PISTON = register(
        "headless_piston", "pistons",
        p -> new HeadlessPistonItem(Blocks.PISTON, p, null),
        new Txt("A Piston without the Piston Head part."),
        Notices.HEADLESS_PISTON_RESETS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PISTON_HEAD = register(
        "piston_head", "pistons",
        p -> new PistonHeadItem(Blocks.PISTON_HEAD, false, false, p, null),
        new Txt("A Piston, but only the Piston Head part (Long variant)."),
        Notices.PISTON_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item SHORT_PISTON_HEAD = register(
        "short_piston_head", "pistons",
        p -> new PistonHeadItem(Blocks.PISTON_HEAD, false, true, p, null),
        new Txt("A Piston, but only the Piston Head part (Short variant)."),
        Notices.PISTON_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item HEADLESS_STICKY_PISTON = register(
        "headless_sticky_piston", "pistons",
        p -> new HeadlessPistonItem(Blocks.STICKY_PISTON, p, null),
        new Txt("A Sticky Piston without the Piston Head part."),
        Notices.HEADLESS_PISTON_RESETS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item STICKY_PISTON_HEAD = register(
        "sticky_piston_head", "pistons",
        p -> new PistonHeadItem(Blocks.PISTON_HEAD, true, false, p, null),
        new Txt("A Sticky Piston, but only the Piston Head part (Long variant)."),
        Notices.PISTON_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item SHORT_STICKY_PISTON_HEAD = register(
        "short_sticky_piston_head", "pistons",
        p -> new PistonHeadItem(Blocks.PISTON_HEAD, true, true, p, null),
        new Txt("A Sticky Piston, but only the Piston Head part (Short variant)."),
        Notices.PISTON_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );





    public static final Item ROSE_BUSH_STEM = register(
        "rose_bush_stem", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.ROSE_BUSH, p, true),
        new Txt("The bottom half of a Rose Bush in item form."),
        Notices.TALL_PLANT_STEM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item ROSE_BUSH_CROWN = register(
        "rose_bush_crown", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.ROSE_BUSH, p, false),
        new Txt("The top half of a Rose Bush in item form."),
        Notices.TALL_PLANT_CROWN_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item SUNFLOWER_STEM = register(
        "sunflower_stem", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.SUNFLOWER, p, true),
        new Txt("The bottom half of a Sunflower in item form."),
        Notices.TALL_PLANT_STEM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item SUNFLOWER_CROWN = register(
        "sunflower_crown", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.SUNFLOWER, p, false),
        new Txt("The top half of a Sunflower in item form."),
        Notices.TALL_PLANT_CROWN_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item TALL_GRASS_STEM = register(
        "tall_grass_stem", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.TALL_GRASS, p, true),
        new Txt("The bottom half of a Tall Grass in item form."),
        Notices.TALL_PLANT_STEM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item TALL_GRASS_CROWN = register(
        "tall_grass_crown", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.TALL_GRASS, p, false),
        new Txt("The top half of a Tall Grass in item form."),
        Notices.TALL_PLANT_CROWN_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PITCHER_PLANT_STEM = register(
        "pitcher_plant_stem", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.PITCHER_PLANT, p, true),
        new Txt("The bottom half of a Pitcher Plant in item form."),
        Notices.TALL_PLANT_STEM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PITCHER_PLANT_CROWN = register(
        "pitcher_plant_crown", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.PITCHER_PLANT, p, false),
        new Txt("The top half of a Pitcher Plant in item form."),
        Notices.TALL_PLANT_CROWN_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PEONY_STEM = register(
        "peony_stem", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.PEONY, p, true),
        new Txt("The bottom half of a Peony in item form."),
        Notices.TALL_PLANT_STEM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PEONY_CROWN = register(
        "peony_crown", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.PEONY, p, false),
        new Txt("The top half of a Peony in item form."),
        Notices.TALL_PLANT_CROWN_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LARGE_FERN_STEM = register(
        "large_fern_stem", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.LARGE_FERN, p, true),
        new Txt("The bottom half of a Large Fern in item form."),
        Notices.TALL_PLANT_STEM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LARGE_FERN_CROWN = register(
        "large_fern_crown", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.LARGE_FERN, p, false),
        new Txt("The top half of a Large Fern in item form."),
        Notices.TALL_PLANT_CROWN_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LILAC_STEM = register(
        "lilac_stem", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.LILAC, p, true),
        new Txt("The bottom half of a Lilac in item form."),
        Notices.TALL_PLANT_STEM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LILAC_CROWN = register(
        "lilac_crown", "tall_plants",
        p -> new CustomHalfBlockItem(Blocks.LILAC, p, false),
        new Txt("The top half of a Lilac in item form."),
        Notices.TALL_PLANT_CROWN_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item KELP_STEM = register(
        "kelp_stem", "long_plants",
        p -> new CustomWaterPlaceableBlockItem(Blocks.KELP_PLANT, p, List.of(Blocks.KELP_PLANT)),
        new Txt("A Kelp Plant block in item form."),
        Notices.HANGING_LONG_PLANT_STEM_RESETS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item KELP_CROWN = register(
        "kelp_crown", "long_plants",
        p -> new CustomWaterPlaceableBlockItem(Blocks.KELP, p, List.of(Blocks.KELP)),
        new Txt("A Kelp block in item form."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CAVE_VINES_STEM = register(
        "cave_vines_stem", "long_plants",
        p -> new CustomCaveVinesItem(Blocks.CAVE_VINES_PLANT, false, p, List.of(Blocks.CAVE_VINES_PLANT)),
        new Txt("A Cave Vines Plant block in item form."),
        Notices.HANGING_LONG_PLANT_STEM_RESETS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CAVE_VINES_STEM_WITH_BERRIES = register(
        "cave_vines_stem_with_berries", "long_plants",
        p -> new CustomCaveVinesItem(Blocks.CAVE_VINES_PLANT, true, p, null),
        new Txt("A Cave Vines Plant block in item form. Comes with Glow Berries."),
        Notices.HANGING_LONG_PLANT_STEM_RESETS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CAVE_VINES_CROWN = register(
        "cave_vines_crown", "long_plants",
        p -> new CustomCaveVinesItem(Blocks.CAVE_VINES, false, p, List.of(Blocks.CAVE_VINES)),
        new Txt("A Cave Vines block in item form."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CAVE_VINES_CROWN_WITH_BERRIES = register(
        "cave_vines_crown_with_berries", "long_plants",
        p -> new CustomCaveVinesItem(Blocks.CAVE_VINES, true, p, null),
        new Txt("A Cave Vines block in item form. Comes with Glow Berries."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item TWISTING_VINES_STEM = register(
        "twisting_vines_stem", "long_plants",
        p -> new CustomBlockItem(Blocks.TWISTING_VINES_PLANT, p, List.of(Blocks.TWISTING_VINES_PLANT)),
        new Txt("A Twisting Vines Plant block in item form."),
        Notices.LONG_PLANT_STEM_RESETS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item TWISTING_VINES_CROWN = register(
        "twisting_vines_crown", "long_plants",
        p -> new CustomBlockItem(Blocks.TWISTING_VINES, p, List.of(Blocks.TWISTING_VINES)),
        new Txt("A Twisting Vines block in item form."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item WEEPING_VINES_STEM = register(
        "weeping_vines_stem", "long_plants",
        p -> new CustomBlockItem(Blocks.WEEPING_VINES_PLANT, p, List.of(Blocks.WEEPING_VINES_PLANT)),
        new Txt("A Weeping Vines Plant block in item form."),
        Notices.HANGING_LONG_PLANT_STEM_RESETS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item WEEPING_VINES_CROWN = register(
        "weeping_vines_crown", "long_plants",
        p -> new CustomBlockItem(Blocks.WEEPING_VINES, p, List.of(Blocks.WEEPING_VINES)),
        new Txt("A Weeping Vines block in item form."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item FIRE = register(
        "fire", "legacy",
        p -> new CustomBlockItem(Blocks.FIRE, p, List.of(Blocks.FIRE, Blocks.SOUL_FIRE), true),
        new Txt("Fire in item form."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item NETHER_PORTAL = register(
        "nether_portal", "legacy",
        p -> new NetherPortalItem(Blocks.NETHER_PORTAL, p, List.of(Blocks.NETHER_PORTAL)),
        new Txt("A Nether Portal block in item form."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item END_PORTAL = register(
        "end_portal", "legacy",
        p -> new CustomBlockItem(Blocks.END_PORTAL, p, List.of(Blocks.END_PORTAL), true),
        new Txt("An End Portal block in item form."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item END_GATEWAY = register(
        "end_gateway", "legacy",
        p -> new CustomBlockItem(Blocks.END_GATEWAY, p, List.of(Blocks.END_GATEWAY), true),
        new Txt("An End Gateway in item form."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item UNLIT_CAMPFIRE = register(
        "unlit_campfire", "unlit_campfires",
        p -> new UnlitCampfireItem(Blocks.CAMPFIRE, p),
        new Txt("A Campfire that is unlit by default."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item UNLIT_SOUL_CAMPFIRE = register(
        "unlit_soul_campfire", "unlit_campfires",
        p -> new UnlitCampfireItem(Blocks.SOUL_CAMPFIRE, p),
        new Txt("A Soul Campfire that is unlit by default."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item WATER_CAULDRON_3X = register(
        "water_cauldron_3x", "cauldrons",
        p -> new CustomCauldronItem(Blocks.WATER_CAULDRON, p, List.of(Blocks.WATER_CAULDRON), 3),
        new Txt("A Cauldron filled with Water."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item WATER_CAULDRON_2X = register(
        "water_cauldron_2x", "cauldrons",
        p -> new CustomCauldronItem(Blocks.WATER_CAULDRON, p, null, 2),
        new Txt("A Cauldron two-thirds full of Water."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item WATER_CAULDRON_1X = register(
        "water_cauldron_1x", "cauldrons",
        p -> new CustomCauldronItem(Blocks.WATER_CAULDRON, p, null, 1),
        new Txt("A Cauldron one-third full of Water."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POWDER_SNOW_CAULDRON_3X = register(
        "powder_snow_cauldron_3x", "cauldrons",
        p -> new CustomCauldronItem(Blocks.POWDER_SNOW_CAULDRON, p, List.of(Blocks.POWDER_SNOW_CAULDRON), 3),
        new Txt("A Cauldron filled with Powder Snow."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POWDER_SNOW_CAULDRON_2X = register(
        "powder_snow_cauldron_2x", "cauldrons",
        p -> new CustomCauldronItem(Blocks.POWDER_SNOW_CAULDRON, p, null, 2),
        new Txt("A Cauldron two-thirds full of Powder Snow."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POWDER_SNOW_CAULDRON_1X = register(
        "powder_snow_cauldron_1x", "cauldrons",
        p -> new CustomCauldronItem(Blocks.POWDER_SNOW_CAULDRON, p, null, 1),
        new Txt("A Cauldron one-third full of Powder Snow."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LAVA_CAULDRON = register(
        "lava_cauldron", "cauldrons",
        p -> new CustomCauldronItem(Blocks.LAVA_CAULDRON, p, List.of(Blocks.LAVA_CAULDRON), 3),
        new Txt("A Cauldron filled with Lava."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item FROSTED_ICE = register(
        "frosted_ice",
        p -> new CustomBlockItem(Blocks.FROSTED_ICE, p, List.of(Blocks.FROSTED_ICE)),
        new Txt("Frosted Ice. This is the Ice created by Forst Walker."),
        Notices.FROSTED_ICE_MELTS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item CANDLE_CAKE = register(
        "candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.CANDLE_CAKE, p, List.of(Blocks.CANDLE_CAKE)),
        new Txt("A Cake with a Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item WHITE_CANDLE_CAKE = register(
        "white_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.WHITE_CANDLE_CAKE, p, List.of(Blocks.WHITE_CANDLE_CAKE)),
        new Txt("A Cake with a White Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item ORANGE_CANDLE_CAKE = register(
        "orange_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.ORANGE_CANDLE_CAKE, p, List.of(Blocks.ORANGE_CANDLE_CAKE)),
        new Txt("A Cake with an Orange Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item MAGENTA_CANDLE_CAKE = register(
        "magenta_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.MAGENTA_CANDLE_CAKE, p, List.of(Blocks.MAGENTA_CANDLE_CAKE)),
        new Txt("A Cake with a Magenta Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIGHT_BLUE_CANDLE_CAKE = register(
        "light_blue_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.LIGHT_BLUE_CANDLE_CAKE, p, List.of(Blocks.LIGHT_BLUE_CANDLE_CAKE)),
        new Txt("A Cake with a Light Blue Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item YELLOW_CANDLE_CAKE = register(
        "yellow_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.YELLOW_CANDLE_CAKE, p, List.of(Blocks.YELLOW_CANDLE_CAKE)),
        new Txt("A Cake with a Yellow Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIME_CANDLE_CAKE = register(
        "lime_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.LIME_CANDLE_CAKE, p, List.of(Blocks.LIME_CANDLE_CAKE)),
        new Txt("A Cake with a Lime Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PINK_CANDLE_CAKE = register(
        "pink_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.PINK_CANDLE_CAKE, p, List.of(Blocks.PINK_CANDLE_CAKE)),
        new Txt("A Cake with a Pink Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item GRAY_CANDLE_CAKE = register(
        "gray_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.GRAY_CANDLE_CAKE, p, List.of(Blocks.GRAY_CANDLE_CAKE)),
        new Txt("A Cake with a Gray Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIGHT_GRAY_CANDLE_CAKE = register(
        "light_gray_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.LIGHT_GRAY_CANDLE_CAKE, p, List.of(Blocks.LIGHT_GRAY_CANDLE_CAKE)),
        new Txt("A Cake with a Light Gray Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CYAN_CANDLE_CAKE = register(
        "cyan_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.CYAN_CANDLE_CAKE, p, List.of(Blocks.CYAN_CANDLE_CAKE)),
        new Txt("A Cake with a Cyan Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PURPLE_CANDLE_CAKE = register(
        "purple_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.PURPLE_CANDLE_CAKE, p, List.of(Blocks.PURPLE_CANDLE_CAKE)),
        new Txt("A Cake with a Purple Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BLUE_CANDLE_CAKE = register(
        "blue_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.BLUE_CANDLE_CAKE, p, List.of(Blocks.BLUE_CANDLE_CAKE)),
        new Txt("A Cake with a Blue Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BROWN_CANDLE_CAKE = register(
        "brown_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.BROWN_CANDLE_CAKE, p, List.of(Blocks.BROWN_CANDLE_CAKE)),
        new Txt("A Cake with a Brown Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item GREEN_CANDLE_CAKE = register(
        "green_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.GREEN_CANDLE_CAKE, p, List.of(Blocks.GREEN_CANDLE_CAKE)),
        new Txt("A Cake with a Green Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item RED_CANDLE_CAKE = register(
        "red_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.RED_CANDLE_CAKE, p, List.of(Blocks.RED_CANDLE_CAKE)),
        new Txt("A Cake with a Red Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BLACK_CANDLE_CAKE = register(
        "black_candle_cake", "candle_cakes",
        p -> new CustomBlockItem(Blocks.BLACK_CANDLE_CAKE, p, List.of(Blocks.BLACK_CANDLE_CAKE)),
        new Txt("A Cake with a Black Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    static final Item WHITE_BED_HEAD = register(
        "white_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.WHITE_BED, p, false),
        new Txt("The head part of a White Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    static final Item WHITE_BED_FOOT = register(
        "white_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.WHITE_BED, p, true),
        new Txt("The foot part of a White Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item ORANGE_BED_HEAD = register(
        "orange_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.ORANGE_BED, p, false),
        new Txt("The head part of an Orange Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item ORANGE_BED_FOOT = register(
        "orange_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.ORANGE_BED, p, true),
        new Txt("The foot part of an Orange Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item MAGENTA_BED_HEAD = register(
        "magenta_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.MAGENTA_BED, p, false),
        new Txt("The head part of a Magenta Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item MAGENTA_BED_FOOT = register(
        "magenta_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.MAGENTA_BED, p, true),
        new Txt("The foot part of a Magenta Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIGHT_BLUE_BED_HEAD = register(
        "light_blue_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.LIGHT_BLUE_BED, p, false),
        new Txt("The head part of a Light Blue Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIGHT_BLUE_BED_FOOT = register(
        "light_blue_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.LIGHT_BLUE_BED, p, true),
        new Txt("The foot part of a Light Blue Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item YELLOW_BED_HEAD = register(
        "yellow_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.YELLOW_BED, p, false),
        new Txt("The head part of a Yellow Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item YELLOW_BED_FOOT = register(
        "yellow_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.YELLOW_BED, p, true),
        new Txt("The foot part of a Yellow Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIME_BED_HEAD = register(
        "lime_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.LIME_BED, p, false),
        new Txt("The head part of a Lime Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIME_BED_FOOT = register(
        "lime_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.LIME_BED, p, true),
        new Txt("The foot part of a Lime Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PINK_BED_HEAD = register(
        "pink_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.PINK_BED, p, false),
        new Txt("The head part of a Pink Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PINK_BED_FOOT = register(
        "pink_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.PINK_BED, p, true),
        new Txt("The foot part of a Pink Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item GRAY_BED_HEAD = register(
        "gray_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.GRAY_BED, p, false),
        new Txt("The head part of a Gray Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item GRAY_BED_FOOT = register(
        "gray_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.GRAY_BED, p, true),
        new Txt("The foot part of a Gray Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIGHT_GRAY_BED_HEAD = register(
        "light_gray_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.LIGHT_GRAY_BED, p, false),
        new Txt("The head part of a Light Gray Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIGHT_GRAY_BED_FOOT = register(
        "light_gray_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.LIGHT_GRAY_BED, p, true),
        new Txt("The foot part of a Light Gray Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CYAN_BED_HEAD = register(
        "cyan_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.CYAN_BED, p, false),
        new Txt("The head part of a Cyan Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CYAN_BED_FOOT = register(
        "cyan_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.CYAN_BED, p, true),
        new Txt("The foot part of a Cyan Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PURPLE_BED_HEAD = register(
        "purple_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.PURPLE_BED, p, false),
        new Txt("The head part of a Purple Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PURPLE_BED_FOOT = register(
        "purple_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.PURPLE_BED, p, true),
        new Txt("The foot part of a Purple Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BLUE_BED_HEAD = register(
        "blue_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.BLUE_BED, p, false),
        new Txt("The head part of a Blue Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BLUE_BED_FOOT = register(
        "blue_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.BLUE_BED, p, true),
        new Txt("The foot part of a Blue Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BROWN_BED_HEAD = register(
        "brown_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.BROWN_BED, p, false),
        new Txt("The head part of a Brown Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BROWN_BED_FOOT = register(
        "brown_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.BROWN_BED, p, true),
        new Txt("The foot part of a Brown Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item GREEN_BED_HEAD = register(
        "green_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.GREEN_BED, p, false),
        new Txt("The head part of a Green Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item GREEN_BED_FOOT = register(
        "green_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.GREEN_BED, p, true),
        new Txt("The foot part of a Green Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item RED_BED_HEAD = register(
        "red_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.RED_BED, p, false),
        new Txt("The head part of a Red Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item RED_BED_FOOT = register(
        "red_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.RED_BED, p, true),
        new Txt("The foot part of a Red Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BLACK_BED_HEAD = register(
        "black_bed_head", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.BLACK_BED, p, false),
        new Txt("The head part of a Black Bed in item form."),
        Notices.BED_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BLACK_BED_FOOT = register(
        "black_bed_foot", "beds",
        p -> new CustomBedHalfBlockItem(Blocks.BLACK_BED, p, true),
        new Txt("The foot part of a Black Bed in item form."),
        Notices.BED_FOOT_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );





    public static final Item ACACIA_DOOR_BOTTOM = register(
        "acacia_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.ACACIA_DOOR, p, true),
        new Txt("The bottom half of an Acacia Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item ACACIA_DOOR_TOP = register(
        "acacia_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.ACACIA_DOOR, p, false),
        new Txt("The top half of an Acacia Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item BAMBOO_DOOR_BOTTOM = register(
        "bamboo_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.BAMBOO_DOOR, p, true),
        new Txt("The bottom half of a Bamboo Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BAMBOO_DOOR_TOP = register(
        "bamboo_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.BAMBOO_DOOR, p, false),
        new Txt("The top half of a Bamboo Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item BIRCH_DOOR_BOTTOM = register(
        "birch_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.BIRCH_DOOR, p, true),
        new Txt("The bottom half of a Birch Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BIRCH_DOOR_TOP = register(
        "birch_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.BIRCH_DOOR, p, false),
        new Txt("The top half of a Birch Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item CHERRY_DOOR_BOTTOM = register(
        "cherry_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.CHERRY_DOOR, p, true),
        new Txt("The bottom half of a Cherry Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CHERRY_DOOR_TOP = register(
        "cherry_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.CHERRY_DOOR, p, false),
        new Txt("The top half of a Cherry Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item CRIMSON_DOOR_BOTTOM = register(
        "crimson_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.CRIMSON_DOOR, p, true),
        new Txt("The bottom half of a Crimson Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CRIMSON_DOOR_TOP = register(
        "crimson_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.CRIMSON_DOOR, p, false),
        new Txt("The top half of a Crimson Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item DARK_OAK_DOOR_BOTTOM = register(
        "dark_oak_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.DARK_OAK_DOOR, p, true),
        new Txt("The bottom half of a Dark Oak Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item DARK_OAK_DOOR_TOP = register(
        "dark_oak_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.DARK_OAK_DOOR, p, false),
        new Txt("The top half of a Dark Oak Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item IRON_DOOR_BOTTOM = register(
        "iron_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.IRON_DOOR, p, true),
        new Txt("The bottom half of an Iron Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item IRON_DOOR_TOP = register(
        "iron_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.IRON_DOOR, p, false),
        new Txt("The top half of an Iron Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item JUNGLE_DOOR_BOTTOM = register(
        "jungle_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.JUNGLE_DOOR, p, true),
        new Txt("The bottom half of a Jungle Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item JUNGLE_DOOR_TOP = register(
        "jungle_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.JUNGLE_DOOR, p, false),
        new Txt("The top half of a Jungle Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item MANGROVE_DOOR_BOTTOM = register(
        "mangrove_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.MANGROVE_DOOR, p, true),
        new Txt("The bottom half of a Mangrove Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item MANGROVE_DOOR_TOP = register(
        "mangrove_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.MANGROVE_DOOR, p, false),
        new Txt("The top half of a Mangrove Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item OAK_DOOR_BOTTOM = register(
        "oak_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.OAK_DOOR, p, true),
        new Txt("The bottom half of an Oak Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item OAK_DOOR_TOP = register(
        "oak_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.OAK_DOOR, p, false),
        new Txt("The top half of an Oak Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item PALE_OAK_DOOR_BOTTOM = register(
        "pale_oak_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.PALE_OAK_DOOR, p, true),
        new Txt("The bottom half of a Pale Oak Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PALE_OAK_DOOR_TOP = register(
        "pale_oak_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.PALE_OAK_DOOR, p, false),
        new Txt("The top half of a Pale Oak Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item SPRUCE_DOOR_BOTTOM = register(
        "spruce_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.SPRUCE_DOOR, p, true),
        new Txt("The bottom half of a Spruce Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item SPRUCE_DOOR_TOP = register(
        "spruce_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.SPRUCE_DOOR, p, false),
        new Txt("The top half of a Spruce Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item WARPED_DOOR_BOTTOM = register(
        "warped_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.WARPED_DOOR, p, true),
        new Txt("The bottom half of a Warped Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item WARPED_DOOR_TOP = register(
        "warped_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.WARPED_DOOR, p, false),
        new Txt("The top half of a Warped Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item COPPER_DOOR_BOTTOM = register(
        "copper_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.COPPER_DOOR, p, true),
        new Txt("The bottom half of a Copper Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item COPPER_DOOR_TOP = register(
        "copper_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.COPPER_DOOR, p, false),
        new Txt("The top half of a Copper Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item EXPOSED_COPPER_DOOR_BOTTOM = register(
        "exposed_copper_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.EXPOSED_COPPER_DOOR, p, true),
        new Txt("The bottom half of an Exposed Copper Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item EXPOSED_COPPER_DOOR_TOP = register(
        "exposed_copper_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.EXPOSED_COPPER_DOOR, p, false),
        new Txt("The top half of an Exposed Copper Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item WEATHERED_COPPER_DOOR_BOTTOM = register(
        "weathered_copper_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.WEATHERED_COPPER_DOOR, p, true),
        new Txt("The bottom half of a Weathere Copper Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item WEATHERED_COPPER_DOOR_TOP = register(
        "weathered_copper_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.WEATHERED_COPPER_DOOR, p, false),
        new Txt("The top half of a Weathere Copper Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );

    public static final Item OXIDIZED_COPPER_DOOR_BOTTOM = register(
        "oxidized_copper_door_bottom", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.OXIDIZED_COPPER_DOOR, p, true),
        new Txt("The bottom half of an Oxidized Copper Door in item form."),
        Notices.DOOR_BOTTOM_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item OXIDIZED_COPPER_DOOR_TOP = register(
        "oxidized_copper_door_top", "doors",
        p -> new CustomDoorHalfBlockItem(Blocks.OXIDIZED_COPPER_DOOR, p, false),
        new Txt("The top half of an Oxidized Copper Door in item form."),
        Notices.DOOR_TOP_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item POTTED_DANDELION = register(
        "potted_dandelion", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_DANDELION, p, List.of(Blocks.POTTED_DANDELION)),
        new Txt("A Dandelion in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_POPPY = register(
        "potted_poppy", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_POPPY, p, List.of(Blocks.POTTED_POPPY)),
        new Txt("A Poppy in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_BLUE_ORCHID = register(
        "potted_blue_orchid", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_BLUE_ORCHID, p, List.of(Blocks.POTTED_BLUE_ORCHID)),
        new Txt("A Blue Orchid in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_ALLIUM = register(
        "potted_allium", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_ALLIUM, p, List.of(Blocks.POTTED_ALLIUM)),
        new Txt("An Allium in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_AZURE_BLUET = register(
        "potted_azure_bluet", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_AZURE_BLUET, p, List.of(Blocks.POTTED_AZURE_BLUET)),
        new Txt("An Azure Bluet in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_RED_TULIP = register(
        "potted_red_tulip", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_RED_TULIP, p, List.of(Blocks.POTTED_RED_TULIP)),
        new Txt("A Red Tulip in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_ORANGE_TULIP = register(
        "potted_orange_tulip", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_ORANGE_TULIP, p, List.of(Blocks.POTTED_ORANGE_TULIP)),
        new Txt("An Orange Tulip in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_WHITE_TULIP = register(
        "potted_white_tulip", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_WHITE_TULIP, p, List.of(Blocks.POTTED_WHITE_TULIP)),
        new Txt("A White Tulip in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_PINK_TULIP = register(
        "potted_pink_tulip", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_PINK_TULIP, p, List.of(Blocks.POTTED_PINK_TULIP)),
        new Txt("A Pink Tulip in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_OXEYE_DAISY = register(
        "potted_oxeye_daisy", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_OXEYE_DAISY, p, List.of(Blocks.POTTED_OXEYE_DAISY)),
        new Txt("An Oxeye Daisy in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CORNFLOWER = register(
        "potted_cornflower", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_CORNFLOWER, p, List.of(Blocks.POTTED_CORNFLOWER)),
        new Txt("A Cornflower in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_LILY_OF_THE_VALLEY = register(
        "potted_lily_of_the_valley", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_LILY_OF_THE_VALLEY, p, List.of(Blocks.POTTED_LILY_OF_THE_VALLEY)),
        new Txt("A Lily of the Valley in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_WITHER_ROSE = register(
        "potted_wither_rose", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_WITHER_ROSE, p, List.of(Blocks.POTTED_WITHER_ROSE)),
        new Txt("A Wither Rose in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_TORCHFLOWER = register(
        "potted_torchflower", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_TORCHFLOWER, p, List.of(Blocks.POTTED_TORCHFLOWER)),
        new Txt("A Torchflower in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_OAK_SAPLING = register(
        "potted_oak_sapling", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_OAK_SAPLING, p, List.of(Blocks.POTTED_OAK_SAPLING)),
        new Txt("An Oak Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_SPRUCE_SAPLING = register(
        "potted_spruce_sapling", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_SPRUCE_SAPLING, p, List.of(Blocks.POTTED_SPRUCE_SAPLING)),
        new Txt("A Spruce Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_BIRCH_SAPLING = register(
        "potted_birch_sapling", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_BIRCH_SAPLING, p, List.of(Blocks.POTTED_BIRCH_SAPLING)),
        new Txt("A Birch Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_JUNGLE_SAPLING = register(
        "potted_jungle_sapling", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_JUNGLE_SAPLING, p, List.of(Blocks.POTTED_JUNGLE_SAPLING)),
        new Txt("A Jungle Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_ACACIA_SAPLING = register(
        "potted_acacia_sapling", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_ACACIA_SAPLING, p, List.of(Blocks.POTTED_ACACIA_SAPLING)),
        new Txt("An Acacia Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_DARK_OAK_SAPLING = register(
        "potted_dark_oak_sapling", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_DARK_OAK_SAPLING, p, List.of(Blocks.POTTED_DARK_OAK_SAPLING)),
        new Txt("A Dark Oak Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CHERRY_SAPLING = register(
        "potted_cherry_sapling", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_CHERRY_SAPLING, p, List.of(Blocks.POTTED_CHERRY_SAPLING)),
        new Txt("A Cherry Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_RED_MUSHROOM = register(
        "potted_red_mushroom", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_RED_MUSHROOM, p, List.of(Blocks.POTTED_RED_MUSHROOM)),
        new Txt("A Red Mushroom in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_BROWN_MUSHROOM = register(
        "potted_brown_mushroom", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_BROWN_MUSHROOM, p, List.of(Blocks.POTTED_BROWN_MUSHROOM)),
        new Txt("A Brown Mushroom in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_FERN = register(
        "potted_fern", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_FERN, p, List.of(Blocks.POTTED_FERN)),
        new Txt("A Fern in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_DEAD_BUSH = register(
        "potted_dead_bush", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_DEAD_BUSH, p, List.of(Blocks.POTTED_DEAD_BUSH)),
        new Txt("A Dead Bush in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CACTUS = register(
        "potted_cactus", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_CACTUS, p, List.of(Blocks.POTTED_CACTUS)),
        new Txt("A Cactus in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_BAMBOO = register(
        "potted_bamboo", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_BAMBOO, p, List.of(Blocks.POTTED_BAMBOO)),
        new Txt("A Bamboo in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_AZALEA = register(
        "potted_azalea", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_AZALEA, p, List.of(Blocks.POTTED_AZALEA)),
        new Txt("An Azalea in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_FLOWERING_AZALEA = register(
        "potted_flowering_azalea", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_FLOWERING_AZALEA, p, List.of(Blocks.POTTED_FLOWERING_AZALEA)),
        new Txt("A Flowering Azalea in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CRIMSON_FUNGUS = register(
        "potted_crimson_fungus", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_CRIMSON_FUNGUS, p, List.of(Blocks.POTTED_CRIMSON_FUNGUS)),
        new Txt("A Crimson Fungus in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_WARPED_FUNGUS = register(
        "potted_warped_fungus", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_WARPED_FUNGUS, p, List.of(Blocks.POTTED_WARPED_FUNGUS)),
        new Txt("A Warped Fungus in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CRIMSON_ROOTS = register(
        "potted_crimson_roots", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_CRIMSON_ROOTS, p, List.of(Blocks.POTTED_CRIMSON_ROOTS)),
        new Txt("Crimson Roots in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_WARPED_ROOTS = register(
        "potted_warped_roots", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_WARPED_ROOTS, p, List.of(Blocks.POTTED_WARPED_ROOTS)),
        new Txt("Warped Roots in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_MANGROVE_PROPAGULE = register(
        "potted_mangrove_propagule", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_MANGROVE_PROPAGULE, p, List.of(Blocks.POTTED_MANGROVE_PROPAGULE)),
        new Txt("A Mangrove Propagule in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_PALE_OAK_SAPLING = register(
        "potted_pale_oak_sapling", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_PALE_OAK_SAPLING, p, List.of(Blocks.POTTED_PALE_OAK_SAPLING)),
        new Txt("A Pale Oak Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_OPEN_EYEBLOSSOM = register(
        "potted_open_eyeblossom", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_OPEN_EYEBLOSSOM, p, List.of(Blocks.POTTED_OPEN_EYEBLOSSOM)),
        new Txt("An Open Eyeblossom in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CLOSED_EYEBLOSSOM = register(
        "potted_closed_eyeblossom", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_CLOSED_EYEBLOSSOM, p, List.of(Blocks.POTTED_CLOSED_EYEBLOSSOM)),
        new Txt("A Closed Eyeblossom in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_GOLDEN_DANDELION = register(
        "potted_golden_dandelion", "potted_plants",
        p -> new CustomBlockItem(Blocks.POTTED_GOLDEN_DANDELION, p, List.of(Blocks.POTTED_GOLDEN_DANDELION)),
        new Txt("A Golden Dandelion in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );








    public static class Colors {
        private Colors() {}
        public static final Vector3i COLOR_LS_RED    = new Vector3i(255, 128, 128).mul(3).div(4);
        public static final Vector3i COLOR_LS_ORANGE = new Vector3i(255, 128,   0).mul(3).div(4);
        public static final Vector3i COLOR_LS_GREEN  = new Vector3i(128, 255, 128).mul(3).div(4);
    }




    public static class Notices {
        private Notices() {}


        public static List<Txt> CUSTOM_BLOCK = List.of(
            new Txt("This item and the block it places don't exist in Vanilla.").color(Colors.COLOR_LS_RED),
            new Txt("They cannot be used on servers without the")               .color(Colors.COLOR_LS_RED),
            new Txt(EngineerSBliss.MOD_NAME + " mod installed and will")        .color(Colors.COLOR_LS_RED),
            new Txt("disappear from worlds when opened without it.")            .color(Colors.COLOR_LS_RED)
        );




        public static List<Txt> CUSTOM_ITEM_ONLY = List.of(
            new Txt("This item doesn't exist in Vanilla.")              .color(Colors.COLOR_LS_RED),
            new Txt("It cannot be used on servers without the")         .color(Colors.COLOR_LS_RED),
            new Txt(EngineerSBliss.MOD_NAME + " mod installed and will").color(Colors.COLOR_LS_RED),
            new Txt("disappear from worlds when opened without it.")    .color(Colors.COLOR_LS_RED)
        );
        public static List<Txt> VANILLA_BLOCK = List.of(
            new Txt("Blocks placed using this item are compatible").color(Colors.COLOR_LS_GREEN),
            new Txt("with Vanilla and can be safely exported in")  .color(Colors.COLOR_LS_GREEN),
            new Txt("worlds, structures, and schematics.")         .color(Colors.COLOR_LS_GREEN)
        );
        public static List<Txt> VANILLA_ENTITY = List.of(
            new Txt("Entities placed using this item are compatible").color(Colors.COLOR_LS_GREEN),
            new Txt("with Vanilla and can be safely exported in")    .color(Colors.COLOR_LS_GREEN),
            new Txt("worlds, structures, and schematics.")           .color(Colors.COLOR_LS_GREEN)
        );




        public static List<Txt> PISTON_HEAD_BREAKS = List.of(
            new Txt("Updates make the block break unless properly").color(Colors.COLOR_LS_ORANGE),
            new Txt("connected to a matching piston block.")       .color(Colors.COLOR_LS_ORANGE)
        );
        public static List<Txt> HEADLESS_PISTON_RESETS = List.of(
            new Txt("This only keeps its headless state when powered.")      .color(Colors.COLOR_LS_ORANGE),
            new Txt("Removing the power source will make the head reappear.").color(Colors.COLOR_LS_ORANGE)
        );




        public static List<Txt> LONG_PLANT_STEM_RESETS = List.of(
            new Txt("Updates from above make the block change to the crown version").color(Colors.COLOR_LS_ORANGE),
            new Txt("unless properly connected to a matching crown block.")         .color(Colors.COLOR_LS_ORANGE)
        );
        public static List<Txt> HANGING_LONG_PLANT_STEM_RESETS = List.of(
            new Txt("Updates from below make the block change to the crown version").color(Colors.COLOR_LS_ORANGE),
            new Txt("unless properly connected to a matching crown block.")         .color(Colors.COLOR_LS_ORANGE)
        );




        public static List<Txt> TALL_PLANT_STEM_BREAKS = List.of(
            new Txt("Updates from above make the block break unless").color(Colors.COLOR_LS_ORANGE),
            new Txt("properly connected to a matching crown block.") .color(Colors.COLOR_LS_ORANGE)
        );
        public static List<Txt> TALL_PLANT_CROWN_BREAKS = List.of(
            new Txt("Updates make the block break unless properly").color(Colors.COLOR_LS_ORANGE),
            new Txt("connected to a matching stem block.")         .color(Colors.COLOR_LS_ORANGE)
        );




        public static List<Txt> DOOR_BOTTOM_BREAKS = List.of(
            new Txt("Updates from above make the block break unless")           .color(Colors.COLOR_LS_ORANGE),
            new Txt("properly connected to a matching bottom half door block.") .color(Colors.COLOR_LS_ORANGE)
        );
        public static List<Txt> DOOR_TOP_BREAKS = List.of(
            new Txt("Updates from below make the block break unless")       .color(Colors.COLOR_LS_ORANGE),
            new Txt("properly connected to a matching top half door block.").color(Colors.COLOR_LS_ORANGE)
        );




        public static List<Txt> BED_FOOT_BREAKS = List.of(
            new Txt("Updates from the head side make the block break unless").color(Colors.COLOR_LS_ORANGE),
            new Txt("properly connected to a matching bed head block.")      .color(Colors.COLOR_LS_ORANGE)
        );
        public static List<Txt> BED_HEAD_BREAKS = List.of(
            new Txt("Updates from foot side make the block break unless").color(Colors.COLOR_LS_ORANGE),
            new Txt("properly connected to a matching bed foot block.")  .color(Colors.COLOR_LS_ORANGE)
        );




        public static List<Txt> FROSTED_ICE_MELTS = List.of(
            new Txt("This block starts melting as soon as it is placed.").color(Colors.COLOR_LS_ORANGE)
        );




        public static List<Txt> FRICTIONLESS_BLOCK_MOVEMENT_ISSUE = List.of(
            new Txt("Despite having no friction, mobs are able to move")           .color(Colors.COLOR_LS_ORANGE),
            new Txt("when walking on it. This is because in Minecraft, friction")  .color(Colors.COLOR_LS_ORANGE),
            new Txt("determines how quickly an entity loses velocity, but doesn't").color(Colors.COLOR_LS_ORANGE),
            new Txt("affect the acceleration mobs produce when moving. This is")   .color(Colors.COLOR_LS_ORANGE),
            new Txt("consistent with the game's physics but not with IRL physics.").color(Colors.COLOR_LS_ORANGE)
        );
        public static List<Txt> FRICTIONFUL_BLOCK_MOVEMENT_ISSUE = List.of(
            new Txt("Despite having infinite friction, mobs aren't able to move")  .color(Colors.COLOR_LS_ORANGE),
            new Txt("when walking on it. This is because in Minecraft, friction")  .color(Colors.COLOR_LS_ORANGE),
            new Txt("determines how quickly an entity loses velocity, and any")    .color(Colors.COLOR_LS_ORANGE),
            new Txt("movement is caused by a change in velocity. So infinite")     .color(Colors.COLOR_LS_ORANGE),
            new Txt("friction makes the mob lose all velocity instantly. This is") .color(Colors.COLOR_LS_ORANGE),
            new Txt("consistent with the game's physics but not with IRL physics.").color(Colors.COLOR_LS_ORANGE)
        );
    }






    private static Item register(String id, Function<CustomItemProperties, Item> factory, Object... lore) {
        return register(id, null, factory, lore);
    }
    private static Item register(String id, @Nullable String modelCustomDir, Function<CustomItemProperties, Item> factory, Object... lore) {
        final List<Component> lines = new ArrayList<>();
        lines.add(Component.empty());
        for(final Object loreEntry : lore) {
            if(loreEntry instanceof Txt line) {
                lines.add(line.lightGray().get());
                lines.add(Component.empty());
            }
            else if(loreEntry instanceof List<?> list) {
                for(final Object maybeLine : list) {
                    if(maybeLine instanceof Txt line) {
                        lines.add(line.get());
                    }
                }
                lines.add(Component.empty());
            }
        }
        return register(id, modelCustomDir, factory, lines);
    }




    private static Item register(String id, @Nullable String modelCustomDir, Function<CustomItemProperties, Item> factory, List<Component> lines) {

        // Create Key
        var key = ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, id));


        // Create item properties with the specified path and set the item ID
        //! Custom item path is required to use subdirectories in assets/engineers-bliss/items.
        final String modelPath = modelCustomDir == null ? id : String.format("%s/%s", modelCustomDir, id);
        @NotNull CustomItemProperties properties = (CustomItemProperties)new CustomItemProperties(modelPath).setId(key);


        // Set item lore if present
        if(!lines.isEmpty()) {
            properties = (CustomItemProperties)properties.component(DataComponents.LORE, new ItemLore(lines, lines));
        }


        // Create item and register it
        Item item = factory.apply(properties);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }





    public static ItemStack named(Item item, String name) {
        ItemStack stack = new ItemStack(item);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
        return stack;
    }




    public static void init() {
        //! This triggers static init
    }
}