package com.snek.engineersbliss.client.mixin.overlays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;




/**
 * A mixin that tracks the computed power level of rail blocks and stores them in a static map
 */
@Mixin(PoweredRailBlock.class)
public class PoweredRailLevelTrackerMixin {


    /**
     * Inject into findPoweredRailSignal.
     * ! This is Vanilla's method for finding the power source of a powered rail chain.
     * ! It also stores a searchDepth that tracks how many blocks have been checked and stops at 9.
     * ! This is what the mixin yoinks to calculate the power level of the rail block.
     */
    @Inject(
        method = "findPoweredRailSignal",
        at = @At("RETURN"),
        cancellable = false
    )
    private void findPoweredRailSignal(final Level level, final BlockPos pos, final BlockState state, final boolean forward, final int searchDepth, final CallbackInfoReturnable<Boolean> cir) {
        final boolean isPowered = cir.getReturnValue();
        if(!isPowered) {
            OverlaysHandler.depowerRail(pos);
        }
        else {
            OverlaysHandler.addRailSource(pos, 9 - searchDepth);
        }
    }
}
//FIXME this might need to be on the server? idk