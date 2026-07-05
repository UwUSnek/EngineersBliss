package com.snek.engineersbliss.client.mixin.alt_textures;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

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
 */
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
        if(blockEntity instanceof SignBlockEntity sign) {
            if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_SIGNS) && !MinecraftUtils.signHasText(sign)) {
                cir.setReturnValue(null);
            }
        }
        if(blockEntity instanceof LidBlockEntity) {
            if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_CHESTS)) {
                cir.setReturnValue(null);
            }
        }
        if(blockEntity instanceof BannerBlockEntity) {
            if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_BANNERS)) {
                cir.setReturnValue(null);
            }
        }
        if(blockEntity instanceof DecoratedPotBlockEntity) {
            if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_DECORATED_POTS)) {
                cir.setReturnValue(null);
            }
        }
        if(blockEntity instanceof BellBlockEntity) {
            if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_BELLS)) {
                cir.setReturnValue(null);
            }
        }
        if(blockEntity instanceof CopperGolemStatueBlockEntity) {
            if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_COPPER_GOLEM_STATUES)) {
                cir.setReturnValue(null);
            }
        }
    }
}