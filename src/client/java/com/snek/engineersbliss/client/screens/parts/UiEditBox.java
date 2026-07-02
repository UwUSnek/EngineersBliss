package com.snek.engineersbliss.client.screens.parts;

import com.snek.engineersbliss.client.screens.Layout;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;



public class UiEditBox extends EditBox {

    public UiEditBox(int x, int y, int width, int height, Component narration) {
        super(Minecraft.getInstance().font, x, y, width, height, narration);
        this.setTextShadow(false);
        this.addFormatter((text, offset) ->
            FormattedCharSequence.forward(text, Layout.textStyleForScale(Layout.FONT_NAME_UI_MEDIUM))
        );
    }
}