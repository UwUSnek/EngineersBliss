package com.snek.engineersbliss.feature_handlers.custom_items;

import java.util.function.Function;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;







public class CustomBlockHandler {
    private CustomBlockHandler() {}


    public static final Block GREEN_SCREEN = register(
        "green_screen",
        Block::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_GREEN)
            .noLootTable()
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .noTerrainParticles()
            .pushReaction(PushReaction.BLOCK)
    );
    public static final Block BLUE_SCREEN = register(
        "blue_screen",
        Block::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_BLUE)
            .noLootTable()
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .noTerrainParticles()
            .pushReaction(PushReaction.BLOCK)
    );







    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(properties.setId(blockKey));
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }




    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, name));
    }

    public static void init() {
        //! This triggers static init
    }
}
