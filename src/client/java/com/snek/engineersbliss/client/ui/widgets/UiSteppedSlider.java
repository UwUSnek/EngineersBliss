package com.snek.engineersbliss.client.ui.widgets;

import java.util.List;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;








public class UiSteppedSlider<T> extends AbstractSliderButton {
    private UiTxt label;
    private final List<T> stepValues;
    private final BiConsumer<Integer, T> afterChangeCallback;


    public UiSteppedSlider(
        final int x, final int y, final int w, final int h, final UiTxt label,
        final List<T> stepValues, final int defaultValueIndex,
        final @Nullable BiConsumer<Integer, T> afterChangeCallback
    ) {
        super(x, y, w, h, new Txt().get(), indexToUnit(defaultValueIndex, stepValues.size()));
        this.label = label;
        this.stepValues = stepValues;
        this.afterChangeCallback = afterChangeCallback;
        updateMessage();
    }




    public static double indexToUnit(final int   index, final int size) { return (double)index / size;  }
    public static int    unitToIndex(final double unit, final int size) { return Math.min(size - 1, (int)(unit * size)); }
    public double indexToUnit(final int   index) { return indexToUnit(index, stepValues.size()); }
    public int    unitToIndex(final double unit) { return unitToIndex(unit,  stepValues.size()); }
    public T getSelectedValue() { return stepValues.get(unitToIndex(value)); }




    @Override
    protected void updateMessage() {
        super.setMessage(label.copy().cat(": " + getSelectedValue()).get());
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




    @Override
    protected void applyValue() {
        if(afterChangeCallback != null) {
            final int selectedIndex = unitToIndex(value);
            afterChangeCallback.accept(selectedIndex, stepValues.get(selectedIndex));
        }
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