package com.snek.engineersbliss.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;




@Mixin(DoorBlock.class)
public interface DoorBlockAccessor {
    @Invoker("getHinge")
    DoorHingeSide invokeGetHinge(BlockPlaceContext context);
}