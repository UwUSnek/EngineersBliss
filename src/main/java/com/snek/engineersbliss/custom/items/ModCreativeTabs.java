package com.snek.engineersbliss.custom.items;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethodStage.Vanilla;
import com.snek.engineersbliss.EngineerSBliss;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








public class ModCreativeTabs {
    private ModCreativeTabs() {}




    public static final ResourceKey<CreativeModeTab> CUSTOM_ITEMS_TAB_KEY = ResourceKey.create(
        BuiltInRegistries.CREATIVE_MODE_TAB.key(),
        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "custom_items")
    );

    public static final CreativeModeTab CUSTOM_ITEMS_TAB = FabricCreativeModeTab.builder()
        .icon(() -> new ItemStack(CustomItemHandler.GREEN_SCREEN.asItem()))
        .title(Component.translatable("creativeTab." + EngineerSBliss.MOD_ID + ".custom_items"))
        .displayItems((params, output) -> {
            output.accept(CustomItemHandler.GREEN_SCREEN);
            output.accept(CustomItemHandler.BLUE_SCREEN);


            output.accept(CustomItemHandler.FRICTIONLESS_BLOCK);
            output.accept(CustomItemHandler.FRICTIONFUL_BLOCK);


            output.accept(CustomItemHandler.ITEM_SOURCE);
            output.accept(CustomItemHandler.ITEM_SINK);
        }).build()
    ;









    public static final ResourceKey<CreativeModeTab> MISSING_ITEMS_TAB_KEY = ResourceKey.create(
        BuiltInRegistries.CREATIVE_MODE_TAB.key(),
        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "missing_items")
    );

    public static final CreativeModeTab MISSING_ITEMS_TAB = FabricCreativeModeTab.builder()
        .icon(() -> new ItemStack(CustomItemHandler.POTTED_CHERRY_SAPLING.asItem()))
        .title(Component.translatable("creativeTab." + EngineerSBliss.MOD_ID + ".missing_items"))
        .displayItems((params, output) -> {


            output.accept(CustomItemHandler.named(Items.STICK,       "Filter Stick")); //TODO meant for filters. add to item lore
            output.accept(CustomItemHandler.named(Items.COBBLESTONE, "Filter Cobblestone")); //TODO meant for fuel filters (sticks can hopper into furnaces and such). add to item lore
            output.accept(CustomItemHandler.named(Items.NAME_TAG,    "Persistent Mob")); //TODO meant for name tagging mobs (makes them persistent). add to item lore


            output.accept(CustomItemHandler.FULL_BEE_NEST);
            output.accept(CustomItemHandler.FULL_BEE_NEST_NO_BEES);
            output.accept(CustomItemHandler.FULL_BEEHIVE);
            output.accept(CustomItemHandler.FULL_BEEHIVE_NO_BEES);


            output.accept(CustomItemHandler.YOUR_PLAYER_HEAD);


            output.accept(CustomItemHandler.ARMOR_STAND_WITH_ARMS);


            output.accept(Items.ENDER_DRAGON_SPAWN_EGG);
            output.accept(Items.WITHER_SPAWN_EGG);


            output.accept(CustomItemHandler.FIRE);
            output.accept(CustomItemHandler.NETHER_PORTAL);
            output.accept(CustomItemHandler.END_PORTAL);
            output.accept(CustomItemHandler.END_GATEWAY);


            output.accept(CustomItemHandler.UNLIT_CAMPFIRE);
            output.accept(CustomItemHandler.UNLIT_SOUL_CAMPFIRE);


            output.accept(CustomItemHandler.WATER_CAULDRON_3X);
            output.accept(CustomItemHandler.WATER_CAULDRON_2X);
            output.accept(CustomItemHandler.WATER_CAULDRON_1X);
            output.accept(CustomItemHandler.POWDER_SNOW_CAULDRON_3X);
            output.accept(CustomItemHandler.POWDER_SNOW_CAULDRON_2X);
            output.accept(CustomItemHandler.POWDER_SNOW_CAULDRON_1X);
            output.accept(CustomItemHandler.LAVA_CAULDRON);


            output.accept(CustomItemHandler.FROSTED_ICE);


            output.accept(CustomItemHandler.CANDLE_CAKE);
            output.accept(CustomItemHandler.WHITE_CANDLE_CAKE);
            output.accept(CustomItemHandler.ORANGE_CANDLE_CAKE);
            output.accept(CustomItemHandler.MAGENTA_CANDLE_CAKE);
            output.accept(CustomItemHandler.LIGHT_BLUE_CANDLE_CAKE);
            output.accept(CustomItemHandler.YELLOW_CANDLE_CAKE);
            output.accept(CustomItemHandler.LIME_CANDLE_CAKE);
            output.accept(CustomItemHandler.PINK_CANDLE_CAKE);
            output.accept(CustomItemHandler.GRAY_CANDLE_CAKE);
            output.accept(CustomItemHandler.LIGHT_GRAY_CANDLE_CAKE);
            output.accept(CustomItemHandler.CYAN_CANDLE_CAKE);
            output.accept(CustomItemHandler.PURPLE_CANDLE_CAKE);
            output.accept(CustomItemHandler.BLUE_CANDLE_CAKE);
            output.accept(CustomItemHandler.BROWN_CANDLE_CAKE);
            output.accept(CustomItemHandler.GREEN_CANDLE_CAKE);
            output.accept(CustomItemHandler.RED_CANDLE_CAKE);
            output.accept(CustomItemHandler.BLACK_CANDLE_CAKE);


            output.accept(CustomItemHandler.POTTED_DANDELION);
            output.accept(CustomItemHandler.POTTED_POPPY);
            output.accept(CustomItemHandler.POTTED_BLUE_ORCHID);
            output.accept(CustomItemHandler.POTTED_ALLIUM);
            output.accept(CustomItemHandler.POTTED_AZURE_BLUET);
            output.accept(CustomItemHandler.POTTED_RED_TULIP);
            output.accept(CustomItemHandler.POTTED_ORANGE_TULIP);
            output.accept(CustomItemHandler.POTTED_WHITE_TULIP);
            output.accept(CustomItemHandler.POTTED_PINK_TULIP);
            output.accept(CustomItemHandler.POTTED_OXEYE_DAISY);
            output.accept(CustomItemHandler.POTTED_CORNFLOWER);
            output.accept(CustomItemHandler.POTTED_LILY_OF_THE_VALLEY);
            output.accept(CustomItemHandler.POTTED_WITHER_ROSE);
            output.accept(CustomItemHandler.POTTED_TORCHFLOWER);
            output.accept(CustomItemHandler.POTTED_OAK_SAPLING);
            output.accept(CustomItemHandler.POTTED_SPRUCE_SAPLING);
            output.accept(CustomItemHandler.POTTED_BIRCH_SAPLING);
            output.accept(CustomItemHandler.POTTED_JUNGLE_SAPLING);
            output.accept(CustomItemHandler.POTTED_ACACIA_SAPLING);
            output.accept(CustomItemHandler.POTTED_DARK_OAK_SAPLING);
            output.accept(CustomItemHandler.POTTED_CHERRY_SAPLING);
            output.accept(CustomItemHandler.POTTED_RED_MUSHROOM);
            output.accept(CustomItemHandler.POTTED_BROWN_MUSHROOM);
            output.accept(CustomItemHandler.POTTED_FERN);
            output.accept(CustomItemHandler.POTTED_DEAD_BUSH);
            output.accept(CustomItemHandler.POTTED_CACTUS);
            output.accept(CustomItemHandler.POTTED_BAMBOO);
            output.accept(CustomItemHandler.POTTED_AZALEA);
            output.accept(CustomItemHandler.POTTED_FLOWERING_AZALEA);
            output.accept(CustomItemHandler.POTTED_CRIMSON_FUNGUS);
            output.accept(CustomItemHandler.POTTED_WARPED_FUNGUS);
            output.accept(CustomItemHandler.POTTED_CRIMSON_ROOTS);
            output.accept(CustomItemHandler.POTTED_WARPED_ROOTS);
            output.accept(CustomItemHandler.POTTED_MANGROVE_PROPAGULE);
            output.accept(CustomItemHandler.POTTED_PALE_OAK_SAPLING);
            output.accept(CustomItemHandler.POTTED_OPEN_EYEBLOSSOM);
            output.accept(CustomItemHandler.POTTED_CLOSED_EYEBLOSSOM);
            output.accept(CustomItemHandler.POTTED_GOLDEN_DANDELION);
        }).build()
    ;









    public static final ResourceKey<CreativeModeTab> BLOCK_PARTS_TAB_KEY = ResourceKey.create(
        BuiltInRegistries.CREATIVE_MODE_TAB.key(),
        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "block_parts")
    );

    public static final CreativeModeTab BLOCK_PARTS_TAB = FabricCreativeModeTab.builder()
        .icon(() -> new ItemStack(CustomItemHandler.CAVE_VINES_CROWN_WITH_BERRIES.asItem()))
        .title(Component.translatable("creativeTab." + EngineerSBliss.MOD_ID + ".block_parts"))
        .displayItems((params, output) -> {
            output.accept(CustomItemHandler.HEADLESS_PISTON);
            output.accept(CustomItemHandler.PISTON_HEAD);
            output.accept(CustomItemHandler.SHORT_PISTON_HEAD);
            output.accept(CustomItemHandler.HEADLESS_STICKY_PISTON);
            output.accept(CustomItemHandler.STICKY_PISTON_HEAD);
            output.accept(CustomItemHandler.SHORT_STICKY_PISTON_HEAD);


            output.accept(CustomItemHandler.ROSE_BUSH_STEM);
            output.accept(CustomItemHandler.ROSE_BUSH_CROWN);
            output.accept(CustomItemHandler.SUNFLOWER_STEM);
            output.accept(CustomItemHandler.SUNFLOWER_CROWN);
            output.accept(CustomItemHandler.TALL_GRASS_STEM);
            output.accept(CustomItemHandler.TALL_GRASS_CROWN);
            output.accept(CustomItemHandler.PITCHER_PLANT_STEM);
            output.accept(CustomItemHandler.PITCHER_PLANT_CROWN);
            output.accept(CustomItemHandler.PEONY_STEM);
            output.accept(CustomItemHandler.PEONY_CROWN);
            output.accept(CustomItemHandler.LARGE_FERN_STEM);
            output.accept(CustomItemHandler.LARGE_FERN_CROWN);
            output.accept(CustomItemHandler.LILAC_STEM);
            output.accept(CustomItemHandler.LILAC_CROWN);


            output.accept(CustomItemHandler.KELP_STEM);
            output.accept(CustomItemHandler.KELP_CROWN);
            output.accept(CustomItemHandler.CAVE_VINES_STEM);
            output.accept(CustomItemHandler.CAVE_VINES_STEM_WITH_BERRIES);
            output.accept(CustomItemHandler.CAVE_VINES_CROWN);
            output.accept(CustomItemHandler.CAVE_VINES_CROWN_WITH_BERRIES);
            output.accept(CustomItemHandler.TWISTING_VINES_STEM);
            output.accept(CustomItemHandler.TWISTING_VINES_CROWN);
            output.accept(CustomItemHandler.WEEPING_VINES_STEM);
            output.accept(CustomItemHandler.WEEPING_VINES_CROWN);


            output.accept(CustomItemHandler.ACACIA_DOOR_BOTTOM);
            output.accept(CustomItemHandler.ACACIA_DOOR_TOP);
            output.accept(CustomItemHandler.BAMBOO_DOOR_BOTTOM);
            output.accept(CustomItemHandler.BAMBOO_DOOR_TOP);
            output.accept(CustomItemHandler.BIRCH_DOOR_BOTTOM);
            output.accept(CustomItemHandler.BIRCH_DOOR_TOP);
            output.accept(CustomItemHandler.CHERRY_DOOR_BOTTOM);
            output.accept(CustomItemHandler.CHERRY_DOOR_TOP);
            output.accept(CustomItemHandler.CRIMSON_DOOR_BOTTOM);
            output.accept(CustomItemHandler.CRIMSON_DOOR_TOP);
            output.accept(CustomItemHandler.DARK_OAK_DOOR_BOTTOM);
            output.accept(CustomItemHandler.DARK_OAK_DOOR_TOP);
            output.accept(CustomItemHandler.IRON_DOOR_BOTTOM);
            output.accept(CustomItemHandler.IRON_DOOR_TOP);
            output.accept(CustomItemHandler.JUNGLE_DOOR_BOTTOM);
            output.accept(CustomItemHandler.JUNGLE_DOOR_TOP);
            output.accept(CustomItemHandler.MANGROVE_DOOR_BOTTOM);
            output.accept(CustomItemHandler.MANGROVE_DOOR_TOP);
            output.accept(CustomItemHandler.OAK_DOOR_BOTTOM);
            output.accept(CustomItemHandler.OAK_DOOR_TOP);
            output.accept(CustomItemHandler.PALE_OAK_DOOR_BOTTOM);
            output.accept(CustomItemHandler.PALE_OAK_DOOR_TOP);
            output.accept(CustomItemHandler.SPRUCE_DOOR_BOTTOM);
            output.accept(CustomItemHandler.SPRUCE_DOOR_TOP);
            output.accept(CustomItemHandler.WARPED_DOOR_BOTTOM);
            output.accept(CustomItemHandler.WARPED_DOOR_TOP);
            output.accept(CustomItemHandler.COPPER_DOOR_BOTTOM);
            output.accept(CustomItemHandler.COPPER_DOOR_TOP);
            output.accept(CustomItemHandler.EXPOSED_COPPER_DOOR_BOTTOM);
            output.accept(CustomItemHandler.EXPOSED_COPPER_DOOR_TOP);
            output.accept(CustomItemHandler.WEATHERED_COPPER_DOOR_BOTTOM);
            output.accept(CustomItemHandler.WEATHERED_COPPER_DOOR_TOP);
            output.accept(CustomItemHandler.OXIDIZED_COPPER_DOOR_BOTTOM);
            output.accept(CustomItemHandler.OXIDIZED_COPPER_DOOR_TOP);


            output.accept(CustomItemHandler.WHITE_BED_HEAD);
            output.accept(CustomItemHandler.WHITE_BED_FOOT);
            output.accept(CustomItemHandler.ORANGE_BED_HEAD);
            output.accept(CustomItemHandler.ORANGE_BED_FOOT);
            output.accept(CustomItemHandler.MAGENTA_BED_HEAD);
            output.accept(CustomItemHandler.MAGENTA_BED_FOOT);
            output.accept(CustomItemHandler.LIGHT_BLUE_BED_HEAD);
            output.accept(CustomItemHandler.LIGHT_BLUE_BED_FOOT);
            output.accept(CustomItemHandler.YELLOW_BED_HEAD);
            output.accept(CustomItemHandler.YELLOW_BED_FOOT);
            output.accept(CustomItemHandler.LIME_BED_HEAD);
            output.accept(CustomItemHandler.LIME_BED_FOOT);
            output.accept(CustomItemHandler.PINK_BED_HEAD);
            output.accept(CustomItemHandler.PINK_BED_FOOT);
            output.accept(CustomItemHandler.GRAY_BED_HEAD);
            output.accept(CustomItemHandler.GRAY_BED_FOOT);
            output.accept(CustomItemHandler.LIGHT_GRAY_BED_HEAD);
            output.accept(CustomItemHandler.LIGHT_GRAY_BED_FOOT);
            output.accept(CustomItemHandler.CYAN_BED_HEAD);
            output.accept(CustomItemHandler.CYAN_BED_FOOT);
            output.accept(CustomItemHandler.PURPLE_BED_HEAD);
            output.accept(CustomItemHandler.PURPLE_BED_FOOT);
            output.accept(CustomItemHandler.BLUE_BED_HEAD);
            output.accept(CustomItemHandler.BLUE_BED_FOOT);
            output.accept(CustomItemHandler.BROWN_BED_HEAD);
            output.accept(CustomItemHandler.BROWN_BED_FOOT);
            output.accept(CustomItemHandler.GREEN_BED_HEAD);
            output.accept(CustomItemHandler.GREEN_BED_FOOT);
            output.accept(CustomItemHandler.RED_BED_HEAD);
            output.accept(CustomItemHandler.RED_BED_FOOT);
            output.accept(CustomItemHandler.BLACK_BED_HEAD);
            output.accept(CustomItemHandler.BLACK_BED_FOOT);
        }).build()
    ;








    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_ITEMS_TAB_KEY,  CUSTOM_ITEMS_TAB);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MISSING_ITEMS_TAB_KEY, MISSING_ITEMS_TAB);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, BLOCK_PARTS_TAB_KEY,   BLOCK_PARTS_TAB);
    }
}