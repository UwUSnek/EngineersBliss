package com.snek.engineersbliss.client.feature_handlers.overlays.attached_data;

import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.network.overlay_data.resolvers.RailInputDataResolver;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;




public class RailAttachedData implements __base_OverlayAttachedData {
    private final int input;
    public int getInput() { return input; }




    /**
     * ! This is mostly to avoid flashing values before the client can sync with the server.
     * ! It also handles server compatibility checks: input is set to -1 if server doesn't have the mod installed.
     */
    public RailAttachedData(final Level level, final BlockPos pos, final BlockState state) {
        if(!NetworkUtils.serverHasMod()) {
            this.input = -1;
        }
        else {
            this.input = RailInputDataResolver.calcPowerLevel(level, pos);
        }
    }


    /**
     * Used by network receiver to update existing entries.
     * ! This is never called if the server doesn't have the mod installed.
     * @param back The input signal.
     * @param side The side input signal.
     * @param out The output signal.
     */
    public RailAttachedData(final int input) {
        this.input = input;
    }
}
