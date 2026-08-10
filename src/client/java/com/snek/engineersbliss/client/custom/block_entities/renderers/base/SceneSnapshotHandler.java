package com.snek.engineersbliss.client.custom.block_entities.renderers.base;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;








public class SceneSnapshotHandler {
    private SceneSnapshotHandler() {}
    private static GpuTexture colorSnapshot;
    private static GpuTexture depthSnapshot;




    /**
     * Registers the event to handle the textures' first creation.
     * This must be called from the client mod initializer function.
     */
    public static void register() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            init(client.getWindow().getWidth(), client.getWindow().getHeight());
        });
    }




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


        // Bind textures. This must be done on startup and after every resize.
        final @NotNull TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        textureManager.register(__base_SpaceWarpingRenderer.SCENE_COLOR_ID, new SceneSnapshotTexture(getColor()));
        textureManager.register(__base_SpaceWarpingRenderer.SCENE_DEPTH_ID, new SceneSnapshotTexture(getDepth()));
    }




    public static void resize(int width, int height) {
        colorSnapshot.close();
        depthSnapshot.close();
        init(width, height);
    }

    public static GpuTexture getColor() { return colorSnapshot; }
    public static GpuTexture getDepth() { return depthSnapshot; }
}