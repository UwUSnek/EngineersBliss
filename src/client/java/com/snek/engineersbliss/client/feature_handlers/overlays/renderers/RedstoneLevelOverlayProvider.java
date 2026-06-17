package com.snek.engineersbliss.client.feature_handlers.overlays.renderers;

import com.snek.engineersbliss.client.feature_handlers.overlays.renderer.TextureOverlayProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;




public final class RedstoneLevelOverlayProvider implements TextureOverlayProvider {

    @Override
    public String calcTexturePath(BlockState state, BlockPos pos) {
        //FIXME actually calculate the thing after fixing the position and state parameters
        return "block/power_levels/1.png";
    }

    @Override
    public double calcVerticalOffset(BlockState state, BlockPos pos) {
        return 0.25;
    }
}

