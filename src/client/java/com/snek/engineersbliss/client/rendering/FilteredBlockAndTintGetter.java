package com.snek.engineersbliss.client.rendering;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;




public class FilteredBlockAndTintGetter implements BlockAndTintGetter {
    private final BlockAndTintGetter delegate;

    public FilteredBlockAndTintGetter(BlockAndTintGetter delegate) {
        this.delegate = delegate;
    }


    @Override
    public BlockState getBlockState(BlockPos pos) {
        BlockState state = delegate.getBlockState(pos);
        if (!RenderFilterHandler.getActiveBlocks().contains(state.getBlock())) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }


    @Override
    public int getBrightness(LightLayer lightLayer, BlockPos pos) {
        BlockState state = delegate.getBlockState(pos);
        if (!RenderFilterHandler.getActiveBlocks().contains(state.getBlock())) {
            return 15;
        }
        return delegate.getBrightness(lightLayer, pos);
    }


    @Override public LevelLightEngine getLightEngine() { return delegate.getLightEngine(); }
    @Override public int getBlockTint(BlockPos pos, ColorResolver resolver) { return delegate.getBlockTint(pos, resolver); }
    @Override public int getHeight() { return delegate.getHeight(); }
    @Override public int getMinY() { return delegate.getMinY(); }
    @Override public BlockEntity getBlockEntity(BlockPos arg0) { return delegate.getBlockEntity(arg0); }
    @Override public FluidState getFluidState(BlockPos pos) { return delegate.getFluidState(pos); }
    @Override public CardinalLighting cardinalLighting() { return delegate.cardinalLighting(); }
}