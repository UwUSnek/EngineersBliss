package com.snek.engineersbliss.client.mixin.alt_textures.renderer_suppressors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.utils.BlockEntityUtils;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;




/**
 * This mixins cancels the dynamic rendering of disabled block entities before any data is computed, skipping the entire pipeline.
 * This is the most efficient way to cancel block entity rendering.
 * ! This is only separate from BlockEntityDispatcherOptimizerMixin to improve code structure and readability.
 */
@SuppressWarnings("java:S6916")
@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityDispatcherSuppressorMixin {

    @SuppressWarnings("unused")
    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$tryExtractRenderState(
        final BlockEntity blockEntity,
        final float partialTicks,
        final ModelFeatureRenderer.CrumblingOverlay breakProgress,
        final CallbackInfoReturnable<BlockEntityRenderState> cir
    ) {
        switch(blockEntity) {
            case SignBlockEntity e -> {
                if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_SIGNS) && !BlockEntityUtils.signHasText(e)) {
                    cir.setReturnValue(null);
                }
            }
            case LidBlockEntity e -> { //! LidBlockEntity covers all chest types
                if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_CHESTS)) {
                    cir.setReturnValue(null);
                }
            }
            case BannerBlockEntity e -> {
                if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_BANNERS)) {
                    cir.setReturnValue(null);
                }
            }
            case DecoratedPotBlockEntity e -> {
                if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_DECORATED_POTS) && !BlockEntityUtils.decoratedPotHasSherds(e)) {
                    cir.setReturnValue(null);
                }
            }
            case BellBlockEntity e -> {
                if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_BELLS)) {
                    cir.setReturnValue(null);
                }
            }
            case CopperGolemStatueBlockEntity e -> {
                if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_COPPER_GOLEM_STATUES)) {
                    cir.setReturnValue(null);
                }
            }
            case LecternBlockEntity e -> {
                if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_LECTERNS)) {
                    cir.setReturnValue(null);
                }
            }
            case BedBlockEntity e -> {
                if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_BEDS)) {
                    cir.setReturnValue(null);
                }
            }
            // case ItemSinkBlockEntity e -> {
            //     //FIXME use the proper setting
            //     // if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_BEDS)) {
            //         cir.setReturnValue(null);
            //     // }
            // }
            default -> {}
        }
    }
}