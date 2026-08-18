package com.snek.engineersbliss.custom.blocks;

import java.util.function.Function;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.custom.block_entities.special.ItemSourceBlockEntity;
import com.snek.engineersbliss.custom.block_entities.special.CosmeticBlackHoleBlockEntity;
import com.snek.engineersbliss.custom.block_entities.special.CosmeticWhiteHoleBlockEntity;
import com.snek.engineersbliss.custom.block_entities.special.ItemPipeBlockEntity;
import com.snek.engineersbliss.custom.block_entities.special.ItemSinkBlockEntity;
import com.snek.engineersbliss.custom.blocks.base.CustomTransparentEntityBlock;
import com.snek.engineersbliss.custom.blocks.base.FrictionSurface;
import com.snek.engineersbliss.custom.blocks.special.CosmeticBlackHoleBlock;
import com.snek.engineersbliss.custom.blocks.special.CosmeticWhiteHoleBlock;
import com.snek.engineersbliss.custom.blocks.special.FrictionlessSurface;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;







public class CustomBlockHandler {
    private CustomBlockHandler() {}




    // "Frictionful" is technically a word and it technically only means that something has a non-zero amount of friction,
    // but it's the best name i could find. "Infinite Friction Block" is too verbose and the other alternatives sound too stupid.

    public static final Block FRICTIONLESS_SURFACE = register(
        "frictionless_surface",
        FrictionlessSurface::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .noLootTable()
            .pushReaction(PushReaction.PUSH_ONLY)
            .sound(SoundType.GLASS)
            .friction(1)
            .noOcclusion()
    );
    public static final Block FRICTIONFUL_SURFACE = register(
        "frictionful_surface",
        FrictionSurface::new,
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .noLootTable()
            .pushReaction(PushReaction.NORMAL)
            .sound(SoundType.GLASS)
            .friction(0)
            .noOcclusion()
    );








    public static final Block COSMETIC_BLACK_HOLE = register(
        "cosmetic_black_hole",
        p -> new CosmeticBlackHoleBlock(p, CosmeticBlackHoleBlockEntity::new),
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.STONE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .noCollision()
            .noLootTable()
            .pushReaction(PushReaction.BLOCK)
            .noOcclusion()
    );
    public static final Block COSMETIC_WHITE_HOLE = register(
        "cosmetic_white_hole",
        p -> new CosmeticWhiteHoleBlock(p, CosmeticWhiteHoleBlockEntity::new),
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.STONE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .noCollision()
            .noLootTable()
            .pushReaction(PushReaction.BLOCK)
            .noOcclusion()
    );
    public static final Block ITEM_SOURCE = register(
        "item_source",
        p -> new CustomTransparentEntityBlock(p, ItemSourceBlockEntity::new),
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.STONE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .noCollision()
            .noLootTable()
            .pushReaction(PushReaction.BLOCK)
            .noOcclusion()
    );
    public static final Block ITEM_SINK = register(
        "item_sink",
        p -> new CustomTransparentEntityBlock(p, ItemSinkBlockEntity::new),
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.STONE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .noCollision()
            .noLootTable()
            .pushReaction(PushReaction.BLOCK)
            .noOcclusion()
    );
    public static final Block ITEM_PIPE = register(
        "item_pipe",
        p -> new CustomTransparentEntityBlock(p, ItemPipeBlockEntity::new),
        BlockBehaviour.Properties.of()
            .strength(-1.0f, 3600000.8f)
            .mapColor(MapColor.STONE)
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .noLootTable()
            .pushReaction(PushReaction.BLOCK)
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
            .sound(SoundType.GLASS)
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
            .sound(SoundType.GLASS)
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
