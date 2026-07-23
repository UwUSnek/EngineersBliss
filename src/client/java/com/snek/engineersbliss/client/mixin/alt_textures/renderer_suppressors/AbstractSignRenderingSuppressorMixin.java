package com.snek.engineersbliss.client.mixin.alt_textures.renderer_suppressors;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.state.properties.WoodType;




/**
 * This mixin cancels sign model rendering when the static signs feature is active.
 * Text rendering is always kept.
 */
@Mixin(AbstractSignRenderer.class)
public abstract class AbstractSignRenderingSuppressorMixin {


    @SuppressWarnings("unused")
    @Inject(method = "submitSign", at = @At("HEAD"), cancellable = true, require = 1)
	private void eb$submitSign(
		final PoseStack poseStack,
		final int lightCoords,
		final WoodType type,
		final Model.Simple signModel,
		@Nullable final ModelFeatureRenderer.CrumblingOverlay breakProgress,
		final SubmitNodeCollector submitNodeCollector,
        final CallbackInfo ci
	) {
        if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_SIGNS)) {
            ci.cancel();
        }
    }


    //! submitSignText is kept unaltered to let the text render properly.
}