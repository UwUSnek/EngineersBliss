package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;




@Mixin(Entity.class)
public class BubbleColumnsPushSuppressorMixin {
    private BubbleColumnsPushSuppressorMixin() {}


    @Inject(method = "handleOnAboveBubbleColumn", at = @At("HEAD"), cancellable = true, require = 1)
    private static void handleOnAboveBubbleColumn(final Entity entity, final boolean dragDown, final BlockPos pos, final CallbackInfo ci) {
        if(CreativeTweaksHandler.clientPlayerHasFeature(entity, CreativeTweakFeature.DISABLE_BUBBLE_COLUMN_DRAG)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleOnInsideBubbleColumn", at = @At("HEAD"), cancellable = true, require = 1)
    private static void handleOnInsideBubbleColumn(final Entity entity, final boolean dragDown, final CallbackInfo ci) {
        if(CreativeTweaksHandler.clientPlayerHasFeature(entity, CreativeTweakFeature.DISABLE_BUBBLE_COLUMN_DRAG)) {
            ci.cancel();
        }
    }
}
