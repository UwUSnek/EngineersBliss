package com.snek.engineersbliss.client.screens.parts;

import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;




public class UiSpacer extends AbstractWidget {
    public UiSpacer() {
        super(50, 50, 50, 50, new Txt().get());
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        // Empty
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        // Empty
    }
}
