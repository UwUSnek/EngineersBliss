package com.snek.engineersbliss.client.mixin.creative_tweaks.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;

import net.minecraft.world.entity.Entity;



@Mixin(Entity.class)
public class ClientFreezeEffectSuppressorMixin {


    @SuppressWarnings("unused")
    @Inject(method = "canFreeze", at = @At("RETURN"), cancellable = true, require = 1)
	private void canFreeze(final CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValueZ()) {
            if(CreativeTweaksHandler.clientPlayerHasFeature(this, CreativeTweakFeature.DISABLE_FREEZING_EFFECT)) {
                cir.setReturnValue(false);
            }
        }
    }
}
