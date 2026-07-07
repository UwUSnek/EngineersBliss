package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweakFeature;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.player.LocalPlayer;




/**
 * This mixin fixes not being able to sprint in shallow water. Minecraft specifically checks for that.
 * This is done by changing the return value of LocalPlayer.isSprintingPossible
 * when the player is in shallow water and the original return value is false and the player has the DISABLE_WATER_SLOWDOWN feature active.
 */
@Mixin(LocalPlayer.class)
public class FluidSpeedSprintFixMixin {


    @SuppressWarnings("unused")
    @Inject(method = "isSprintingPossible", at = @At("RETURN"), cancellable = true)
    private void isSprintingPossible(final boolean allowedInShallowWater, final CallbackInfoReturnable<Boolean> cir) {
        final LocalPlayer _this = (LocalPlayer)(Object)this;
        if(!cir.getReturnValueZ()) {
            if(CreativeTweaksHandler.clientPlayerHasFeature(_this, CreativeTweakFeature.DISABLE_WATER_SLOWDOWN) && _this.isInShallowWater()) {
                cir.setReturnValue(true);
            }
        }
    }
}