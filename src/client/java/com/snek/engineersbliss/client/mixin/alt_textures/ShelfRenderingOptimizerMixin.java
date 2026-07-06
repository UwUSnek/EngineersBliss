package com.snek.engineersbliss.client.mixin.alt_textures;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.utils.BlockEntityUtils;

import net.minecraft.client.renderer.blockentity.ShelfRenderer;
import net.minecraft.client.renderer.blockentity.state.ShelfRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import net.minecraft.world.phys.Vec3;




@Mixin(ShelfRenderer.class)
public class ShelfRenderingOptimizerMixin {


    @SuppressWarnings("unused")
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true, require = 1)
    private void extractRenderState(
        final ShelfBlockEntity blockEntity,
        final ShelfRenderState state,
        final float partialTicks,
        final Vec3 cameraPosition,
        @Nullable final ModelFeatureRenderer.CrumblingOverlay breakProgress,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.OPTIMIZED_SHELVES)) {
            if(!BlockEntityUtils.shelfHasItems(blockEntity)) {
                ci.cancel();
            }
        }
    }
}
