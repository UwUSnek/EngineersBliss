package com.snek.engineersbliss.client.utils.textures.svg;

import com.snek.engineersbliss.EngineerSBliss;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;





public final class SvgTextureReloadListener implements SimpleSynchronousResourceReloadListener {

    private static final FileToIdConverter CONVERTER = new FileToIdConverter("textures", ".svg");


    @Override
    public Identifier getFabricId() {
        return Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "svg_textures");
    }


    @Override
    public void onResourceManagerReload(final ResourceManager resourceManager) {
        final Set<Identifier> found = new HashSet<>();

        for(final Map.Entry<Identifier, Resource> entry : CONVERTER.listMatchingResources(resourceManager).entrySet()) {
            final Identifier svgId = CONVERTER.fileToId(entry.getKey());
            // final Identifier mcmetaId = svgId.withPath(svgId.getPath() + ".mcmeta");  //TODO REMOVE

            // final SvgMetadataSection meta = eb$readSvgMeta(resourceManager, mcmetaId); //TODO REMOVE
            // if(meta == null) {
            //     EngineerSBliss.LOGGER.warn("SVG texture {} is missing its .svg.mcmeta metadata, skipping.", svgId);
            //     continue;
            // }

            final byte[] bytes;
            try(InputStream is = entry.getValue().open()) {
                bytes = is.readAllBytes();
            }
            catch(final IOException e) {
                EngineerSBliss.LOGGER.error("Failed reading SVG texture {}. {}", svgId, e.getMessage(), new Throwable());
                continue;
            }

            // SvgTextureTracker.getOrRegister(svgId, bytes, meta);  //TODO REMOVE
            SvgTextureTracker.getOrRegister(svgId, bytes);
            found.add(svgId);
        }

        SvgTextureTracker.retainOnly(found);
        EngineerSBliss.LOGGER.info("Found {} SVG textures", found.size());
    }

 //TODO REMOVE
    // private static SvgMetadataSection eb$readSvgMeta(final ResourceManager resourceManager, final Identifier mcmetaId) {
    //     final Resource mcmetaResource = resourceManager.getResource(mcmetaId).orElse(null);
    //     if(mcmetaResource == null) return null;
    //     try(InputStream is = mcmetaResource.open()) {
    //         return net.minecraft.server.packs.resources.ResourceMetadata.fromJsonStream(is).getSection(SvgMetadataSection.TYPE).orElse(null);
    //     }
    //     catch(final IOException e) {
    //         EngineerSBliss.LOGGER.error("Could not read SVG metadata {}. {}", mcmetaId, e.getMessage(), new Throwable());
    //         return null;
    //     }
    // }
}