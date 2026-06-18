package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.renderer.TextureOverlayProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;




public final class RedstoneLevelOverlayProvider implements TextureOverlayProvider {


    @Override
    public boolean shouldRender(BlockState state, BlockPos pos) {
        return state.is(Blocks.REDSTONE_WIRE);
    }


    @Override
    public String calcTexturePath(BlockState state, BlockPos pos) {
        return "block/power_levels/" + state.getValue(RedStoneWireBlock.POWER).intValue() + ".png";
    }


    @Override
    public double calcVerticalOffset(BlockState state, BlockPos pos) {
        return 0.005;
    }
}

