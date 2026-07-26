package com.snek.engineersbliss.client.ui.widgets.sliders;

import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.screens.Screen;








public abstract class UiAnalogueSlider extends UiSlider {
    private final double min;
    private final double max;


    protected UiAnalogueSlider(final Screen screen, final int x, final int y, final int w, final int h, final UiTxt label, final double min, final double max, final double initial) {
        super(screen, x, y, w, h, label, (initial - min) / (max - min), null);
        this.min = min;
        this.max = max;
        updateMessage();
    }

    public double getRealValue() { return min + value * (max - min); }


    @Override
    public UiTxt buildValueText() {
        return new UiTxt(String.format("%.2f", getRealValue()));
    }
}