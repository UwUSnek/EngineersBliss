package com.snek.engineersbliss.client.mixin.misc;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.screens.AvifTextureTracker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ReloadableTexture;
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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.concurrent.CompletableFuture;







/**
 * A mixin that adds support for textures of AVIF image format.
 * Textures are loaded asynchronously. A placeholder texture is returned while waiting.
 */
@Mixin(TextureContents.class)
public class AvifTextureReaderMixin {
    private AvifTextureReaderMixin() {}



    // Placeholder texture used while the actual textures load in
    private static NativeImage LOADING_IMAGE;
    static {
        try(InputStream s = AvifTextureReaderMixin.class.getResourceAsStream("/assets/" + EngineerSBliss.MOD_ID + "/textures/gui/placeholder_texture.png")) {
            LOADING_IMAGE = NativeImage.read(s);
        }
        catch(final IOException e) {
            e.printStackTrace(); //TODO use proper logging
        }
    }

    private static NativeImage buildPlaceholderImage() {
        final NativeImage copy = new NativeImage(LOADING_IMAGE.getWidth(), LOADING_IMAGE.getHeight(), false);
        MemoryUtil.memCopy(LOADING_IMAGE.getPointer(), copy.getPointer(), LOADING_IMAGE.getWidth() * LOADING_IMAGE.getHeight() * 4L);
        return copy;
    }




    @SuppressWarnings("unused")
    @Inject(method = "load", at = @At("HEAD"), cancellable = true, require = 1)
    private static void load(final ResourceManager resourceManager, final Identifier id, final CallbackInfoReturnable<TextureContents> cir) throws IOException {
        if(!id.getPath().endsWith(".avif")) return;

        final NativeImage placeholder = buildPlaceholderImage();
        cir.setReturnValue(new TextureContents(placeholder, null));

        CompletableFuture.runAsync(() -> {
            try {
                final Resource resource = resourceManager.getResourceOrThrow(id);
                final BufferedImage buffered;
                try(InputStream is = resource.open()) {
                    buffered = ImageIO.read(is);
                }
                if(buffered == null) return;

                final int w = buffered.getWidth();
                final int h = buffered.getHeight();
                final int[] pixels;
                if(buffered.getType() == BufferedImage.TYPE_INT_ARGB) {
                    pixels = ((DataBufferInt) buffered.getRaster().getDataBuffer()).getData().clone();
                }
                else {
                    final BufferedImage argb = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    final Graphics2D g = argb.createGraphics();
                    try { g.drawImage(buffered, 0, 0, null); } finally { g.dispose(); }
                    pixels = ((DataBufferInt) argb.getRaster().getDataBuffer()).getData();
                }

                final TextureMetadataSection metadata = resource.metadata().getSection(TextureMetadataSection.TYPE).orElse(null);

                ClientScheduler.run(() -> {
                    final NativeImage image = new NativeImage(w, h, false); //! Closed by the apply call
                    final IntBuffer ibuf = MemoryUtil.memIntBuffer(image.getPointer(), w * h);
                    for(int i = 0; i < pixels.length; i++) {
                        final int p = pixels[i];
                        ibuf.put(i, (p & 0xFF00FF00) | ((p & 0x00FF0000) >> 16) | ((p & 0x000000FF) << 16));
                    }
                    final AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(id);
                    if(tex instanceof final ReloadableTexture reloadable) {
                        reloadable.apply(new TextureContents(image, metadata));
                        AvifTextureTracker.markLoaded(id);
                    }
                    else {
                        System.out.println("TEXTURE IS NOT RELOADABLE");//TODO use proper error reporting
                    }
                });
            } catch(final Exception e) {
                e.printStackTrace(); //TODO use proper error reporting
            }
        });
    }
}