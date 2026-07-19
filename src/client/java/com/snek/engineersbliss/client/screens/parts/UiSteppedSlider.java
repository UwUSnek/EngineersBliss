package com.snek.engineersbliss.client.screens.parts;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.components.AbstractSliderButton;




public class UiSteppedSlider<T> extends AbstractSliderButton {
    private final Txt label;
    private final List<T> stepValues;
    private final Consumer<T> onApplyValue;


    public UiSteppedSlider(
        final int x, final int y, final int w, final int h, final Txt label,
        final List<T> stepValues, final int defaultValueIndex,
        final @Nullable Consumer<T> onApplyValue
    ) {
        super(x, y, w, h, new UiTxt().get(), indexToUnit(defaultValueIndex, stepValues.size()));
        this.label = label;
        this.stepValues = stepValues;
        this.onApplyValue = onApplyValue;
        updateMessage();
    }


    public static double indexToUnit(final int   index, final int size) { return (double)index / size;  }
    public static int    unitToIndex(final double unit, final int size) { return Math.min(size - 1, (int)(unit * size)); }
    public double indexToUnit(final int   index) { return indexToUnit(index, stepValues.size()); }
    public int    unitToIndex(final double unit) { return unitToIndex(unit,  stepValues.size()); }
    public T getSelectedValue() { return stepValues.get(unitToIndex(value)); }


    @Override
    protected void updateMessage() {
        setMessage(label.copy().cat(": " + getSelectedValue()).get());
    }


    @Override
    protected void applyValue() {
        if(onApplyValue != null) onApplyValue.accept(getSelectedValue());
    }


    static double snap(final double value, int steps) {
        --steps;
        return Math.round(value * steps) / (double) steps;
    }

    @Override
    protected void setValue(final double newValue) {
        final double snappedValue = snap(newValue, stepValues.size());
        super.setValue(snappedValue); //! Superclass handles 0-1 clamping
    }
}

//TODO show like a graph or something that visually ilustrates the magnitude of each step's value