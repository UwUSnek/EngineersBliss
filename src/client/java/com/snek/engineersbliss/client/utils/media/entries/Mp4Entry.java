package com.snek.engineersbliss.client.utils.media.entries;

import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.utils.media.MediaTracker;
import com.snek.engineersbliss.client.utils.media.support.Mp4FrameSource;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;
import com.snek.engineersbliss.utils.scheduler.TaskHandler;








//TODO videoPlayer.close();
//TODO frameSource.close();
//TODO private static final long STALE_MS = 10000L;
//TODO maybe close players afte a while? idk. prob not required? they shouldn't use much ram or cpu anyway.
//TODO though it is still one thread on wait per video.

public class Mp4Entry extends __base_MediaEntry {
    private static final long INACTIVITY_RESET_MS = 10_000;
    private static final long TARGET_FRAME_MS = 1_000L / 30;

    //! Width and height are updated asynchronously after reading the first frame of the MP4.
    //! this.initialized gates everything.

    // Video tracking
    private AtomicBoolean textureInitialized;
    private AtomicBoolean frameSourceInitialized;
    private long lastFrameMs;
    private Mp4FrameSource frameSource; //! Effectively final. Assigned asynchronously from a worker thread.
    private DynamicTexture texture;     //! Effectively final. Assigned asynchronously from the main thread.
    private NativeImage    image;       //! Effectively final. Assigned asynchronously from the main thread.


    // Video data & player instance
    // final Path filePath;  //TODO remove
    // Mp4Player  videoPlayer;
    // long       lastRequestTime; //TODO remove
    // boolean    currentlyLoading;  //TODO remove
    // //! currentlyLoading TRUE means "The player doesn't exist yet and a thread is currently taking care of that. Don't try to create a new one." //TODO remove

    private Identifier calcTextureId() {
        return getResourceId().withPrefix("media_tracker.mp4.");
    }




    /**
     * Advances the current video by 1 frame and updates the initialization state.
     * Also resets the video if past the inactivity time limit.
     * Does nothing if the frameSource is not available yet.
     */
    private void advanceFrame() {
        if(!frameSourceInitialized.get()) return;

        // Retrieve current timestamp. Reset the video if past INACTIVITY_RESET_MS, otherwise return if the next frame hasn't been reached yet.
        //! This doesn't reset the video instantly but it does detect past inactivity. The video is reset before the first frame after inactivity is played.
        final long now = System.currentTimeMillis();
        final long elapsed = now - lastFrameMs;
        if(elapsed > INACTIVITY_RESET_MS) frameSource.requestReset();
        if(elapsed < TARGET_FRAME_MS) return;

        // Retrieve new frame
        final IntBuffer abgr = frameSource.takeFrame();
        if(abgr == null) return;

        // Copy the texture to memory directly for better performance and upload it to the GPU.
        MemoryUtil.memCopy(MemoryUtil.memAddress(abgr), image.getPointer(), (long)abgr.capacity() * 4);
        texture.upload();
        lastFrameMs = now;
        textureInitialized.set(true);
    }
    public void close() { //FIXME call this from somewhere when the video isnt needed anymore? idk when that would be though
        //FIXME wait for full initialization before trying to close stuff
        frameSource.close();
        Minecraft.getInstance().getTextureManager().release(calcTextureId());
        image.close();
    }




    @Override
    public @Nullable Identifier __internal_requestTextureFor(int desiredWidth, int desiredHeight) {
        if(!textureInitialized.get()) return MediaTracker.PLACEHOLDER_TEXTURE_ID;
        final long now = System.currentTimeMillis();

        // Advance frame
        advanceFrame();
        return calcTextureId();
    }




    public Mp4Entry(final Identifier resourceId, final Path filePath) {
        super(resourceId, 0, 0);
        // this.filePath         = path; //TODO remove
        // this.videoPlayer      = null;
        // this.lastRequestTime  = 0;
        // this.currentlyLoading = false;


        // Create frame source asynchronously (cpu intensive) and mark as initialized. //! GPU-related operations need to be executed on the main thread.
        // Worker thread waits for the main thread to populate size, image, and texture,
        this.textureInitialized     = new AtomicBoolean(false);
        this.frameSourceInitialized = new AtomicBoolean(false);
        this.lastFrameMs = 0;
        CompletableFuture.runAsync(() -> {
            frameSource = new Mp4FrameSource(filePath);
            final @NotNull TaskHandler gpuTasksHandler = ClientScheduler.run(() -> {
                final Identifier textureId = calcTextureId();
                updateSize(frameSource.getWidth(), frameSource.getHeight());
                image     = new NativeImage(getWidth(), getHeight(), false); //! Contains junk data. Gated by the "this.textureInitialized" flag.
                texture   = new DynamicTexture(textureId::toString, image);
                Minecraft.getInstance().getTextureManager().register(textureId, texture);
            });
            //TODO replace with a .join method in the task handler
            while(!gpuTasksHandler.isComplete()) {
                try { Thread.sleep(10); } catch(final InterruptedException _) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            frameSource.start();
            frameSourceInitialized.set(true);
        });
    }
}