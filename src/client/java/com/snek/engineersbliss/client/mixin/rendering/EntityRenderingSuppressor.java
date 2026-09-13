package com.snek.engineersbliss.client.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.rendering.RenderingServerFeatureSet;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;

//? if <=26.1.2 {
    // import net.minecraft.client.DeltaTracker;
    // import net.minecraft.client.Camera;
    // import net.minecraft.client.renderer.culling.Frustum;
//? } else {
    import com.mojang.blaze3d.vertex.PoseStack;
    import net.minecraft.client.renderer.SubmitNodeCollector;
//? }







@Mixin(LevelRenderer.class)
public class EntityRenderingSuppressor {




    //? if <=26.1.2 {
        // @SuppressWarnings("unused")
        // @Inject(method = "extractVisibleEntities", at = @At("HEAD"), cancellable = true, require = 1)
        // private void eb$extractVisibleEntities(
        //     final Camera camera,
        //     final Frustum frustum,
        //     final DeltaTracker deltaTracker,
        //     final LevelRenderState output,
        //     final CallbackInfo ci
        // ) {
        //     if(!ClientFeatureSync.getFeatureB(RenderingServerFeatureSet.RENDER_ENTITIES)) {
        //         ci.cancel();
        //     }
        // }
    //? } else {
        @SuppressWarnings("unused")
        @Inject(method = "submitEntities", at = @At("HEAD"), cancellable = true, require = 1)
        private void eb$submitEntities(
            final PoseStack poseStack,
            final LevelRenderState levelRenderState,
            final SubmitNodeCollector output,
            final CallbackInfo ci
        ) {
            if(!ClientFeatureSync.getFeatureB(RenderingServerFeatureSet.RENDER_ENTITIES)) {
                ci.cancel();
            }
        }
    //? }
}