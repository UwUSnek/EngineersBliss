package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;




/**
 * A mixin that hides screen overlays from ScreenEffectRenderer when needed.
 * This handles "suffocating in a solid block" overlays and fire/water overlays.
 */
@Mixin(ScreenEffectRenderer.class)
public class ScreenOverlayHiderMixin {



    @Inject(method = "getViewBlockingState", at = @At("HEAD"), cancellable = true, require = 1)
    private static void getViewBlockingState(final Player player, final CallbackInfoReturnable<BlockState> cir) {
        if(CreativeTweaksServerHandler.canPlayerPhaseThroughBlocks(player)) {
            cir.setReturnValue(null);
        }
    }
}
