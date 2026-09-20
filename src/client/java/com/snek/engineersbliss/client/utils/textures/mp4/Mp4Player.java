package com.snek.engineersbliss.client.utils.textures.mp4;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;




final class Mp4Player {
    private static final long TARGET_FRAME_NANOS = 1_000_000_000L / 30;

    private final Identifier       textureId;
    private final Mp4FrameSource source;
    private final DynamicTexture   texture;
    private final NativeImage      image;

    private long lastFrameNanos = 0;


    Mp4Player(final Identifier id, final Path path) {
        source    = new Mp4FrameSource(path);
        textureId = id.withSuffix(".video");
        image     = new NativeImage(source.getWidth(), source.getHeight(), false);
        texture   = new DynamicTexture(textureId::toString, image);

        Minecraft.getInstance().getTextureManager().register(textureId, texture);
        source.start();
    }


    Identifier getTextureId() {
        return textureId;
    }


    void tick() {
        final long now = System.nanoTime();
        if(now - lastFrameNanos < TARGET_FRAME_NANOS) return;

        final int[] argb = source.takeFrame();
        if(argb == null) return;

        writeFrame(argb);
        texture.upload();
        lastFrameNanos = now;
    }


    private void writeFrame(final int[] argb) {
        final int w = image.getWidth(), h = image.getHeight();
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                final int c = argb[y * w + x];
                final int a = (c >>> 24) & 0xFF;
                final int r = (c >>> 16) & 0xFF;
                final int g = (c >>>  8) & 0xFF;
                final int b =  c         & 0xFF;
                image.setPixel(x, y, (a << 24) | (b << 16) | (g << 8) | r);
            }
        }
    }


    void close() {
        source.close();
        Minecraft.getInstance().getTextureManager().release(textureId);
        image.close();
    }
}