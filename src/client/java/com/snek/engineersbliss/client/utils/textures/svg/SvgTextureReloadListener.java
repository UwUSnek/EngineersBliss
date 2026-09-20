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

import org.jetbrains.annotations.NotNull;





public final class SvgTextureReloadListener implements SimpleSynchronousResourceReloadListener {

    private static final FileToIdConverter CONVERTER = new FileToIdConverter("textures", ".svg");


    @Override
    public Identifier getFabricId() {
        return Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "svg_textures");
    }


    @Override
    public void onResourceManagerReload(final ResourceManager resourceManager) {
        final Set<Identifier> found = new HashSet<>();

        for(final @NotNull Map.Entry<Identifier, Resource> entry : CONVERTER.listMatchingResources(resourceManager).entrySet()) {
            final Identifier svgId = CONVERTER.fileToId(entry.getKey());
            final byte[] bytes;
            try(final @NotNull InputStream input = entry.getValue().open()) {
                bytes = input.readAllBytes();
            }
            catch(final @NotNull IOException e) {
                EngineerSBliss.LOGGER.error("Failed reading SVG texture {}. {}", svgId, e.getMessage(), new Throwable());
                continue;
            }

            SvgTextureTracker.getOrRegister(svgId, bytes);
            found.add(svgId);
        }

        SvgTextureTracker.retainOnly(found);
        EngineerSBliss.LOGGER.info("Found {} SVG textures", found.size());
    }
}