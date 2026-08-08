package com.snek.engineersbliss.client.custom.block_entities.renderers;

import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;




public class SceneSnapshot {
    private static GpuTexture colorSnapshot;
    private static GpuTexture depthSnapshot;

    public static void init(int width, int height) {
        GpuDevice device = RenderSystem.getDevice();
        colorSnapshot = device.createTexture(
            "scene_color_snapshot",
            GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING,
            TextureFormat.RGBA8, width, height, 1, 1
        );
        depthSnapshot = device.createTexture(
            "scene_depth_snapshot",
            GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING,
            TextureFormat.DEPTH32, width, height, 1, 1
        );
    }

    public static void resize(int width, int height) {
        colorSnapshot.close();
        depthSnapshot.close();
        init(width, height);
    }

    public static GpuTexture getColor() { return colorSnapshot; }
    public static GpuTexture getDepth() { return depthSnapshot; }
}