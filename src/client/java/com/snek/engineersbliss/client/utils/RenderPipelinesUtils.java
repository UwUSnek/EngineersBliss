package com.snek.engineersbliss.client.utils;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.client.renderer.RenderPipelines;

//? if <=26.1.2 {
//? } else {
    import net.minecraft.client.renderer.BindGroupLayouts;
//? }




public class RenderPipelinesUtils {
    private RenderPipelinesUtils() {}
    //? if <=26.1.2 {
        /*public static final RenderPipeline.Snippet MATRICES_PROJECTION_SNIPPET = RenderPipelines.MATRICES_PROJECTION_SNIPPET;
    *///? } else {
        public static final RenderPipeline.Snippet MATRICES_PROJECTION_SNIPPET = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .buildSnippet()
        ;
    //? }
}
