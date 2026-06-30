package com.snek.engineersbliss.client.mixin.creative_tweaks.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;




@Mixin(AbstractBoat.class)
public class ClientEntityPhasingBoatMixin {


    @Inject(method = "push", at = @At("HEAD"), cancellable = true, require = 1)
    public void push(final Entity entity, final CallbackInfo ci) {
        if(CreativeTweaksHandler.clientPlayerHasFeature(CreativeTweakFeature.PHASE_THROUGH_ENTITIES)) {
            ci.cancel();
        }
    }
}
