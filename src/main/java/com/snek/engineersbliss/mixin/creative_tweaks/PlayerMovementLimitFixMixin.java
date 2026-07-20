package com.snek.engineersbliss.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;




/**
 * This mixin makes the server skip the "Player <name> moved too fast!" check.
 * ! This feature is required for the custom creative flying speed feature to work on dedicated servers.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class PlayerMovementLimitFixMixin {
    @Shadow public ServerPlayer player;


    @SuppressWarnings("unused")
    @Inject(method = "shouldCheckPlayerMovement", at = @At("HEAD"), cancellable = true, require = 1)
    private void eb$shouldCheckPlayerMovement(final boolean isFallFlying, final CallbackInfoReturnable<Boolean> cir) {
        if(player.isCreative()) {
            cir.setReturnValue(false);
        }
    }
}