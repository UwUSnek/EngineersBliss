package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.HoneyBlock;




@Mixin(HoneyBlock.class)
public class HoneyBlockFeaturesMixin {
    private HoneyBlockFeaturesMixin() {}


    @SuppressWarnings("unused")
    @Inject(method = "doSlideMovement", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$doSlideMovement(final Entity entity, CallbackInfo ci) {
        if(ClientFeatureSync.creativePlayerHasFeature(entity, CreativeTweaksServerFeatureSet.DISABLE_HONEY_SLIDING)) {
            ci.cancel();
        }
    }
}
