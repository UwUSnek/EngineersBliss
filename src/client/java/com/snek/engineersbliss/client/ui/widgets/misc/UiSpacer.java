package com.snek.engineersbliss.client.ui.widgets.misc;

import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;




public class UiSpacer extends AbstractWidget {
    private final int bgColor;


    public UiSpacer(final int bgColor) {
        super(50, 50, 50, 50, new Txt().get());
        this.bgColor = bgColor;
    }
    public UiSpacer() {
        this(0x00000000);
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
