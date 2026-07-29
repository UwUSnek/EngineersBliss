package com.snek.engineersbliss.client.mixin.alt_textures.renderer_suppressors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.utils.BlockEntityUtils;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.state.HangingSignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.Vec3;




/**
 * ! Block model rendering is cancelled from AbstractSignRenderingSuppressorMixin.
 * This specialized mixin cancels text transform calculations rendering (done in extractRenderState)
 * when the sign doesn't contain any text in either of its faces.
 * This further improves sign rendering performance.
 */
@Mixin(HangingSignRenderer.class)
public abstract class HangingSignRenderingSuppressorMixin extends AbstractSignRenderer<HangingSignRenderState> {


    //! This exists so Java doesn't cry about the OOP stuff
    //! This is never actually called
    protected HangingSignRenderingSuppressorMixin(BlockEntityRendererProvider.Context arg0) {
        super(arg0);
    }




    @SuppressWarnings("unused")
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$extractRenderState(
        final SignBlockEntity blockEntity,
        final HangingSignRenderState state,
        final float partialTicks,
        final Vec3 cameraPosition,
        final ModelFeatureRenderer.CrumblingOverlay breakProgress,
        final CallbackInfo ci
    ) {
        if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_SIGNS)) {
            if(!BlockEntityUtils.signHasText(blockEntity)) {
                ci.cancel();
            }
        }
    }
}