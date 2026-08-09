package com.snek.engineersbliss.client.mixin.custom_items.rendering;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;

import com.snek.engineersbliss.client.custom.block_entities.renderers.SceneSnapshot;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;




@Mixin(FeatureRenderDispatcher.class)
public class SceneCaptureMixin {

    @SuppressWarnings("unused")
    @Inject(method = "renderTranslucentFeatures", at = @At("HEAD"))
    private void engineersbliss$captureSceneSnapshot(CallbackInfo ci) {

        // Find target and its dimensions
        final @NotNull Minecraft mc = Minecraft.getInstance();
        final @NotNull RenderTarget mainTarget = mc.getMainRenderTarget();
        int width = mainTarget.width;
        int height = mainTarget.height;

        // Fix texture size if the screen target changed dimensions
        if(SceneSnapshot.getColor().getWidth(0) != width || SceneSnapshot.getColor().getHeight(0) != height) {
            SceneSnapshot.resize(width, height);
        }

        // Pull screen data
        final @NotNull CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        encoder.copyTextureToTexture(mainTarget.getColorTexture(), SceneSnapshot.getColor(), 0, 0, 0, 0, 0, width, height);
        encoder.copyTextureToTexture(mainTarget.getDepthTexture(), SceneSnapshot.getDepth(), 0, 0, 0, 0, 0, width, height);
    }
}