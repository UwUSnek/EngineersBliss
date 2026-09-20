package com.snek.engineersbliss.client.ui.renderer;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;




public class GaussianBlurPipRenderer extends PictureInPictureRenderer<GaussianBlurRenderState> {

    private GpuTexture scratchTex;
    private GpuTextureView scratchView;

    @Override
    public Class<GaussianBlurRenderState> getRenderStateClass() {
        return GaussianBlurRenderState.class;
    }

    @Override
    protected String getTextureLabel() {
        return "blur";
    }

    // Force redraws so the texture doesn't get cached
    @Override
    protected boolean textureIsReadyToBlit(GaussianBlurRenderState state) {
        return false;
    }

    @Override
    protected void renderToTexture(GaussianBlurRenderState state, PoseStack poseStack, SubmitNodeCollector unused) {
        final int width  = state.x1() - state.x0();
        final int height = state.y1() - state.y0();
        ensureScratch(width, height);

        // Source texture
        GpuTextureView sourceView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();

        // horizontal blur
        runBlurPass(sourceView, scratchView, state.radius(), true, state.x0(), state.y0());

        // vertical blur
        runBlurPass(scratchView, RenderSystem.outputColorTextureOverride, state.radius(), false, 0, 0);
    }

    private void runBlurPass(GpuTextureView src, GpuTextureView dst, float radius, boolean horizontal, int srcOffsetX, int srcOffsetY) {
    }

    private void ensureScratch(int width, int height) {
        if (scratchTex != null && scratchTex.getWidth(0) == width && scratchTex.getHeight(0) == height) return;
        if (scratchTex != null) { scratchTex.close(); scratchView.close(); }
        GpuDevice device = RenderSystem.getDevice();
        scratchTex  = device.createTexture(
            () -> "UI blur scratch",
            GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING | GpuTexture.USAGE_RENDER_ATTACHMENT,
            GpuFormat.RGBA8_UNORM,
            width, height, 1, 1
        );
        scratchView = device.createTextureView(scratchTex);
    }

    @Override
    public void close() {
        super.close();
        if (scratchTex  != null) scratchTex.close();
        if (scratchView != null) scratchView.close();
    }
}