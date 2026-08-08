package com.snek.engineersbliss.client.custom.block_entities.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;

import net.minecraft.client.renderer.texture.AbstractTexture;




public class SceneSnapshotTexture extends AbstractTexture {
    private final GpuTextureView view;

    public SceneSnapshotTexture(GpuTexture backing) {
        this.view = RenderSystem.getDevice().createTextureView(backing);
    }

    @Override
    public GpuTextureView getTextureView() { return view; }

    @Override
    public GpuSampler getSampler() {
        return RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
    }
}