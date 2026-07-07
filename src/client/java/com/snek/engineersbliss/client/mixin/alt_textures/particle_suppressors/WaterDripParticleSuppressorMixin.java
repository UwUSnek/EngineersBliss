package com.snek.engineersbliss.client.mixin.alt_textures.particle_suppressors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.WaterFluid;




/**
 * A mixin that stops water blocks from emitting drip particles.
 */
@Mixin(WaterFluid.class)
public class WaterDripParticleSuppressorMixin {
    private WaterDripParticleSuppressorMixin() {}


    @SuppressWarnings("unused")
    @Inject(method = "getDripParticle", at = @At("HEAD"), cancellable = true, require = 1)
	private void getDripParticle(final CallbackInfoReturnable<ParticleOptions> cir) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.NO_DRIP_PARTICLES)) {
            cir.setReturnValue(null);
        }
	}
}
