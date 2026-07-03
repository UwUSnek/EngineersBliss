package com.snek.engineersbliss.client.screens.parts;

import com.snek.engineersbliss.client.utils.Layout;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;



public class UiEditBox extends EditBox {

    public UiEditBox(final int x, final int y, final int width, final int height, final Component narration) {
        super(Minecraft.getInstance().font, x, y, width, height, narration);
        this.setTextShadow(false);
        this.addFormatter((text, offset) ->
            FormattedCharSequence.forward(text, Layout.textStyleForScale(Layout.FONT_NAME_UI_MEDIUM))
        );
    }
}