package com.snek.engineersbliss.client.mixin.custom_items;

import com.snek.engineersbliss.feature_handlers.custom_items.base.__base_CustomItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.world.entity.item.ItemEntity;




@Mixin(ItemEntityRenderer.class)
public abstract class FullBrightItemEntityMixin {

    @SuppressWarnings("unused")
    @Inject(method = "extractRenderState", at = @At("TAIL"), cancellable = false, require = 1)
    private void eb$extractRenderState(ItemEntity entity, ItemEntityRenderState state, float partialTicks, CallbackInfo ci) {
        if(entity.getItem().getItem() instanceof __base_CustomItem customItem && customItem.isFullBright()) {
            state.lightCoords = 0xF000F0;
        }
    }
}