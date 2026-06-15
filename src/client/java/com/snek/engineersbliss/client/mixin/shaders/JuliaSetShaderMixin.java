package com.snek.engineersbliss.client.mixin.shaders;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.screens.alt_textures.AltTexturesScreen;
import com.snek.engineersbliss.EngineerSBliss;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.snek.engineersbliss.client.mixin.accessors.GameRendererAccessor;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;

// @Mixin(GameRenderer.class)
// public abstract class JuliaSetShaderMixin {


//     @Inject(method = "render", at = @At("TAIL"))
//     private void renderJulia(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
//         Minecraft mc = Minecraft.getInstance();
//         if (!(mc.screen instanceof AltTexturesScreen)) return;

//         PostChain chain = mc.getShaderManager().getPostChain(
//             Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "julia"),
//             LevelTargetBundle.MAIN_TARGETS
//         );
//         if (chain == null) {
//             EngineerSBliss.LOGGER.error("Failed to load Julia post chain!");
//             return;
//         }

//         CrossFrameResourcePool pool = ((GameRendererAccessor)(Object)this).getResourcePool();
//         chain.process(mc.getMainRenderTarget(), pool);
//     }
// }


// @Mixin(net.minecraft.client.gui.screens.Screen.class)
// public abstract class JuliaSetShaderMixin {

//     @Inject(method = "extractRenderState", at = @At("TAIL"))
//     private void onScreenRender(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//         Minecraft mc = Minecraft.getInstance();
//         if (!(mc.screen instanceof AltTexturesScreen)) return;

//         PostChain chain = mc.getShaderManager().getPostChain(
//             Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "julia"),
//             Set.of(Identifier.fromNamespaceAndPath("minecraft", "main"))
//         );
//         if (chain == null) {
//             EngineerSBliss.LOGGER.error("Julia post chain is null");
//             return;
//         }

//         CrossFrameResourcePool pool = ((GameRendererAccessor)(Object)mc.gameRenderer).getResourcePool();
//         chain.process(mc.getMainRenderTarget(), pool);
//         mc.getMainRenderTarget().blitToScreen();
//     }
// }


@Mixin(GameRenderer.class)
public abstract class JuliaSetShaderMixin {

    @Inject(method = "render", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/render/GuiRenderer;endFrame()V"
    ))
    private void onAfterGuiRender(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.screen instanceof AltTexturesScreen)) return;

        PostChain chain = mc.getShaderManager().getPostChain(
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "julia"),
            Set.of(Identifier.fromNamespaceAndPath("minecraft", "main"))
        );
        if (chain == null) {
            EngineerSBliss.LOGGER.error("Julia post chain is null");
            return;
        }

        CrossFrameResourcePool pool = ((GameRendererAccessor)this).getResourcePool();
        chain.process(mc.getMainRenderTarget(), pool);
    }
}