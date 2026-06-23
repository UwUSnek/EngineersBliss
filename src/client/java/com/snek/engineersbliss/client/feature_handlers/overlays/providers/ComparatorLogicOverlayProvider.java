package com.snek.engineersbliss.client.feature_handlers.overlays.providers;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.ComparatorAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.__base_OverlayAttachedData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




public class ComparatorLogicOverlayProvider extends __base_TextOverlayProvider {




    @Override
    public boolean shouldRender(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData) {
        return
            //! Comparator level is always shown, including 0 (no signal) and -1 (no server data)
            OverlaysHandler.getFeature(OverlayFeature.COMPARATOR_LOGIC_SNIPPET) &&
            state.is(Blocks.COMPARATOR)
        ;
    }


    @Override
    public double calcVerticalOffset(final BlockState state, final BlockPos pos, final __base_OverlayAttachedData attachedData) {
        return 0.75;
    }


    @Override
    public float calcScale(final BlockState state, final BlockPos pos, final __base_OverlayAttachedData attachedData) {
        return 0.25f;
    }


    @Override
    public TextureProviderDisplay getDisplay() {
        return TextureProviderDisplay.BILLBOARD;
    }


    @Override
    public int getMaxRenderDistance() {
        return 8;
    }


    @Override
    public String calcText(BlockState state, BlockPos pos, __base_OverlayAttachedData attachedData) {

        // Return no server data text if server doesn't have the mod installed
        // ! Data constructor sets the output signal to -1 if the server doesn't have the mod installed.
        // ! Displaying the correct levels depends on server packets.
        final ComparatorAttachedData data = (ComparatorAttachedData)attachedData;
        if(data == null || data.getOutSignal() == -1) {
            return NO_SERVER_DATA_TEXT;
        }

        // Fetch proper data otherwise
        else {
            final int back = data.getBackSignal();
            if(back == 0) {
                return "No input";
            }
            else {
                final int side = data.getSideSignal();
                final int out  = data.getOutSignal ();
                return data.getMode() ?
                    String.format("max(0,%d-%d) ➡ %d", back, side,       out) :
                    String.format("%d≥%d?%d:0 ➡ %d",   back, side, back, out)
                ;
            }
        }
    }
}