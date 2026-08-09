package com.snek.engineersbliss.client.custom.block_entities.renderers;

import java.util.List;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.custom.block_entities.special.ItemSinkBlockEntity;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
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
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;








public class ItemSinkBlockEntityRenderer implements BlockEntityRenderer<ItemSinkBlockEntity, ItemSinkBlockEntityRenderer.ItemSinkRenderState> {
    public static final Identifier SCENE_COLOR_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "scene_color_snapshot");
    public static final Identifier SCENE_DEPTH_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "scene_depth_snapshot");


    public static final RenderPipeline ITEM_SINK_PIPELINE = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/item_sink"))
            .withVertexShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "block/item_sink"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "block/item_sink"))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
            // .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, true))
            //! No depth stencil, depth is handles by the shader
            .withSampler("Sampler0")
            .withSampler("SceneSampler")
            .withSampler("SceneDepthSampler")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .build()
        )
    ;

    public static final RenderType ITEM_SINK_RENDER_TYPE = RenderType.create(
        EngineerSBliss.MOD_ID + ":item_sink",
        RenderSetup.builder(ITEM_SINK_PIPELINE)
            .withTexture("SceneSampler", SCENE_COLOR_ID)
            .withTexture("SceneDepthSampler", SCENE_DEPTH_ID)
            .createRenderSetup()
        )
    ;


    public ItemSinkBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }


    @Override
    public ItemSinkRenderState createRenderState() {
        return new ItemSinkRenderState();
    }


    @Override
    public void extractRenderState(
        ItemSinkBlockEntity blockEntity,
        ItemSinkRenderState state,
        float tickProgress,
        Vec3 cameraPos,
        @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    }


    @Override
    public void submit(ItemSinkRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        //! Override with no-op for custom rendering after all translucent things have been drawn.
    }


    /**
     * Must be called after SceneSnapshot has been updated for this frame or the renderer will sample a texture containing its own previous draw
     */
    public static void renderDirect(
        List<ItemSinkRenderState> states,
        PoseStack poseStack,
        MultiBufferSource.BufferSource bufferSource,
        CameraRenderState cameraState,
        Vec3 camPos
    ) {
        for(ItemSinkRenderState state : states) {
            BlockPos blockPos = state.blockPos;
            poseStack.pushPose();
            poseStack.translate(blockPos.getX() - camPos.x + 0.5, blockPos.getY() - camPos.y + 0.5, blockPos.getZ() - camPos.z + 0.5);
            poseStack.mulPose(cameraState.orientation);

            VertexConsumer consumer = bufferSource.getBuffer(ITEM_SINK_RENDER_TYPE);
            var pose = poseStack.last();
            consumer.addVertex(pose,  1f, -1f, 0).setUv(1, 0);
            consumer.addVertex(pose,  1f,  1f, 0).setUv(1, 1);
            consumer.addVertex(pose, -1f,  1f, 0).setUv(0, 1);
            consumer.addVertex(pose, -1f, -1f, 0).setUv(0, 0);

            poseStack.popPose();
        }
    }


    public static class ItemSinkRenderState extends BlockEntityRenderState {
        // Empty. Default data is enough
    }
}