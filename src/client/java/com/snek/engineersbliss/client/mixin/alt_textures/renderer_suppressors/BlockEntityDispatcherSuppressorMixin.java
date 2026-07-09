package com.snek.engineersbliss.client.mixin.alt_textures.renderer_suppressors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.utils.BlockEntityUtils;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;




/**
 * This mixin is not required, but it helps improve performance of static block entity models.
 * It cancels all the preparation steps done before block entity data is submitted and things are rendered.
 * This gains a few FPS. Sometimes.
 * ! This is only separate from BlockEntityDispatcherOptimizerMixin to improve code structure and readability.
 */
@SuppressWarnings("java:S6916")
@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityDispatcherSuppressorMixin {

    @SuppressWarnings("unused")
    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true, require = 1)
    private void tryExtractRenderState(
        final BlockEntity blockEntity,
        final float partialTicks,
        final ModelFeatureRenderer.CrumblingOverlay breakProgress,
        final CallbackInfoReturnable<BlockEntityRenderState> cir
    ) {
        switch(blockEntity) {
            case SignBlockEntity e -> {
                if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_SIGNS) && !BlockEntityUtils.signHasText(e)) {
                    cir.setReturnValue(null);
                }
            }
            case LidBlockEntity e -> { //! LidBlockEntity covers all chest types
                if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_CHESTS)) {
                    cir.setReturnValue(null);
                }
            }
            case BannerBlockEntity e -> {
                if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_BANNERS)) {
                    cir.setReturnValue(null);
                }
            }
            case DecoratedPotBlockEntity e -> {
                if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_DECORATED_POTS) && !BlockEntityUtils.decoratedPotHasSherds(e)) {
                    cir.setReturnValue(null);
                }
            }
            case BellBlockEntity e -> {
                if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_BELLS)) {
                    cir.setReturnValue(null);
                }
            }
            case CopperGolemStatueBlockEntity e -> {
                if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_COPPER_GOLEM_STATUES)) {
                    cir.setReturnValue(null);
                }
            }
            default -> {}
        }
    }
}