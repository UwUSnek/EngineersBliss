package com.snek.engineersbliss.client.feature_handlers.overlays.attached_data;

import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.mixin.accessors.ComparatorBlockAccessor;
import com.snek.engineersbliss.mixin.accessors.DiodeBlockAccessor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ComparatorMode;




public class ComparatorAttachedData implements __base_OverlayAttachedData {
    private final int back;
    private final int side;
    private final int out;
    //! Mode: true = SUBTRACT, false = COMPARE
    private final boolean mode;
    public int getBackSignal() { return back; }
    public int getSideSignal() { return side; }
    public int getOutSignal () { return out;  }
    public boolean getMode() { return mode; }


    /**
     * ! This is mostly to avoid flashing values before the client can sync with the server.
     * ! Not computing client comparator values would work too but this looks prettier.
     * ! This also handles server compatibility checks: output is set to -1 if server doesn't have the mod installed.
     */
    public ComparatorAttachedData(final Level level, final BlockPos pos, final BlockState state) {
        if(!NetworkUtils.serverHasMod()) {
            this.back = -1;
            this.side = 0;
            this.out = 0;
            this.mode = false;
        }
        else {
            this.back = ((ComparatorBlockAccessor)       Blocks.COMPARATOR).invokeGetInputSignal       (level, pos, state);
            this.side = ((DiodeBlockAccessor)(DiodeBlock)Blocks.COMPARATOR).invokeGetAlternateSignal   (level, pos, state);
            this.out  = ((ComparatorBlockAccessor)       Blocks.COMPARATOR).invokeCalculateOutputSignal(level, pos, state);
            this.mode = state.getValue(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT;
        }
    }


    /**
     * Used by network receiver to update existing entries.
     * ! This is never called if the server doesn't have the mod installed.
     * @param back The input signal.
     * @param side The side input signal.
     * @param out The output signal.
     * @param mode The configured comparator mode according to the server. true = SUBTRACT, false = COMPARE.
     */
    public ComparatorAttachedData(final int back, final int side, final int out, final boolean mode) {
        this.back = back;
        this.side = side;
        this.out  = out ;
        this.mode = mode;
    }
}
