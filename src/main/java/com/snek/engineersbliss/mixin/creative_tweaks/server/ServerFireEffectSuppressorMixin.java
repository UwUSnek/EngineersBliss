package com.snek.engineersbliss.mixin.creative_tweaks.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.ServerFeatureSync;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;

import net.minecraft.world.entity.Entity;



@Mixin(Entity.class)
public class ServerFireEffectSuppressorMixin {


    @SuppressWarnings("unused")
    @Inject(method = "fireImmune", at = @At("RETURN"), cancellable = true, require = 1)
	private void eb$fireImmune(final CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValueZ()) {
            if(ServerFeatureSync.creativePlayerHasFeature(this, CreativeTweaksServerFeatureSet.DISABLE_FIRE_EFFECT)) {
                cir.setReturnValue(true);
            }
        }
    }
}
