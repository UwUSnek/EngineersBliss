package com.snek.engineersbliss.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;




/**
 * This accessor lets the powered rail level tracker call findPoweredRailSignal which is protected in PoweredRailBlock.
 * ! Despite the name, PoweredRailBlock covers both Powered Rails and Activator Rails. Activator rails are just a special type of powered rail.
 */
@Mixin(PoweredRailBlock.class)
public interface PoweredRailBlockAccessor {

    @Invoker("findPoweredRailSignal")
    boolean invokeFindPoweredRailSignal(Level level, BlockPos pos, BlockState state, boolean forward, int searchDepth);
}