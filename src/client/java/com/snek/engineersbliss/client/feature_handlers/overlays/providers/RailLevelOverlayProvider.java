package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.RailAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;
import com.snek.engineersbliss.feature_handlers.ServerFeatureSync;
import com.snek.engineersbliss.feature_handlers.overlays.OverlaysServerFeatureSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;




public final class RailLevelOverlayProvider extends __base_TextureOverlayProvider {
    private static final float TILT = (float)(Math.PI / 4);


    @Override
    public boolean shouldRender(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return
            ClientFeatureSync.getFeatureB(OverlaysServerFeatureSet.RAIL_POWER_LEVELS) &&
            attachedData != null &&
            ServerFeatureSync.stateHasFeature(state, OverlaysServerFeatureSet.RAIL_POWER_LEVELS) &&
            ((RailAttachedData)attachedData).getInput() != 0 //! Exclude 0 but include -1 as that's the fallback unknown level value
        ;
    }


    @Override
    public String calcTexturePath(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {

        // Return unknown level sprite if server doesn't have the mod installed
        // ! Data constructor sets the output signal to -1 if the server doesn't have the mod installed.
        // ! Displaying the correct levels depends on server packets.
        final RailAttachedData data = (RailAttachedData)attachedData;
        if(data == null || data.getInput() == -1) {
            return UNKNOWN_LEVEL_TEXTURE;
        }

        // Fetch proper data otherwise
        else {
            final String powerLevelStr = String.valueOf(data.getInput());
            return "overlays/power_levels/" + powerLevelStr + ".png";
        }
    }


    @Override
    public double calcVerticalOffset(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        final PoweredRailBlock rail = (PoweredRailBlock)state.getBlock();
        final RailShape shape = state.getValue(rail.getShapeProperty());
        return PIXEL_HEIGHT * (1d + (shape.isSlope() ? 8.25 : 0)) + 0.02;
    }


    @Override
    public double calcWidth(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return 0.25;
    }


    @Override
    public TextureProviderDisplay getDisplay() {
        return TextureProviderDisplay.Y_LOCKED;
    }


    @Override
    public @Nullable Vector3f calcPostRotation(final BlockState state, final BlockPos pos, final __base_OverlayAttachedData attachedData) {
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

