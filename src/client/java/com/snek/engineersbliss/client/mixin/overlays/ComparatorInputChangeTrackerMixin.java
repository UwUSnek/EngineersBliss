package com.snek.engineersbliss.client.mixin.overlays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.OverlayAttachedDataComparator;

import net.minecraft.world.level.block.entity.ComparatorBlockEntity;




/**
 * A mixin that tracks the computed output level of comparators
 */
@Mixin(ComparatorBlockEntity.class)
public class ComparatorInputChangeTrackerMixin {

    @Inject(method = "setOutputSignal", at = @At("RETURN"))
	private void setOutputSignal(final int value, final CallbackInfo ci) {
        final ComparatorBlockEntity be = (ComparatorBlockEntity)(Object)this;
        OverlaysHandler.updateAttachedData(be.getBlockPos(), new OverlayAttachedDataComparator(value));
    }
}