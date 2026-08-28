package com.snek.engineersbliss.client.utils.textures.svg;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.resources.Identifier;



//TODO merge with svgTextureTracker
public final class SvgScaleTracker {
    private SvgScaleTracker() {}

    private static int lastGuiScaleIndex = -1;


    public static void tick() {
        final int guiScaleIndex = SettingsFeatureHandler.getCurrentGuiScaleIndex();
        if(guiScaleIndex == lastGuiScaleIndex) return;
        lastGuiScaleIndex = guiScaleIndex;

        for(final Identifier id : SvgTextureTracker.all().keySet()) {
            final NativeImage image = SvgTextureTracker.acquire(id, guiScaleIndex);
            if(image == null) continue;
            final AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(id);
            if(tex instanceof final ReloadableTexture reloadable) {
                reloadable.apply(new TextureContents(image, null));
            }
            else image.close();
        }
    }
}