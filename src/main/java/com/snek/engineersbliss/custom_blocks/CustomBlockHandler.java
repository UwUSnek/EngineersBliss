package com.snek.engineersbliss.custom_blocks;

import java.util.function.Function;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.custom_blocks.special.FrictionlessBlock;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;







public class CustomBlockHandler {
    private CustomBlockHandler() {}




    // "Frictionful" is technically a word and it technically only means that something has a non-zero amount of friction,
    // but it's the best name i could find. "Infinite Friction Block" is too verbose and the other alternatives sound too stupid.

    public static final Block FRICTIONLESS_BLOCK = register(
        "frictionless_block",
        FrictionlessBlock::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .noLootTable()
            .noTerrainParticles()
            .pushReaction(PushReaction.PUSH_ONLY)
            .friction(1)
            .noOcclusion()
    );
    public static final Block FRICTIONFUL_BLOCK = register(
        "frictionful_block",
        TransparentBlock::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .noLootTable()
            .noTerrainParticles()
            .pushReaction(PushReaction.NORMAL)
            .friction(0)
            .noOcclusion()
    );




    public static final Block GREEN_SCREEN = register(
        "green_screen",
        Block::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_GREEN)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::always)
            .isSuffocating(Blocks::never)
            .noLootTable()
            .noOcclusion()
            .noTerrainParticles()
            .pushReaction(PushReaction.BLOCK)
    );
    public static final Block BLUE_SCREEN = register(
        "blue_screen",
        Block::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_BLUE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::always)
            .isSuffocating(Blocks::never)
            .noLootTable()
            .noOcclusion()
            .noTerrainParticles()
            .pushReaction(PushReaction.BLOCK)
    );







    private static Block register(String id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {

        // Create Key
        final ResourceKey blockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, id));

        // Register block
        Block block = blockFactory.apply(properties.setId(blockKey));
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }




    public static void init() {
        //! This triggers static init
    }
}
