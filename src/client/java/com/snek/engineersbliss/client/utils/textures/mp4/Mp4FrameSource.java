package com.snek.engineersbliss.client.utils.textures.mp4;

import com.snek.engineersbliss.EngineerSBliss;

import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;




//! Input videos are RGB with a BW Alpha channel stacked below the real picture.

//! MP4 H264 is the best format for maximum compression, low ram usage, and low-ish decoding times.
//! It's also the most widely supported format so storing alpha this way is the best solution rn.


final class Mp4FrameSource {
    private static final int QUEUE_CAPACITY = 2;

    private final Path                 path;
    private final Thread               decodeThread;
    private final AtomicBoolean        running = new AtomicBoolean(true);
    private final BlockingQueue<int[]> queue   = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    private final int width;
    private final int height;
    private FrameGrab  grab;


    Mp4FrameSource(final Path path) {
        this.path = path;
        try {
            grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(path.toFile()));
            final Picture first = grab.getNativeFrame();
            if(first == null) throw new IllegalStateException("Video has no frames: " + path);

            final BufferedImage dims = AWTUtil.toBufferedImage(first);
            width  = dims.getWidth();
            height = dims.getHeight() / 2;
            queue.add(toArgb(first));
        }
        catch(final Exception e) {
            throw new RuntimeException("Failed to open video " + path, e);
        }

        decodeThread = new Thread(this::decodeLoop, "video-decode-" + path.getFileName());
        decodeThread.setDaemon(true);
    }


    void start() {
        decodeThread.start();
    }


    int getWidth()  { return width; }
    int getHeight() { return height; }


    /** Returns the next decoded frame, or null if none is ready yet. */
    int[] takeFrame() {
        return queue.poll();
    }


    void close() {
        running.set(false);
        decodeThread.interrupt();
    }


    private void decodeLoop() {
        try {
            while(running.get()) {
                final Picture picture = grab.getNativeFrame();
                if(picture == null) {
                    grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(path.toFile()));
                    continue;
                }
                queue.put(toArgb(picture));
            }
        }
        catch(final InterruptedException _) {
            Thread.currentThread().interrupt();
        }
        catch(final Exception e) {
            EngineerSBliss.LOGGER.error("Failed decoding video {}. {}", path, e.getMessage(), new Throwable());
        }
    }


    private static int[] toArgb(final Picture picture) {
        final BufferedImage buf    = AWTUtil.toBufferedImage(picture);
        final int           w      = buf.getWidth();
        final int           fullH  = buf.getHeight();
        final int           h      = fullH / 2;
        final byte[]        bytes  = ((DataBufferByte)buf.getRaster().getDataBuffer()).getData();

        final int[] out = new int[w * h];
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                final int rgbOff = (y * w + x) * 3;
                final int aOff   = ((y + h) * w + x) * 3;

                final int b = bytes[rgbOff + 2] & 0xFF;
                final int g = bytes[rgbOff + 1] & 0xFF;
                final int r = bytes[rgbOff + 0] & 0xFF;
                final int a = bytes[aOff      ] & 0xFF;

                out[y * w + x] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
        return out;
    }
}