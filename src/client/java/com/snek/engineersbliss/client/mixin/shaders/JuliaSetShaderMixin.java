package com.snek.engineersbliss.client.mixin.shaders;

import java.util.Random;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.screens.julia_set.JuliaSetScreen;
import com.snek.engineersbliss.EngineerSBliss;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.snek.engineersbliss.client.mixin.accessors.GameRendererAccessor;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;




@Mixin(GameRenderer.class)
public abstract class JuliaSetShaderMixin {

    private static final Random rnd = new Random();
    private static int currentJuliaIndex = -1;
    private static JuliaSetScreen lastScreen = null;




    @SuppressWarnings("unused")
    @Inject(
        method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/render/GuiRenderer;endFrame()V"
        ),
        cancellable = false,
        require = 1
    )
    private void onAfterGuiRender(final DeltaTracker deltaTracker, final boolean advanceGameTime, final CallbackInfo ci) {

        // Return and reset screen if current screen is not a julia renderer
        final Minecraft mc = Minecraft.getInstance();
        if (!(mc.screen instanceof final JuliaSetScreen screen)) {
            lastScreen = null;
            return;
        }

        // If it is and it has just been opened, update the shader index and screen instance reference
        if(screen != lastScreen) {
            lastScreen = screen;
            currentJuliaIndex = rnd.nextInt(4) + 2;
        }
        final PostChain chain = mc.getShaderManager().getPostChain(
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "julia_" + currentJuliaIndex),
            Set.of(Identifier.fromNamespaceAndPath("minecraft", "main"))
        );
        if(chain == null) {
            EngineerSBliss.LOGGER.error("Julia post chain is null");
            return;
        }

        final CrossFrameResourcePool pool = ((GameRendererAccessor)this).getResourcePool();
        chain.process(mc.getMainRenderTarget(), pool);
    }
}