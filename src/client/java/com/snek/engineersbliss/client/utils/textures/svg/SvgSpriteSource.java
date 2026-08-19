package com.snek.engineersbliss.client.utils.textures.svg;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;








public record SvgSpriteSource(String sourcePath) implements SpriteSource {


    public static final MapCodec<SvgSpriteSource> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.STRING.optionalFieldOf("source", "textures/gui/sprites").forGetter(SvgSpriteSource::sourcePath)
    ).apply(inst, SvgSpriteSource::new));




    @Override
    public void run(final ResourceManager resourceManager, final Output output) {
        final FileToIdConverter converter = new FileToIdConverter("textures/" + sourcePath, ".svg");

        final var found = converter.listMatchingResources(resourceManager);
        EngineerSBliss.LOGGER.info("Found {} svg files", found.size());
        EngineerSBliss.LOGGER.debug("svg files: {}", found.keySet());

        for(final Map.Entry<Identifier, Resource> entry : converter.listMatchingResources(resourceManager).entrySet()) {
            final Identifier svgFileId = entry.getKey();
            final Identifier spriteId = converter.fileToId(svgFileId); // Strip prefix and extension

            final Identifier mcmetaId = svgFileId.withPath(svgFileId.getPath() + ".mcmeta");
            final SvgMetadataSection meta = eb$readSvgMeta(resourceManager, mcmetaId);

            if(meta == null) continue;

            final byte[] bytes;
            try(InputStream is = entry.getValue().open()) {
                bytes = is.readAllBytes();
            }
            catch(final IOException e) {
                com.snek.engineersbliss.EngineerSBliss.LOGGER.error("Failed reading SVG sprite {}. {}", svgFileId, e.getMessage(), new Throwable());
                continue;
            }


            SvgTextureTracker.getOrRegister(spriteId, bytes, meta);

            // Register a sprite for each GUI Scale
            for(int scaleIndex = 0; scaleIndex < SettingsServerFeatureSet.GUI_SCALE.getValues().size(); scaleIndex++) {
                final int _scaleIndex = scaleIndex;
                final Identifier scaledSpriteId = SvgTextureTracker.getOptimalSprite(spriteId, _scaleIndex);
                output.add(scaledSpriteId, resourceLoader -> {
                    final NativeImage scaledImage = SvgTextureTracker.acquire(spriteId, _scaleIndex);
                    return new SpriteContents(scaledSpriteId, new FrameSize(scaledImage.getWidth(), scaledImage.getHeight()), scaledImage);
                });
            }
        }
    }




    private static SvgMetadataSection eb$readSvgMeta(final ResourceManager resourceManager, final Identifier mcmetaId) {
        final Resource mcmetaResource = resourceManager.getResource(mcmetaId).orElse(null);
        if(mcmetaResource == null) return null;
        try(InputStream is = mcmetaResource.open()) {
            return net.minecraft.server.packs.resources.ResourceMetadata.fromJsonStream(is).getSection(SvgMetadataSection.TYPE).orElse(null);
        }
        catch(final IOException e) {
            com.snek.engineersbliss.EngineerSBliss.LOGGER.error("Could not read SVG metadata {}. {}", mcmetaId, e.getMessage(), new Throwable());
            return null;
        }
    }




    @Override
    public MapCodec<SvgSpriteSource> codec() {
        return MAP_CODEC;
    }
}