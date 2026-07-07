package com.snek.engineersbliss.client.mixin.alt_textures.particle_suppressors.redstone_dust;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;




/**
 * One of the mixins that stop redstone blocks from emitting red dust particles.
 */
@Mixin(LeverBlock.class)
public class LeverDustParticleSuppressorMixin {
    private LeverDustParticleSuppressorMixin() {}


    //! Levers have a "makeParticle" static method all particle logic is delegated to, so it can get away with a simple inject
    @SuppressWarnings("unused")
    @Inject(method = "makeParticle", at = @At("HEAD"), cancellable = true, require = 1)
    private static void makeParticle(
        final BlockState state,
        final LevelAccessor level,
        final BlockPos pos,
        final float scale,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.NO_REDSTONE_DUST_PARTICLES)) {
            ci.cancel();
        }
    }
}
