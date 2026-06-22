package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_OverlayProvider {
    protected __base_OverlayProvider() {}
    public static final double PIXEL_HEIGHT = 1f / 16;




    /**
     * Determines if the current overlay should render on the target block.
     * ! This should take into account the current block type and OverlaysHandler's feature states.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return True if the overlay needs to be rendered, false otherwise.
     */
    public abstract boolean shouldRender(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData);
}
