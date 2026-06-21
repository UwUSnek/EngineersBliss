package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;




public abstract class TextureOverlayProvider extends __base_OverlayProvider {


    /**
     * Determines if the current overlay should render on the target block.
     * ! This function assumes that OverlaysHandler's relevant feature is always ON.
     * ! User settings are NEVER taken in consideration, as that's the global feature map's responsibility.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return True if the overlay needs to be rendered, false otherwise.
     */
    public abstract boolean shouldRender(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData);


    /**
     * Calculates the path of the texture to render.
     * This is relative to the "textures" directory of the mod.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The path of the texture.
     */
    public abstract String calcTexturePath(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData);


    /**
     * Calculates the vertical offset of the texture, with 0 being the top face of the target block.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The vertical offset of the texture.
     */
    public abstract double calcVerticalOffset(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData);


    /**
     * Calculates the X and Z width of the texture, with 1 being a full block.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The width of the texture.
     */
    public abstract double calcWidth(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData);


    /**
     * Returns the display type that should be used for this overlay.
     * @return The display type.
     */
    public abstract TextureProviderDisplay getDisplay();


    /**
     * Calculates the rotation that is to be applied to the rendered texture after display type alignment.
     * @param state The blockstate of the block.
     * @param pos The position of the block.
     * @return The rotation expressed as euler angles, in radians, or null if no additional rotation should be applied.
     */
    public @Nullable Vector3f calcPostRotation(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData) {
        return null;
    }
}
