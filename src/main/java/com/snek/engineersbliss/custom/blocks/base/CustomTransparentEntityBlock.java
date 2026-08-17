package com.snek.engineersbliss.custom.blocks.base;

import java.util.function.BiFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;




public class CustomTransparentEntityBlock extends TransparentBlock implements EntityBlock {
    private final BiFunction<BlockPos, BlockState, BlockEntity> blockEntityFactory;

    public CustomTransparentEntityBlock(BlockBehaviour.Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> blockEntityFactory) {
        super(properties);
        this.blockEntityFactory = blockEntityFactory;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityFactory.apply(pos, state);
    }
}
