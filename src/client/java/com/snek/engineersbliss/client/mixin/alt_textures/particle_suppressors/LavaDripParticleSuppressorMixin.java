package com.snek.engineersbliss.client.mixin.alt_textures.particle_suppressors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.LavaFluid;




/**
 * A mixin that stops lava blocks from emitting drip particles.
 */
@Mixin(LavaFluid.class)
public class LavaDripParticleSuppressorMixin {
    private LavaDripParticleSuppressorMixin() {}


    @SuppressWarnings("unused")
    @Inject(method = "getDripParticle", at = @At("HEAD"), cancellable = true, require = 1)
	private void eb$getDripParticle(final CallbackInfoReturnable<ParticleOptions> cir) {
        if(ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.NO_DRIP_PARTICLES)) {
            cir.setReturnValue(null);
        }
	}
}
