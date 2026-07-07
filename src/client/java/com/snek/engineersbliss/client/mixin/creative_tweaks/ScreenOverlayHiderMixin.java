package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweakFeature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;




/**
 * A mixin that hides screen overlays from ScreenEffectRenderer when needed.
 * This handles "suffocating in a solid block" overlays and the Water overlay.
 * Being on fire (& fire overlay) and the freezing effect are handled separately.
 */
@Mixin(ScreenEffectRenderer.class)
public class ScreenOverlayHiderMixin {
    private ScreenOverlayHiderMixin() {}




    @SuppressWarnings("unused")
    @Inject(method = "getViewBlockingState", at = @At("HEAD"), cancellable = true, require = 1)
    private static void getViewBlockingState(final Player player, final CallbackInfoReturnable<BlockState> cir) {
        if(CreativeTweaksHandler.shouldPlayerPhaseThroughBlocks(player)) {
            cir.setReturnValue(null);
        }
    }


    @SuppressWarnings("unused")
    @Inject(method = "renderWater", at = @At("HEAD"), cancellable = true, require = 1)
	private static void renderWater(final Minecraft minecraft, final PoseStack poseStack, final MultiBufferSource bufferSource, final CallbackInfo ci) {
        if(CreativeTweaksHandler.clientPlayerHasFeature(minecraft.player, CreativeTweakFeature.DISABLE_WATER_OVERLAY)) {
            ci.cancel();
        }
    }
}
