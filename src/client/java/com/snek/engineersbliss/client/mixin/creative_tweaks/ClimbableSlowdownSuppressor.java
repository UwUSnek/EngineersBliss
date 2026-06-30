package com.snek.engineersbliss.client.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;






/**
 * This mixin stops the player from being slowed down horizontally by climbable blocks.
 * This is done by storing the original delta value and resetting the X and Z components with it after LivingEntity.handleOnClimbable 's logic
 */
@Mixin(LivingEntity.class)
public class ClimbableSlowdownSuppressor {
    @Unique private static Vec3 originalDelta;




    @Inject(method = "handleOnClimbable", at = @At("HEAD"))
    private void captureOriginal(final Vec3 delta, final CallbackInfoReturnable<Vec3> cir) {
        if((Object)this instanceof Player) {
            originalDelta = delta;
        }
    }




    @Inject(method = "handleOnClimbable", at = @At("RETURN"), cancellable = true)
    private void handleOnClimbable(final Vec3 delta, final CallbackInfoReturnable<Vec3> cir) {
        final LivingEntity _this = (LivingEntity)(Object)this;


        if(_this.onClimbable()) {
            final BlockState state = _this.getInBlockState();


            // Handle Ladders
            if(state.is(Blocks.LADDER) && CreativeTweaksServerHandler.playerHasFeature(_this, CreativeTweakFeature.DISABLE_LADDER_SLOWDOWN)) {
                Vec3 clamped = cir.getReturnValue();
                cir.setReturnValue(new Vec3(originalDelta.x, clamped.y, originalDelta.z));
            }


            // Handle Vines
            if(state.is(Blocks.VINE) && CreativeTweaksServerHandler.playerHasFeature(_this, CreativeTweakFeature.DISABLE_VINES_SLOWDOWN)) {
                Vec3 clamped = cir.getReturnValue();
                cir.setReturnValue(new Vec3(originalDelta.x, clamped.y, originalDelta.z));
            }


            // Handle Twisting Vines
            if(
                (state.is(Blocks.TWISTING_VINES) || state.is(Blocks.TWISTING_VINES_PLANT)) &&
                CreativeTweaksServerHandler.playerHasFeature(_this, CreativeTweakFeature.DISABLE_TWISTING_VINES_SLOWDOWN)
            ) {
                Vec3 clamped = cir.getReturnValue();
                cir.setReturnValue(new Vec3(originalDelta.x, clamped.y, originalDelta.z));
            }


            // Handle Weeping Vines
            if(
                (state.is(Blocks.WEEPING_VINES) || state.is(Blocks.WEEPING_VINES_PLANT)) &&
                CreativeTweaksServerHandler.playerHasFeature(_this, CreativeTweakFeature.DISABLE_WEEPING_VINES_SLOWDOWN)
            ) {
                Vec3 clamped = cir.getReturnValue();
                cir.setReturnValue(new Vec3(originalDelta.x, clamped.y, originalDelta.z));
            }
        }
    }
}
