package com.snek.engineersbliss.client.mixin.alt_textures.particle_suppressors.redstone_dust;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneOreBlock;




/**
 * One of the mixins that stop redstone blocks from emitting red dust particles.
 * RedstoneOreBlock covers the normal ore and the deepslate variant, so this mixin is enough for both.
 */
@Mixin(RedStoneOreBlock.class)
public class OreDustParticleSuppressorMixin {
    private OreDustParticleSuppressorMixin() {}


    //! Similarly to levers, Ores have a "makeParticle" static method all particle logic is delegated to, so they can get away with a simple inject
    @SuppressWarnings("unused")
    @Inject(method = "spawnParticles", at = @At("HEAD"), cancellable = true, require = 1)
	private static void eb$spawnParticles(
        final Level level,
        final BlockPos pos,
        final CallbackInfo ci
    ) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.NO_REDSTONE_DUST_PARTICLES)) {
            ci.cancel();
        }
    }
}
