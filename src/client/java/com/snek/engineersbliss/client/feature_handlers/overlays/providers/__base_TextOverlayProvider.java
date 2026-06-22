package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;




/**
 * Base class for overlay providers that render text instead of a texture quad.
 */
public abstract class __base_TextOverlayProvider extends __base_OverlayProvider {


    /**
     * Calculates the string to draw.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The string to draw.
     */
    public abstract String calcText(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData);


    /**
     * Calculates the color of the text. Defaults to opaque white.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The color of the text as packed ARGB values.
     */
    public int calcColor(final BlockState state, final BlockPos pos, final __base_OverlayAttachedData attachedData) {
        return 0xFFFFFFFF;
    }


    /**
     * Calculates the vertical offset of the texture, with 0 being the top face of the target block.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The vertical offset of the texture.
     */
    public double calcVerticalOffset(final BlockState state, final BlockPos pos, final __base_OverlayAttachedData attachedData) {
        return 0.5;
    }


    /**
     * Calculates the scale of the text to display.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The scale of the text in units of default size.
     */
    public float calcScale(final BlockState state, final BlockPos pos, final __base_OverlayAttachedData attachedData) {
        return 1f;
    }


    /**
     * Returns the display type that should be used for this overlay.
     * @return The display type.
     */
    public TextureProviderDisplay getDisplay() {
        return TextureProviderDisplay.CAMERA_LOCKED;
    }
}