package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.OverlayAttachedDataComparator;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




public final class ComparatorLevelOverlayProvider implements TextureOverlayProvider {


    @Override
    public boolean shouldRender(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        if(state.is(Blocks.COMPARATOR)) {
            final int powerLevel = attachedData == null ? 0 : ((OverlayAttachedDataComparator)attachedData).getOutputLevel();
            return powerLevel > 0;
        }
        return false;
    }

    @Override
    public String calcTexturePath(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        final String powerLevelStr = attachedData == null ? "unknown" : String.valueOf(((OverlayAttachedDataComparator)attachedData).getOutputLevel());
        return "block/power_levels/" + powerLevelStr + ".png";
    }

    @Override
    public double calcVerticalOffset(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return (1f / 16 * 2) + 0.025;
    }

    @Override
    public double calcWidth(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return 0.15;
    }
}

