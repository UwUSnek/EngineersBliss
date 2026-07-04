package com.snek.engineersbliss.client.mixin.rendering.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderFilterHandler;

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


    @SuppressWarnings("unused")
    @Inject(
        method = "getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;",
        at = @At("RETURN"),
        cancellable = true,
        require = 0
    )
    private void getBlockState(final int blockX, final int blockY, final int blockZ, final CallbackInfoReturnable<BlockState> cir) {
        final BlockState state = cir.getReturnValue();

        if(!RenderFilterHandler.shouldBlockRender(state)) {
            cir.setReturnValue(Blocks.AIR.defaultBlockState());
        }
    }
}

//TODO add Iris compatibility. maybe shaders too?