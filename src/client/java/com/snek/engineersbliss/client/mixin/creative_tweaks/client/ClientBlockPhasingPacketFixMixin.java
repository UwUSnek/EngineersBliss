package com.snek.engineersbliss.client.mixin.creative_tweaks.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;




/**
 * This mixin stops the server-side collision checks done when a movement packet is received to allow players to freely fly through solid blocks.
 * ! This is required for the feature to work.
 * ! This check runs separately from client-side LocalPlayer.suffocatesAt block pushing effect and the Entity.collide movement calculation.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class ClientBlockPhasingPacketFixMixin {


    @SuppressWarnings("unused")
    @Inject(method = "isEntityCollidingWithAnythingNew", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$isEntityCollidingWithAnythingNew(
        final LevelReader level, final Entity entity, final AABB oldAABB,
        final double newX, final double newY, final double newZ,
        final CallbackInfoReturnable<Boolean> cir
    ) {
        if(CreativeTweaksHandler.shouldPlayerPhaseThroughBlocks(entity)) {
            cir.setReturnValue(false);
        }
    }
}