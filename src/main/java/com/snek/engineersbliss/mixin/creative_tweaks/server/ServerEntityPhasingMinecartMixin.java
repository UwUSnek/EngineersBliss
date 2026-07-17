package com.snek.engineersbliss.mixin.creative_tweaks.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakServerFeatureSet;
import com.snek.engineersbliss.network.features.ServerFeatureSync;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;




@Mixin(AbstractMinecart.class)
public class ServerEntityPhasingMinecartMixin {


    @SuppressWarnings("unused")
    @Inject(method = "push", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$push(final Entity entity, final CallbackInfo ci) {
        if(ServerFeatureSync.serverPlayerHasFeature(entity, CreativeTweakServerFeatureSet.PHASE_THROUGH_ENTITIES)) {
            ci.cancel();
        }
    }
}
