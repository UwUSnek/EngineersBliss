package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;








public class UiSpacer extends __base_UiWidget {
    // private int bgColor;


    public UiSpacer(final Screen screen, final int bgColor) {
        super(screen);
        // this.bgColor = bgColor;
        setBgColor(bgColor);
    }
    public UiSpacer(final Screen screen) {
        this(screen, 0x00000000);
    }


    @Override
    public void relayoutSelf() {
        // Empty
    }




    // public void setBgColor(final int newBgColor) {
    //     bgColor = newBgColor;
    // }




    // @Override
    // protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    //     if((bgColor & 0xFF000000) != 0) {
    //         graphics.fill(getX(), getY(), getRight(), getBottom(), bgColor);
    //     }
    // }
}
