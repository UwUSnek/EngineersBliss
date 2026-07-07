package com.snek.engineersbliss.client.mixin.alt_textures.renderer_suppressors;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.utils.BlockEntityUtils;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.renderer.blockentity.state.DecoratedPotRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.phys.Vec3;








/**
 * This mixin stops model rendering of decorated pots with customized sides.
 * This is done by replacing the submit function with a custom version that only submits the sprites of the sherds.
 */
@Mixin(DecoratedPotRenderer.class)
public abstract class DecoratedPotRenderingSuppressorMixin implements BlockEntityRenderer<DecoratedPotBlockEntity, DecoratedPotRenderState> {

    @Shadow private SpriteGetter sprites;
    @Shadow private ModelPart frontSide;
    @Shadow private ModelPart backSide;
    @Shadow private ModelPart leftSide;
    @Shadow private ModelPart rightSide;
    @Shadow private static SpriteId getSideSprite(final Optional<Item> item) { return null; }





    @SuppressWarnings("unused")
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true, require = 1)
    private void extractRenderState(
        final DecoratedPotBlockEntity blockEntity,
        final DecoratedPotRenderState state,
        final float partialTicks,
        final Vec3 cameraPosition,
        @Nullable final ModelFeatureRenderer.CrumblingOverlay breakProgress,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_DECORATED_POTS)) {
            if(BlockEntityUtils.decoratedPotHasSherds(state.decorations)) {
                ci.cancel();
            }
        }
    }




    @SuppressWarnings("unused")
    @Inject(
        method = "submit(Lnet/minecraft/client/renderer/blockentity/state/DecoratedPotRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At("HEAD"), cancellable = true, require = 1
    )
    private void submit(
        final DecoratedPotRenderState state,
        final PoseStack poseStack,
        final SubmitNodeCollector submitNodeCollector,
        final CameraRenderState camera,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_DECORATED_POTS)) {
            final PotDecorations decorations = state.decorations;
            if(BlockEntityUtils.decoratedPotHasSherds(decorations)) {


                // Push pose. Same logic as vanilla but without the wobbling animation (static model can't wobble)
                poseStack.pushPose();
                poseStack.mulPose(DecoratedPotRenderer.modelTransformation(state.direction));


                // Render each of the 4 sides. Same logic as Vanilla, but in loop format
                //! Skip rendering of default sides, these are already covered by the static model.
                final      ModelPart[]  sideModelsIndexed = new ModelPart[]{ frontSide,           backSide,           leftSide,           rightSide };
                final Optional<Item>[] decorationsIndexed = new  Optional[]{ decorations.front(), decorations.back(), decorations.left(), decorations.right() };
                for(int i = 0; i < 4; ++i) {
                    final Optional<Item> item = decorationsIndexed[i];
                    if(!item.isEmpty() && item.get() != Items.BRICK) {
                        final SpriteId spriteId = getSideSprite(item);
                        submitNodeCollector.submitModelPart(
                            sideModelsIndexed[i],
                            poseStack,
                            spriteId.renderType(RenderTypes::entitySolid),
                            state.lightCoords,
                            OverlayTexture.NO_OVERLAY,
                            this.sprites.get(spriteId),
                            false,
                            false,
                            -1,
                            null,
                            0
                        );
                    }
                }


                // Pop pose and cancel vanilla rendering
                poseStack.popPose();
                ci.cancel();
            }
        }
    }
}