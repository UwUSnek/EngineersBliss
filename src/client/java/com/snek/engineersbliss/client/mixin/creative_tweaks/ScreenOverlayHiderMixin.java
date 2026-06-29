package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;




/**
 * A mixin that hides screen overlays from ScreenEffectRenderer when needed.
 * This handles "suffocating in a solid block" overlays and the Water overlay.
 * Being on fire (& fire overlay) and the freezing effect are handled separately.
 */
@Mixin(ScreenEffectRenderer.class)
public class ScreenOverlayHiderMixin {



    @Inject(method = "getViewBlockingState", at = @At("HEAD"), cancellable = true, require = 1)
    private static void getViewBlockingState(final Player player, final CallbackInfoReturnable<BlockState> cir) {
        if(CreativeTweaksServerHandler.canPlayerPhaseThroughBlocks(player)) {
            cir.setReturnValue(null);
        }
    }


    @Inject(method = "renderItemActivationAnimation", at = @At("HEAD"), cancellable = true, require = 1)
	private void renderItemActivationAnimation(final PoseStack poseStack, final float partialTicks, final SubmitNodeCollector submitNodeCollector, final CallbackInfo ci) {
        ci.cancel();
    }


    @Inject(method = "renderWater", at = @At("HEAD"), cancellable = true, require = 1)
	private static void renderWater(final Minecraft minecraft, final PoseStack poseStack, final MultiBufferSource bufferSource, final CallbackInfo ci) {
        if(CreativeTweaksServerHandler.playerHasFeature(Minecraft.getInstance().player, CreativeTweakFeature.DISABLE_WATER_OVERLAY)) {
            ci.cancel();
        }
    }
}
