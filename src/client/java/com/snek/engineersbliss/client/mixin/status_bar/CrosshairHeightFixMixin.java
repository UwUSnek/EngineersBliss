package com.snek.engineersbliss.client.mixin.status_bar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.client.feature_handlers.status_bar.StatusBarHandler;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.GuiGraphicsExtractor;
//? if <=26.1.2 {
    import net.minecraft.client.gui.Gui;
//? } else {
    /*import net.minecraft.client.gui.Hud;
*///? }




//? if <=26.1.2 {
    @Mixin(Gui.class)
//? } else {
    /*@Mixin(Hud.class)
*///? }
public class CrosshairHeightFixMixin {

    @SuppressWarnings("unused")
    @Redirect(
        method = "extractCrosshair",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiHeight()I"
            //! <= 26.1.2 and 26.2+ use the same method and GuiGraphicsExtractor.guiHeight stays unchanged.
            //! The only difference is that the old Gui got renamed to Hud. new Gui is a different class.
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
