package com.snek.engineersbliss.client.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;




@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
    @Accessor("levelRenderState")
    LevelRenderState getLevelRenderState();
}