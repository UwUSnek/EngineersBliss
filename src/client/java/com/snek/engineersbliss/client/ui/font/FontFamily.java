package com.snek.engineersbliss.client.ui.font;

import net.minecraft.client.gui.Font;




@FunctionalInterface
public interface FontFamily {
    Font apply(float scaleMultiplier);
}
