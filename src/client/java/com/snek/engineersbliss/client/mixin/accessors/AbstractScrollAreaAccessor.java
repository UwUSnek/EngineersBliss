package com.snek.engineersbliss.client.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.components.AbstractScrollArea;




@Mixin(AbstractScrollArea.class)
public interface AbstractScrollAreaAccessor {
    @Accessor("scrolling")
    boolean isScrolling();
}