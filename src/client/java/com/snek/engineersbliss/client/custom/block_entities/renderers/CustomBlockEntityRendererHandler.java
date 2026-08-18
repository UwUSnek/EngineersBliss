package com.snek.engineersbliss.client.custom.block_entities.renderers;

import com.snek.engineersbliss.client.custom.block_entities.renderers.implementations.BlackAndWhiteHoleBlockEntityRenderer;
import com.snek.engineersbliss.custom.block_entities.CustomBlockEntityHandler;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;




public class CustomBlockEntityRendererHandler {
    private CustomBlockEntityRendererHandler() {}
    public static void register() {
        BlockEntityRenderers.register(CustomBlockEntityHandler.COSMETIC_BLACK_HOLE, c -> new BlackAndWhiteHoleBlockEntityRenderer("block", "black_hole", c));
        BlockEntityRenderers.register(CustomBlockEntityHandler.COSMETIC_WHITE_HOLE, c -> new BlackAndWhiteHoleBlockEntityRenderer("block", "white_hole", c));
        BlockEntityRenderers.register(CustomBlockEntityHandler.ITEM_SINK,           c -> new BlackAndWhiteHoleBlockEntityRenderer("block", "black_hole", c));
        BlockEntityRenderers.register(CustomBlockEntityHandler.ITEM_SOURCE,         c -> new BlackAndWhiteHoleBlockEntityRenderer("block", "white_hole", c));
    }
}
