package com.snek.engineersbliss.client.custom.block_entities.renderers.base;

import java.util.List;

import com.snek.engineersbliss.EngineerSBliss;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;








public abstract class __base_SpaceWarpingRenderer<E extends BlockEntity, S extends BlockEntityRenderState> implements BlockEntityRenderer<E, S> {
    public static final Identifier SCENE_COLOR_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "scene_color_snapshot");
    public static final Identifier SCENE_DEPTH_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "scene_depth_snapshot");


    private final RenderType renderType;
    private float planeSize;

    public float getPlaneSize() { return planeSize; }
    public void setPlaneSize(final float newPlaneSize) { planeSize = newPlaneSize; }




    protected __base_SpaceWarpingRenderer(final float initialPlaneSize, final String shaderPathRoot, final String id, BlockEntityRendererProvider.Context context) {
        super();
        this.planeSize = initialPlaneSize;
        renderType = RenderType.create(
            String.format("%s:%s", EngineerSBliss.MOD_ID, id),
            RenderSetup.builder(
                RenderPipelines.register(
                    RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
                        .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("pipeline/%s",           id)))
                        .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("%s/%s", shaderPathRoot, id)))
                        .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("%s/%s", shaderPathRoot, id)))
                        .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
                        //! No depth stencil, depth is handled by the shader
                        .withSampler("Sampler0")
                        .withSampler("SceneSampler")
                        .withSampler("SceneDepthSampler")
                        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                        .withCull(false)
                    .build()
                )
            )
                .withTexture("SceneSampler",      SCENE_COLOR_ID)
                .withTexture("SceneDepthSampler", SCENE_DEPTH_ID)
                .createRenderSetup()
            )
        ;
    }




    @Override
    public void extractRenderState(
        final E blockEntity,
        final S state,
        final float tickProgress,
        final Vec3 cameraPos,
        final @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    }


    @Override
    public void submit(S state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        //! Override with no-op for custom rendering after all translucent things have been drawn.
    }




    /**
     * Must be called after SceneSnapshot has been updated for this frame or the renderer will sample a texture containing its own previous draw
     */
    public void render(
        List<S> states,
        PoseStack poseStack,
        MultiBufferSource.BufferSource bufferSource,
        CameraRenderState cameraState,
        Vec3 camPos
    ) {
        for(S state : states) {
            BlockPos blockPos = state.blockPos;
            poseStack.pushPose();
            poseStack.translate(blockPos.getX() - camPos.x + 0.5, blockPos.getY() - camPos.y + 0.5, blockPos.getZ() - camPos.z + 0.5);
            poseStack.mulPose(cameraState.orientation);

            VertexConsumer consumer = bufferSource.getBuffer(renderType);
            var pose = poseStack.last();
            final float halfPlaneSize = planeSize / 2f;
            consumer.addVertex(pose,  1f * halfPlaneSize, -1f * halfPlaneSize, 0).setUv(1, 0);
            consumer.addVertex(pose,  1f * halfPlaneSize,  1f * halfPlaneSize, 0).setUv(1, 1);
            consumer.addVertex(pose, -1f * halfPlaneSize,  1f * halfPlaneSize, 0).setUv(0, 1);
            consumer.addVertex(pose, -1f * halfPlaneSize, -1f * halfPlaneSize, 0).setUv(0, 0);

            poseStack.popPose();
        }
    }
}