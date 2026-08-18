package com.snek.engineersbliss.custom.blocks.special;

import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.custom.block_entities.CustomBlockEntityHandler;
import com.snek.engineersbliss.custom.block_entities.special.CosmeticWhiteHoleBlockEntity;
import com.snek.engineersbliss.custom.blocks.base.CosmeticBlackAndWhiteHoleBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;








public class CosmeticWhiteHoleBlock extends CosmeticBlackAndWhiteHoleBlock {

    public CosmeticWhiteHoleBlock(Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> blockEntityFactory) {
        super(properties, blockEntityFactory);
    }


    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CustomBlockEntityHandler.COSMETIC_WHITE_HOLE, CosmeticWhiteHoleBlockEntity::tick);
    }
}
