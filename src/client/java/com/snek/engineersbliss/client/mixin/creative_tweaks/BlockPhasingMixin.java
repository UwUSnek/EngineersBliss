package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;



/**
 * This mixin cancels the pushing effect of solid blocks if the player is phasing through them and flying in creative mode.
 * This is done by making "suffocatesAt" always return false,
 * so the game thinks the player can freely stand inside of any block and doesn't try to push them out.
 */
@Mixin(LocalPlayer.class)
public class BlockPhasingMixin {


    @SuppressWarnings("unused")
    @Inject(method = "suffocatesAt", at = @At("HEAD"), cancellable = true, require = 1)
	private void eb$suffocatesAt(final BlockPos pos, final CallbackInfoReturnable<Boolean> cir) {
        if(ClientFeatureSync.shouldPlayerPhaseThroughBlocks(this)) {
            cir.setReturnValue(false);
        }
    }
}
