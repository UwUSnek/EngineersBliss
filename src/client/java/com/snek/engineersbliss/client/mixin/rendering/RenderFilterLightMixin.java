package com.snek.engineersbliss.client.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;




@Mixin(LightEngine.class)
public class RenderFilterLightMixin {

    @Inject(method = "getOpacity", at = @At("HEAD"), cancellable = true)
    public void getOpacity(final BlockState state, final CallbackInfoReturnable<Integer> cir) {
        if(!RenderFilterHandler.getActiveBlocks().contains(state.getBlock())) {
            cir.setReturnValue(0);
        }
    }
}