package com.snek.engineersbliss.client.ui.widgets;

import com.snek.engineersbliss.client.ui.font.Fonts;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;



public class UiEditBox extends EditBox {

    public UiEditBox(final int x, final int y, final int width, final int height, final Component narration) {
        super(Fonts.ui.regular.get(1f).getFont(), x, y, width, height, narration);
        this.setTextShadow(false);
        this.addFormatter((text, offset) ->
            FormattedCharSequence.forward(text, Style.EMPTY.withFont(Fonts.ui.regular.get(1f).getDescription()))
        );
    }
}