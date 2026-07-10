package com.snek.engineersbliss.client.screens.parts;

import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;




public class UiButton extends Button {
    private int fgColor = 0xFFEEEEEE;
    private int textMarginPx = 4;



    public UiButton(final int x, final int y, final int width, final int height, final Txt message, final Button.OnPress onPress) {
        super(x, y, width, height, message.get(), onPress, DEFAULT_NARRATION);
    }


    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        final Font font = Minecraft.getInstance().font;
        graphics.fill(getX(), getY(), getX() + width, getY() + height, Layout.bgColor);

        final int lineHeight = font.lineHeight;
        final int textX = getX() + textMarginPx;
        final int textY = getY() + (height - lineHeight) / 2;
        graphics.text(font, this.message, textX, textY, fgColor);
    }
}
