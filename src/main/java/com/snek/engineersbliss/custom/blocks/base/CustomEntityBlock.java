package com.snek.engineersbliss.custom.blocks.base;

import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;








public class CustomEntityBlock extends BaseEntityBlock {
    public static final MapCodec<CustomEntityBlock> CODEC = simpleCodec(CustomEntityBlock::new);
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }


    private final @Nullable BiFunction<BlockPos, BlockState, BlockEntity> blockEntityFactory;
    public CustomEntityBlock(BlockBehaviour.Properties properties) {
        this(properties, null);
    }
    public CustomEntityBlock(BlockBehaviour.Properties properties, @Nullable BiFunction<BlockPos, BlockState, BlockEntity> blockEntityFactory) {
        super(properties);
        this.blockEntityFactory = blockEntityFactory;
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if(blockEntityFactory != null) {
            return blockEntityFactory.apply(pos, state);
        }
        else {
            throw new IllegalStateException("CustomEntityBlock implementations must either provide a block entity factory or override newBlockEntity");
        }
    }
}