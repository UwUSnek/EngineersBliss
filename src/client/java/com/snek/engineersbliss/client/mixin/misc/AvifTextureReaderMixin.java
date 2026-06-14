package com.snek.engineersbliss.client.mixin.misc;

import com.github.gotson.nightmonkeys.heif.imageio.plugins.HeifImageReader;
import com.github.gotson.nightmonkeys.heif.imageio.plugins.HeifImageReaderSpi;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.snek.engineersbliss.client.utils.scheduler.Scheduler;

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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.github.gotson.nightmonkeys.heif.imageio.plugins.HeifImageReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;







/**
 * A mixin that adds support for textures of AVIF image format.
 * Textures are loaded asynchronously. A placeholder texture is returned while waiting.
 */
@Mixin(TextureContents.class)
public class AvifTextureReaderMixin {
    private AvifTextureReaderMixin() {}


    // // A set containing the textures that are currently being loaded in asynchronously
    // private static final Set<Identifier> loadingIn = ConcurrentHashMap.newKeySet();




    @Inject(method = "load", at = @At("HEAD"), cancellable = true)
    private static void load(final ResourceManager resourceManager, final Identifier id, final CallbackInfoReturnable<TextureContents> cir) throws IOException {
        if(!id.getPath().endsWith(".avif")) return;

        NativeImage placeholder = new NativeImage(1, 1, false);
        placeholder.setPixelABGR(0, 0, 0xFF_FF_00_FF);
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

                Scheduler.run(() -> {
                    final NativeImage image = new NativeImage(w, h, false); //! Closed by the apply call
                    final IntBuffer ibuf = MemoryUtil.memIntBuffer(image.getPointer(), w * h);
                    for (int i = 0; i < pixels.length; i++) {
                        final int p = pixels[i];
                        ibuf.put(i, (p & 0xFF00FF00) | ((p & 0x00FF0000) >> 16) | ((p & 0x000000FF) << 16));
                    }
                    AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(id);
                    if(tex instanceof ReloadableTexture reloadable) {
                        reloadable.apply(new TextureContents(image, metadata));
                    }
                    else {
                        System.out.println("TEXTURE IS NOT RELOADABLE");//TODO use proper error reporting
                    }
                });
            } catch(Exception e) {
                e.printStackTrace(); //TODO use proper error reporting
            }
        });
    }
}