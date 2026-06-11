package com.snek.engineersbliss.client.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRendering;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderingImpl;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;




@Mixin(FluidRenderingImpl.class)
public class RenderFilterFluidMixin {
    private RenderFilterFluidMixin() { }



    @Inject(
        method = "render",
        at = @At("HEAD"),
        cancellable = true
    )
	private static void render(
        final FluidRenderer fluidRenderer,
        final FluidRenderHandler handler,
        final BlockAndTintGetter level,
        final BlockPos pos,
        final FluidRenderer.Output output,
        final BlockState blockState,
        final FluidState fluidState,
        final FluidRendering.DefaultRenderer defaultRenderer,
        final CallbackInfo ci
    ) {
        if(!RenderFilterHandler.getActiveBlocks().contains(blockState.getBlock())) {
            ci.cancel();
        }
    }



    @Inject(
        method = "renderDefault",
        at = @At("HEAD"),
        cancellable = true
    )
	private static void renderDefault(
        final FluidRenderer fluidRenderer,
        final FluidRenderHandler handler,
        final BlockAndTintGetter level,
        final BlockPos pos,
        final FluidRenderer.Output output,
        final BlockState blockState,
        final FluidState fluidState,
        final CallbackInfo ci
    ) {
        if(!RenderFilterHandler.getActiveBlocks().contains(blockState.getBlock())) {
            ci.cancel();
        }
	}



    @Inject(
        method = "renderVanillaDefault",
        at = @At("HEAD"),
        cancellable = true
    )
	private static void renderVanillaDefault(
        final FluidRenderer fluidRenderer,
        final BlockAndTintGetter level,
        final BlockPos pos,
        final FluidRenderer.Output output,
        final BlockState blockState,
        final FluidState fluidState,
        final CallbackInfo ci
    ) {
        if(!RenderFilterHandler.getActiveBlocks().contains(blockState.getBlock())) {
            ci.cancel();
        }
	}
}


//FIXME avoid culling on fluid surfaces