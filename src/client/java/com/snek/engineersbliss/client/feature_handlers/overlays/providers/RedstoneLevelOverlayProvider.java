package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;




public final class RedstoneLevelOverlayProvider implements TextureOverlayProvider {


    @Override
    public boolean shouldRender(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return state.is(Blocks.REDSTONE_WIRE) && state.getValue(RedStoneWireBlock.POWER) > 0;
    }

    @Override
    public String calcTexturePath(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return "overlays/power_levels/" + state.getValue(RedStoneWireBlock.POWER).intValue() + ".png";
    }

    @Override
    public double calcVerticalOffset(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return (AltTexturesHandler.getFeature(AltTextureFeature.REDSTONE_WIRE_3D) ? PIXEL_HEIGHT * 0.4 : 0.01) + 0.02;
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

