package com.snek.engineersbliss.client.custom.block_entities.renderers.implementations;
import com.snek.engineersbliss.client.custom.block_entities.renderers.base.__base_SpaceWarpingRenderer;
import com.snek.engineersbliss.custom.block_entities.special.ItemSinkBlockEntity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;








public class ItemSinkBlockEntityRenderer extends __base_SpaceWarpingRenderer<ItemSinkBlockEntity, ItemSinkBlockEntityRenderer.ItemSinkRenderState> {
    public ItemSinkBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(2f, "block", "item_sink", context);
    }


    @Override
    public ItemSinkRenderState createRenderState() {
        return new ItemSinkRenderState();
    }


    public static class ItemSinkRenderState extends BlockEntityRenderState {
        // Empty. Default data is enough
    }
}