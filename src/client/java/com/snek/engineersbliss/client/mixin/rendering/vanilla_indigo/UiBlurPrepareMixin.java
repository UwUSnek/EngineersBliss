package com.snek.engineersbliss.client.mixin.rendering.vanilla_indigo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.client.ui.renderer.UiBlur;

import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.GameRenderer;




@Mixin(GuiRenderer.class)
public abstract class UiBlurPrepareMixin {

    // @Inject (method = "render", at = @At("HEAD"), cancellable = false, require = 1)
    // private void eb$prepareUiBlur(final CallbackInfo ci) {
        // UiBlur.prepare();
    // }

    //BUG this 2nd version works for full screen blur but idk if its good for partial blurs. first version hides in-game HUD
    @Redirect (method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;processBlurEffect()V"))
    private void eb$sampleUiBlur(final GameRenderer gameRenderer) {
        UiBlur.prepare();
    }
}
