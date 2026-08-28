package com.snek.engineersbliss.client.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.CharacterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class TestMixin {
    @Inject(method = "charTyped(JLnet/minecraft/client/input/CharacterEvent;)V", at = @At("HEAD"))
    private void engineersbliss$logCharTyped(long handle, CharacterEvent event, CallbackInfo ci) {
        System.out.println("charTyped codepoint=" + event.codepoint() + " char=" + (char) event.codepoint());
    }
}//TODO REMOVE