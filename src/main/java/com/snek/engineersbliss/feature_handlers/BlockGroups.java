package com.snek.engineersbliss.feature_handlers;

import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;








/**
 * A class containing groups of blocks usable during the static initialization.
 * ! This MUST be updated manually as new blocks are added to affected categories.
 * ! This is required because Minecraft's tag registries are not available during static initialization.
 */
public class BlockGroups {
    private BlockGroups() {}

    public static final List<Block> ALL_LANTERNS = List.of(
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
    public static final List<Block> ALL_SIGNS = List.of(
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
    public static final List<Block> ALL_HANGING_SIGNS = List.of(
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
    public static final List<Block> ALL_CHESTS = List.of(
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
    public static final List<Block> ALL_BANNERS = List.of(
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
    public static final List<Block> ALL_BEDS = List.of(
        Blocks.WHITE_BED,
        Blocks.ORANGE_BED,
        Blocks.MAGENTA_BED,
        Blocks.LIGHT_BLUE_BED,
        Blocks.YELLOW_BED,
        Blocks.LIME_BED,
        Blocks.PINK_BED,
        Blocks.GRAY_BED,
        Blocks.LIGHT_GRAY_BED,
        Blocks.CYAN_BED,
        Blocks.PURPLE_BED,
        Blocks.BLUE_BED,
        Blocks.BROWN_BED,
        Blocks.GREEN_BED,
        Blocks.RED_BED,
        Blocks.BLACK_BED
    );
    public static final List<Block> ALL_COPPER_GOLEM_STATUES = List.of(
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