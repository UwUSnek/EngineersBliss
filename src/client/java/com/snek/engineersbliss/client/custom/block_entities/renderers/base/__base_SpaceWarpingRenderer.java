package com.snek.engineersbliss.client.custom.block_entities.renderers.base;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
//? if <=26.1.2 {
    // import java.util.OptionalInt;
//? } else {
//? }

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.RenderPipelinesUtils;

//? if <=26.1.2 {
//? } else {
    import com.mojang.blaze3d.PrimitiveTopology;
    import com.mojang.blaze3d.pipeline.BindGroupLayout;
    import com.mojang.blaze3d.IndexType;
//? }
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
//? if <=26.1.2 {
    // import com.mojang.blaze3d.vertex.VertexFormat;
//? } else {
    import net.minecraft.client.Minecraft;
    import net.minecraft.client.renderer.rendertype.PreparedRenderType;
    import net.minecraft.client.renderer.BindGroupLayouts;
//? }
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;








public abstract class __base_SpaceWarpingRenderer<E extends BlockEntity, S extends __base_SpaceWarpingRenderer.__base_SpaceWarpingRenderState> extends __base_CustomBlockEntityRenderer<E, S> {
    public static final Identifier SCENE_COLOR_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "scene_color_snapshot");
    public static final Identifier SCENE_DEPTH_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "scene_depth_snapshot");
    //? if <=26.1.2 {
    //? } else {
        public static final BindGroupLayout SCENE_SAMPLER       = BindGroupLayout.builder().withSampler("SceneSampler").build();
        public static final BindGroupLayout SCENE_DEPTH_SAMPLER = BindGroupLayout.builder().withSampler("SceneDepthSampler").build();
        public static final BindGroupLayout PLANE_SIZE_DATA     = BindGroupLayout.builder().withUniform("PlaneSizeData", UniformType.UNIFORM_BUFFER).build();
    //? }


    public abstract float calcPlaneSize(BlockEntity e);




    private final RenderSetup renderSetup;
    private final RenderPipeline renderPipeline;
    protected __base_SpaceWarpingRenderer(final String shaderPathRoot, final String id, BlockEntityRendererProvider.Context context) {
        super();
        RenderType.create(
            String.format("%s:%s", EngineerSBliss.MOD_ID, id),
            renderSetup = RenderSetup.builder(
                renderPipeline = RenderPipelines.register(
                    RenderPipeline.builder(RenderPipelinesUtils.MATRICES_PROJECTION_SNIPPET)
                        .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("pipeline/%s",           id)))
                        .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("%s/%s", shaderPathRoot, id)))
                        .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("%s/%s", shaderPathRoot, id)))
                        //? if <=26.1.2 {
                            // .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
                            // .withSampler("Sampler0")
                            // .withSampler("SceneSampler")
                            // .withSampler("SceneDepthSampler")
                            // .withUniform("PlaneSizeData", UniformType.UNIFORM_BUFFER)
                        //? } else {
                            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
                            .withPrimitiveTopology(PrimitiveTopology.QUADS)
                            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
                            .withBindGroupLayout(SCENE_SAMPLER)
                            .withBindGroupLayout(SCENE_DEPTH_SAMPLER)
                            .withBindGroupLayout(PLANE_SIZE_DATA)
                        //? }
                        //! No depth stencil, depth is handled by the shader
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
        super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.planeSize = calcPlaneSize(blockEntity);
    }


    @Override
    public void submit(S state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        //! Override with no-op for custom rendering after all translucent things have been drawn.
    }




    public void render(
        List<S> states,
        PoseStack poseStack,
        CameraRenderState cameraState,
        Vec3 camPos,
        CommandEncoder encoder,
        GpuTextureView colorTarget,
        GpuTextureView depthTarget
    ) {
        for(S state : states) {
            BlockPos blockPos = state.blockPos;
            poseStack.pushPose();
            poseStack.translate(blockPos.getX() - camPos.x + 0.5, blockPos.getY() - camPos.y + 0.5, blockPos.getZ() - camPos.z + 0.5);
            poseStack.mulPose(cameraState.orientation);
            var pose = poseStack.last();
            final float h = state.planeSize / 2f;

            // Build vertex bytes. POSITION_TEX = vec3 pos + vec2 uv = 20 bytes/vertex
            ByteBuffer vertexData = MemoryUtil.memAlloc(4 * 20);
            writeVertex(vertexData, pose,  h, -h, 0, 1, 0);
            writeVertex(vertexData, pose,  h,  h, 0, 1, 1);
            writeVertex(vertexData, pose, -h,  h, 0, 0, 1);
            writeVertex(vertexData, pose, -h, -h, 0, 0, 0);
            vertexData.flip();

            GpuBuffer vertices;
            try {
                vertices = RenderSystem.getDevice().createBuffer(() -> "space_warp_vertices", GpuBuffer.USAGE_VERTEX, vertexData);
            }
            finally {
                MemoryUtil.memFree(vertexData);
            }

            // Reuse vanilla's shared quad index buffer
            //? if <=26.1.2 {
                // RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
                // GpuBuffer indices = autoIndices.getBuffer(6);
                // VertexFormat.IndexType indexType = autoIndices.type();
            //? } else {
                RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
                GpuBuffer indices = autoIndices.getBuffer(6);
                IndexType indexType = autoIndices.type();
            //? }

            // Uniforms
            GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(
                //? if <=26.1.2 {
                    // RenderSystem.getModelViewMatrix(),
                //? } else {
                    RenderSystem.getModelViewMatrixCopy(),
                //? }
                new Vector4f(1,1,1,1),
                new Vector3f(),
                new Matrix4f()
            );
            GpuBufferSlice planeSizeSlice = PlaneSizeUniforms.INSTANCE.write(state.planeSize);

            try(RenderPass renderPass = encoder.createRenderPass(() ->
                "space_warp_draw",
                colorTarget,
                //? if <=26.1.2 {
                    // OptionalInt.empty(),
                //? } else {
                    Optional.empty(),
                //? }
                depthTarget,
                OptionalDouble.empty()
            )) {
                renderPass.setPipeline(renderPipeline);
                RenderSystem.bindDefaultUniforms(renderPass);
                renderPass.setUniform("DynamicTransforms", dynamicTransforms);
                renderPass.setUniform("PlaneSizeData", planeSizeSlice);

                //? if <=26.1.2 {
                    // for(var entry : renderSetup.getTextures().entrySet()) {
                    //     renderPass.bindTexture(entry.getKey(), entry.getValue().textureView(), entry.getValue().sampler());
                    // }
                    // renderPass.setVertexBuffer(0, vertices);
                    // renderPass.setIndexBuffer(indices, indexType);
                    // renderPass.drawIndexed(0, 0, 6, 1);
                //? } else {
                    List<PreparedRenderType.Texture> preparedTextures = renderSetup.prepareTextures(
                        Minecraft.getInstance().getTextureManager(),
                        RenderSystem.getSamplerCache(),
                        null,  // overlayTexture
                        null   // lightmapTexture
                    );
                    for(PreparedRenderType.Texture tex : preparedTextures) {
                        renderPass.bindTexture(tex.name(), tex.textureView(), tex.sampler());
                    }
                    renderPass.setVertexBuffer(0, vertices.slice());
                    renderPass.setIndexBuffer(indices, indexType);
                    renderPass.drawIndexed(6, 1, 0, 0, 0);
                //? }

            }
            catch(Exception e) {
                EngineerSBliss.LOGGER.error("Couldn't create render pass.", e);
            }

            vertices.close();
            poseStack.popPose();
        }
    }



    public static class __base_SpaceWarpingRenderState extends BlockEntityRenderState {
        public float planeSize;
    }
}