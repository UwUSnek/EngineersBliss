package com.snek.engineersbliss.client.screens.parts;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;




public abstract class __base_AnalogueSlider extends AbstractSliderButton {
    private final String label;
    private final double min;
    private final double max;

    public __base_AnalogueSlider(final int x, final int y, final int w, final int h, final String label, final double min, final double max, final double initial) {
        super(x, y, w, h, Component.empty(), (initial - min) / (max - min));
        this.label = label;
        this.min = min;
        this.max = max;
        updateMessage();
    }

    public double getRealValue() { return min + value * (max - min); }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal(label + ": " + String.format("%.2f", getRealValue())));
    }

    @Override protected abstract void applyValue();
}