package com.snek.engineersbliss.client.utils.textures;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.resources.Identifier;




public final class SvgScaleTracker {
    private SvgScaleTracker() {}

    private static int lastScale = -1;


    public static int currentScale() {
        final double raw = Minecraft.getInstance().getWindow().getGuiScale();
        return Math.clamp((int)Math.round(raw), 1, 4);
    }


    public static void tick() {
        final int scale = currentScale();
        if(scale == lastScale) return;
        lastScale = scale;

        for(final Identifier id : SvgTextureTracker.all().keySet()) {
            final NativeImage image = SvgTextureTracker.acquire(id, scale);
            if(image == null) continue;
            final AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(id);
            if(tex instanceof final ReloadableTexture reloadable) {
                reloadable.apply(new TextureContents(image, null));
            }
            else image.close();
        }
    }
}