package com.snek.engineersbliss.client.utils.textures.mp4;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;
import com.snek.engineersbliss.utils.scheduler.TaskHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

import java.nio.IntBuffer;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;




final class Mp4Player {
    private static final long TARGET_FRAME_MS = 1_000L / 30;

    private final Identifier     textureId;
    private final Mp4FrameSource source;
    private       DynamicTexture texture; //! Effectively final. Assigned from the main thread.
    private       NativeImage    image;   //! Effectively final. Assigned from the main thread.
    private long lastFrameMs = 0;


    //! Mp4Player is always created asynchronously. GPU-related operations need to be executed on the main thread.
    Mp4Player(final Identifier id, final Path path) {
        source    = new Mp4FrameSource(path);
        textureId = id.withSuffix(".video");
        final @NotNull TaskHandler gpuTasksHandler = ClientScheduler.run(() -> {
            image     = new NativeImage(source.getWidth(), source.getHeight(), false);
            image.fillRect(0, 0, source.getWidth(), source.getHeight(), 0x00000000); //! Clear junk texture data
            //TODO use placeholder texture here too ^ so there isn't a visible "nothing" gap between loading and video
            texture   = new DynamicTexture(textureId::toString, image);
            Minecraft.getInstance().getTextureManager().register(textureId, texture);
        });
        while(!gpuTasksHandler.isComplete()) {
            try { Thread.sleep(10); } catch(final InterruptedException _) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        source.start();
    }


    Identifier getTextureId() {
        return textureId;
    }


    void tick() {
        final long now = System.currentTimeMillis();
        if(now - lastFrameMs < TARGET_FRAME_MS) return;

        final IntBuffer agbr = source.takeFrame();
        if(agbr == null) return;

        writeFrame(agbr);
        texture.upload();
        lastFrameMs = now;
    }


    // Direct bulk memory copy from the int buffer [ABGR] to the output NativeImage [ABGR]
    private void writeFrame(final IntBuffer abgr) {
        MemoryUtil.memCopy(MemoryUtil.memAddress(abgr), image.getPointer(), (long)abgr.capacity() * 4);
        //! Buffers are reused and must NOT be freed manually.
    }


    void close() {
        source.close();
        Minecraft.getInstance().getTextureManager().release(textureId);
        image.close();
    }
}