package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.HumanoidArm;








@SuppressWarnings("java:S4144")
@Mixin(ItemInHandRenderer.class)
public class HandAnimationsSuppressorMixin {


    @SuppressWarnings("unused")
    @Inject(method = "swingArm", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$swingArm(final float attack, final PoseStack poseStack, final int invert, final HumanoidArm arm, final CallbackInfo ci) {
        if(ClientFeatureSync.getFeatureB(CreativeTweaksServerFeatureSet.DISABLE_HAND_SWING_ANIMATION)) {
            ci.cancel();
        }
    }







    //! This blocks the main hand height recalculation cause by item changes
    @SuppressWarnings("unused")
    @ModifyExpressionValue(
        method = "renderHandsWithItems",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/item/ItemModelResolver;swapAnimationScale(Lnet/minecraft/world/item/ItemStack;)F",
            ordinal = 0
        )
    )
    private float eb$swapAnimationScaleMainHand(float original) {
        if(ClientFeatureSync.getFeatureB(CreativeTweaksServerFeatureSet.DISABLE_ITEM_CHANGE_ANIMATION)) {
            return 0.0f;
        }
        return original;
    }




    //! This blocks the off hand height recalculation cause by item changes
    @SuppressWarnings("unused")
    @ModifyExpressionValue(
        method = "renderHandsWithItems",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/item/ItemModelResolver;swapAnimationScale(Lnet/minecraft/world/item/ItemStack;)F",
            ordinal = 1
        )
    )
    private float eb$swapAnimationScaleOffHand(float original) {
        if(CreativeTweaksClientHandler.creativePlayerHasFeature(Minecraft.getInstance().player, CreativeTweaksServerFeatureSet.DISABLE_ITEM_CHANGE_ANIMATION)) {
            return 0.0f;
        }
        return original;
    }




    //! This forces the item to change instantly instead of waiting for an animation that doesn't exist anymore
    @SuppressWarnings("unused")
    @ModifyReturnValue(method = "shouldInstantlyReplaceVisibleItem", at = @At("RETURN"), require = 1)
    private boolean eb$shouldInstantlyReplaceVisibleItem(boolean original) {
        if(CreativeTweaksClientHandler.creativePlayerHasFeature(Minecraft.getInstance().player, CreativeTweaksServerFeatureSet.DISABLE_ITEM_CHANGE_ANIMATION)) {
            return true;
        }
        return original;
    }
}
