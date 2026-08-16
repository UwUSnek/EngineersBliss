package com.snek.engineersbliss.client.mixin.status_bar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.snek.engineersbliss.client.feature_handlers.status_bar.StatusBarHandler;

import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;




@Mixin(ContextualBarRenderer.class)
public interface VanillaContextualBarHeightChangerMixin {

    @ModifyReturnValue(method = "top", at = @At("RETURN"), require = 1)
    default int eb$top(int original) {
        if(StatusBarHandler.shouldRender() && StatusBarHandler.isBottom()) {
            return original - StatusBarHandler.getHeight();
        }
        else {
            return original;
        }
    }
}