package com.snek.engineersbliss.client.utils.media.entries;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.utils.media.MediaTracker;

import net.minecraft.resources.Identifier;








// Base Entry for any media resource. This contains common metadata and the raw resource ID.
public abstract class __base_MediaEntry {
    private final Identifier resourceId;    // The ID of the raw unprocessed media resource. //! This does NOT include the "textures/" prefix and file extension.
    private       int w;                    // Width of the raw resource, in pixels.
    private       int h;                    // Height of the raw resource, in pixels.
    private final float ratio;              // Width units for each Height unit.

    public final Identifier getResourceId() { return resourceId; }
    public final int        getWidth     () { return w;          }
    public final int        getHeight    () { return h;          }
    public final float      getRatio     () { return ratio;      }

    protected __base_MediaEntry(final Identifier resourceId, final int w, final int h) {
        this.resourceId = resourceId;
        this.w     = w;
        this.h     = h;
        this.ratio = (float)w / h;
    }

    /**
     * Updates the registered width and height of the entry.
     * This should be called by implementations in case the size of the media cannot be determined during object construction.
     */
    protected void updateSize(final int newWidth, final int newHeight) {
        w = newWidth;
        h = newHeight;
    }

    /**
     * Tries to retrieve the ID of the processed, ready-to-use texture registered in Minecraft's TextureManager that best fits the desired size.<br>
     * This can be a normal image texture, an SVG cache, or a frame from a video.
     * @return The ID of the requested texture, or null if the texture is not available.
     *     Implementations are allowed to return a texture of size different from the desired size. This should be the closest match available.
     */
    protected abstract @Nullable Identifier __internal_requestTextureFor(final int desiredWidth, final int desiredHeight);
    public final @NotNull Identifier requestTextureFor(final int desiredWidth, final int desiredHeight) {
        final Identifier r = __internal_requestTextureFor(desiredWidth, desiredHeight);
        return r == null ? MediaTracker.MISSING_MEDIA_TEXTURE_ID : r;
    }
}