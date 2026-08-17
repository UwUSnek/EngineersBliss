package com.snek.engineersbliss.client.mixin.custom_items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.snek.engineersbliss.custom.items.base.__base_CustomItem;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;




@Mixin(ItemInHandRenderer.class)
public abstract class FullBrightHandItemMixin {

    @SuppressWarnings("unused")
    @ModifyVariable(method = "renderItem", at = @At("HEAD"), argsOnly = true, require = 1)
    private int eb$renderItem(
        int lightCoords,
        LivingEntity mob,
        ItemStack itemStack,
        ItemDisplayContext type,
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector
    ) {
        if(itemStack.getItem() instanceof __base_CustomItem customItem && customItem.isFullBright()) {
            return 0xF000F0;
        }
        else {
            return lightCoords;
        }
    }
}