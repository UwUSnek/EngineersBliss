package com.snek.engineersbliss.client.utils.media;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.media.entries.Mp4Entry;
import com.snek.engineersbliss.client.utils.media.entries.PngEntry;
import com.snek.engineersbliss.client.utils.media.entries.SvgEntry;
import com.snek.engineersbliss.client.utils.media.entries.__base_MediaEntry;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;








/**
 * A class that keeps track of all known PNG, SVG, and MP4 media from any namespace. //TODO check if this actually checks all namespaces
 * This stores the dimensions and aspect ratio of each media resource and their data's Identifiers.
 *
 * Raw texture management and GPU operations are handled by Minecraft's resource system.
 * This class simply unifies media formats and stores image metadata.
 */
public class MediaTracker implements SimpleSynchronousResourceReloadListener {
    private static final Path CACHE_DIR = FabricLoader.getInstance().getGameDir().resolve("cache").resolve(EngineerSBliss.MOD_ID).resolve("resources").resolve("mp4");


    // File path converters
    private static final FileToIdConverter PNG_CONVERTER = new FileToIdConverter("textures", ".png");
    private static final FileToIdConverter SVG_CONVERTER = new FileToIdConverter("textures", ".svg");
    private static final FileToIdConverter MP4_CONVERTER = new FileToIdConverter("textures", ".mp4");


    // Tracked media entries
    public static final Identifier PLACEHOLDER_TEXTURE_ID   = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "gui/placeholder_texture");
    public static final Identifier MISSING_MEDIA_TEXTURE_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "gui/missing_resource_texture");
    private static Map<Identifier, __base_MediaEntry> tracked = new HashMap<>(); //! A simple HashMap is fine for SimpleSynchronousResourceReloadListener


    @Override
    public Identifier getFabricId() {
        return Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "media_tracker");
    }




    /**
     * Returns the media identified by the provided ID.
     * @return The media, or a "Missing Resource" media entry if the media is not available.
     */
    public static __base_MediaEntry getMedia(final Identifier id) {
        final __base_MediaEntry r = tracked.get(id);
        return r == null ? tracked.get(MISSING_MEDIA_TEXTURE_ID) : r;
    }




    @Override
    public void onResourceManagerReload(final ResourceManager resourceManager) {
        tracked.clear();


        // PNGs
        for(final @NotNull var e : PNG_CONVERTER.listMatchingResources(resourceManager).entrySet()) {
            final @NotNull Identifier filePath = e.getKey();
            final @NotNull Identifier fileId   = PNG_CONVERTER.fileToId(filePath);
            tracked.put(fileId, new PngEntry(fileId));
        }


        // SVGs
        for(final @NotNull var e : SVG_CONVERTER.listMatchingResources(resourceManager).entrySet()) {
            final @NotNull Identifier filePath = e.getKey();
            final @NotNull Identifier fileId   = SVG_CONVERTER.fileToId(filePath);
            final byte[] bytes;
            try(final @NotNull InputStream input = e.getValue().open()) {
                bytes = input.readAllBytes();
            }
            catch(final @NotNull IOException _e) {
                EngineerSBliss.LOGGER.error("Failed to read SVG texture {}. {}", filePath, _e.getMessage(), new Throwable());
                continue;
            }
            tracked.put(fileId, new SvgEntry(fileId, bytes));
        }


        // MP4s //! Copy MP4s to a cache directory so the reader can open them properly
        try { Files.createDirectories(CACHE_DIR); } catch(final IOException _e) {
            EngineerSBliss.LOGGER.error("Failed creating video texture cache dir. {}", _e.getMessage(), new Throwable());
            return;
        }
        for(final @NotNull var e : MP4_CONVERTER.listMatchingResources(resourceManager).entrySet()) {
            final @NotNull Identifier filePath = e.getKey();
            final @NotNull Identifier fileId   = MP4_CONVERTER.fileToId(filePath);
            final @NotNull Path trueCachePath = CACHE_DIR.resolve(filePath.getNamespace() + "_" + filePath.getPath().replace('/', '_'));
            try(final @NotNull InputStream input = e.getValue().open()) {
                Files.copy(input, trueCachePath, StandardCopyOption.REPLACE_EXISTING);
            }
            catch(final @NotNull IOException _e) {
                EngineerSBliss.LOGGER.error("Failed to read MP4 video {}. {}", filePath, _e.getMessage(), new Throwable());
                continue;
            }

            tracked.put(fileId, new Mp4Entry(fileId, trueCachePath));
        }


        EngineerSBliss.LOGGER.info("Found {} media files", tracked.size());
    }
}
