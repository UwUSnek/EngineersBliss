package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.mojang.blaze3d.textures.GpuTexture;
import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedFloat;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;





// public class MediaWidget extends __base_UiWidget {
public class MediaWidget {
    // public record MediaEntry(
    //     Identifier id,
    //     int w, int h,
    //     AnimatedFloat alpha
    // ){}




    // // Media target
    // public static @Nullable Float calcMediaAspectRatio(final Identifier location) {
    //     if(SvgTextureTracker.isRegistered(location)) {
    //         // return SvgTextureTracker.getSize(location); //FIXME take the svg's ratio
    //         return null;
    //     }
    //     else if(Mp4TextureTracker.isRegistered(location)) {
    //         return Mp4TextureTracker.getSize(location);
    //     }
    //     else {
    //         final Identifier pngId = location.withPath("textures/" + location.getPath() + ".png");
    //         final AbstractTexture abstractTexture = Minecraft.getInstance().getTextureManager().getTexture(pngId);
    //         if(abstractTexture != null) {
    //             final GpuTexture gpuTexture = abstractTexture.getTextureView().texture();
    //             return new Vector2f(gpuTexture.getWidth(0), gpuTexture.getHeight(0));
    //         }
    //         return null;
    //     }
    // }
    // private @Nullable Queue<MediaEntry> mediaTargets;
    // private @NotNull MediaAlignment mediaAlignment;
    // public void setMediaTarget(final Identifier newMediaTarget) {
    //     final AnimatedFloat alpha = new AnimatedFloat(0f, fadeTimeMs);
    //     final Vector2f size = calcMediaSize(newMediaTarget);
    //     if(size != null) {
    //         mediaTargets.add(new MediaEntry(newMediaTarget, alpha));
    //         alpha.startNewTransition(1f);
    //         getMediaTarget().alpha.startNewTransition(0f);
    //     }
    //     //FIXME placeholder texture or something?
    // }
    // public void setMediaAlignment(final MediaAlignment newAlignment) { mediaAlignment = newAlignment; }
    // public MediaEntry getMediaTarget() { return mediaTargets.peek(); }
    // public MediaAlignment getMediaAlignment() { return mediaAlignment; }


    // // Fading effect
    // private int fadeTimeMs;
    // public void setFadeTime(final int newFadeTime) { fadeTimeMs = newFadeTime; }
    // public int getFadeTime() { return fadeTimeMs; }




    // public MediaWidget(final UiScreen screen, final MediaAlignment mediaAlignment, final int fadeTimeMs) {
    //     super(screen);
    //     this.mediaTargets = new ConcurrentLinkedQueue<>();
    //     this.mediaAlignment = mediaAlignment;
    //     this.fadeTimeMs = fadeTimeMs;
    //     setBgColor(0x0);
    // }
    // @Override
    // public void relayoutSelf() {
    //     // Empty
    // }




    // @Override
    // public void extractBackground(UiGraphics graphics, float mouseX, float mouseY, float a) {
    //     super.extractBackground(graphics, mouseX, mouseY, a);
    //     switch(mediaAlignment) {
    //         case CONTAIN: {

    //         }
    //         case CONTAIN_X: {

    //         }
    //         case CONTAIN_Y: {

    //         }
    //         case COVER: {

    //         }
    //         case COVER_X: {

    //         }
    //         case COVER_Y: {

    //         }
    //         case STRETCH: {
    //             graphics.blit.wh()
    //         }
    //     }
    // }
}
