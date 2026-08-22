package com.snek.engineersbliss.client.mixin.creative_tweaks.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;




/**
 * This mixin stops the player from swimming, crawling, or crouching when phasing through solid blocks.
 * ! This prevents incorrect block placements caused by the forced player pose.
 */
@Mixin(Player.class)
public class ClientBlockPhasingPoseFixMixin {


    @SuppressWarnings("unused")
    @Inject(method = "canPlayerFitWithinBlocksAndEntitiesWhen", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$canPlayerFitWithinBlocksAndEntitiesWhen(final Pose newPose, final CallbackInfoReturnable<Boolean> cir) {
        if(CreativeTweaksClientHandler.shouldPlayerPhaseThroughBlocks(this)) {
            cir.setReturnValue(true);
        }
    }
}
