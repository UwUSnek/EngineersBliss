package com.snek.engineersbliss.client.custom.block_entities.renderers.implementations;
import com.snek.engineersbliss.client.custom.block_entities.renderers.base.__base_SpaceWarpingRenderer;
import com.snek.engineersbliss.custom.block_entities.special.ItemSourceBlockEntity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;








public class ItemSourceBlockEntityRenderer extends __base_SpaceWarpingRenderer<ItemSourceBlockEntity, ItemSourceBlockEntityRenderer.ItemSourceRenderState> {
    public ItemSourceBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(4f, "block", "item_source", context);
    }


    @Override
    public ItemSourceRenderState createRenderState() {
        return new ItemSourceRenderState();
    }


    public static class ItemSourceRenderState extends BlockEntityRenderState {
        // Empty. Default data is enough
    }
}