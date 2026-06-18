package com.snek.engineersbliss.client.mixin.accessors;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;




@Mixin(BlockEntityRenderers.class)
public interface BlockEntityRenderersAccessor {
    @Accessor("PROVIDERS")
    static Map<BlockEntityType<?>, BlockEntityRendererProvider<?, ?>> getProviders() {
        throw new AssertionError();
    }
}