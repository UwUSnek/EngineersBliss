package com.snek.engineersbliss.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;



/**
 * Server-side feature mixin.
 * This mixin has an identical counterpart on the client in order to guarantee identical logic calculations and minimize client desynchronization.
 */
@Mixin(SlimeBlock.class)
public class SlimeFeaturesMixin {


    /**
     * Disable slime slowdown by removing the stepOn function.
     * Superclass stepOn is no-op so cancelling the entire call is enough.
     * ! Slime blocks have custom friction. This is handled by the FrictionFeaturesMixin mixin, together with the other blocks with custom friction values.
     */
    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    public void stepOn(final Level level, final BlockPos pos, final BlockState onState, final Entity entity, final CallbackInfo ci) {
        if(level.isClientSide()) return;
        if(entity instanceof Player player) {
            final long featureMask = CreativeTweaksServerHandler.getToggleFeatures(player);
            if(CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN.hasFlagBit(featureMask)) {
                ci.cancel();
            }
        }
    }

    /**
     * Disable slime bouncing behaviour by removing the bounceUp function.
     * This function is exclusive to Slime Blocks and only handles bouncing, so it can be fully removed safely.
     */
    @Inject(method = "bounceUp", at = @At("HEAD"), cancellable = true)
    public void bounceUp(final Entity entity, final CallbackInfo ci) {
        if(!entity.level().isClientSide()) return;
        if(entity instanceof Player player) {
            final long featureMask = CreativeTweaksServerHandler.getToggleFeatures(player);
            if(CreativeTweakFeature.DISABLE_SLIME_BOUNCE.hasFlagBit(featureMask)) {
                ci.cancel();
            }
        }
    }
}
