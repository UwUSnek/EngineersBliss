package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;








public class UiCheckbox extends UiToggleButton {


    public UiCheckbox(final UiScreen screen, final boolean initialValue, final @Nullable Consumer<UiCheckbox> pressCallback) {
        super(screen, initialValue, new UiTxt(""), pressCallback == null ? null : b -> pressCallback.accept((UiCheckbox)b), '\0');
        indicatorWidth.clear().setWF(0.75f);
    }
    public UiCheckbox(final UiScreen screen, final boolean initialValue) {
        this(screen, initialValue, null);
    }
    public UiCheckbox(final UiScreen screen, final @Nullable Consumer<UiCheckbox> pressCallback) {
        this(screen, false, pressCallback);
    }
    public UiCheckbox(final UiScreen screen) {
        this(screen, null);
    }


    @Override
    public void extractBackground(UiGraphics graphics, float mouseX, float mouseY, float a) {
        //! No superclass call. Don't draw the default indicator, sprite, or label.
        if((getBgColor() & 0xFF000000) != 0) {
            graphics.fill(getXF(), getYF(), getRight(), getBottom(), getBgColor());
        }
        final float indicatorSize = indicatorWidth.getPx();
        final float alpha = isActive() ? 1f : Layout.disabledAlpha;
        graphics.fill(getXF() + indicatorSize, getYF() + indicatorSize, getRight() - indicatorSize, getBottom() - indicatorSize, indicatorColor.compute(), alpha);
    }
}
