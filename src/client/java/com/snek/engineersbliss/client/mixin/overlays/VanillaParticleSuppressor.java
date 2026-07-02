package com.snek.engineersbliss.client.mixin.overlays;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;




@Mixin(ClientLevel.class)
public class VanillaParticleSuppressor {

    @SuppressWarnings("unused")
    @Inject(method = "getMarkerParticleTarget", at = @At("HEAD"), cancellable = true, require = 1)
	private void getMarkerParticleTarget(CallbackInfoReturnable<Block> cir) {
        if(OverlaysHandler.getFeature(OverlayFeature.BETTER_BARRIER_DISPLAY)) {
            final @NotNull ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
            if(stack.getItem() == Items.BARRIER) cir.setReturnValue(null);
        }
        if(OverlaysHandler.getFeature(OverlayFeature.BETTER_LIGHT_BLOCK_DISPLAY)) {
            final @NotNull ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
            if(stack.getItem() == Items.LIGHT) cir.setReturnValue(null);
        }
    }
}
