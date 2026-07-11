package com.snek.engineersbliss.client.screens.parts;

import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;




public class UiTextWidget extends AbstractWidget {
    final Component label;
    float textScale = 1f;
    final TextAlignment alignment;
    final int color;




    public UiTextWidget(final Txt label, final TextAlignment alignment, final int color) {
        this(50, 50, 50, 50, label, alignment, color);
    }

    public UiTextWidget(final int x, final int y, final int w, final int h, final Txt label, final TextAlignment alignment, final int color) {
        this(x, y, w, h, label.get(), alignment, color);
        if(label instanceof UiTxt uiTxt) textScale = uiTxt.getTextScale();
    }

    public UiTextWidget(final Component label, final TextAlignment alignment, final int color) {
        this(50, 50, 50, 50, label, alignment, color);
    }

    public UiTextWidget(final int x, final int y, final int w, final int h, final Component label, final TextAlignment alignment, final int color) {
        super(x, y, w, h, new Txt().get());
        this.label = label;
        this.alignment = alignment;
        this.color = color;
    }



    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        final Font font = Minecraft.getInstance().font;
        final int x = getX() + Layout.textMarginPx;
        final int y = getY() + (height - font.lineHeight) / 2;
        graphics.pose().pushMatrix();
        graphics.pose().scale(textScale, textScale);
        switch(alignment) {
            case LEFT:   { graphics.        text(font, label, (int)(x / textScale), (int)(y / textScale), color); break; }
            case CENTER: { graphics.centeredText(font, label, (int)(x / textScale), (int)(y / textScale), color); break; }
        }
        graphics.pose().popMatrix();
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
