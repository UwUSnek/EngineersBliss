package com.snek.engineersbliss.client.ui.renderer;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;




public final class UiVertexFormats {
    private UiVertexFormats() {}


    public static final VertexFormat AA_FILL = VertexFormat.builder()
        .add("Position", VertexFormatElement.POSITION)
        .add("Color",    VertexFormatElement.COLOR)
        .add("UV0",      VertexFormatElement.UV0)
        .add("UV1",      VertexFormatElement.UV1)
    .build();
    public static final VertexFormat AA_BLIT = VertexFormat.builder()
        .add("Position", VertexFormatElement.POSITION)
        .add("LineWidth",VertexFormatElement.LINE_WIDTH)
        .add("UV0",      VertexFormatElement.UV0)
        .add("UV1",      VertexFormatElement.UV1)
        .add("UV2",      VertexFormatElement.UV2)
        .add("Color",    VertexFormatElement.COLOR)
    .build();

    public static void init() {
        // Empty, starts static init
    }
}