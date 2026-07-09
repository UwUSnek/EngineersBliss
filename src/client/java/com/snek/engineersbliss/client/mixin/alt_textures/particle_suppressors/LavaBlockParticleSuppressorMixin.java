package com.snek.engineersbliss.client.mixin.alt_textures.particle_suppressors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.LavaFluid;




/**
 * A mixin that stops lava blocks from emitting particles.
 */
@Mixin(LavaFluid.class)
public class LavaBlockParticleSuppressorMixin {
    private LavaBlockParticleSuppressorMixin() {}


    @SuppressWarnings("unused")
    @Redirect(
        method = "animateTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
        ),
        require = 1
    )
    private void addParticle(
        final Level level,
        final ParticleOptions particle,
        final double x, final double y, final double z,
        final double xSpeed, final double ySpeed, final double zSpeed
    ) {
        if(!AltTexturesHandler.getFeature(AltTextureFeature.NO_LAVA_PARTICLES)) {
            level.addParticle(particle, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
