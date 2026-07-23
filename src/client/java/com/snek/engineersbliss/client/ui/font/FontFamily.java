package com.snek.engineersbliss.client.ui.font;




@FunctionalInterface
public interface FontFamily {
    ScaledFont get(float scaleMultiplier);
}
