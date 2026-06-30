package com.snek.engineersbliss.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.world.entity.Entity;



@Mixin(Entity.class)
public class FireEffectSuppressorMixin {


    @Inject(method = "fireImmune", at = @At("RETURN"), cancellable = true, require = 1)
	private void fireImmune(final CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValueZ()) {
            final Entity entity = (Entity)(Object)this;
            if(CreativeTweaksServerHandler.playerHasFeature(entity, CreativeTweakFeature.DISABLE_FIRE_EFFECT)) {
                cir.setReturnValue(true);
            }
        }
    }
}
