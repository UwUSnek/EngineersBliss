package com.snek.engineersbliss.client.mixin.alt_textures.particle_suppressors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;




/**
 * A mixin that stops campfire blocks from emitting particles.
 * Soul campfires are just a special type of campfires, so this covers both.
 */
@Mixin(CampfireBlock.class)
public class CampfireBlockParticleSuppressorMixin {


    @Redirect(
        method = "animateTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
        ),
        require = 1
    )
    private void redirectAnimateTickParticle(
        final Level level,
        final ParticleOptions particle,
        final double x, final double y, final double z,
        final double xSpeed, final double ySpeed, final double zSpeed
    ) {
        if(!AltTexturesHandler.getFeature(AltTextureFeature.NO_CAMPFIRE_PARTICLES)) {
            level.addParticle(particle, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }




    // Block makeParticles (called by dowse function)
    @SuppressWarnings("unused")
    @Inject(method = "makeParticles", at = @At("HEAD"), cancellable = true, require = 1)
	private static void makeParticles(
        final Level level,
        final BlockPos pos,
        final boolean isSignalFire,
        final boolean smoking,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.NO_CAMPFIRE_PARTICLES)) {
            ci.cancel();
        }
    }
}
