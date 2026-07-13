package com.snek.engineersbliss.feature_handlers.custom_items;

import java.util.function.Function;

import com.snek.engineersbliss.EngineerSBliss;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;



public class CustomItemHandler {
    private CustomItemHandler() {}



    public static final Block GREEN_SCREEN = register(
        "green_screen",
        Block::new,
        BlockBehaviour.Properties.of()
			.strength(-1.0F, 3600000.8F)
			.mapColor(MapColor.COLOR_GREEN)
			.noLootTable()
			.noOcclusion()
			.isValidSpawn(Blocks::never)
			.noTerrainParticles()
			.pushReaction(PushReaction.BLOCK),
        true
    );
    public static final Block BLUE_SCREEN = register(
        "blue_screen",
        Block::new,
        BlockBehaviour.Properties.of()
			.strength(-1.0F, 3600000.8F)
			.mapColor(MapColor.COLOR_BLUE)
			.noLootTable()
			.noOcclusion()
			.isValidSpawn(Blocks::never)
			.noTerrainParticles()
			.pushReaction(PushReaction.BLOCK),
        true
    );


    private static Block register(
        String name,
        Function<BlockBehaviour.Properties, Block> blockFactory,
        BlockBehaviour.Properties properties,
        boolean shouldRegisterItem
    ) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(properties.setId(blockKey));

        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = keyOfItem(name);
            BlockItem blockItem = new BlockItem(block, new Item.Properties()
                .setId(itemKey)
                .useBlockDescriptionPrefix()
            );
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

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
