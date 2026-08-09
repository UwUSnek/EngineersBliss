package com.snek.engineersbliss.client.mixin.custom_items.rendering;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.joml.Matrix4fc;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.snek.engineersbliss.client.custom.block_entities.renderers.ItemSinkBlockEntityRenderer;
import com.snek.engineersbliss.client.custom.block_entities.renderers.SceneSnapshot;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.phys.Vec3;








@Mixin(LevelRenderer.class)
public abstract class ItemSinkPassMixin {

    @Shadow @Final private LevelTargetBundle targets;
    @Shadow @Final private RenderBuffers renderBuffers;
    @Shadow @Final private LevelRenderState levelRenderState;


    @Inject(method = "addLateDebugPass", at = @At("HEAD"))
    private void engineersbliss$addItemSinkPass(
        FrameGraphBuilder frame,
        CameraRenderState camera,
        GpuBufferSlice fog,
        Matrix4fc modelViewMatrix,
        CallbackInfo ci
    ) {
        FramePass pass = frame.addPass("item_sink");
        this.targets.main = pass.readsAndWrites(this.targets.main);
        final ResourceHandle<RenderTarget> mainTarget = this.targets.main;

        pass.executes(() -> {
            RenderTarget target = mainTarget.get();
            int width = target.width;
            int height = target.height;


            // Resize textures if the window size has changed
            if(SceneSnapshot.getColor().getWidth(0) != width || SceneSnapshot.getColor().getHeight(0) != height) {
                SceneSnapshot.resize(width, height);
            }


            // Load textures in for the first time
            final CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
            encoder.copyTextureToTexture(target.getColorTexture(), SceneSnapshot.getColor(), 0, 0, 0, 0, 0, width, height);
            encoder.copyTextureToTexture(target.getDepthTexture(), SceneSnapshot.getDepth(), 0, 0, 0, 0, 0, width, height);


            // Find render states of the blocks
            List<ItemSinkBlockEntityRenderer.ItemSinkRenderState> sinkStates = new ArrayList<>();
            for(BlockEntityRenderState state : this.levelRenderState.blockEntityRenderStates) {
                if(state instanceof ItemSinkBlockEntityRenderer.ItemSinkRenderState sinkState) {
                    sinkStates.add(sinkState);
                }
            }


            // Sort blocks by distance from the camera
            sinkStates.sort((a, b) -> Double.compare(
                camera.pos.distanceToSqr(Vec3.atCenterOf(b.blockPos)),
                camera.pos.distanceToSqr(Vec3.atCenterOf(a.blockPos))
            ));


            // Draw blocks starting from the farthest one, update sampled textures after each draw
            if(!sinkStates.isEmpty()) {
                PoseStack poseStack = new PoseStack();
                MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();

                for(ItemSinkBlockEntityRenderer.ItemSinkRenderState sinkState : sinkStates) {
                    encoder.copyTextureToTexture(target.getColorTexture(), SceneSnapshot.getColor(), 0, 0, 0, 0, 0, width, height);
                    encoder.copyTextureToTexture(target.getDepthTexture(), SceneSnapshot.getDepth(), 0, 0, 0, 0, 0, width, height);

                    ItemSinkBlockEntityRenderer.renderDirect(List.of(sinkState), poseStack, bufferSource, camera, camera.pos);
                    bufferSource.endBatch();
                }
            }
        });
    }
}