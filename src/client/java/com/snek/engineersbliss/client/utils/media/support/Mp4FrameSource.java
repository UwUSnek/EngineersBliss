package com.snek.engineersbliss.client.utils.media.support;

import com.snek.engineersbliss.EngineerSBliss;

import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;








//! Input videos are RGB with a BW Alpha channel stacked below the real picture.

//! MP4 H264 is the best format for maximum compression, low ram usage, and low-ish decoding times.
//! It's also the most widely supported format so storing alpha this way is the best solution rn.


public class Mp4FrameSource {


    // Basic data
    private final Path          path;
    private final Thread        decodeThread;
    private final AtomicBoolean running;
    private final AtomicBoolean resetRequested;
    private FrameGrab           grab;




    // Frame pool
    private final int width;
    private final int height;
    public int getWidth()  { return width; }
    public int getHeight() { return height; }

    private static final int POOL_SIZE = 2;
    private IntBuffer[] pool;
    private final ArrayBlockingQueue<Integer> ready = new ArrayBlockingQueue<>(POOL_SIZE - 1);
    private int writeIdx = 0;
    private void initFramePool() {
        this.pool = new IntBuffer[POOL_SIZE];
        for(int i = 0; i < POOL_SIZE; i++) {
            pool[i] = MemoryUtil.memAllocInt(width * height); //! Direct buffer. Required for bulk memcpy
        }
    }
    public @Nullable IntBuffer takeFrame() {
        final Integer idx = ready.poll();
        return idx == null ? null : pool[idx];
    }
    private void storeFrame(final Picture picture) {
        final BufferedImage buf   = AWTUtil.toBufferedImage(picture);
        final int           w     = buf.getWidth();
        final int           fullH = buf.getHeight();
        final int           h     = fullH / 2;
        final byte[]        bytes = ((DataBufferByte)buf.getRaster().getDataBuffer()).getData();

        final @NotNull IntBuffer out = pool[writeIdx];
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                final int rgbOff = (y * w + x) * 3;
                final int aOff   = ((y + h) * w + x) * 3;
                final int b = bytes[rgbOff + 0] & 0xFF;
                final int g = bytes[rgbOff + 1] & 0xFF;
                final int r = bytes[rgbOff + 2] & 0xFF;
                final int a = bytes[aOff      ] & 0xFF;
                out.put(y * w + x, (a << 24) | (b << 16) | (g << 8) | r);
            }
        }
        // Stops if consumer hasn't caught up
        try { ready.put(writeIdx); } catch(final InterruptedException _) { Thread.currentThread().interrupt(); }
        writeIdx = (writeIdx + 1) % POOL_SIZE;
    }




    public Mp4FrameSource(final Path path) {
        this.path = path;
        this.running = new AtomicBoolean(true);
        this.resetRequested = new AtomicBoolean(false);
        try {
            grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(path.toFile()));
            final Picture first = grab.getNativeFrame();
            if(first == null) throw new IllegalStateException("Video has no frames: " + path); //TODO replace with missing media texture or something
            final BufferedImage dims = AWTUtil.toBufferedImage(first);
            width  = dims.getWidth();
            height = dims.getHeight() / 2;
        }
        catch(final Exception e) {
            throw new RuntimeException("Failed to open video " + path, e);
        }
        initFramePool();
        decodeThread = new Thread(this::decodeLoop, "video-decode-" + path.getFileName());
        decodeThread.setDaemon(true);
    }

    public void start() {
        decodeThread.start();
    }
    public void close() {
        running.set(false);
        decodeThread.interrupt();
        try { decodeThread.join(); } catch(final InterruptedException _) { Thread.currentThread().interrupt(); }
        for(final IntBuffer b : pool) MemoryUtil.memFree(b);
    }
    public void requestReset() {
        resetRequested.set(true);
    }




    private void decodeLoop() {
        try {
            while(running.get()) {

                // Grab next frame and store it in the queue
                final Picture picture = grab.getNativeFrame();
                if(picture == null) {
                    grab.seekToFramePrecise(0);
                    continue;
                }
                storeFrame(picture);

                // Reset to first frame if needed
                if(resetRequested.get()) {
                    grab.seekToFramePrecise(0);
                    resetRequested.set(false);
                }
            }
        }
        catch(final @NotNull Exception e) {
            EngineerSBliss.LOGGER.error("Failed decoding video {}. {}", path, e.getMessage(), new Throwable());
        }
    }
}