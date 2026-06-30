package com.snek.engineersbliss.mixin.creative_tweaks.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.world.entity.Entity;




@Mixin(Entity.class)
public class ServerEntityPhasingMixin {


    @SuppressWarnings("unused")
    @Inject(method = "push", at = @At("HEAD"), cancellable = true, require = 1)
    private void push(final Entity entity, final CallbackInfo ci) {
        final Entity _this = (Entity)(Object)this;
        if(
            CreativeTweaksServerHandler.serverPlayerHasFeature(entity, CreativeTweakFeature.PHASE_THROUGH_ENTITIES) ||
            CreativeTweaksServerHandler.serverPlayerHasFeature(_this,  CreativeTweakFeature.PHASE_THROUGH_ENTITIES)
        ) {
            ci.cancel();
        }
    }
}
