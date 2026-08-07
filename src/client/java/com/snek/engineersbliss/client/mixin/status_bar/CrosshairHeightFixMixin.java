package com.snek.engineersbliss.client.mixin.status_bar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.screens.status_bar.StatusBarRenderer;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

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

        //! Position=TOP doesn't require any fix.
        if(!ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.STATUS_BAR_POSITION)) {
            return instance.guiHeight() + ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.STATUS_BAR_HEIGHT);
        }
        else {
            return instance.guiHeight();
        }
    }
}
