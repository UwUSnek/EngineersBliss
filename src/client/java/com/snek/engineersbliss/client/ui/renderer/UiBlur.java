package com.snek.engineersbliss.client.ui.renderer;

import java.nio.ByteBuffer;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryStack;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.TextureSetup;








/**
 * Holds and updates the screen space blur texture used by UiGraphics.
 * The blur is computed once per frame, before the GUI is drawn, and sampled by every blurred element.
 */
public final class UiBlur {

    // Downscale factor for performance.
    private static final int DOWNSCALE = 1; //TODO increase this with higher radii.

    private static final int TEXTURE_USAGE = GpuTexture.USAGE_TEXTURE_BINDING | GpuTexture.USAGE_RENDER_ATTACHMENT;
    private static final int CONFIG_SIZE   = new Std140SizeCalculator().putVec2().putFloat().get();

    private static GpuTexture     texA    = null;
    private static GpuTexture     texB    = null;
    private static GpuTextureView viewA   = null;
    private static GpuTextureView viewB   = null;
    private static GpuBuffer      configH = null;
    private static GpuBuffer      configV = null;

    private static int   texWidth        = 0;
    private static int   texHeight       = 0;
    private static float uploadedRadius  = 1f;
    private static float requestedRadius = 0;




    private UiBlur() {}




    /**
     * Requests a blurred backdrop for the current frame.
     * ! The blur texture is shared, so the largest radius requested during a frame wins.
     * @param radius The blur radius, in GUI pixels
     */
    public static void request(final float radius) {
        requestedRadius = Math.max(requestedRadius, radius);
        ensureTargets();
    }


    /**
     * Returns the TextureSetup of the current blur texture.
     */
    public static TextureSetup textureSetup() {
        ensureTargets();
        return TextureSetup.singleTexture(viewB, sampler());
    }


    /**
     * Runs the two blur passes. Must be called after the world has been drawn and before the GUI is drawn.
     */
    public static void prepare() {
        if(requestedRadius <= 0f) return;
        ensureTargets();
        ensureConfig(requestedRadius);
        requestedRadius = 0f;

        final GpuTextureView source = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
        runPass(source, viewA, configH);
        runPass(viewA,  viewB, configV);
    }




    private static void runPass(final GpuTextureView src, final GpuTextureView dst, final GpuBuffer config) {
        try(final @NotNull RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "UI blur", dst, Optional.empty())) {
            pass.setPipeline(UiRenderPipelines.GAUSSIAN_BLUR);
            RenderSystem.bindDefaultUniforms(pass);
            pass.bindTexture("InSampler", src, sampler());
            pass.setUniform("BlurConfig", config);
            pass.draw(3, 1, 0, 0);
        }
    }


    private static GpuSampler sampler() {
        return RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
    }



    private static void ensureTargets() {
        final @NotNull RenderTarget main = Minecraft.getInstance().gameRenderer.mainRenderTarget();
        final int width  = Math.max(1, main.width  / DOWNSCALE);
        final int height = Math.max(1, main.height / DOWNSCALE);
        if(texA != null && texWidth == width && texHeight == height) return;

        closeTargets();
        texWidth  = width;
        texHeight = height;

        final @NotNull GpuDevice device = RenderSystem.getDevice();
        texA  = device.createTexture(() -> "UI blur A", TEXTURE_USAGE, GpuFormat.RGBA8_UNORM, texWidth, texHeight, 1, 1);
        texB  = device.createTexture(() -> "UI blur B", TEXTURE_USAGE, GpuFormat.RGBA8_UNORM, texWidth, texHeight, 1, 1);
        viewA = device.createTextureView(texA);
        viewB = device.createTextureView(texB);
    }

    private static void ensureConfig(final float radius) {
        if(configH != null && uploadedRadius == radius) return;
        closeConfig();
        uploadedRadius = radius;
        configH = createConfig("UI blur config H", 1f, 0f, Math.max(1f, radius));
        configV = createConfig("UI blur config V", 0f, 1f, Math.max(1f, radius / DOWNSCALE));
    }

    private static GpuBuffer createConfig(final String label, final float dirX, final float dirY, final float pixelRadius) {
        try(final MemoryStack stack = MemoryStack.stackPush()) {
            final ByteBuffer data = Std140Builder.onStack(stack, CONFIG_SIZE)
                .putVec2(dirX, dirY)
                .putFloat(pixelRadius)
                .get()
            ;
            return RenderSystem.getDevice().createBuffer(() -> label, GpuBuffer.USAGE_UNIFORM, data);
        }
    }




    private static void closeTargets() {
        if(viewA != null) { viewA.close(); viewA = null; }
        if(viewB != null) { viewB.close(); viewB = null; }
        if(texA  != null) { texA .close(); texA  = null; }
        if(texB  != null) { texB .close(); texB  = null; }
    }


    private static void closeConfig() {
        if(configH != null) { configH.close(); configH = null; }
        if(configV != null) { configV.close(); configV = null; }
        uploadedRadius = -1f;
    }


    public static void close() {
        closeTargets();
        closeConfig();
    }
}