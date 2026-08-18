package com.snek.engineersbliss.client.custom.block_entities.renderers.base;

import java.nio.ByteBuffer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;








public abstract class __base_CustomBlockEntityRenderer<E extends BlockEntity, S extends BlockEntityRenderState> implements BlockEntityRenderer<E, S> {
    protected __base_CustomBlockEntityRenderer() {
        super();
    }


    @Override
    public int getViewDistance() {
        return Integer.MAX_VALUE;
    }


    protected static void writeVertex(ByteBuffer buf, PoseStack.Pose pose, float x, float y, float z, float u, float v) {
        org.joml.Vector3f pos = new org.joml.Vector3f(x, y, z);
        pose.pose().transformPosition(pos);
        buf.putFloat(pos.x).putFloat(pos.y).putFloat(pos.z);
        buf.putFloat(u).putFloat(v);
    }
}
