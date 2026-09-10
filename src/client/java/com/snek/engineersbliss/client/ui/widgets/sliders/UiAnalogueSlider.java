package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.DoubleFunction;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.screens.Screen;








public abstract class UiAnalogueSlider extends UiSlider {
    private final double min;
    private final double max;


    protected UiAnalogueSlider(
        final Screen screen,
        final UiTxt label,
        final double min, final double max, final double initial,
        final @Nullable Function<UiSlider, UiTxt> valueFormatter
    ) {
        final Function<UiSlider, UiTxt> _valueFormatter = valueFormatter == null ? s -> new UiTxt(String.format("%.2f", ((UiAnalogueSlider)s).getRealValue())) : valueFormatter;
        super(screen, label, (initial - min) / (max - min), null, _valueFormatter);
        this.min = min;
        this.max = max;
        updateMessage();
    }




    public double getRealValue() {
        return min + value * (max - min);
    }
}