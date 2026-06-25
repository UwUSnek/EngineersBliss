package com.snek.engineersbliss.mixin.creative_tweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;




@Mixin(ServerGamePacketListenerImpl.class)
public class PlayerMovementLimitFixMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "shouldCheckPlayerMovement", at = @At("HEAD"), cancellable = true)
    private void shouldCheckPlayerMovement(boolean isFallFlying, CallbackInfoReturnable<Boolean> cir) {
        if(player.getAbilities().instabuild) {
            cir.setReturnValue(false);
        }
    }
}