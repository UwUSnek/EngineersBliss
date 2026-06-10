package com.snek.engineersbliss.client.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.rendering.FilteredBlockAndTintGetter;
import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AltModelBlockRendererImpl;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;




@Mixin(AltModelBlockRendererImpl.class)
public class RenderFilterModelMixin {

    @ModifyVariable(
        method = "tesselateBlock",
        at = @At("HEAD"),
        argsOnly = true,
        remap = false
    )
    public BlockAndTintGetter wrapLevel(BlockAndTintGetter level) {
        return new FilteredBlockAndTintGetter(level);
    }



    @Inject(
        method = "tesselateBlock",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    public void tesselateBlock(QuadEmitter output, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState blockState, BlockStateModel model, long seed, CallbackInfo ci) {
        if (!RenderFilterHandler.getActiveBlocks().contains(blockState.getBlock())) {
            ci.cancel();
        }
    }

}