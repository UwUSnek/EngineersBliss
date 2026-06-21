package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.RailAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




public final class RailLevelOverlayProvider implements TextureOverlayProvider {


    @Override
    public boolean shouldRender(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return
            (state.is(Blocks.POWERED_RAIL) || state.is(Blocks.ACTIVATOR_RAIL)) //&&
            // attachedData != null && //TODO
            // ((RailAttachedData)attachedData).getInputSignal() > 0 //TODO
        ;
    }


    @Override
    public String calcTexturePath(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        final RailAttachedData data = (RailAttachedData)attachedData;
        if(data != null) {
            final String powerLevelStr = String.valueOf(data.getInputSignal());
            return "overlays/power_levels/" + powerLevelStr + ".png";
        }
        return "";
    }


    @Override
    public double calcVerticalOffset(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return PIXEL_HEIGHT + 0.02;
    }


    @Override
    public double calcWidth(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return 0.25;
    }


    @Override
    public TextureProviderDisplay getDisplay() {
        return TextureProviderDisplay.Y_LOCKED;
    }
}

