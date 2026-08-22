package com.snek.engineersbliss.client.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;



//FIXME this might not work with that one popular light engine optimization mod
//FIXME check if it doesn't work, add a custom mixin for it if that's the case

@Mixin(LightEngine.class)
public class RenderingFilterLightMixin {

    @Inject(method = "getOpacity", at = @At("HEAD"), cancellable = true, require = 1)
    public void eb$getOpacity(final BlockState state, final CallbackInfoReturnable<Integer> cir) {

        if(!RenderingFilterHandler.shouldStateRender(state)) {
            cir.setReturnValue(1);
            //! Light opacity 0 lets light propagate indefinitely
            //! Light opacity 1 dampens light by 1 ech block travelled, like Air
        }
    }
}