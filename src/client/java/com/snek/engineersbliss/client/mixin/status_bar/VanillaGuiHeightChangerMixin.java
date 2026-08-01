package com.snek.engineersbliss.client.mixin.status_bar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.snek.engineersbliss.client.screens.status_bar.StatusBarRenderer;

import net.minecraft.client.gui.GuiGraphicsExtractor;




/**
 * A mixin that shrinks the reported GUI height to leave space for the status bar.
 * This doesn't catch everything, mods can still read the actual height of the window and draw on top of the status bar, but it's the best option.
 */
@Mixin(GuiGraphicsExtractor.class)
public class VanillaGuiHeightChangerMixin {


    @SuppressWarnings("unused")
    @ModifyReturnValue(method = "guiHeight", at = @At("RETURN"), require = 1)
    private int eb$guiHeight(int original) {
        return original - StatusBarRenderer.STATUS_BAR_HEIGHT;
    }
}
