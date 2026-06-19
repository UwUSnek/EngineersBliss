package com.snek.engineersbliss.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.state.BlockState;




@Mixin(ComparatorBlock.class)
public interface ComparatorBlockAccessor {

    @Invoker("getInputSignal")
    int invokeGetInputSignal(Level level, BlockPos pos, BlockState state);

    @Invoker("calculateOutputSignal")
    int invokeCalculateOutputSignal(final Level level, final BlockPos pos, final BlockState state);
}