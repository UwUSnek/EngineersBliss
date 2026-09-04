package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.UiGraphics;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedColor;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Easings;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;





public class UiToggleButton extends UiButton {
	protected static final float INDICATOR_WIDTH = 0.25f;
    protected boolean value;

    // Background toggle indicator
    private int bgColorAlt = Layout.bgColorAlt;
    private AnimatedColor indicatorColor;
    public void setBgColorAlt(final int newColor) { bgColorAlt = newColor; markBgDirty(); }
    public int getBgBaseColorAlt() { return bgColorAlt; }

    // Value formatters
    private ValueFormatter<Boolean> valueFormatter;
    public ValueFormatter<Boolean> getValueFormatter() {
        return valueFormatter;
    }
    public String formatValue(final boolean n, final boolean shortUnit) {
        return valueFormatter.format(n, shortUnit);
    }



    public UiToggleButton(final Screen screen, final boolean initialValue, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final @Nullable ValueFormatter<Boolean> valueFormatter, final char key, final TextAlignment alignment) {
        super(screen, label, pressCallback, key, alignment);
        finalizeInit(initialValue, valueFormatter);
    }
    public UiToggleButton(final Screen screen, final boolean initialValue, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final TextAlignment alignment) {
        super(screen, label, pressCallback, alignment);
        finalizeInit(initialValue, valueFormatter);
    }


    public UiToggleButton(final Screen screen, final boolean initialValue, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key) {
        super(screen, label, pressCallback, key);
        finalizeInit(initialValue, valueFormatter);
    }
    public UiToggleButton(final Screen screen, final boolean initialValue, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback) {
        super(screen, label, pressCallback);
        finalizeInit(initialValue, valueFormatter);
    }


    private void finalizeInit(final boolean initialValue, final @Nullable ValueFormatter<Boolean> valueFormatter) {
        this.value = initialValue;
        this.valueFormatter = valueFormatter != null ? valueFormatter : (n, u) -> n.booleanValue() ? "ON" : "OFF";
        this.indicatorColor = new AnimatedColor(calculateNewIndicatorColor(), Layout.toggleTransitionDuration, Easings.sineIn);
        getRightLabelMargin().clear().setHF(INDICATOR_WIDTH);
    }




    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        value = !getValue();
        indicatorColor.startNewTransition(calculateNewIndicatorColor());
    }

    public boolean getValue() {
        return value;
    }




    @Override
    public void extractBackground(UiGraphics graphics, float mouseX, float mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.fill(getRight() - getRightLabelMargin().getPx(), getYF(), getRight(), getBottom(), indicatorColor.compute());
    }

    public int calculateNewIndicatorColor() {
        return getValue() ? Layout.bgColorAlt : 0x0;
    }
}
