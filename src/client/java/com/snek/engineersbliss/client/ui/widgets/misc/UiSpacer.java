package com.snek.engineersbliss.client.ui.widgets.misc;

import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;








public class UiSpacer extends __base_UiWidget {


    public UiSpacer(final Screen screen, final int bgColor) {
        super(screen);
        setBgColor(bgColor);
    }
    public UiSpacer(final Screen screen) {
        this(screen, 0x00000000);
    }


    @Override
    public void relayoutSelf() {
        // Empty
    }


    // Spacers reject clicks by default
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return false;
    }
}
