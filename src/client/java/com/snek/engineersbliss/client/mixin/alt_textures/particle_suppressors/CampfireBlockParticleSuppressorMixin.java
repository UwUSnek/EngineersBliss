package com.snek.engineersbliss.client.mixin.alt_textures.particle_suppressors;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;




/**
 * A mixin that stops campfire blocks from emitting particles.
 * Soul campfires are just a special type of campfires, so this covers both.
 */
@Mixin(CampfireBlock.class)
public class CampfireBlockParticleSuppressorMixin {
    @Shadow private boolean spawnParticles;



    // This function is just a copy of Vanilla's logic without the particle spawning part
    @SuppressWarnings("unused")
    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true, require = 1)
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random, final CallbackInfo ci) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.NO_CAMPFIRE_PARTICLES)) {
            ci.cancel();
            if(state.getValue(CampfireBlock.LIT).booleanValue()) {
                if(random.nextInt(10) == 0) {
                    level.playLocalSound(
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
                        0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F,
                        false
                    );
                }
            }
        }
    }


    // This function is just a copy of Vanilla's logic without the particle spawning part
    @SuppressWarnings("unused")
    @Inject(method = "dowse", at = @At("HEAD"), cancellable = true, require = 1)
    private static void dowse(@Nullable final Entity source, final LevelAccessor level, final BlockPos pos, final BlockState state, final CallbackInfo ci) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.NO_CAMPFIRE_PARTICLES)) {
            level.gameEvent(source, GameEvent.BLOCK_CHANGE, pos);
            ci.cancel();
        }
    }
}
