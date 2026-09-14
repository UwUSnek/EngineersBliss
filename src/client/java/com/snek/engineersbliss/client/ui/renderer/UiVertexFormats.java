package com.snek.engineersbliss.client.ui.renderer;

import com.mojang.blaze3d.vertex.VertexFormat;
//? if <=26.1.2 {
    import com.mojang.blaze3d.vertex.VertexFormatElement;
//? } else {
    /*import com.mojang.blaze3d.GpuFormat;
*///? }




public final class UiVertexFormats {
    private UiVertexFormats() {}


    //? if <=26.1.2 {
        public static final VertexFormat AA_FILL = VertexFormat.builder()
            .add("Position",  VertexFormatElement.POSITION)
            .add("LineWidth", VertexFormatElement.LINE_WIDTH)
            .add("UV0",       VertexFormatElement.UV0)
            .add("Color",     VertexFormatElement.COLOR)
        .build();
        public static final VertexFormat AA_BLIT = VertexFormat.builder()
            .add("Position",  VertexFormatElement.POSITION)
            .add("LineWidth", VertexFormatElement.LINE_WIDTH)
            .add("UV0",       VertexFormatElement.UV0)
            .add("UV1",       VertexFormatElement.UV1)
            .add("UV2",       VertexFormatElement.UV2)
            .add("Color",     VertexFormatElement.COLOR)
        .build();
        public static final VertexFormat AA_MULTILINE = VertexFormat.builder()
            .add("Position",  VertexFormatElement.POSITION)
            .add("LineWidth", VertexFormatElement.LINE_WIDTH)
            .add("UV0",       VertexFormatElement.UV0)
            .add("Color",     VertexFormatElement.COLOR)
        .build();
        public static final VertexFormat MULTILINE_AREA = VertexFormat.builder()
            .add("Position",  VertexFormatElement.POSITION)
            .add("Color",     VertexFormatElement.COLOR)
        .build();
    //? } else {
        /*//FIXME maybe use proper formats instead of smuggling stuff through the existing bindings?
        //FIXME idk if 26.2 actually supports that. It looks like it does. Might still be binding stuff to the hard coded names though.
        public static final VertexFormat AA_FILL = VertexFormat.builder(0)
            .addAttribute("Position",  GpuFormat.RGB32_FLOAT)
            .addAttribute("LineWidth", GpuFormat.R32_FLOAT)
            .addAttribute("UV0",       GpuFormat.RG32_FLOAT)
            .addAttribute("Color",     GpuFormat.RGBA8_UNORM)
        .build();

        public static final VertexFormat AA_BLIT = VertexFormat.builder(0)
            .addAttribute("Position",  GpuFormat.RGB32_FLOAT)
            .addAttribute("LineWidth", GpuFormat.R32_FLOAT)
            .addAttribute("UV0",       GpuFormat.RG32_FLOAT)
            .addAttribute("UV1",       GpuFormat.RG16_SINT)
            .addAttribute("UV2",       GpuFormat.RG16_SINT)
            .addAttribute("Color",     GpuFormat.RGBA8_UNORM)
        .build();

        public static final VertexFormat AA_MULTILINE = VertexFormat.builder(0)
            .addAttribute("Position",  GpuFormat.RGB32_FLOAT)
            .addAttribute("LineWidth", GpuFormat.R32_FLOAT)
            .addAttribute("UV0",       GpuFormat.RG32_FLOAT)
            .addAttribute("Color",     GpuFormat.RGBA8_UNORM)
        .build();

        public static final VertexFormat MULTILINE_AREA = VertexFormat.builder(0)
            .addAttribute("Position",  GpuFormat.RGB32_FLOAT)
            .addAttribute("Color",     GpuFormat.RGBA8_UNORM)
        .build();
    *///? }


    public static void init() {
        // Empty, starts static init
    }
}