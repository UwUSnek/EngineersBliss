package com.snek.engineersbliss.client.mixin.alt_textures.particle_suppressors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;




/**
 * A mixin that stops campfire block entities from emitting particles.
 * Soul campfires are just a special type of campfires, so this covers both.
 */
@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityParticleSuppressorMixin {
    private CampfireBlockEntityParticleSuppressorMixin() {}


    @SuppressWarnings("unused")
    @Inject(method = "particleTick", at = @At("HEAD"), cancellable = true, require = 1)
	private static void particleTick(final Level level, final BlockPos pos, final BlockState state, final CampfireBlockEntity entity, final CallbackInfo ci) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.NO_CAMPFIRE_PARTICLES)) {
            ci.cancel();
        }
    }
}
