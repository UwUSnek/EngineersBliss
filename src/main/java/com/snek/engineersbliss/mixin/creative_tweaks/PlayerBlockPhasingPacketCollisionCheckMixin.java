package com.snek.engineersbliss.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;



@Mixin(ServerGamePacketListenerImpl.class)
public class PlayerBlockPhasingPacketCollisionCheckMixin {


    //TODO remove, prob doesnt do anything
    @Inject(method = "isEntityCollidingWithAnythingNew", at = @At("HEAD"), cancellable = true, require = 1)
    private void isEntityCollidingWithAnythingNew(
        LevelReader level, Entity entity, AABB oldAABB,
        double newX, double newY, double newZ,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if(entity instanceof Player player) {
            long featureMask = CreativeTweaksServerHandler.getToggleFeatures(player);
            if(CreativeTweakFeature.PHASE_THROUGH_BLOCKS_FLY.hasFlagBit(featureMask)) {
                if(player.getAbilities().instabuild && player.getAbilities().flying) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}