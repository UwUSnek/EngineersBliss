package com.snek.engineersbliss.client.mixin.status_bar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.client.feature_handlers.status_bar.StatusBarHandler;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;




@Mixin(Gui.class)
public class CrosshairHeightFixMixin {

    @SuppressWarnings("unused")
    @Redirect(
        method = "extractCrosshair",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiHeight()I"
        ),
        require = 2
    )
    private int eb$restoreCrosshairHeight(GuiGraphicsExtractor instance) {
        if(StatusBarHandler.shouldRender() && StatusBarHandler.isBottom()) {
            return instance.guiHeight() + StatusBarHandler.getHeight();
        }
        else {
            return instance.guiHeight();
        }
    }
}
