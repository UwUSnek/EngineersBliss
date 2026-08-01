package com.snek.engineersbliss.client.utils.textures;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.view.ViewBox;
import com.mojang.blaze3d.platform.NativeImage;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;








public final class SvgRasterizer {
    private SvgRasterizer() {}

    private static final SVGLoader LOADER = new SVGLoader();




    public static NativeImage rasterize(final byte[] svgBytes, final int width, final int height) {
        final SVGDocument doc;
        try(InputStream is = new ByteArrayInputStream(svgBytes)) {
            doc = LOADER.load(is, null, LoaderContext.createDefault());
        }
        catch(final Exception e) {
            throw new RuntimeException("Failed to read SVG stream", e);
        }
        if (doc == null) throw new RuntimeException("Failed to parse SVG");

        final BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // ViewBox tells JSVG the target pixel rect
        doc.render(null, g, new ViewBox(0, 0, width, height));
        g.dispose();

        return toNativeImage(buf);
    }




    public static NativeImage copy(final NativeImage src) {
        final NativeImage out = new NativeImage(src.getWidth(), src.getHeight(), false);
        MemoryUtil.memCopy(src.getPointer(), out.getPointer(), src.getWidth() * src.getHeight() * 4L);
        return out;
    }




    private static NativeImage toNativeImage(final BufferedImage img) {
        final int w = img.getWidth(), h = img.getHeight();
        final NativeImage out = new NativeImage(w, h, false);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                final int argb = img.getRGB(x, y);
                final int a  = (argb >>> 24) & 0xFF;
                final int r  = (argb >>> 16) & 0xFF;
                final int gC = (argb >>>  8) & 0xFF;
                final int b  =  argb         & 0xFF;
                out.setPixel(x, y, (a << 24) | (b << 16) | (gC << 8) | r);
            }
        }
        return out;
    }
}