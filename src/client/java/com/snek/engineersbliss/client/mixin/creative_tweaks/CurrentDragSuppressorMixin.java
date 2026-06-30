package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.world.entity.player.Player;





@Mixin(Player.class)
public class CurrentDragSuppressorMixin {


    @SuppressWarnings("unused")
    @Inject(method = "isPushedByFluid", at = @At("RETURN"), cancellable = true, require = 1)
    private void isPushedByFluid(final CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValueZ()) {
            final Player _this = (Player)(Object)this;
            if(CreativeTweaksServerHandler.serverPlayerHasFeature(_this, CreativeTweakFeature.DISABLE_CURRENT_DRAG)) {
                cir.setReturnValue(false);
            }
        }
    }
}
