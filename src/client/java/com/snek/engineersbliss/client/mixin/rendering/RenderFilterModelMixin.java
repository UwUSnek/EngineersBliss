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
        argsOnly = true
    )
    public BlockAndTintGetter wrapLevel(final BlockAndTintGetter level) {
        return new FilteredBlockAndTintGetter(level);
    }



    @Inject(
        method = "tesselateBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    public void tesselateBlock(
        final QuadEmitter output,
        final float x, final float y, final float z,
        final BlockAndTintGetter level, final BlockPos pos,
        final BlockState blockState,
        final BlockStateModel model,
        final long seed,
        final CallbackInfo ci
    ) {
        if(!RenderFilterHandler.getActiveBlocks().contains(blockState.getBlock())) {
            ci.cancel();
        }
    }

}