package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;








/**
 * A UiSlider that snaps to increments and can define arbitrary values for each step.
 */
public class UiSteppedSlider<T> extends UiSlider {

    private final @Nullable BiConsumer<Integer, T> afterChangeCallback;


    // The list of possible values.
    private final List<T> stepValues;
    public final List<T> getStepValues() { return stepValues; }


    // Value formatters
    private final Function<T, String> valueFormatter;
    public Function<T, String> getValueFormatter() {
        return valueFormatter;
    }
    public String formatValueAt(final int valueIndex) {
        return valueFormatter.apply(getStepValues().get(valueIndex));
    }




    @SuppressWarnings("unchecked")
    public UiSteppedSlider(
        final Screen screen,
        final int x, final int y, final int w, final int h, final UiTxt label,
        final List<T> stepValues, final int defaultValueIndex,
        final @Nullable BiConsumer<Integer, T> afterChangeCallback,
        final @Nullable Function<T, String> valueFormatter
    ) {
        super(
            screen,
            x, y, w, h,
            label, indexToUnit(defaultValueIndex, stepValues.size()),
            null,
            valueFormatter != null
                ? s -> new UiTxt(valueFormatter.apply(((UiSteppedSlider<T>)s).getSelectedValue()))
                : s -> new UiTxt(      String.valueOf(((UiSteppedSlider<T>)s).getSelectedValue()))
        );
        this.stepValues = stepValues;
        this.afterChangeCallback = afterChangeCallback;
        this.valueFormatter = valueFormatter != null ? valueFormatter::apply : String::valueOf;
        updateMessage();
    }




    public static double indexToUnit(final int   index, final int size) { return (double)index / (size - 1);  }
    public static int    unitToIndex(final double unit, final int size) { return Math.min(size - 1, (int)Math.round(unit * (size - 1))); }
    public double indexToUnit(final int   index) { return indexToUnit(index, stepValues.size()); }
    public int    unitToIndex(final double unit) { return unitToIndex(unit,  stepValues.size()); }

    public T getSelectedValue() {
        return stepValues.get(unitToIndex(value, stepValues.size()));
    }




    @Override
    protected void updateMessage() {
        //! Guard the automatic updateMessage call done by UiSlider.
        //! stepValues is null so trying to read it would cause a NPE.
        if(stepValues != null) super.updateMessage();
    }




    static double snap(final double value, int steps) {
        --steps;
        return Math.round(value * steps) / (double) steps;
    }


    @Override
    protected void setValue(final double newValue) {
        final double snappedValue = snap(newValue, stepValues.size());

        //! Superclass handles 0-1 clamping and checks for the value to be different before calling applyValue.
        super.setValue(snappedValue);
    }

    @Override
    protected void applyValue() {
        super.applyValue();
        if(afterChangeCallback != null) {
            final int selectedIndex = unitToIndex(value, stepValues.size());
            afterChangeCallback.accept(selectedIndex, stepValues.get(selectedIndex));
        }
    }










    @Override
    public boolean keyPressed(KeyEvent event) {
        if(isHovered()) {
            if(event.isLeft()) {
                final double newValue = indexToUnit(unitToIndex(value) - 1);
                setValue(Math.clamp(newValue, 0.0, 1.0));
                return true;
            }
            if(event.isRight()) {
                final double newValue = indexToUnit(unitToIndex(value) + 1);
                setValue(Math.clamp(newValue, 0.0, 1.0));
                return true;
            }
        }
        return false;
    }








    protected double magnitudeOf(final @NotNull T value) {
        if(value instanceof final @NotNull Number number) return number.doubleValue();
        return Double.parseDouble(String.valueOf(value));
    }



    @Override
    public void drawCachedBackground(final NativeImage image, final int w, final int h) {
        super.drawCachedBackground(image, w, h);
        final int n = stepValues.size();

        // Calculate magnitudes
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        final double[] magnitudes = new double[n];
        for(int i = 0; i < n; i++) {
            magnitudes[i] = magnitudeOf(stepValues.get(i));
            if(magnitudes[i] < min) min = magnitudes[i];
            if(magnitudes[i] > max) max = magnitudes[i];
        }
        final double range = (max - min == 0) ? 1 : (max - min);


        // Calculate local point coordinates
        final int size = h;
        final int graphLeft = w - size;
        final double[] px = new double[n];
        final double[] py = new double[n];
        for(int i = 0; i < n; i++) {
            px[i] = graphLeft + (double)(size - 1) * i / (n - 1);
            py[i] = h - ((magnitudes[i] - min) / range) * h;
        }


        // Draw graph area
        RenderingUtils.extractLineArea(image, px, py, h, graphLeft, graphLeft + (int)(value * size), Layout.SliderGraphFillColor);

        // Draw the actual line on top. RenderingUtils.extractLine handles 2-axis antialiasing automatically
        RenderingUtils.extractLine(image, px, py, 1f, Layout.SliderGraphLineColor);
    }
}