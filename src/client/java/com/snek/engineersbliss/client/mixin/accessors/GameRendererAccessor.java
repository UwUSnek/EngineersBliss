package com.snek.engineersbliss.client.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;

import net.minecraft.client.renderer.GameRenderer;




@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Accessor
    CrossFrameResourcePool getResourcePool();
}