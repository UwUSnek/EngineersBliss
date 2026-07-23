package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.feature_handlers.overlays.OverlaysServerFeatureSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;




public final class RedstoneLevelOverlayProvider extends __base_TextureOverlayProvider {


    @Override
    public boolean shouldRender(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return
            ClientFeatureSync.getFeatureB(OverlaysServerFeatureSet.REDSTONE_WIRE_POWER_LEVELS) &&
            state.is(Blocks.REDSTONE_WIRE) &&
            state.getValue(RedStoneWireBlock.POWER) > 0
        ;
    }

    @Override
    public String calcTexturePath(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return "overlays/power_levels/" + state.getValue(RedStoneWireBlock.POWER).intValue() + ".png";
    }

    @Override
    public double calcVerticalOffset(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return (ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.REDSTONE_WIRE_3D) ? PIXEL_HEIGHT * 0.4 : 0.01) + 0.02;
    }

    @Override
    public double calcWidth(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return 0.25;
    }

    @Override
    public TextureProviderDisplay getDisplay() {
        return TextureProviderDisplay.Y_LOCKED;
    }
}

