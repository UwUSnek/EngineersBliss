package com.snek.engineersbliss.feature_handlers.custom_items;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3i;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.feature_handlers.custom_items.special.ArmorStandWithArmsItem;
import com.snek.engineersbliss.feature_handlers.custom_items.special.CustomBeehiveItem;
import com.snek.engineersbliss.feature_handlers.custom_items.special.HeadlessPistonItem;
import com.snek.engineersbliss.feature_handlers.custom_items.special.PistonHeadItem;
import com.snek.engineersbliss.feature_handlers.custom_items.special.YourPlayerHeadItem;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Blocks;







public class CustomItemHandler {
    private CustomItemHandler() {}



    public static final Item GREEN_SCREEN = register(
        "green_screen",
        p -> new BlockItem(CustomBlockHandler.GREEN_SCREEN, p),
        new Txt("A perfectly green block with no shading."),
        Notices.CUSTOM_BLOCK
    );
    public static final Item BLUE_SCREEN = register(
        "blue_screen",
        p -> new BlockItem(CustomBlockHandler. BLUE_SCREEN, p),
        new Txt("A perfectly blue block with no shading."),
        Notices.CUSTOM_BLOCK
    );




    public static final Item FULL_BEE_NEST = register(
        "full_bee_nest",
        p -> new CustomBeehiveItem(Blocks.BEE_NEST, p, 3, 5),
        new Txt("A Bee Nest with 5 levels of Honey and 3 Bees."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item FULL_BEE_NEST_NO_BEES = register(
        "full_bee_nest_no_bees",
        p -> new CustomBeehiveItem(Blocks.BEE_NEST, p, 0, 5),
        new Txt("A Bee Nest with 5 levels of Honey but no Bees."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item FULL_BEEHIVE = register(
        "full_beehive",
        p -> new CustomBeehiveItem(Blocks.BEEHIVE,  p, 3, 5),
        new Txt("A Beehive with 5 levels of Honey and 3 Bees."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item FULL_BEEHIVE_NO_BEES = register(
        "full_beehive_no_bees",
        p -> new CustomBeehiveItem(Blocks.BEEHIVE,  p, 0, 5),
        new Txt("A Beehive with 5 levels of Honey but no Bees."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item YOUR_PLAYER_HEAD = register(
        "your_player_head",
        p -> new YourPlayerHeadItem(Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD, Direction.DOWN, p),
        new Txt("Your Player Head."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );



    public static final Item ARMOR_STAND_WITH_ARMS = register(
        "armor_stand_with_arms",
        ArmorStandWithArmsItem::new,
        new Txt("It comes with arms!"),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_ENTITY
    );



    public static final Item HEADLESS_STICKY_PISTON = register(
        "headless_sticky_piston",
        p -> new HeadlessPistonItem(Blocks.STICKY_PISTON, p),
        new Txt("A Sticky Piston without the Piston Head part."),
        Notices.HEADLESS_PISTON_RESETS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item HEADLESS_PISTON = register(
        "headless_piston",
        p -> new HeadlessPistonItem(Blocks.PISTON, p),
        new Txt("A Piston without the Piston Head part."),
        Notices.HEADLESS_PISTON_RESETS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item STICKY_PISTON_HEAD = register(
        "sticky_piston_head",
        p -> new PistonHeadItem(Blocks.PISTON_HEAD, true, false, p),
        new Txt("A Sticky Piston, but only the Piston Head part (Long variant)."),
        Notices.PISTON_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PISTON_HEAD = register(
        "piston_head",
        p -> new PistonHeadItem(Blocks.PISTON_HEAD, false, false, p),
        new Txt("A Piston, but only the Piston Head part (Long variant)."),
        Notices.PISTON_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item SHORT_STICKY_PISTON_HEAD = register(
        "short_sticky_piston_head",
        p -> new PistonHeadItem(Blocks.PISTON_HEAD, true, true, p),
        new Txt("A Sticky Piston, but only the Piston Head part (Short variant)."),
        Notices.PISTON_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item SHORT_PISTON_HEAD = register(
        "short_piston_head",
        p -> new PistonHeadItem(Blocks.PISTON_HEAD, false, true, p),
        new Txt("A Piston, but only the Piston Head part (Short variant)."),
        Notices.PISTON_HEAD_BREAKS, Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item CANDLE_CAKE = register(
        "candle_cake",
        p -> new BlockItem(Blocks.CANDLE_CAKE, p),
        new Txt("A Cake with a Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
);
    public static final Item WHITE_CANDLE_CAKE = register(
        "white_candle_cake",
        p -> new BlockItem(Blocks.WHITE_CANDLE_CAKE, p),
        new Txt("A Cake with a White Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item ORANGE_CANDLE_CAKE = register(
        "orange_candle_cake",
        p -> new BlockItem(Blocks.ORANGE_CANDLE_CAKE, p),
        new Txt("A Cake with an Orange Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item MAGENTA_CANDLE_CAKE = register(
        "magenta_candle_cake",
        p -> new BlockItem(Blocks.MAGENTA_CANDLE_CAKE, p),
        new Txt("A Cake with a Magenta Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIGHT_BLUE_CANDLE_CAKE = register(
        "light_blue_candle_cake",
        p -> new BlockItem(Blocks.LIGHT_BLUE_CANDLE_CAKE, p),
        new Txt("A Cake with a Light Blue Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item YELLOW_CANDLE_CAKE = register(
        "yellow_candle_cake",
        p -> new BlockItem(Blocks.YELLOW_CANDLE_CAKE, p),
        new Txt("A Cake with a Yellow Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIME_CANDLE_CAKE = register(
        "lime_candle_cake",
        p -> new BlockItem(Blocks.LIME_CANDLE_CAKE, p),
        new Txt("A Cake with a Lime Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PINK_CANDLE_CAKE = register(
        "pink_candle_cake",
        p -> new BlockItem(Blocks.PINK_CANDLE_CAKE, p),
        new Txt("A Cake with a Pink Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item GRAY_CANDLE_CAKE = register(
        "gray_candle_cake",
        p -> new BlockItem(Blocks.GRAY_CANDLE_CAKE, p),
        new Txt("A Cake with a Gray Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item LIGHT_GRAY_CANDLE_CAKE = register(
        "light_gray_candle_cake",
        p -> new BlockItem(Blocks.LIGHT_GRAY_CANDLE_CAKE, p),
        new Txt("A Cake with a Light Gray Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item CYAN_CANDLE_CAKE = register(
        "cyan_candle_cake",
        p -> new BlockItem(Blocks.CYAN_CANDLE_CAKE, p),
        new Txt("A Cake with a Cyan Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item PURPLE_CANDLE_CAKE = register(
        "purple_candle_cake",
        p -> new BlockItem(Blocks.PURPLE_CANDLE_CAKE, p),
        new Txt("A Cake with a Purple Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BLUE_CANDLE_CAKE = register(
        "blue_candle_cake",
        p -> new BlockItem(Blocks.BLUE_CANDLE_CAKE, p),
        new Txt("A Cake with a Blue Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BROWN_CANDLE_CAKE = register(
        "brown_candle_cake",
        p -> new BlockItem(Blocks.BROWN_CANDLE_CAKE, p),
        new Txt("A Cake with a Brown Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item GREEN_CANDLE_CAKE = register(
        "green_candle_cake",
        p -> new BlockItem(Blocks.GREEN_CANDLE_CAKE, p),
        new Txt("A Cake with a Green Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item RED_CANDLE_CAKE = register(
        "red_candle_cake",
        p -> new BlockItem(Blocks.RED_CANDLE_CAKE, p),
        new Txt("A Cake with a Red Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item BLACK_CANDLE_CAKE = register(
        "black_candle_cake",
        p -> new BlockItem(Blocks.BLACK_CANDLE_CAKE, p),
        new Txt("A Cake with a Black Candle on top."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );




    public static final Item POTTED_DANDELION = register(
        "potted_dandelion",
        p -> new BlockItem(Blocks.POTTED_DANDELION, p),
        new Txt("A Dandelion in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_POPPY = register(
        "potted_poppy",
        p -> new BlockItem(Blocks.POTTED_POPPY, p),
        new Txt("A Poppy in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_BLUE_ORCHID = register(
        "potted_blue_orchid",
        p -> new BlockItem(Blocks.POTTED_BLUE_ORCHID, p),
        new Txt("A Blue Orchid in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_ALLIUM = register(
        "potted_allium",
        p -> new BlockItem(Blocks.POTTED_ALLIUM, p),
        new Txt("An Allium in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_AZURE_BLUET = register(
        "potted_azure_bluet",
        p -> new BlockItem(Blocks.POTTED_AZURE_BLUET, p),
        new Txt("An Azure Bluet in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_RED_TULIP = register(
        "potted_red_tulip",
        p -> new BlockItem(Blocks.POTTED_RED_TULIP, p),
        new Txt("A Red Tulip in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_ORANGE_TULIP = register(
        "potted_orange_tulip",
        p -> new BlockItem(Blocks.POTTED_ORANGE_TULIP, p),
        new Txt("An Orange Tulip in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_WHITE_TULIP = register(
        "potted_white_tulip",
        p -> new BlockItem(Blocks.POTTED_WHITE_TULIP, p),
        new Txt("A White Tulip in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_PINK_TULIP = register(
        "potted_pink_tulip",
        p -> new BlockItem(Blocks.POTTED_PINK_TULIP, p),
        new Txt("A Pink Tulip in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_OXEYE_DAISY = register(
        "potted_oxeye_daisy",
        p -> new BlockItem(Blocks.POTTED_OXEYE_DAISY, p),
        new Txt("An Oxeye Daisy in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CORNFLOWER = register(
        "potted_cornflower",
        p -> new BlockItem(Blocks.POTTED_CORNFLOWER, p),
        new Txt("A Cornflower in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_LILY_OF_THE_VALLEY = register(
        "potted_lily_of_the_valley",
        p -> new BlockItem(Blocks.POTTED_LILY_OF_THE_VALLEY, p),
        new Txt("A Lily of the Valley in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_WITHER_ROSE = register(
        "potted_wither_rose",
        p -> new BlockItem(Blocks.POTTED_WITHER_ROSE, p),
        new Txt("A Wither Rose in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_TORCHFLOWER = register(
        "potted_torchflower",
        p -> new BlockItem(Blocks.POTTED_TORCHFLOWER, p),
        new Txt("A Torchflower in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_OAK_SAPLING = register(
        "potted_oak_sapling",
        p -> new BlockItem(Blocks.POTTED_OAK_SAPLING, p),
        new Txt("An Oak Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_SPRUCE_SAPLING = register(
        "potted_spruce_sapling",
        p -> new BlockItem(Blocks.POTTED_SPRUCE_SAPLING, p),
        new Txt("A Spruce Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_BIRCH_SAPLING = register(
        "potted_birch_sapling",
        p -> new BlockItem(Blocks.POTTED_BIRCH_SAPLING, p),
        new Txt("A Birch Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_JUNGLE_SAPLING = register(
        "potted_jungle_sapling",
        p -> new BlockItem(Blocks.POTTED_JUNGLE_SAPLING, p),
        new Txt("A Jungle Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_ACACIA_SAPLING = register(
        "potted_acacia_sapling",
        p -> new BlockItem(Blocks.POTTED_ACACIA_SAPLING, p),
        new Txt("An Acacia Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_DARK_OAK_SAPLING = register(
        "potted_dark_oak_sapling",
        p -> new BlockItem(Blocks.POTTED_DARK_OAK_SAPLING, p),
        new Txt("A Dark Oak Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CHERRY_SAPLING = register(
        "potted_cherry_sapling",
        p -> new BlockItem(Blocks.POTTED_CHERRY_SAPLING, p),
        new Txt("A Cherry Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_RED_MUSHROOM = register(
        "potted_red_mushroom",
        p -> new BlockItem(Blocks.POTTED_RED_MUSHROOM, p),
        new Txt("A Red Mushroom in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_BROWN_MUSHROOM = register(
        "potted_brown_mushroom",
        p -> new BlockItem(Blocks.POTTED_BROWN_MUSHROOM, p),
        new Txt("A Brown Mushroom in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_FERN = register(
        "potted_fern",
        p -> new BlockItem(Blocks.POTTED_FERN, p),
        new Txt("A Fern in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_DEAD_BUSH = register(
        "potted_dead_bush",
        p -> new BlockItem(Blocks.POTTED_DEAD_BUSH, p),
        new Txt("A Dead Bush in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CACTUS = register(
        "potted_cactus",
        p -> new BlockItem(Blocks.POTTED_CACTUS, p),
        new Txt("A Cactus in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_BAMBOO = register(
        "potted_bamboo",
        p -> new BlockItem(Blocks.POTTED_BAMBOO, p),
        new Txt("A Bamboo in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_AZALEA = register(
        "potted_azalea",
        p -> new BlockItem(Blocks.POTTED_AZALEA, p),
        new Txt("An Azalea in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_FLOWERING_AZALEA = register(
        "potted_flowering_azalea",
        p -> new BlockItem(Blocks.POTTED_FLOWERING_AZALEA, p),
        new Txt("A Flowering Azalea in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CRIMSON_FUNGUS = register(
        "potted_crimson_fungus",
        p -> new BlockItem(Blocks.POTTED_CRIMSON_FUNGUS, p),
        new Txt("A Crimson Fungus in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_WARPED_FUNGUS = register(
        "potted_warped_fungus",
        p -> new BlockItem(Blocks.POTTED_WARPED_FUNGUS, p),
        new Txt("A Warped Fungus in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CRIMSON_ROOTS = register(
        "potted_crimson_roots",
        p -> new BlockItem(Blocks.POTTED_CRIMSON_ROOTS, p),
        new Txt("Crimson Roots in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_WARPED_ROOTS = register(
        "potted_warped_roots",
        p -> new BlockItem(Blocks.POTTED_WARPED_ROOTS, p),
        new Txt("Warped Roots in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_MANGROVE_PROPAGULE = register(
        "potted_mangrove_propagule",
        p -> new BlockItem(Blocks.POTTED_MANGROVE_PROPAGULE, p),
        new Txt("A Mangrove Propagule in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_PALE_OAK_SAPLING = register(
        "potted_pale_oak_sapling",
        p -> new BlockItem(Blocks.POTTED_PALE_OAK_SAPLING, p),
        new Txt("A Pale Oak Sapling in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_OPEN_EYEBLOSSOM = register(
        "potted_open_eyeblossom",
        p -> new BlockItem(Blocks.POTTED_OPEN_EYEBLOSSOM, p),
        new Txt("An Open Eyeblossom in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_CLOSED_EYEBLOSSOM = register(
        "potted_closed_eyeblossom",
        p -> new BlockItem(Blocks.POTTED_CLOSED_EYEBLOSSOM, p),
        new Txt("A Closed Eyeblossom in a Flower Pot."),
        Notices.CUSTOM_ITEM_ONLY, Notices.VANILLA_BLOCK
    );
    public static final Item POTTED_GOLDEN_DANDELION = register(
        "potted_golden_dandelion",
        p -> new BlockItem(Blocks.POTTED_GOLDEN_DANDELION, p),
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
    }






    private static Item register(String id, java.util.function.Function<Item.Properties, Item> factory, Object... lore) {
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
        return register(id, factory, lines);
    }




    private static Item register(String id, java.util.function.Function<Item.Properties, Item> factory, List<Component> lines) {

        // Create Key
        var key = net.minecraft.resources.ResourceKey.create(
            net.minecraft.core.registries.Registries.ITEM,
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, id)
        );


        // Create item properties and set the lore
        Item.Properties properties = new Item.Properties().setId(key);
        if(!lines.isEmpty()) {
            properties = properties.component(DataComponents.LORE, new ItemLore(lines, lines));
        }


        // Create item and register it
        Item item = factory.apply(properties);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }




    public static void init() {
        //! This triggers static init
    }
}
//TODO rename models/item to custom_item
//TODO update references in items/