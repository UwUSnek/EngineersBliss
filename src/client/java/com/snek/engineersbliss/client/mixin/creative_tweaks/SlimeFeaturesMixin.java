package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;



/**
 * Client counterpart of the server's feature mixin.
 * This prevents incorrect client-side calculations done before the server syncs up.
 * Sometimes Vanilla client logic can block server-side features because of how Minecraft handles movement synchronization.
 */
@Mixin(SlimeBlock.class)
public class SlimeFeaturesMixin {


    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    public void stepOn(final Level level, final BlockPos pos, final BlockState onState, final Entity entity, final CallbackInfo ci) {
        if(!level.isClientSide()) return;
        if(entity instanceof Player) {
            if(CreativeTweaksHandler.hasFeature(CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "bounceUp", at = @At("HEAD"), cancellable = true)
    public void bounceUp(final Entity entity, final CallbackInfo ci) {
        if(!entity.level().isClientSide()) return;
        if(entity instanceof Player) {
            if(CreativeTweaksHandler.hasFeature(CreativeTweakFeature.DISABLE_SLIME_BOUNCE)) {
                ci.cancel();
            }
        }
    }
}
