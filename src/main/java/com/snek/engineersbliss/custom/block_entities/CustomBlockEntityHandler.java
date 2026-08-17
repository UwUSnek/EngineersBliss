package com.snek.engineersbliss.custom.block_entities;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.custom.block_entities.special.ItemSourceBlockEntity;
import com.snek.engineersbliss.custom.block_entities.special.ItemPipeBlockEntity;
import com.snek.engineersbliss.custom.block_entities.special.ItemSinkBlockEntity;
import com.snek.engineersbliss.custom.blocks.CustomBlockHandler;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;








public class CustomBlockEntityHandler {
    private CustomBlockEntityHandler() {}




    public static final BlockEntityType<ItemSourceBlockEntity> ITEM_SOURCE = register(
        "item_source",
        FabricBlockEntityTypeBuilder.create(
            ItemSourceBlockEntity::new,
            CustomBlockHandler.ITEM_SOURCE
        ).build()
    );
    public static final BlockEntityType<ItemSinkBlockEntity> ITEM_SINK = register(
        "item_sink",
        FabricBlockEntityTypeBuilder.create(
            ItemSinkBlockEntity::new,
            CustomBlockHandler.ITEM_SINK
        ).build()
    );
    public static final BlockEntityType<ItemPipeBlockEntity> ITEM_PIPE = register(
        "item_pipe",
        FabricBlockEntityTypeBuilder.create(
            ItemPipeBlockEntity::new,
            CustomBlockHandler.ITEM_PIPE
        ).build()
    );








    private static <T extends BlockEntityType<?>> T register(String name, T type) {

        // Create key
        final var key = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, name));

        // Register block entity
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, type);
    }




    public static void init() {
        //! This also triggers static init

        // Register storage block entities
        ItemStorage.SIDED.registerForBlockEntity((be, dir) -> be.getStorage(), CustomBlockEntityHandler.ITEM_SOURCE);
        ItemStorage.SIDED.registerForBlockEntity((be, dir) -> be.getStorage(), CustomBlockEntityHandler.ITEM_SINK);
        ItemStorage.SIDED.registerForBlockEntity((be, dir) -> be.getStorage(), CustomBlockEntityHandler.ITEM_PIPE);
    }
}