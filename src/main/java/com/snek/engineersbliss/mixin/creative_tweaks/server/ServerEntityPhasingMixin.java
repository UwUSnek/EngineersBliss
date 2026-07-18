package com.snek.engineersbliss.mixin.creative_tweaks.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;
import com.snek.engineersbliss.network.features.ServerFeatureSync;

import net.minecraft.world.entity.Entity;




@Mixin(Entity.class)
public class ServerEntityPhasingMixin {


    @SuppressWarnings("unused")
    @Inject(method = "push", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$push(final Entity entity, final CallbackInfo ci) {
        if(
            ServerFeatureSync.serverPlayerHasFeature(entity, CreativeTweaksServerFeatureSet.PHASE_THROUGH_ENTITIES) ||
            ServerFeatureSync.serverPlayerHasFeature(this,   CreativeTweaksServerFeatureSet.PHASE_THROUGH_ENTITIES)
        ) {
            ci.cancel();
        }
    }
}
