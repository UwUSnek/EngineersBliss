package com.snek.engineersbliss.client.ui.widgets;

import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;




public abstract class UiAnalogueSlider extends AbstractSliderButton {
    private UiTxt label;
    private final double min;
    private final double max;


    protected UiAnalogueSlider(final int x, final int y, final int w, final int h, final UiTxt label, final double min, final double max, final double initial) {
        super(x, y, w, h, Component.empty(), (initial - min) / (max - min));
        this.label = label;
        this.min = min;
        this.max = max;
        updateMessage();
    }

    public double getRealValue() { return min + value * (max - min); }




    @Override
    protected void updateMessage() {
        super.setMessage(label.copy().cat(": " + String.format("%.2f", getRealValue())).get());
    }
    @Override
    public void setMessage(Component message) {
        throw new UnsupportedOperationException("Use .setLabel(label) instead");
    }
    public void setLabel(final Component label) {
        super.setMessage(label);
        this.label = new UiTxt(label);
    }
    public void setLabel(final UiTxt label) {
        super.setMessage(label.get());
        this.label = (UiTxt)label.copy();
    }




    @Override protected abstract void applyValue();
}