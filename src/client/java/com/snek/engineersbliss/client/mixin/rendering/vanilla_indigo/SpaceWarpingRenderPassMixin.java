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
import com.snek.engineersbliss.custom.block_entities.CustomBlockEntityHandler;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;
import com.snek.engineersbliss.utils.data_types.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
//? if <=26.1.2 {
    // import org.joml.Matrix4fc;
    // import net.minecraft.client.renderer.MultiBufferSource;
//? } else {
    import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
//? }
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


    //! 26.2+ straight up has no debug pass in the LevelRenderer.
    //! addAlwaysOnTopPass is unrelated, but it works just fine for this mixin.
    //? if <= 26.1.2 {
        // @SuppressWarnings({ "unused", "unchecked" })
        // @Inject(method = "addLateDebugPass", at = @At("HEAD"))
        // private void eb$addItemSinkPass(
        //     final FrameGraphBuilder frame,
        //     final CameraRenderState camera,
        //     final GpuBufferSlice fog,
        //     final Matrix4fc modelViewMatrix,
        //     final CallbackInfo ci
        // ) {
    //? } else {
        @SuppressWarnings({ "unused", "unchecked" })
        @Inject(method = "addAlwaysOnTopPass", at = @At("HEAD"))
        private void eb$addItemSinkPass(
            final FrameGraphBuilder frame,
            final FeatureRenderDispatcher.PreparedFrame featureFrame,
            final GpuBufferSlice fog,
            final CallbackInfo ci
        ) {
            final @NotNull CameraRenderState camera = this.levelRenderState.cameraRenderState;
    //? }

        // Pass if custom shaded blocks are OFF
        if(!ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.BLOCK_SHADERS)) {
            return;
        }

        final @NotNull FramePass pass = frame.addPass("item_sink");
        this.targets.main = pass.readsAndWrites(this.targets.main);
        final @NotNull ResourceHandle<RenderTarget> mainTarget = this.targets.main;
        pass.executes(() -> {
//TODO remove
try {
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
//TODO remove
var rendererMap = ((BlockEntityRenderDispatcherAccessor)dispatcher).getRenderers();
System.out.println("rendererMapSize=" + rendererMap.size()
+ " blackHole=" + rendererMap.get(CustomBlockEntityHandler.COSMETIC_BLACK_HOLE)
+ " whiteHole=" + rendererMap.get(CustomBlockEntityHandler.COSMETIC_WHITE_HOLE));
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


//TODO remove
long relevantCount = this.levelRenderState.blockEntityRenderStates.stream()
    .filter(s -> s.blockEntityType == CustomBlockEntityHandler.COSMETIC_BLACK_HOLE
              || s.blockEntityType == CustomBlockEntityHandler.COSMETIC_WHITE_HOLE
              || s.blockEntityType == CustomBlockEntityHandler.ITEM_SINK
              || s.blockEntityType == CustomBlockEntityHandler.ITEM_SOURCE)
    .count();
System.out.println("totalStates=" + this.levelRenderState.blockEntityRenderStates.size() + " relevantStates=" + relevantCount);


            // Draw blocks starting from the farthest one, update sampled textures after each draw
            if(!renderStates.isEmpty()) {
                final @NotNull PoseStack poseStack = new PoseStack();
                //? if <=26.1.2 {
                    // final @NotNull MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
                //? } else {
                    //! 26.2 doesn't require need endBatch()
                //? }

                for(final @NotNull var renderState : renderStates) {
                    encoder.copyTextureToTexture(target.getColorTexture(), SceneSnapshotHandler.getColor(), 0, 0, 0, 0, 0, width, height);
                    encoder.copyTextureToTexture(target.getDepthTexture(), SceneSnapshotHandler.getDepth(), 0, 0, 0, 0, 0, width, height);

                    renderState.getSecond().render(
                        List.of(renderState.getFirst()),
                        poseStack, camera, camera.pos, encoder,
                        target.getColorTextureView(),
                        target.getDepthTextureView()
                    );
                    //? if <=26.1.2 {
                        // bufferSource.endBatch();
                    //? } else {
                        //! 26.2 doesn't require need endBatch()
                    //? }
                }
            }
//TODO remove
} catch (Throwable t) { System.err.println(t.getMessage());t.printStackTrace(); }
        });
    }
}