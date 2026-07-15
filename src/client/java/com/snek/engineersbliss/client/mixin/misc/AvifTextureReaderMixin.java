package com.snek.engineersbliss.client.mixin.misc;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.avif_textures.AvifAtlasMetadataSection;
import com.snek.engineersbliss.client.utils.avif_textures.AvifTextureTracker;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * A mixin that adds support for asynchronous texture loading and sprite sheet atlases.
 * A placeholder texture is returned while waiting.
 */
@Mixin(TextureContents.class)
public class AvifTextureReaderMixin {
    private AvifTextureReaderMixin() {}


    // Worker Thread pool
    private static final ExecutorService AVIF_DECODE_POOL = Executors.newFixedThreadPool(
        Math.max(2, Runtime.getRuntime().availableProcessors() - 1),
        r -> {
            Thread t = new Thread(r, "eb-avif-decode");
            t.setDaemon(true);
            return t;
        }
    );


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


    // Creates a copy of the placeholder image. Images are cached on the GPU and the object is freed the first time they are used.
    private static NativeImage eb$buildPlaceholderImage() {
        final NativeImage copy = new NativeImage(LOADING_IMAGE.getWidth(), LOADING_IMAGE.getHeight(), false);
        MemoryUtil.memCopy(LOADING_IMAGE.getPointer(), copy.getPointer(), LOADING_IMAGE.getWidth() * LOADING_IMAGE.getHeight() * 4L);
        return copy;
    }


    @SuppressWarnings("unused")
    @Inject(method = "load", at = @At("HEAD"), cancellable = true, require = 1)
    private static void eb$load(final ResourceManager resourceManager, final Identifier id, final CallbackInfoReturnable<TextureContents> cir) throws IOException {
        final Resource resource = resourceManager.getResourceOrThrow(id);
        final AvifAtlasMetadataSection atlasMeta = resource.metadata().getSection(AvifAtlasMetadataSection.TYPE).orElse(null);

        // Not one of our animated sheets -> let vanilla handle it untouched, synchronously
        if(atlasMeta == null) return;

        AvifTextureTracker.registerAtlas(id, atlasMeta);

        final NativeImage placeholder = eb$buildPlaceholderImage();
        cir.setReturnValue(new TextureContents(placeholder, null));

        CompletableFuture.runAsync(() -> {
            try {
                final TextureMetadataSection metadata = resource.metadata().getSection(TextureMetadataSection.TYPE).orElse(null);

                final NativeImage image;
                try(InputStream is = resource.open()) {
                    image = NativeImage.read(is); //! Closed by the apply call
                }
                if(image == null) return;

                ClientScheduler.run(() -> {
                    final AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(id);
                    if(tex instanceof final ReloadableTexture reloadable) {
                        reloadable.apply(new TextureContents(image, metadata));
                        AvifTextureTracker.markLoaded(id);
                    }
                    else {
                        image.close(); //! Nobody will consume it - avoid leaking the native buffer
                        System.out.println("TEXTURE IS NOT RELOADABLE");//TODO use proper error reporting
                    }
                });
            } catch(final Exception e) {
                e.printStackTrace(); //TODO use proper error reporting
            }
        }, AVIF_DECODE_POOL);
    }
}
