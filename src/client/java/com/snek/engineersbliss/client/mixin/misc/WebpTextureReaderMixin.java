package com.snek.engineersbliss.client.mixin.misc;

import com.luciad.imageio.webp.WebPImageReaderSpi;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

@Mixin(TextureContents.class)
public class WebpTextureReaderMixin {
    private WebpTextureReaderMixin() {}

    static {
        IIORegistry.getDefaultInstance().registerServiceProvider(new WebPImageReaderSpi());
    }

    @Inject(method = "load", at = @At("HEAD"), cancellable = true)
    private static void load(final ResourceManager resourceManager, final Identifier id, final CallbackInfoReturnable<TextureContents> cir) throws IOException {
        if (!id.getPath().endsWith(".webp")) return;

        final Resource resource = resourceManager.getResourceOrThrow(id);

        final BufferedImage buffered;
        try (InputStream is = resource.open()) {
            final byte[] header = is.readNBytes(12);
            if (header.length < 12
                || header[0] != 'R' || header[1] != 'I' || header[2] != 'F' || header[3] != 'F'
                || header[8] != 'W' || header[9] != 'E' || header[10] != 'B' || header[11] != 'P') {
                return;
            }
            try (ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
                buf.write(header);
                is.transferTo(buf);
                buffered = ImageIO.read(new MemoryCacheImageInputStream(
                    new ByteArrayInputStream(buf.toByteArray())
                ));
            }
        }

        if (buffered == null) return;

        final int w = buffered.getWidth();
        final int h = buffered.getHeight();

        // Convert to ARGB if needed
        final int[] pixels;
        if (buffered.getType() == BufferedImage.TYPE_INT_ARGB) {
            pixels = ((DataBufferInt) buffered.getRaster().getDataBuffer()).getData();
        } else {
            final BufferedImage argb = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g = argb.createGraphics();
            try { g.drawImage(buffered, 0, 0, null); }
            finally { g.dispose(); }
            pixels = ((DataBufferInt) argb.getRaster().getDataBuffer()).getData();
        }

        final NativeImage image = new NativeImage(w, h, false);
        final IntBuffer ibuf = MemoryUtil.memIntBuffer(image.getPointer(), w * h);
        for (int i = 0; i < pixels.length; i++) {
            // ARGB -> ABGR
            final int p = pixels[i];
            ibuf.put(i, (p & 0xFF00FF00) | ((p & 0x00FF0000) >> 16) | ((p & 0x000000FF) << 16));
        }

        final TextureMetadataSection metadata = resource.metadata()
            .getSection(TextureMetadataSection.TYPE)
            .orElse(null);

        cir.setReturnValue(new TextureContents(image, metadata));
    }
}