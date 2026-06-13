package com.snek.engineersbliss.client.mixin.rendering.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.snek.engineersbliss.client.rendering.RenderFilterHandler;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




/**
 * Sodium equivalent of RenderFilterBlockMixin.
 * ! This covers rendering, AO calculation and face culling logic for Sodium's chunk compiler.
 */
@Mixin(LevelSlice.class)
public class RenderFilterBlockSodiumMixin {

    @Inject(
        method = "getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;",
        at = @At("RETURN"),
        cancellable = true,
        remap = false
    )
    public void getBlockState(int blockX, int blockY, int blockZ, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = cir.getReturnValue();
        if(state == null) return;


        //! Block vanilla and return if rendering is disabled
        if(
            !RenderFilterHandler.getRenderFluids()        && !state.getFluidState().isEmpty() ||
            !RenderFilterHandler.getRenderBlockEntities() && state.hasBlockEntity()           ||
            !RenderFilterHandler.getRenderBlocks()        && state.getFluidState().isEmpty() && !state.hasBlockEntity()
        ) {
            cir.setReturnValue(Blocks.AIR.defaultBlockState());
            return;
        }


        // If rendering of the block category is enabled, check the individual filters
        if(!RenderFilterHandler.getActiveBlocks().contains(state.getBlock())) {
            cir.setReturnValue(Blocks.AIR.defaultBlockState());
        }
    }
}

//TODO add Iris compatibility. maybe shaders too?