package com.snek.engineersbliss.client.mixin.alt_textures;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.utils.BlockEntityUtils;

import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.blockentity.state.CampfireRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.phys.Vec3;




@Mixin(CampfireRenderer.class)
public class CampfireRenderingOptimizerMixin {


    @SuppressWarnings("unused")
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true, require = 1)
    private void extractRenderState(
        final CampfireBlockEntity blockEntity,
        final CampfireRenderState state,
        final float partialTicks,
        final Vec3 cameraPosition,
        @Nullable final ModelFeatureRenderer.CrumblingOverlay breakProgress,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.OPTIMIZED_CAMPFIRES)) {
            if(!BlockEntityUtils.campfireHasItems(blockEntity)) {
                ci.cancel();
            }
        }
    }
}
