package com.snek.engineersbliss.client.feature_handlers.overlays.attached_data;

import com.snek.engineersbliss.client.mixin.accessors.PoweredRailBlockAccessor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;




public class RailAttachedData implements __base_OverlayAttachedData {

    // Mixin intercepts vanilla level calculations and sets this value, then the constructor reads it
    private static ThreadLocal<Integer> computedValue = ThreadLocal.withInitial(() -> 0);
    public static void resetComputedValue() { computedValue.set(0); }
    public static void updateComputedValue(int value) { computedValue.set(Math.max(computedValue.get(), value)); }



    private final int input;
    public int getInputSignal() { return input; }




    public RailAttachedData(final Level level, final BlockPos pos, final BlockState state) {

        //! PoweredRailBlock.findPoweredRailSignal needs to be called on the current block type as the check also tests for that.
        //! Boolean parameter defines the direction in which the checks move, so calling this twice is required (forwards and backwards)

        //! Checks start at depth 0 and end at depth 8 (9 powered blocks on each direction, including the source)
        //! Checks start at depth 0 and end at depth 8 (9 powered blocks)

        // Reset computed data
        resetComputedValue();


        // Calculate new data
        if(level.hasNeighborSignal(pos)) {
            this.input = 9;
        }
        else {
            ((PoweredRailBlockAccessor)state.getBlock()).invokeFindPoweredRailSignal(level, pos, state, true,  0);
            ((PoweredRailBlockAccessor)state.getBlock()).invokeFindPoweredRailSignal(level, pos, state, false, 0);
            this.input = computedValue.get();
        }
    }
}
