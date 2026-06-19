package com.snek.engineersbliss.client.feature_handlers.overlays.attached_data;

import com.snek.engineersbliss.client.mixin.accessors.ComparatorBlockAccessor;
import com.snek.engineersbliss.client.mixin.accessors.DiodeBlockAccessor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;




public class OverlayAttachedDataComparator extends __base_OverlayAttachedData {
    private final int outputLevel;
    public int getOutputLevel() { return outputLevel; }


    // Used by overlay handler when blocks are changed or chunks load in
    public OverlayAttachedDataComparator(final Level level, final BlockPos pos, final BlockState state) {
        final int inputSignal  = ((ComparatorBlockAccessor)       Blocks.COMPARATOR).invokeGetInputSignal       (level, pos, state);
        final int sideSignal   = ((DiodeBlockAccessor)(DiodeBlock)Blocks.COMPARATOR).invokeGetAlternateSignal   (level, pos, state);
        final int outputSignal = ((ComparatorBlockAccessor)       Blocks.COMPARATOR).invokeCalculateOutputSignal(level, pos, state);
        this.outputLevel = outputSignal;
        //TODO display other stuff
    }


    // Used by network receiver to update existing entries
    public OverlayAttachedDataComparator(final int outputLevel) {
        this.outputLevel = outputLevel;
    }
}
