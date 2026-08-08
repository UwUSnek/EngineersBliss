package com.snek.engineersbliss.client.custom.block_entities.renderers;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.custom.block_entities.special.ItemSinkBlockEntity;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

// assets/engineersbliss/shaders/item_sink.vsh
// assets/engineersbliss/shaders/item_sink.fsh
public class ItemSinkBlockEntityRenderer
        implements BlockEntityRenderer<ItemSinkBlockEntity, ItemSinkBlockEntityRenderer.ItemSinkRenderState> {

    public static final RenderPipeline ITEM_SINK_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.END_PORTAL_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/item_sink"))
                    .withVertexShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "item_sink"))
                    .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "item_sink"))
                    .build());

    public static final RenderType ITEM_SINK_RENDER_TYPE = RenderType.create(
            EngineerSBliss.MOD_ID + ":item_sink",
            RenderSetup.builder(ITEM_SINK_PIPELINE).createRenderSetup());

    public ItemSinkBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
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
            @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    }

    @Override
    public void submit(ItemSinkRenderState state, PoseStack matrices, SubmitNodeCollector queue,
            CameraRenderState cameraState) {
        matrices.pushPose();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.mulPose(cameraState.orientation);

        queue.submitCustomGeometry(matrices, ITEM_SINK_RENDER_TYPE, (pose, consumer) -> {
            // consumer.addVertex(pose, -0.5f, -0.5f, 0);
            // consumer.addVertex(pose, -0.5f, 0.5f, 0);
            // consumer.addVertex(pose, 0.5f, 0.5f, 0);
            // consumer.addVertex(pose, 0.5f, -0.5f, 0);

            consumer.addVertex(pose, 0.5f, -0.5f, 0);
            consumer.addVertex(pose, 0.5f, 0.5f, 0);
            consumer.addVertex(pose, -0.5f, 0.5f, 0);
            consumer.addVertex(pose, -0.5f, -0.5f, 0);
        });

        matrices.popPose();
    }

    public static class ItemSinkRenderState extends BlockEntityRenderState {
    }
}