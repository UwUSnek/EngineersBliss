package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
import com.snek.engineersbliss.client.utils.Layout;
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
    private final ValueFormatter<T> valueFormatter;
    public ValueFormatter<T> getValueFormatter() {
        return valueFormatter;
    }
    public String formatValueAt(final int valueIndex, final boolean shortUnit) {
        return valueFormatter.format(getStepValues().get(valueIndex), shortUnit);
    }




    @SuppressWarnings("unchecked")
    public UiSteppedSlider(
        final Screen screen,
        final UiTxt label,
        final List<T> stepValues, final int defaultValueIndex,
        final @Nullable BiConsumer<Integer, T> afterChangeCallback,
        final @Nullable ValueFormatter<T> valueFormatter
    ) {
        final Function<UiSlider, UiTxt> _valueFormatter = valueFormatter != null
            ? s -> new UiTxt(valueFormatter.format(((UiSteppedSlider<T>)s).getSelectedValue(), false))
            : s -> new UiTxt(       String.valueOf(((UiSteppedSlider<T>)s).getSelectedValue()))
        ;
        super(screen, label, indexToUnit(defaultValueIndex, stepValues.size()), null, _valueFormatter);
        this.stepValues = stepValues;
        this.afterChangeCallback = afterChangeCallback;
        this.valueFormatter = valueFormatter != null ? valueFormatter::format : (n, u) -> String.valueOf(n);
        getRightLabelMargin().clear().addHF(1f).addPx(Layout.textMarginPx);
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
        fireChangeCallback();
    }

    protected void fireChangeCallback() {
        if(afterChangeCallback != null) {
            final int selectedIndex = unitToIndex(value, stepValues.size());
            afterChangeCallback.accept(selectedIndex, stepValues.get(selectedIndex));
        }
    }










    @Override
    public boolean keyPressed(KeyEvent event) {
        if(isHovered() && !isBeingDragged()) {
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








    protected float magnitudeOf(final @NotNull T value) {
        if(value instanceof final @NotNull Number number) return number.floatValue();
        return Float.parseFloat(String.valueOf(value));
    }



    @Override
    public void extractBackground(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        final int n = stepValues.size();

        // Calculate magnitudes
        float min = +Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        final float[] magnitudes = new float[n];
        for(int i = 0; i < n; i++) {
            magnitudes[i] = magnitudeOf(stepValues.get(i));
            if(magnitudes[i] < min) min = magnitudes[i];
            if(magnitudes[i] > max) max = magnitudes[i];
        }
        final float range = (max - min == 0) ? 1 : (max - min);


        // Calculate local point coordinates
        final float size = getHeightF();
        final float graphLeft = getRight() - size;
        final float[] px = new float[n];
        final float[] py = new float[n];
        for(int i = 0; i < n; i++) {
            px[i] = graphLeft + (size - 1) * i / (n - 1);
            py[i] = getYF() + size - ((magnitudes[i] - min) / range) * size;
        }


        // Draw graph area
        graphics.enableScissor(getX(), getY(), (int)Math.ceil(graphLeft + value * size) + 1, (int)Math.ceil(getBottom()) + 1);
        graphics.multiLineArea(graphLeft, getYF(), getRight(), getBottom(), px, py, Layout.SliderGraphFillColor);
        graphics.disableScissor();

        // Draw the actual line on top
        graphics.multiLine(graphLeft, getYF(), getRight(), getBottom(), px, py, 1f, Layout.SliderGraphLineColor);
    }
}