package com.snek.engineersbliss.client.mixin.rendering.vanilla_indigo;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4fc;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.snek.engineersbliss.client.custom.block_entities.renderers.base.SceneSnapshotHandler;
import com.snek.engineersbliss.client.custom.block_entities.renderers.base.__base_SpaceWarpingRenderer;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.mixin.accessors.BlockEntityRenderDispatcherAccessor;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;
import com.snek.engineersbliss.utils.data_types.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.phys.Vec3;








@Mixin(LevelRenderer.class)
public abstract class SpaceWarpingRenderPassMixin {

    @Shadow @Final private LevelTargetBundle targets;
    @Shadow @Final private RenderBuffers renderBuffers;
    @Shadow @Final private LevelRenderState levelRenderState;


    @SuppressWarnings({ "unused", "unchecked" })
    @Inject(method = "addLateDebugPass", at = @At("HEAD"))
    private void engineersbliss$addItemSinkPass(
        final FrameGraphBuilder frame,
        final CameraRenderState camera,
        final GpuBufferSlice fog,
        final Matrix4fc modelViewMatrix,
        final CallbackInfo ci
    ) {

        // Pass if custom shaded blocks are OFF
        if(!ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.BLOCK_SHADERS)) {
            return;
        }

        final @NotNull FramePass pass = frame.addPass("item_sink");
        this.targets.main = pass.readsAndWrites(this.targets.main);
        final @NotNull ResourceHandle<RenderTarget> mainTarget = this.targets.main;

        pass.executes(() -> {
            final @NotNull  RenderTarget target = mainTarget.get();
            final int width = target.width;
            final int height = target.height;


            // Resize textures if the window size has changed
            if(SceneSnapshotHandler.getColor().getWidth(0) != width || SceneSnapshotHandler.getColor().getHeight(0) != height) {
                SceneSnapshotHandler.resize(width, height);
            }


            // Load textures in for the first time
            final @NotNull CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
            encoder.copyTextureToTexture(target.getColorTexture(), SceneSnapshotHandler.getColor(), 0, 0, 0, 0, 0, width, height);
            encoder.copyTextureToTexture(target.getDepthTexture(), SceneSnapshotHandler.getDepth(), 0, 0, 0, 0, 0, width, height);


            // Find render states of the blocks and their renderers
            List<Pair<BlockEntityRenderState, __base_SpaceWarpingRenderer>> renderStates = new ArrayList<>();
            for(final @NotNull BlockEntityRenderState state : this.levelRenderState.blockEntityRenderStates) {
                final BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
                final @NotNull var genericRendererInstance = ((BlockEntityRenderDispatcherAccessor)dispatcher).getRenderers().get(state.blockEntityType);
                if(genericRendererInstance instanceof final @NotNull __base_SpaceWarpingRenderer rendererInstance) {
                    renderStates.add(Pair.from(state, rendererInstance));
                }
            }


            // Sort blocks by distance from the camera
            renderStates.sort((a, b) -> Double.compare(
                camera.pos.distanceToSqr(Vec3.atCenterOf(b.getFirst().blockPos)),
                camera.pos.distanceToSqr(Vec3.atCenterOf(a.getFirst().blockPos))
            ));

            // Trim to max shaded blocks amount setting
            final int maxBlocks = SettingsServerFeatureSet.BLOCK_SHADER_LIMIT.getValues().get(ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.BLOCK_SHADER_LIMIT));
            if(renderStates.size() > maxBlocks) {
                renderStates.subList(0, renderStates.size() - maxBlocks).clear();
            }



            // Draw blocks starting from the farthest one, update sampled textures after each draw
            if(!renderStates.isEmpty()) {
                final @NotNull PoseStack poseStack = new PoseStack();
                final @NotNull MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();

                for(final @NotNull var renderState : renderStates) {
                    encoder.copyTextureToTexture(target.getColorTexture(), SceneSnapshotHandler.getColor(), 0, 0, 0, 0, 0, width, height);
                    encoder.copyTextureToTexture(target.getDepthTexture(), SceneSnapshotHandler.getDepth(), 0, 0, 0, 0, 0, width, height);

                    renderState.getSecond().render(
                        List.of(renderState.getFirst()),
                        poseStack, camera, camera.pos, encoder,
                        target.getColorTextureView(),
                        target.getDepthTextureView()
                    );
                    bufferSource.endBatch();
                }
            }
        });
    }
}