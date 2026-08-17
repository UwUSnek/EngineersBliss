package com.snek.engineersbliss.client.mixin.misc;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.textures.atlases.AtlasMetadataSection;
import com.snek.engineersbliss.client.utils.textures.atlases.TextureAtlasTracker;
import com.snek.engineersbliss.client.utils.textures.svg.SvgMetadataSection;
import com.snek.engineersbliss.client.utils.textures.svg.SvgRasterizer;
import com.snek.engineersbliss.client.utils.textures.svg.SvgScaleTracker;
import com.snek.engineersbliss.client.utils.textures.svg.SvgTextureTracker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
 * A mixin that adds support for asynchronous texture loading, sprite sheet atlases, and atlas metadata.
 * A placeholder texture is returned while waiting.
 */
@Mixin(TextureContents.class)
public class AsyncTextureLoaderMixin {
    private AsyncTextureLoaderMixin() {}


    // Worker Thread pool
    private static final ExecutorService DECODE_TREAD_POOL = Executors.newFixedThreadPool(
        Math.max(2, Runtime.getRuntime().availableProcessors() - 1),
        r -> {
            final @NotNull Thread t = new Thread(r, "eb-png-decode");
            t.setDaemon(true);
            return t;
        }
    );


    // Placeholder texture used while the actual textures load in
    private static NativeImage LOADING_IMAGE;
    static {
        try(InputStream s = AsyncTextureLoaderMixin.class.getResourceAsStream("/assets/" + EngineerSBliss.MOD_ID + "/textures/gui/placeholder_texture.png")) {
            LOADING_IMAGE = NativeImage.read(s);
        }
        catch(final @NotNull IOException e) {
            EngineerSBliss.LOGGER.error("Could not load placeholder texture. {}", e.getMessage(), new Throwable());
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

        final String path = id.getPath();

        // Atlas

        if(path.endsWith(".png")) {
            final String basePath = path.substring(0, path.length() - 4);
            final Identifier svgId = id.withPath(basePath + ".svg");

            final Resource svgResource = resourceManager.getResource(svgId).orElse(null);
            if(svgResource != null) {
                final SvgMetadataSection svgMeta = eb$readSvgMeta(resourceManager, id.withPath(basePath + ".svg.mcmeta"));
                if(svgMeta == null) {
                    EngineerSBliss.LOGGER.error("SVG texture {} is missing its .svg.mcmeta metadata.", svgId, new Throwable());
                    return;
                }

                final byte[] bytes;
                try(InputStream is = svgResource.open()) {
                    bytes = is.readAllBytes();
                }

                SvgTextureTracker.getOrRegister(id, bytes, svgMeta);
                final NativeImage svgImage = SvgTextureTracker.acquire(id, SvgScaleTracker.currentScale());
                cir.setReturnValue(new TextureContents(svgImage, null));
                return;
            }
        }



        // SVG

        final Resource resource = resourceManager.getResourceOrThrow(id);

        final AtlasMetadataSection atlasMeta = resource.metadata().getSection(AtlasMetadataSection.TYPE).orElse(null);
        if(atlasMeta == null) return;

        TextureAtlasTracker.registerAtlas(id, atlasMeta);

        final NativeImage placeholder = eb$buildPlaceholderImage();
        cir.setReturnValue(new TextureContents(placeholder, null));

        CompletableFuture.runAsync(() -> {
            try {
                final NativeImage image;
                try(InputStream is = resource.open()) {
                    image = NativeImage.read(is); //! Closed by the apply call
                }
                if(image != null) {
                    final @Nullable TextureMetadataSection metadata = resource.metadata().getSection(TextureMetadataSection.TYPE).orElse(null);
                    ClientScheduler.run(() -> {
                        final AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(id);
                        if(tex instanceof final @NotNull ReloadableTexture reloadable) {
                            reloadable.apply(new TextureContents(image, metadata));
                            TextureAtlasTracker.markLoaded(id);
                        }
                        else {
                            image.close();
                            EngineerSBliss.LOGGER.error("Texture {} is not reloadable.", id, new Throwable());
                        }
                    });
                }
            }
            catch(final @NotNull IOException e) {
                EngineerSBliss.LOGGER.error("Could not load texture {}. {}", id, e.getMessage(), new Throwable());
            }
        }, DECODE_TREAD_POOL);
    }




    /**
     * Reads the SvgMetadataSection from an .svg.mcmeta file.
     */
    @Nullable
    private static SvgMetadataSection eb$readSvgMeta(final ResourceManager resourceManager, final Identifier mcmetaId) {
        final Resource mcmetaResource = resourceManager.getResource(mcmetaId).orElse(null);
        if(mcmetaResource == null) return null;

        try(InputStream is = mcmetaResource.open()) {
            return net.minecraft.server.packs.resources.ResourceMetadata.fromJsonStream(is)
                .getSection(SvgMetadataSection.TYPE)
                .orElse(null);
        }
        catch(final @NotNull IOException e) {
            EngineerSBliss.LOGGER.error("Could not read SVG metadata {}. {}", mcmetaId, e.getMessage(), new Throwable());
            return null;
        }
    }
}
