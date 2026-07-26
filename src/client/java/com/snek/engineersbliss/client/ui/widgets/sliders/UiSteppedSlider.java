package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.List;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.NativeImage;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.screens.Screen;








/**
 * A UiSlider that snaps to increments and can define arbitrary values for each step.
 */
public class UiSteppedSlider<T> extends UiSlider {


    // The list of possible values.
    private final List<T> stepValues;



    public UiSteppedSlider(
        final Screen screen,
        final int x, final int y, final int w, final int h, final UiTxt label,
        final List<T> stepValues, final int defaultValueIndex,
        final @Nullable BiConsumer<Integer, T> afterChangeCallback
    ) {
        super(screen, x, y, w, h, label, indexToUnit(defaultValueIndex, stepValues.size()), n -> {
            if(afterChangeCallback != null) {
                final int selectedIndex = unitToIndex(n, stepValues.size());
                afterChangeCallback.accept(selectedIndex, stepValues.get(selectedIndex));
            }
        });
        this.stepValues = stepValues;
        updateMessage();
    }




    public static double indexToUnit(final int   index, final int size) { return (double)index / (size - 1);  }
    public static int    unitToIndex(final double unit, final int size) { return Math.min(size - 1, (int)Math.round(unit * (size - 1))); }
    public double indexToUnit(final int   index) { return indexToUnit(index, stepValues.size()); }
    public int    unitToIndex(final double unit) { return unitToIndex(unit,  stepValues.size()); }
    public T getSelectedValue() { return stepValues.get(unitToIndex(value)); }




    @Override
    public UiTxt buildValueText() {
        return new UiTxt(String.valueOf(getSelectedValue()));
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
        super.setValue(snappedValue); //! Superclass handles 0-1 clamping
    }

    // @Override
    // protected void handleCursorPosition(final int mouseX, final int mouseY, final double guiScale) {
    //     if(isBeingDragged()) {
    //         long handle = Minecraft.getInstance().getWindow().handle();
    //         GLFW.glfwSetCursorPos(
    //             handle,
    //             guiScale * (getX() + getWidth() * snap(value, stepValues.size())),
    //             guiScale * (getY() + getHeight() / 2d)
    //         );
    //     }
    // }




    protected double magnitudeOf(final T value) {
        if(value instanceof Number number) return number.doubleValue();
        return Double.parseDouble(String.valueOf(value));
    }




    @Override
    public void drawCachedBackground(final NativeImage image, final int w, final int h) {
        super.drawCachedBackground(image, w, h);
        final int n = stepValues.size();

        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        final double[] mags = new double[n];
        for(int i = 0; i < n; i++) {
            mags[i] = magnitudeOf(stepValues.get(i));
            if(mags[i] < min) min = mags[i];
            if(mags[i] > max) max = mags[i];
        }
        final double range = (max - min == 0) ? 1 : (max - min);

        final double[] px = new double[n];
        final double[] py = new double[n];
        for(int i = 0; i < n; i++) {
            px[i] = (double)w * i / (n - 1);
            py[i] = h - ((mags[i] - min) / range) * h;
        }

        RenderingUtils.extractLine(image, px, py, 1.0f, Layout.SliderGraphLineColor);
    }
}