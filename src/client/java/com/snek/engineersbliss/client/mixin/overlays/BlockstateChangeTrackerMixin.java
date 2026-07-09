package com.snek.engineersbliss.client.mixin.overlays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;




@Mixin(ClientLevel.class)
public class BlockstateChangeTrackerMixin {


    @SuppressWarnings("unused")
    @Inject(method = "sendBlockUpdated", at = @At("RETURN"))
	private void eb$sendBlockUpdated(
        final BlockPos pos,
        final BlockState old,
        final BlockState current,
        final int updateFlags,
        final CallbackInfo ci
    ) {

        // If the block change wasn't rejected, call the overlay handler callback
        OverlaysHandler.onBlockChanged((ClientLevel)(Object)this, pos, current);
        //FIXME handle invalid block changes? if needed
    }
}