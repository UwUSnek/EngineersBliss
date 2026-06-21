package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.RailAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;




public final class RailLevelOverlayProvider extends __base_TextureOverlayProvider {
    private static final float TILT = (float)(Math.PI / 4);


    @Override
    public boolean shouldRender(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return
            attachedData != null &&
            OverlayFeature.RAIL_POWER_LEVELS.affects(state.getBlock()) &&
            ((RailAttachedData)attachedData).getInput() != 0 //! Exclude 0 but include -1 as that's the fallback unknown level value
        ;
    }


    @Override
    public String calcTexturePath(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        final RailAttachedData data = (RailAttachedData)attachedData;
        if(data != null) {
            final String powerLevelStr = String.valueOf(data.getInput());
            return "overlays/power_levels/" + powerLevelStr + ".png";
        }
        return "";
    }


    @Override
    public double calcVerticalOffset(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        final PoweredRailBlock rail = (PoweredRailBlock)state.getBlock();
        final RailShape shape = state.getValue(rail.getShapeProperty());
        return PIXEL_HEIGHT * (1d + (shape.isSlope() ? 8.25 : 0)) + 0.02;
    }


    @Override
    public double calcWidth(BlockState state, BlockPos pos, @Nullable __base_OverlayAttachedData attachedData) {
        return 0.25;
    }


    @Override
    public TextureProviderDisplay getDisplay() {
        return TextureProviderDisplay.Y_LOCKED;
    }


    @Override
    public @Nullable Vector3f calcPostRotation(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData) {
        final PoweredRailBlock rail = (PoweredRailBlock)state.getBlock();
        final RailShape shape = state.getValue(rail.getShapeProperty());


        if(shape.isSlope()) return switch(shape) {
            case ASCENDING_NORTH -> new Vector3f(+TILT, 0, 0);
            case ASCENDING_SOUTH -> new Vector3f(-TILT, 0, 0);
            case ASCENDING_EAST  -> new Vector3f(0, 0, +TILT);
            case ASCENDING_WEST  -> new Vector3f(0, 0, -TILT);
            //! Default is never actually matched, this just stops Java from crying about it
            default -> null;
        };
        else {
            return new Vector3f(0, 0, 0);
        }
    }
}

