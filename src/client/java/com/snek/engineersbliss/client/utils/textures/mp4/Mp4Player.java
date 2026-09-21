package com.snek.engineersbliss.client.utils.textures.mp4;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.utils.Layout;
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
    private boolean initialized;
    private long lastFrameMs;


    //! Mp4Player is always created asynchronously. GPU-related operations need to be executed on the main thread.
    Mp4Player(final Identifier id, final Path path) {
        source    = new Mp4FrameSource(path);
        textureId = id.withSuffix(".video");
        this.initialized = false;
        this.lastFrameMs = 0;
        final @NotNull TaskHandler gpuTasksHandler = ClientScheduler.run(() -> {
            image     = new NativeImage(source.getWidth(), source.getHeight(), false); //! Contains junk data. "Loading" texture ID is returned until initialized.
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
        return initialized ? textureId : Layout.PLACEHOLDER_TEXTURE_ID;
    }


    void tick() {
        final long now = System.currentTimeMillis();
        if(now - lastFrameMs < TARGET_FRAME_MS) return;

        final IntBuffer agbr = source.takeFrame();
        if(agbr == null) return;

        writeFrame(agbr);
        texture.upload();
        lastFrameMs = now;
        initialized = true;
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