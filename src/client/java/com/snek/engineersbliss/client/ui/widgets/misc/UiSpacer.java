package com.snek.engineersbliss.client.ui.widgets.misc;

import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;




public class UiSpacer extends AbstractWidget {
    private int bgColor;


    public UiSpacer(final int x, final int y, final int w, final int h, final int bgColor) {
        super(x, y, w, h, new Txt().get());
        this.bgColor = bgColor;
    }
    public UiSpacer(final int bgColor) {
        this(50, 50, 50, 50, bgColor);
    }
    public UiSpacer() {
        this(0x00000000);
    }




    public void setBgColor(final int newBgColor) {
        bgColor = newBgColor;
    }




    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if((bgColor & 0xFF000000) != 0) {
            graphics.fill(getX(), getY(), getRight(), getBottom(), bgColor);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        // Empty
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return false;
    }
}
