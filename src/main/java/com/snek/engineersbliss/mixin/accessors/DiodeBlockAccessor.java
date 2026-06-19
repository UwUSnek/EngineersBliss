package com.snek.engineersbliss.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;





@Mixin(DiodeBlock.class)
public interface DiodeBlockAccessor {

    @Invoker("getAlternateSignal")
    int invokeGetAlternateSignal(SignalGetter level, BlockPos pos, BlockState state);
}
