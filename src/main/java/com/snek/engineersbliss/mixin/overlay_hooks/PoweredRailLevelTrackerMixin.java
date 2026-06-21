package com.snek.engineersbliss.mixin.overlay_hooks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.mixin.overlay_data.RailInputChangeTrackerMixin;
import com.snek.engineersbliss.network.overlay_data.handlers.RailInputDataHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;




/**
 * A mixin that tracks the computed power level of rail blocks and stores them in a static map
 */
@Mixin(PoweredRailBlock.class)
public class PoweredRailLevelTrackerMixin {


    /**
     * Inject into isSameRailWithPower.
     * ! This is Vanilla's method for finding the power source of a powered rail chain.
     * ! The calculated source position is stored in the update detector's class in a static field to be used right away.
     *
     * ! Rails that are the source of the signal need to be checked externally using level.hasNeighborSignal(pos).
     * ! This mixin only detects rails that are powered by other rails.
     */
    @Inject(
        method = "isSameRailWithPower",
        at = @At("RETURN"),
        cancellable = false
    )
    private void isSameRailWithPower(final Level level, final BlockPos pos, final boolean forward, final int searchDepth, final RailShape dir, final CallbackInfoReturnable<Boolean> cir) {
        if(level.isClientSide()) return;
        if(!RailInputDataHandler.isRecording()) return;
        if(!cir.getReturnValue().booleanValue()) return;


        // If the current iteration is the one that found the source of the signal, update the static field with this position
        if(level.hasNeighborSignal(pos)) {
            RailInputDataHandler.setSignalSourcePos(pos);
        }
    }
}