package com.snek.engineersbliss.client.utils.textures.mp4;

import com.snek.engineersbliss.EngineerSBliss;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;








public final class Mp4TextureReloadListener implements SimpleSynchronousResourceReloadListener {

    private static final FileToIdConverter CONVERTER = new FileToIdConverter("textures", ".mp4");
    private static final Path              CACHE_DIR = FabricLoader.getInstance().getGameDir().resolve("cache").resolve(EngineerSBliss.MOD_ID).resolve("video_textures");


    @Override
    public Identifier getFabricId() {
        return Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "video_textures");
    }


    @Override
    public void onResourceManagerReload(final ResourceManager resourceManager) {
        final Set<Identifier> found = new HashSet<>();

        try {
            Files.createDirectories(CACHE_DIR);
        }
        catch(final IOException e) {
            EngineerSBliss.LOGGER.error("Failed creating video texture cache dir. {}", e.getMessage(), new Throwable());
            return;
        }

        for(final @NotNull Map.Entry<Identifier, Resource> entry : CONVERTER.listMatchingResources(resourceManager).entrySet()) {
            final Identifier videoId = CONVERTER.fileToId(entry.getKey());
            final Path       cached  = CACHE_DIR.resolve(videoId.getNamespace() + "_" + videoId.getPath().replace('/', '_'));

            try(final @NotNull InputStream input = entry.getValue().open()) {
                Files.copy(input, cached, StandardCopyOption.REPLACE_EXISTING);
            }
            catch(final @NotNull IOException e) {
                EngineerSBliss.LOGGER.error("Failed reading video texture {}. {}", videoId, e.getMessage(), new Throwable());
                continue;
            }

            Mp4TextureTracker.getOrRegister(videoId, cached);
            found.add(videoId);
        }

        Mp4TextureTracker.clearAllBut(found);
        EngineerSBliss.LOGGER.info("Found {} video textures", found.size());
    }
}