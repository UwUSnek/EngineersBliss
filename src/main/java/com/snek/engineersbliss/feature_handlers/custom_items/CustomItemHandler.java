package com.snek.engineersbliss.feature_handlers.custom_items;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.feature_handlers.custom_items.special.CustomBeehiveItem;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;







public class CustomItemHandler {
    private CustomItemHandler() {}



    public static final Item GREEN_SCREEN = register("green_screen", p -> new BlockItem(CustomBlockHandler.GREEN_SCREEN, p));
    public static final Item  BLUE_SCREEN = register( "blue_screen", p -> new BlockItem(CustomBlockHandler. BLUE_SCREEN, p));


    public static final Item            CANDLE_CAKE = register(           "candle_cake", p -> new BlockItem(Blocks.           CANDLE_CAKE, p));
    public static final Item      WHITE_CANDLE_CAKE = register(     "white_candle_cake", p -> new BlockItem(Blocks.     WHITE_CANDLE_CAKE, p));
    public static final Item     ORANGE_CANDLE_CAKE = register(    "orange_candle_cake", p -> new BlockItem(Blocks.    ORANGE_CANDLE_CAKE, p));
    public static final Item    MAGENTA_CANDLE_CAKE = register(   "magenta_candle_cake", p -> new BlockItem(Blocks.   MAGENTA_CANDLE_CAKE, p));
    public static final Item LIGHT_BLUE_CANDLE_CAKE = register("light_blue_candle_cake", p -> new BlockItem(Blocks.LIGHT_BLUE_CANDLE_CAKE, p));
    public static final Item     YELLOW_CANDLE_CAKE = register(    "yellow_candle_cake", p -> new BlockItem(Blocks.    YELLOW_CANDLE_CAKE, p));
    public static final Item       LIME_CANDLE_CAKE = register(      "lime_candle_cake", p -> new BlockItem(Blocks.      LIME_CANDLE_CAKE, p));
    public static final Item       PINK_CANDLE_CAKE = register(      "pink_candle_cake", p -> new BlockItem(Blocks.      PINK_CANDLE_CAKE, p));
    public static final Item       GRAY_CANDLE_CAKE = register(      "gray_candle_cake", p -> new BlockItem(Blocks.      GRAY_CANDLE_CAKE, p));
    public static final Item LIGHT_GRAY_CANDLE_CAKE = register("light_gray_candle_cake", p -> new BlockItem(Blocks.LIGHT_GRAY_CANDLE_CAKE, p));
    public static final Item       CYAN_CANDLE_CAKE = register(      "cyan_candle_cake", p -> new BlockItem(Blocks.      CYAN_CANDLE_CAKE, p));
    public static final Item     PURPLE_CANDLE_CAKE = register(    "purple_candle_cake", p -> new BlockItem(Blocks.    PURPLE_CANDLE_CAKE, p));
    public static final Item       BLUE_CANDLE_CAKE = register(      "blue_candle_cake", p -> new BlockItem(Blocks.      BLUE_CANDLE_CAKE, p));
    public static final Item      BROWN_CANDLE_CAKE = register(     "brown_candle_cake", p -> new BlockItem(Blocks.     BROWN_CANDLE_CAKE, p));
    public static final Item      GREEN_CANDLE_CAKE = register(     "green_candle_cake", p -> new BlockItem(Blocks.     GREEN_CANDLE_CAKE, p));
    public static final Item        RED_CANDLE_CAKE = register(       "red_candle_cake", p -> new BlockItem(Blocks.       RED_CANDLE_CAKE, p));
    public static final Item      BLACK_CANDLE_CAKE = register(     "black_candle_cake", p -> new BlockItem(Blocks.     BLACK_CANDLE_CAKE, p));


    public static final Item POTTED_DANDELION          = register("potted_dandelion",          p -> new BlockItem(Blocks.POTTED_DANDELION,          p));
    public static final Item POTTED_POPPY              = register("potted_poppy",              p -> new BlockItem(Blocks.POTTED_POPPY,              p));
    public static final Item POTTED_BLUE_ORCHID        = register("potted_blue_orchid",        p -> new BlockItem(Blocks.POTTED_BLUE_ORCHID,        p));
    public static final Item POTTED_ALLIUM             = register("potted_allium",             p -> new BlockItem(Blocks.POTTED_ALLIUM,             p));
    public static final Item POTTED_AZURE_BLUET        = register("potted_azure_bluet",        p -> new BlockItem(Blocks.POTTED_AZURE_BLUET,        p));
    public static final Item POTTED_RED_TULIP          = register("potted_red_tulip",          p -> new BlockItem(Blocks.POTTED_RED_TULIP,          p));
    public static final Item POTTED_ORANGE_TULIP       = register("potted_orange_tulip",       p -> new BlockItem(Blocks.POTTED_ORANGE_TULIP,       p));
    public static final Item POTTED_WHITE_TULIP        = register("potted_white_tulip",        p -> new BlockItem(Blocks.POTTED_WHITE_TULIP,        p));
    public static final Item POTTED_PINK_TULIP         = register("potted_pink_tulip",         p -> new BlockItem(Blocks.POTTED_PINK_TULIP,         p));
    public static final Item POTTED_OXEYE_DAISY        = register("potted_oxeye_daisy",        p -> new BlockItem(Blocks.POTTED_OXEYE_DAISY,        p));
    public static final Item POTTED_CORNFLOWER         = register("potted_cornflower",         p -> new BlockItem(Blocks.POTTED_CORNFLOWER,         p));
    public static final Item POTTED_LILY_OF_THE_VALLEY = register("potted_lily_of_the_valley", p -> new BlockItem(Blocks.POTTED_LILY_OF_THE_VALLEY, p));
    public static final Item POTTED_WITHER_ROSE        = register("potted_wither_rose",        p -> new BlockItem(Blocks.POTTED_WITHER_ROSE,        p));
    public static final Item POTTED_TORCHFLOWER        = register("potted_torchflower",        p -> new BlockItem(Blocks.POTTED_TORCHFLOWER,        p));
    public static final Item POTTED_OAK_SAPLING        = register("potted_oak_sapling",        p -> new BlockItem(Blocks.POTTED_OAK_SAPLING,        p));
    public static final Item POTTED_SPRUCE_SAPLING     = register("potted_spruce_sapling",     p -> new BlockItem(Blocks.POTTED_SPRUCE_SAPLING,     p));
    public static final Item POTTED_BIRCH_SAPLING      = register("potted_birch_sapling",      p -> new BlockItem(Blocks.POTTED_BIRCH_SAPLING,      p));
    public static final Item POTTED_JUNGLE_SAPLING     = register("potted_jungle_sapling",     p -> new BlockItem(Blocks.POTTED_JUNGLE_SAPLING,     p));
    public static final Item POTTED_ACACIA_SAPLING     = register("potted_acacia_sapling",     p -> new BlockItem(Blocks.POTTED_ACACIA_SAPLING,     p));
    public static final Item POTTED_DARK_OAK_SAPLING   = register("potted_dark_oak_sapling",   p -> new BlockItem(Blocks.POTTED_DARK_OAK_SAPLING,   p));
    public static final Item POTTED_CHERRY_SAPLING     = register("potted_cherry_sapling",     p -> new BlockItem(Blocks.POTTED_CHERRY_SAPLING,     p));
    public static final Item POTTED_RED_MUSHROOM       = register("potted_red_mushroom",       p -> new BlockItem(Blocks.POTTED_RED_MUSHROOM,       p));
    public static final Item POTTED_BROWN_MUSHROOM     = register("potted_brown_mushroom",     p -> new BlockItem(Blocks.POTTED_BROWN_MUSHROOM,     p));
    public static final Item POTTED_FERN               = register("potted_fern",               p -> new BlockItem(Blocks.POTTED_FERN,               p));
    public static final Item POTTED_DEAD_BUSH          = register("potted_dead_bush",          p -> new BlockItem(Blocks.POTTED_DEAD_BUSH,          p));
    public static final Item POTTED_CACTUS             = register("potted_cactus",             p -> new BlockItem(Blocks.POTTED_CACTUS,             p));
    public static final Item POTTED_BAMBOO             = register("potted_bamboo",             p -> new BlockItem(Blocks.POTTED_BAMBOO,             p));
    public static final Item POTTED_AZALEA             = register("potted_azalea",             p -> new BlockItem(Blocks.POTTED_AZALEA,             p));
    public static final Item POTTED_FLOWERING_AZALEA   = register("potted_flowering_azalea",   p -> new BlockItem(Blocks.POTTED_FLOWERING_AZALEA,   p));
    public static final Item POTTED_CRIMSON_FUNGUS     = register("potted_crimson_fungus",     p -> new BlockItem(Blocks.POTTED_CRIMSON_FUNGUS,     p));
    public static final Item POTTED_WARPED_FUNGUS      = register("potted_warped_fungus",      p -> new BlockItem(Blocks.POTTED_WARPED_FUNGUS,      p));
    public static final Item POTTED_CRIMSON_ROOTS      = register("potted_crimson_roots",      p -> new BlockItem(Blocks.POTTED_CRIMSON_ROOTS,      p));
    public static final Item POTTED_WARPED_ROOTS       = register("potted_warped_roots",       p -> new BlockItem(Blocks.POTTED_WARPED_ROOTS,       p));
    public static final Item POTTED_MANGROVE_PROPAGULE = register("potted_mangrove_propagule", p -> new BlockItem(Blocks.POTTED_MANGROVE_PROPAGULE, p));
    public static final Item POTTED_PALE_OAK_SAPLING   = register("potted_pale_oak_sapling",   p -> new BlockItem(Blocks.POTTED_PALE_OAK_SAPLING,   p));
    public static final Item POTTED_OPEN_EYEBLOSSOM    = register("potted_open_eyeblossom",    p -> new BlockItem(Blocks.POTTED_OPEN_EYEBLOSSOM,    p));
    public static final Item POTTED_CLOSED_EYEBLOSSOM  = register("potted_closed_eyeblossom",  p -> new BlockItem(Blocks.POTTED_CLOSED_EYEBLOSSOM,  p));
    public static final Item POTTED_GOLDEN_DANDELION   = register("potted_golden_dandelion",   p -> new BlockItem(Blocks.POTTED_GOLDEN_DANDELION,   p));


    public static final Item FULL_BEE_NEST         = register("full_bee_nest",         p -> new CustomBeehiveItem(Blocks.BEE_NEST, p, 3, 5));
    public static final Item FULL_BEE_NEST_NO_BEES = register("full_bee_nest_no_bees", p -> new CustomBeehiveItem(Blocks.BEE_NEST, p, 0, 5));
    public static final Item FULL_BEEHIVE          = register("full_beehive",          p -> new CustomBeehiveItem(Blocks.BEEHIVE,  p, 3, 5));
    public static final Item FULL_BEEHIVE_NO_BEES  = register("full_beehive_no_bees",  p -> new CustomBeehiveItem(Blocks.BEEHIVE,  p, 0, 5));






    private static Item register(String id, java.util.function.Function<Item.Properties, Item> factory) {
        var key = net.minecraft.resources.ResourceKey.create(
            net.minecraft.core.registries.Registries.ITEM,
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, id)
        );
        Item item = factory.apply(new Item.Properties().setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }


    public static void init() {
        //! This triggers static init
    }
}
