package com.snek.engineersbliss.client.utils.media.entries;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;








// Specialized Entry for PNG media.
public class PngEntry extends __base_MediaEntry {
    private Identifier textureId;


    private Identifier calcTextureId() {
        return getResourceId().withPath("textures/" + getResourceId().getPath() + ".png");
    }

    @Override
    public @Nullable Identifier __internal_requestTextureFor(int desiredWidth, int desiredHeight) {
        return textureId;
    }

    public PngEntry(final Identifier resourceId) {
        super(resourceId, 0, 0);
        this.textureId = calcTextureId();
        final AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(textureId);
        if(texture != null) {
            //BUG this prob isn't a thing. getTexture always returns a texture instance but it doesnt always contain a valid path. These will show up as the checkerboard texture
            updateSize(texture.getTexture().getWidth(0), texture.getTexture().getHeight(0));
        }
    }
}