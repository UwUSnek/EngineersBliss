package com.snek.engineersbliss.client.mixin.status_bar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.status_bar.StatusBarHandler;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
//? if <=26.1.2 {
    /*import net.minecraft.client.gui.Gui;
*///? } else {
    import net.minecraft.client.gui.Hud;
//? }




//? if <=26.1.2 {
    /*@Mixin(Gui.class)
*///? } else {
    @Mixin(Hud.class)
//? }
public class VanillaHotbarHeightChangerMixin {
    //! <= 26.1.2 and 26.2+ use the same method and class.
    //! The only difference is that the old Gui got renamed to Hud. New Gui is a different class.

    @SuppressWarnings("unused")
    @Inject(method = "extractHotbarAndDecorations", at = @At("HEAD"))
    private void eb$pushShift(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (StatusBarHandler.shouldRender() && StatusBarHandler.isBottom()) {
            graphics.pose().pushMatrix();
            graphics.pose().translate(0f, -(float)StatusBarHandler.getVanillaHeight());
        }
    }

    @SuppressWarnings("unused")
    @Inject(method = "extractHotbarAndDecorations", at = @At("RETURN"))
    private void eb$popShift(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (StatusBarHandler.shouldRender() && StatusBarHandler.isBottom()) {
            graphics.pose().popMatrix();
        }
    }
}