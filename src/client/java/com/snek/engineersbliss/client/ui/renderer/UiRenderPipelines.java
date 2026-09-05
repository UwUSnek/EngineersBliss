package com.snek.engineersbliss.client.ui.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;



/**
 * Custom render pipelines for UI elements.
 */
public final class UiRenderPipelines {
    private UiRenderPipelines() {}


    public static final RenderPipeline AA_FILL = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/aa_fill"))
            .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_fill"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_fill"))
            .withVertexFormat(UiVertexFormats.AA_FILL, VertexFormat.Mode.QUADS)         // Custom antialiased rect data.
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))      // Translucent is required for transparent pixels.
            .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false)) // No depth testing, no depth writes.
            .withCull(false)                                                            // Makes UI rendering more reliable.
            .build()
        )
    ;
    public static final RenderPipeline AA_BLIT = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withLocation      (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pipeline/aa_blit"))
            .withVertexShader  (Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_blit"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "ui/aa_blit"))
            .withVertexFormat(UiVertexFormats.AA_BLIT, VertexFormat.Mode.QUADS)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
            .withCull(false)
            .withSampler("Sampler0")
            .build()
        )
    ;

    public static void init() {
        // Empty, this starts the static initialization
    }
}