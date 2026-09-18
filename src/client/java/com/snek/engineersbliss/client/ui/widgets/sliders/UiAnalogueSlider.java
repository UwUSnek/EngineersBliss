package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.DoubleConsumer;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
import com.snek.engineersbliss.client.utils.UiTxt;








public abstract class UiAnalogueSlider extends UiSlider {
    private final double min;
    private final double max;
    private final @Nullable DoubleConsumer afterChangeCallback;


    // Value formatters
    private final ValueFormatter<Double> valueFormatter;
    public ValueFormatter<Double> getValueFormatter() {
        return valueFormatter;
    }
    public String formatValueAt(final double value, final boolean shortUnit) {
        return valueFormatter.format(value, shortUnit);
    }


    protected UiAnalogueSlider(
        final UiScreen screen,
        final UiTxt label,
        final double min, final double max, final double initial,
        final @Nullable DoubleConsumer afterChangeCallback,
        final @Nullable ValueFormatter<Double> valueFormatter
    ) {
        final Function<UiSlider, UiTxt> _valueFormatter = valueFormatter != null
            ? s -> new UiTxt(valueFormatter.format(((UiAnalogueSlider)s).getRealValue(), false))
            : s -> new UiTxt(String.format("%.2f", ((UiAnalogueSlider)s).getRealValue()))
        ;
        super(screen, label, (initial - min) / (max - min), null, _valueFormatter);
        this.min = min;
        this.max = max;
        this.valueFormatter = valueFormatter != null ? valueFormatter::format : (n, u) -> String.format("%.2f", n);
        this.afterChangeCallback = afterChangeCallback;
        updateMessage();
    }


    public double getRealValue() {
        return min + value * (max - min);
    }


    @Override
    protected void applyValue() {
        super.applyValue();
        fireChangeCallback();
    }

    protected void fireChangeCallback() {
        if(afterChangeCallback != null) {
            afterChangeCallback.accept(getRealValue());
        }
    }
}