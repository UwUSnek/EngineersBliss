package com.snek.engineersbliss.client.mixin.alt_textures;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;




@Mixin(ChestRenderer.class)
public abstract class ChestRenderingSuppressorMixin {


    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true, require = 1)
    private void extractRenderState(
        final BlockEntity blockEntity,
        final ChestRenderState state,
        final float partialTicks,
        final Vec3 cameraPosition,
        final ModelFeatureRenderer.CrumblingOverlay breakProgress,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_CHESTS)) {
            ci.cancel();
        }
    }


    @Inject(method = "submit", at = @At("HEAD"), cancellable = true, require = 1)
    private void submit(
        final ChestRenderState state,
        final PoseStack poseStack,
        final SubmitNodeCollector submitNodeCollector,
        final CameraRenderState camera,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_CHESTS)) {
            ci.cancel();
        }
    }
}