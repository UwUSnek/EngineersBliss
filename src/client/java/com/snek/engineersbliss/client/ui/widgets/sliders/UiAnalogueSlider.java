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
        final Screen screen, final int x, final int y, final int w, final int h,
        final UiTxt label,
        final double min, final double max, final double initial,
        final @Nullable Function<UiSlider, UiTxt> valueFormatter
    ) {
        super(
            screen, x, y, w, h,
            label,
            (initial - min) / (max - min),
            null,
            valueFormatter == null ? s -> new UiTxt(String.format("%.2f", ((UiAnalogueSlider)s).getRealValue())) : valueFormatter
        );
        this.min = min;
        this.max = max;
        updateMessage();
    }




    public double getRealValue() {
        return min + value * (max - min);
    }
}