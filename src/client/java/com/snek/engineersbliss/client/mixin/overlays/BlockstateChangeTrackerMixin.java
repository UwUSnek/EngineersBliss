package com.snek.engineersbliss.client.mixin.overlays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;




@Mixin(ClientLevel.class)
public class BlockstateChangeTrackerMixin {

    @Inject(method = "setBlock", at = @At("RETURN"))
	private void setBlock(
        final BlockPos pos,
        final BlockState blockState,
        // @UpdateFlags final int updateFlags,
        final int updateFlags,
        final int updateLimit,
        final CallbackInfoReturnable<Boolean> cir
    ) {
        if(cir.getReturnValue().booleanValue()) {
            OverlaysHandler.onBlockChanged((ClientLevel)(Object)this, pos, blockState);
        }
    }
}