package com.snek.engineersbliss.client.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MouseHandler;




@Mixin(MouseHandler.class)
public interface MouseHandlerAccessor {
    @Accessor("xpos")
    void setXpos(double x);

    @Accessor("ypos")
    void setYpos(double y);
}