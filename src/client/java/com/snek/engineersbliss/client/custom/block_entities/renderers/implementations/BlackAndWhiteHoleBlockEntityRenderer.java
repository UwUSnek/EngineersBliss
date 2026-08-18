package com.snek.engineersbliss.client.custom.block_entities.renderers.implementations;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.custom.block_entities.renderers.base.__base_SpaceWarpingRenderer;
import com.snek.engineersbliss.custom.block_entities.base.CosmeticBlackAndWhiteHoleBlockEntity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;








public class BlackAndWhiteHoleBlockEntityRenderer extends __base_SpaceWarpingRenderer<BlockEntity, __base_SpaceWarpingRenderer.__base_SpaceWarpingRenderState> {
    public BlackAndWhiteHoleBlockEntityRenderer(final String shaderPathRoot, final String id, BlockEntityRendererProvider.Context context) {
        super(shaderPathRoot, id, context);
    }


    @Override
    public float calcPlaneSize(final BlockEntity entity) {
        if(entity instanceof final @NotNull CosmeticBlackAndWhiteHoleBlockEntity e) {
            return e.getSize() * 4f;
        }
        else {
            return 4f;
        }
    }


    @Override
    public __base_SpaceWarpingRenderState createRenderState() {
        return new __base_SpaceWarpingRenderState();
    }
}