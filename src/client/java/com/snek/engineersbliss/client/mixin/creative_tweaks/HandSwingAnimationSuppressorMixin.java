package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.HumanoidArm;




@Mixin(ItemInHandRenderer.class)
public class HandSwingAnimationSuppressorMixin {

    @SuppressWarnings("unused")
	@Inject(method = "swingArm", at = @At("HEAD"), cancellable = true)
	private void swingArm(final float attack, final PoseStack poseStack, final int invert, final HumanoidArm arm, final CallbackInfo ci) {
        if(CreativeTweaksHandler.clientPlayerHasFeature(Minecraft.getInstance().player, CreativeTweakFeature.DISABLE_HAND_SWING_ANIMATION)) {
            ci.cancel();
        }
    }

//     @Inject(method = "applyEquipOffset", at = @At("HEAD"), cancellable = true)
//     private void applyEquipOffset(PoseStack matrices, HumanoidArm arm, float equipProgress, CallbackInfo ci) {
//         ci.cancel();
//     }
}
