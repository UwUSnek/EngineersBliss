package com.snek.engineersbliss.custom.block_entities.special;

import com.snek.engineersbliss.custom.block_entities.CustomBlockEntityHandler;
import com.snek.engineersbliss.custom.block_entities.base.CosmeticBlackAndWhiteHoleBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;




public class CosmeticBlackHoleBlockEntity extends CosmeticBlackAndWhiteHoleBlockEntity {
    public CosmeticBlackHoleBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntityHandler.COSMETIC_BLACK_HOLE, pos, state);
    }
    public CosmeticBlackHoleBlockEntity(BlockPos pos, BlockState state, float size, float growth) {
        super(CustomBlockEntityHandler.COSMETIC_BLACK_HOLE, pos, state, size, growth);
    }
}
