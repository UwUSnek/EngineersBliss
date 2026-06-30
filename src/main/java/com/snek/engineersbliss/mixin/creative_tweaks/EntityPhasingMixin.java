package com.snek.engineersbliss.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.world.entity.Entity;




@Mixin(Entity.class)
public class EntityPhasingMixin {


    @Inject(method = "push", at = @At("HEAD"), cancellable = true, require = 1)
    public void push(final Entity entity, final CallbackInfo ci) {
        final Entity _this = (Entity)(Object)this;
        if(
            CreativeTweaksServerHandler.playerHasFeature(entity, CreativeTweakFeature.PHASE_THROUGH_ENTITIES) ||
            CreativeTweaksServerHandler.playerHasFeature(_this,  CreativeTweakFeature.PHASE_THROUGH_ENTITIES)
        ) {
            ci.cancel();
        }
    }
}
