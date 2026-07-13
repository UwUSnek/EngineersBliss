package com.snek.engineersbliss.mixin.creative_tweaks.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakServerFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.world.entity.Entity;



@Mixin(Entity.class)
public class ServerFireEffectSuppressorMixin {


    @SuppressWarnings("unused")
    @Inject(method = "fireImmune", at = @At("RETURN"), cancellable = true, require = 1)
	private void eb$fireImmune(final CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValueZ()) {
            if(CreativeTweaksServerHandler.serverPlayerHasFeature(this, CreativeTweakServerFeature.DISABLE_FIRE_EFFECT)) {
                cir.setReturnValue(true);
            }
        }
    }
}
