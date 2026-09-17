package com.snek.engineersbliss.client.mixin.status_bar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.snek.engineersbliss.client.feature_handlers.status_bar.StatusBarHandler;

//! Simple name change in 26.2
//? if <=26.1.2 {
    /*import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
*///? } else {
    import net.minecraft.client.gui.contextualbar.ContextualBar;
//? }




//! Simple name change in 26.2
//? if <=26.1.2 {
    /*@Mixin(ContextualBarRenderer.class)
*///? } else {
    @Mixin(ContextualBar.class)
//? }
public interface VanillaContextualBarHeightChangerMixin {

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