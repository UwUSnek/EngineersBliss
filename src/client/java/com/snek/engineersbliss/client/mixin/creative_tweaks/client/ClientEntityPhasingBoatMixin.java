package com.snek.engineersbliss.client.mixin.creative_tweaks.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;




@Mixin(AbstractBoat.class)
public class ClientEntityPhasingBoatMixin {


    @SuppressWarnings("unused")
    @Inject(method = "push", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$push(final Entity entity, final CallbackInfo ci) {
        if(ClientFeatureSync.shouldPlayerPhaseThroughEntities(entity)) {
            ci.cancel();
        }
    }
}
