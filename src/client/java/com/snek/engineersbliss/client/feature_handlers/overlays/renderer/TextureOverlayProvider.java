package com.snek.engineersbliss.client.feature_handlers.overlays.renderer;

import com.snek.engineersbliss.client.feature_handlers.overlays.providers.__base_OverlayProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;




public interface TextureOverlayProvider extends __base_OverlayProvider {


    /**
     * Determines if the current overlay should render on the target block.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return True if the overlay needs to be rendered, false otherwise.
     */
    public boolean shouldRender(BlockState state, BlockPos pos);


    /**
     * Calculates the path of the texture to render.
     * This is relative to the "textures" directory of the mod.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The path of the texture.
     */
    public String calcTexturePath(BlockState state, BlockPos pos);


    /**
     * Calculates the vertical offset of the texture, with 0 being the top face of the target block.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The vertical offset of the texture.
     */
    public double calcVerticalOffset(BlockState state, BlockPos pos);


    //FIXME add rotation function
}
