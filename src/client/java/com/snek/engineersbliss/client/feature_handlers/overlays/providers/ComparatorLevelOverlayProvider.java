package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.ComparatorAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;
import com.snek.engineersbliss.feature_handlers.overlays.OverlaysServerFeatureSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




public final class ComparatorLevelOverlayProvider extends __base_TextureOverlayProvider {


    @Override
    public boolean shouldRender(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return
            ClientFeatureSync.getFeatureB(OverlaysServerFeatureSet.COMPARATOR_POWER_LEVELS) &&
            state.is(Blocks.COMPARATOR)
        ;
    }


    @Override
    public String calcTexturePath(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {

        // Return unknown level sprite if server doesn't have the mod installed
        // ! Data constructor sets the output signal to -1 if the server doesn't have the mod installed.
        // ! Displaying the correct levels depends on server packets.
        final ComparatorAttachedData data = (ComparatorAttachedData)attachedData;
        if(data == null || data.getOutSignal() == -1) {
            return UNKNOWN_LEVEL_TEXTURE;
        }

        // Fetch proper data otherwise
        else {
            final String powerLevelStr = String.valueOf(data.getOutSignal());
            return "overlays/power_levels/" + powerLevelStr + ".png";
        }
    }


    @Override
    public double calcVerticalOffset(final BlockState state, final BlockPos pos, @Nullable final __base_OverlayAttachedData attachedData) {
        return PIXEL_HEIGHT * 2 + 0.02;
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

