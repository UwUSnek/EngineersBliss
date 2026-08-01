package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedColor;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Easings;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;





public class UiToggleButton extends UiButton {
	protected static final int INDICATOR_WIDTH = 4;
    protected boolean value;

    // Background toggle indicator
    private int bgColorAlt = Layout.bgColorAlt;
    private AnimatedColor indicatorColor;
    public void setBgColorAlt(final int newColor) { bgColorAlt = newColor; markBgDirty(); }
    public int getBgBaseColorAlt() { return bgColorAlt; }




    public UiToggleButton(final Screen screen, final boolean initialValue, final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key, final TextAlignment alignment) {
        super(screen, x, y, width, height, label, pressCallback, key, alignment);
        finalizeInit(initialValue);
    }
    public UiToggleButton(final Screen screen, final boolean initialValue, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key, final TextAlignment alignment) {
        super(screen, label, pressCallback, key, alignment);
        finalizeInit(initialValue);
    }
    public UiToggleButton(final Screen screen, final boolean initialValue, final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final TextAlignment alignment) {
        super(screen, x, y, width, height, label, pressCallback, alignment);
        finalizeInit(initialValue);
    }
    public UiToggleButton(final Screen screen, final boolean initialValue, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final TextAlignment alignment) {
        super(screen, label, pressCallback, alignment);
        finalizeInit(initialValue);
    }


    public UiToggleButton(final Screen screen, final boolean initialValue, final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key) {
        super(screen, x, y, width, height, label, pressCallback, key);
        finalizeInit(initialValue);
    }
    public UiToggleButton(final Screen screen, final boolean initialValue, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key) {
        super(screen, label, pressCallback, key);
        finalizeInit(initialValue);
    }
    public UiToggleButton(final Screen screen, final boolean initialValue, final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback) {
        super(screen, x, y, width, height, label, pressCallback);
        finalizeInit(initialValue);
    }
    public UiToggleButton(final Screen screen, final boolean initialValue, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback) {
        super(screen, label, pressCallback);
        finalizeInit(initialValue);
    }


    private void finalizeInit(final boolean initialValue) {
        this.value = initialValue;
        this.indicatorColor = new AnimatedColor(calculateNewIndicatorColor(), Layout.toggleTransitionDuration, Easings.sineIn);
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
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.fill(getRight() - INDICATOR_WIDTH, getY(), getRight(), getBottom(), indicatorColor.compute());
    }

    public int calculateNewIndicatorColor() {
        return getValue() ? Layout.bgColorAlt : 0x0;
    }
}
