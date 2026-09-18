package com.snek.engineersbliss.client.ui.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.RenderPipelinesUtils;
import com.mojang.blaze3d.shaders.UniformType;

//? if <=26.1.2 {
    /*import com.mojang.blaze3d.vertex.VertexFormat;
*///? } else {
    import net.minecraft.client.renderer.BindGroupLayouts;
    import com.mojang.blaze3d.PrimitiveTopology;
    import com.mojang.blaze3d.pipeline.BindGroupLayout;
//? }
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;








/**
 * Custom render pipelines for UI elements.
 */
public final class UiRenderPipelines {
    private UiRenderPipelines() {}


    public static final RenderPipeline.Snippet GUI_BASE = RenderPipeline.builder(RenderPipelinesUtils.MATRICES_PROJECTION_SNIPPET)
        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))      // Translucent is required for transparent pixels.
        .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false)) // No depth testing, no depth writes.
        .withCull(false)                                                            // Makes UI rendering more reliable.
        .buildSnippet()
    ;




    public static final RenderPipeline AA_FILL = RenderPipelines.register(
        RenderPipeline.builder(GUI_BASE)
            .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/aa_fill"))
            .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_fill"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_fill"))
            //? if <=26.1.2 {
                /*.withVertexFormat(UiVertexFormats.AA_FILL, VertexFormat.Mode.QUADS)
            *///? } else {
                .withVertexBinding(0, UiVertexFormats.AA_FILL)
                .withPrimitiveTopology(PrimitiveTopology.QUADS)
            //? }
            .build()
        )
    ;
    public static final RenderPipeline AA_BLIT = RenderPipelines.register(
        RenderPipeline.builder(GUI_BASE)
            .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/aa_blit"))
            .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_blit"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_blit"))
            //? if <=26.1.2 {
                /*.withVertexFormat(UiVertexFormats.AA_BLIT, VertexFormat.Mode.QUADS)
                .withSampler("Sampler0")
            *///? } else {
                .withVertexBinding(0, UiVertexFormats.AA_BLIT)
                .withPrimitiveTopology(PrimitiveTopology.QUADS)
                .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            //? }
            .build()
        )
    ;
    public static final RenderPipeline AA_MULTILINE = RenderPipelines.register(
        RenderPipeline.builder(GUI_BASE)
            .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/aa_multiline"))
            .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_multiline"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_multiline"))
            //? if <=26.1.2 {
                /*.withVertexFormat(UiVertexFormats.AA_MULTILINE, VertexFormat.Mode.QUADS)
            *///? } else {
                .withVertexBinding(0, UiVertexFormats.AA_MULTILINE)
                .withPrimitiveTopology(PrimitiveTopology.QUADS)
            //? }
            .build()
        )
    ;
    public static final RenderPipeline MULTILINE_AREA = RenderPipelines.register(
        RenderPipeline.builder(GUI_BASE)
            .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/multiline_area"))
            .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/multiline_area"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/multiline_area"))
            //? if <=26.1.2 {
                /*.withVertexFormat(UiVertexFormats.MULTILINE_AREA, VertexFormat.Mode.QUADS)
            *///? } else {
                .withVertexBinding(0, UiVertexFormats.MULTILINE_AREA)
                .withPrimitiveTopology(PrimitiveTopology.QUADS)
            //? }
            .build()
        )
    ;




    public static final RenderPipeline AA_BLUR = RenderPipelines.register(
        RenderPipeline.builder(GUI_BASE)
            .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/aa_blur"))
            .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_blit"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_blur"))
            //? if <=26.1.2 {
                /*.withVertexFormat(UiVertexFormats.AA_BLIT, VertexFormat.Mode.QUADS)
                .withSampler("Sampler0")
            *///? } else {
                .withVertexBinding(0, UiVertexFormats.AA_BLIT)
                .withPrimitiveTopology(PrimitiveTopology.QUADS)
                .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            //? }
            .build()
        )
    ;
    public static final RenderPipeline GAUSSIAN_BLUR = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.POST_PROCESSING_SNIPPET)
            .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/gaussian_blur"))
            .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/blur"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/blur"))
            //? if <=26.1.2 {
                /*.withSampler("InSampler")
                .withUniform("BlurConfig", UniformType.UNIFORM_BUFFER)
            *///? } else {
                .withBindGroupLayout(BindGroupLayout.builder()
                    .withSampler("InSampler")
                    .withUniform("BlurConfig", UniformType.UNIFORM_BUFFER)
                    .build()
                )
            //? }
            .build()
        )
    ;




    public static void init() {
        // Empty, this starts the static initialization
    }
}