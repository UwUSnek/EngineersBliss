package com.snek.engineersbliss.client.screens.parts;

import java.util.List;
import java.util.function.BiConsumer;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;




public class SteppedSlider<T> extends AbstractSliderButton {
    private final String label;
    private final List<T> stepValues;
    private final BiConsumer<SteppedSlider<T>, T> onApplyValue;


    public SteppedSlider(
        final int x, final int y, final int w, final int h, final String label,
        final List<T> stepValues, final int defaultValueIndex,
        final BiConsumer<SteppedSlider<T>, T> onApplyValue
    ) {
        super(x, y, w, h, Component.empty(), indexToUnit(defaultValueIndex, stepValues.size()));
        this.label = label;
        this.stepValues = stepValues;
        this.onApplyValue = onApplyValue;
        updateMessage();
    }


    private static double indexToUnit(final int   index, final int size) { return (double)index / size;  }
    private static int    unitToIndex(final double unit, final int size) { return Math.min(size - 1, (int)(unit * size)); }
    private double indexToUnit(final int   index) { return indexToUnit(index, stepValues.size()); }
    private int    unitToIndex(final double unit) { return unitToIndex(unit,  stepValues.size()); }
    public T getSelectedValue() { return stepValues.get(unitToIndex(value)); }


    @Override
    protected void updateMessage() {
        setMessage(Component.literal(label + ": " + String.format("%.2f", getSelectedValue())));
    }


    @Override
    protected void applyValue() {
        onApplyValue.accept(this, getSelectedValue());
    }
}