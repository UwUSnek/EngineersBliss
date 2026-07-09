package com.snek.engineersbliss.client.mixin.alt_textures.particle_suppressors.redstone_dust;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;




/**
 * One of the mixins that stop redstone blocks from emitting red dust particles.
 */
@Mixin(RedStoneWireBlock.class)
public class WireDustParticleSuppressorMixin {
    private WireDustParticleSuppressorMixin() {}


    //! Redstone Wire only has particles in its animateTick logic so it can get away with a simple inject
    @SuppressWarnings("unused")
    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true, require = 1)
	private void animateTick(
        final BlockState state,
        final Level level,
        final BlockPos pos,
        final RandomSource random,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.NO_REDSTONE_DUST_PARTICLES)) {
            ci.cancel();
        }
    }
}
