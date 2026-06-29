package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;



@Mixin(Entity.class)
public class FireEffectSuppressorMixin {


    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true, require = 1)
	private void fireImmune(final CallbackInfoReturnable<Boolean> cir) {
        if(CreativeTweaksServerHandler.playerHasFeature(Minecraft.getInstance().player, CreativeTweakFeature.DISABLE_FIRE_EFFECT)) {
            cir.setReturnValue(true);
        }
    }
}
