package com.snek.engineersbliss.client.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;








@Mixin(ParticleEngine.class)
public class AllParticlesSuppressor {

    // // @SuppressWarnings("unused")
    // // @Inject(
    // //     method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
    // //     at = @At("HEAD"), cancellable = true, require = 1
    // // )
    // // private void eb$addParticle(
    // //     final ParticleOptions particle,
    // //     final double x,
    // //     final double y,
    // //     final double z,
    // //     final double xd,
    // //     final double yd,
    // //     final double zd,
    // //     CallbackInfo ci
    // // ) {
    // //     if(!RenderingFilterHandler.getRenderParticles()) ci.cancel();
    // // }




    // // @SuppressWarnings("unused")
    // // @Inject(
    // //     method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)V",
    // //     at = @At("HEAD"), cancellable = true, require = 1
    // // )
    // // private void eb$addParticle(
    // //     final ParticleOptions particle,
    // //     final boolean overrideLimiter,
    // //     final boolean alwaysShow,
    // //     final double x,
    // //     final double y,
    // //     final double z,
    // //     final double xd,
    // //     final double yd,
    // //     final double zd,
    // //     CallbackInfo ci
    // // ) {
    // //     if(!RenderingFilterHandler.getRenderParticles()) ci.cancel();
    // // }




    // // @SuppressWarnings("unused")
    // // @Inject(
    // //     method = "addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
    // //     at = @At("HEAD"), cancellable = true, require = 1
    // // )
    // // private void eb$addAlwaysVisibleParticle(
    // //     final ParticleOptions particle,
    // //     final double x,
    // //     final double y,
    // //     final double z,
    // //     final double xd,
    // //     final double yd,
    // //     final double zd,
    // //     CallbackInfo ci
    // // ) {
    // //     if(!RenderingFilterHandler.getRenderParticles()) ci.cancel();
    // // }




    // // @SuppressWarnings("unused")
    // // @Inject(
    // //     method = "addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",
    // //     at = @At("HEAD"), cancellable = true, require = 1
    // // )
    // // private void eb$addAlwaysVisibleParticle(
    // //     final ParticleOptions particle,
    // //     final boolean overrideLimiter,
    // //     final double x,
    // //     final double y,
    // //     final double z,
    // //     final double xd,
    // //     final double yd,
    // //     final double zd,
    // //     CallbackInfo ci
    // // ) {
    // //     if(!RenderingFilterHandler.getRenderParticles()) ci.cancel();
    // // }




    // @SuppressWarnings("unused")
    // @Inject(method = "doAddParticle", at = @At("HEAD"), cancellable = true, require = 1)
	// private void eb$doAddParticle(
	// 	final ParticleOptions particle,
	// 	final boolean overrideLimiter,
	// 	final boolean alwaysShowParticles,
	// 	final double x,
	// 	final double y,
	// 	final double z,
	// 	final double xd,
	// 	final double yd,
	// 	final double zd,
    //     CallbackInfo ci
    // ) {
    //     if(!RenderingFilterHandler.getRenderParticles()) ci.cancel();
    // }




    @SuppressWarnings("unused")
    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true, require = 1)
	private void eb$createParticle(
		final ParticleOptions options,
        final double x,
        final double y,
        final double z,
        final double xa,
        final double ya,
        final double za,
        final CallbackInfoReturnable<Particle> cir
	) {
        if(!RenderingFilterHandler.getRenderParticles()) cir.setReturnValue(null);
    }


    @SuppressWarnings("unused")
    @Inject(method = "add", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$add(final Particle p, final CallbackInfo ci) {
        if(!RenderingFilterHandler.getRenderParticles()) ci.cancel();
    }
}