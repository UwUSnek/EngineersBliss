package com.snek.engineersbliss.client.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.Minecraft;




@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Invoker("pick")
    void invokePick(float partialTicks);

    @Invoker("startAttack")
    boolean invokeStartAttack();

    @Invoker("startUseItem")
    void invokeStartUseItem();
}