package com.snek.engineersbliss.client.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;



/**
 * This mixin intercepts getBlockState calls in RenderSectionRegion, replacing hidden blocks with air.
 * ! This covers rendering, AO calculation and face culling logic.
 * ! This covers normal blocks, block entities, and fluids.
 * ! Most render mods pass through here, so the filter is automatically compatible with them.
 */
@Mixin(RenderSectionRegion.class)
public class RenderFilterBlockMixin {
    @Inject(
        method = "getBlockState",
        at = @At("RETURN"),
        cancellable = true
    )
    public void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = cir.getReturnValue();
        if(!RenderFilterHandler.getActiveBlocks().contains(state.getBlock())) {
            cir.setReturnValue(Blocks.AIR.defaultBlockState());
        }
    }
}