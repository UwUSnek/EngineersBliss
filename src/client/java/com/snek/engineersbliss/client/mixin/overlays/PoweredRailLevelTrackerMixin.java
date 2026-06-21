package com.snek.engineersbliss.client.mixin.overlays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.RailAttachedData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
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
     * ! It also stores a searchDepth that tracks how many blocks have been checked and stops at 9.
     * ! This is what the mixin yoinks to calculate the power level of the rail block.
     *
     * ! The calculated level is stored in the data's class in a static field to be used right away.
     *
     * ! This mixin is active on the server in single player.
     * ! Additional checks ensure its only ever called on the client.
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
        if(!level.isClientSide()) return;
        if(!cir.getReturnValue().booleanValue()) return;


        // If the current iteration is the one that found the source of the signal, update the static field with this power level
        //! Levels start at 8 - n (8 is the first non-directly-powered rail block)
        if(level.hasNeighborSignal(pos)) {
            RailAttachedData.updateComputedValue(8 - searchDepth);
        }
    }
}