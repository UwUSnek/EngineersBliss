package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;

import net.minecraft.world.entity.player.Player;





@Mixin(Player.class)
public class CurrentDragSuppressorMixin {


    @SuppressWarnings("unused")
    @Inject(method = "isPushedByFluid", at = @At("RETURN"), cancellable = true, require = 1)
    private void eb$isPushedByFluid(final CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValueZ()) {
            if(ClientFeatureSync.creativePlayerHasFeature(this, CreativeTweaksServerFeatureSet.DISABLE_CURRENT_DRAG)) {
                cir.setReturnValue(false);
            }
        }
    }
}
