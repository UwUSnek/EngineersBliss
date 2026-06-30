package com.snek.engineersbliss.client.mixin.creative_tweaks.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;




/**
 * This mixin cancels block collisions if the player is flying in creative mode.
 * This is done by replacing the collide logic with a no-op, so the movement is never altered and the player can freely travel through blocks.
 * ! This is required for the feature to work.
 * ! This check runs separately from the LocalPlayer.suffocatesAt block pushing effect and the server's ServerGamePacketListenerImpl.isEntityCollidingWithAnythingNew check.
 */
@Mixin(Entity.class)
public class ClientBlockPhasingMixin {


    @Inject(method = "collide", at = @At("HEAD"), cancellable = true, require = 1)
	private void collide(final Vec3 movement, final CallbackInfoReturnable<Vec3> cir) {
        if(CreativeTweaksHandler.shouldPlayerPhaseThroughBlocks()) {
            cir.setReturnValue(movement);
        }
    }
}
