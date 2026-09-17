package com.snek.engineersbliss.client.mixin.status_bar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.snek.engineersbliss.client.feature_handlers.status_bar.StatusBarHandler;
//? if <=26.1.2 {
    /*import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
*///? } else {
    import net.minecraft.client.gui.contextualbar.ContextualBar;
//? }




//? if <=26.1.2 {
    /*@Mixin(ContextualBarRenderer.class)
*///? } else {
    @Mixin(ContextualBar.class)
//? }
public interface VanillaContextualBarHeightChangerMixin {
    //! <= 26.1.2 and 26.2+ use the same method and class.
    //! The only difference is that the old ContextualBarRenderer got renamed to ContextualBar.

    @ModifyReturnValue(method = "top", at = @At("RETURN"), require = 1)
    default int eb$top(int original) {
        if(StatusBarHandler.shouldRender() && StatusBarHandler.isBottom()) {
            return original - StatusBarHandler.getVanillaHeight();
        }
        else {
            return original;
        }
    }
}