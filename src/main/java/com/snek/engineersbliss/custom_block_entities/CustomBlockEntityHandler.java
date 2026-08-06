package com.snek.engineersbliss.custom_block_entities;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.custom_block_entities.special.InfiniteItemSourceBlockEntity;
import com.snek.engineersbliss.custom_block_entities.special.ItemSinkBlockEntity;
import com.snek.engineersbliss.custom_blocks.CustomBlockHandler;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;








public class CustomBlockEntityHandler {
    private CustomBlockEntityHandler() {}




    public static final BlockEntityType<InfiniteItemSourceBlockEntity> INFINITE_ITEM_SOURCE = register(
        "infinite_item_source",
        FabricBlockEntityTypeBuilder.create(
            InfiniteItemSourceBlockEntity::new,
            CustomBlockHandler.INFINITE_ITEM_SOURCE
        ).build()
    );
    public static final BlockEntityType<ItemSinkBlockEntity> ITEM_SINK = register(
        "item_sink",
        FabricBlockEntityTypeBuilder.create(
            ItemSinkBlockEntity::new,
            CustomBlockHandler.ITEM_SINK
        ).build()
    );








    private static <T extends BlockEntityType<?>> T register(String name, T type) {

        // Create key
        final var key = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, name));

        // Register block entity
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, type);
    }


    public static void init() {
        //! This triggers static init
    }
}