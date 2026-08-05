package com.snek.engineersbliss.feature_handlers.custom_items;

import java.util.function.Function;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.feature_handlers.custom_items.special_blocks.FrictionlessBlock;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;







public class CustomBlockHandler {
    private CustomBlockHandler() {}



    // Sliding in water and lava stops you quickly, but that's not an issue since it's the expected behaviour.
    // Technically, Air also has friction, but for whatever reason Minecraft Vanilla has extremely high air friction.
    // So the Frictionless Block needs to compensate for that in order to feel truly frictionless.

    // The formula for speed (in air) is: speed * blockFriction * 0.91. So 1 friction means "multiply the speed by 0.91 every tick".
    // Using 1.0989010989 works okay-ish, even though the value is technically outside of the valid range for friction [0-1]. //FIXME fix this. remove if outdated
    // Using 1 makes the Frictionless Block feel more like Blue Ice, which would defies the purpose of having a dedicated frictionless block.
    // Using 0 for the Frictionful Block

    // "Frictionful" is technically an english word and it technically only means that something has a non-zero amount of friction,
    // but it's the best name i could find. "Infinite Friction Block" is too verbose and the other alternatives sound too stupid.

    public static final Block FRICTIONLESS_BLOCK = register(
        "frictionless_block",
        FrictionlessBlock::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .noLootTable()
            .noTerrainParticles()
            .pushReaction(PushReaction.PUSH_ONLY)
            .friction(1)
    );
    public static final Block FRICTIONFUL_BLOCK = register(
        "frictionful_block",
        Block::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .noLootTable()
            .noTerrainParticles()
            .pushReaction(PushReaction.NORMAL)
            .friction(0)
    );




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
