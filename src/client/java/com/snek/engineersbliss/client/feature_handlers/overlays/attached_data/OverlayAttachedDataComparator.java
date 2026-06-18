package com.snek.engineersbliss.client.feature_handlers.overlays.attached_data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.BlockState;




public class OverlayAttachedDataComparator extends __base_OverlayAttachedData {
    private final int outputLevel;
    public int getOutputLevel() { return outputLevel; }


    public OverlayAttachedDataComparator(final Level level, final BlockPos pos, final BlockState state) {
        final ComparatorBlockEntity be = (ComparatorBlockEntity)level.getBlockEntity(pos);
        this.outputLevel = be == null ? 0 : be.getOutputSignal();
    }

    public OverlayAttachedDataComparator(final int outputLevel) {
        this.outputLevel = outputLevel;
    }
}
