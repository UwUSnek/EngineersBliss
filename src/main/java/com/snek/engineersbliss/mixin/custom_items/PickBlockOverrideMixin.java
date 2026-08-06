package com.snek.engineersbliss.mixin.custom_items;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.snek.engineersbliss.custom.items.PickBlockOverrideManager;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;




/**
 * A mixin that lets BlockStateBase.getCloneItemStack use custom block mapping overrides.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class PickBlockOverrideMixin {

    @SuppressWarnings("unused")
    @ModifyReturnValue(
        method = "getCloneItemStack(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Lnet/minecraft/world/item/ItemStack;",
        at = @At("RETURN")
    )
    private ItemStack eb$getCloneItemStack(ItemStack original) {
        final @Nullable Item custom = PickBlockOverrideManager.getOverride(((BlockStateBase)(Object)this).getBlock());
        if(custom == null) {
            return original;
        }
        else {
            //! Build a new item using the original count, or 1 if original is 0.
            //! Blocks not pickable in Vanilla such as End Portals return an ItemStack.EMPTY, which has count 0.
            //! Blindly copying the count would break custom pick overrides.
            final int ogCount = original.getCount();
            return new ItemStack(custom.builtInRegistryHolder(), ogCount > 0 ? ogCount : 1);
        }
    }
}