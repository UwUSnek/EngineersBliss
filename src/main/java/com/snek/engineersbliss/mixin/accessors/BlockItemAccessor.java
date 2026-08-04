package com.snek.engineersbliss.mixin.accessors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;




@Mixin(BlockItem.class)
public interface BlockItemAccessor {

    @Invoker("updateBlockStateFromTag")
    BlockState invokeUpdateBlockStateFromTag(BlockPos pos, Level level, ItemStack itemStack, BlockState placedState);

    @Invoker("updateBlockEntityComponents")
    static void invokeUpdateBlockEntityComponents(Level level, BlockPos pos, ItemStack itemStack) {}
}