package com.snek.engineersbliss.mixin.creative_tweaks.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.feature_handlers.ServerFeatureSync;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;




@Mixin(AbstractBoat.class)
public class ServerEntityPhasingBoatMixin {


    @SuppressWarnings("unused")
    @Inject(method = "push", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$push(final Entity entity, final CallbackInfo ci) {
        if(ServerFeatureSync.creativePlayerHasFeature(entity, CreativeTweaksServerFeatureSet.PHASE_THROUGH_ENTITIES)) {
            ci.cancel();
        }
    }
}
