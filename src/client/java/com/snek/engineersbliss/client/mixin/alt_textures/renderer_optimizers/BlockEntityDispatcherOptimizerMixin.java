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
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;




/**
 * This mixin blocks all dynamic rendering logic from certain half-block-entities when not needed.
 * This also skips the distance check and render state creation, significantly improving FPS.
 * ! This is only separate from BlockEntityDispatcherSuppressorMixin to improve code structure and readability.
 */
@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityDispatcherOptimizerMixin {

    @SuppressWarnings("unused")
    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true, require = 1)
    private void tryExtractRenderState(
        final BlockEntity blockEntity,
        final float partialTicks,
        final ModelFeatureRenderer.CrumblingOverlay breakProgress,
        final CallbackInfoReturnable<BlockEntityRenderState> cir
    ) {
        switch((Object)this) {
            case CampfireBlockEntity e when
                AltTexturesHandler.getFeature(AltTextureFeature.OPTIMIZED_CAMPFIRES) && BlockEntityUtils.campfireHasItems(e)
                -> cir.setReturnValue(null);
            case ShelfBlockEntity e when
                AltTexturesHandler.getFeature(AltTextureFeature.OPTIMIZED_SHELVES) && BlockEntityUtils.shelfHasItems(e)
                -> cir.setReturnValue(null);
            default -> {}
        }
    }
}