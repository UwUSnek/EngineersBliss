package com.snek.engineersbliss.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;




/**
 * This mixin stops the player from swimming, crawling, or crouching when phasing through solid blocks.
 * ! This prevents incorrect block placements caused by the forced player pose.
 */
@Mixin(Player.class)
public class BlockPhasingPoseFixMixin {

    @Inject(method = "canPlayerFitWithinBlocksAndEntitiesWhen", at = @At("HEAD"), cancellable = true, require = 1)
    private void canPlayerFitWithinBlocksAndEntitiesWhen(final Pose newPose, final CallbackInfoReturnable<Boolean> cir) {
        if(CreativeTweaksServerHandler.canPlayerPhaseThroughBlocks((Player)(Object)this)) {
            cir.setReturnValue(true);
        }
    }
}
