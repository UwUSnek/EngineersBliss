package com.snek.engineersbliss.custom.blocks.base;

import java.util.function.BiFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;








public class CosmeticBlackAndWhiteHoleBlock extends CustomTransparentEntityBlock {
    public CosmeticBlackAndWhiteHoleBlock(Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> blockEntityFactory) {
        super(properties, blockEntityFactory);
    }
}
