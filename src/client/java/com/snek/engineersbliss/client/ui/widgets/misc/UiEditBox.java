package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.function.Consumer;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiTextHandlerWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;








public class UiEditBox extends __base_UiTextHandlerWidget {
    private static final Style HINT_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);

    private final UiTxt hint;
    private final Consumer<String> responder;


    public UiEditBox(final net.minecraft.client.gui.screens.Screen screen, final UiTxt hint, final Consumer<String> responder) {
        super(screen, new UiTxt(Component.empty()), TextAlignment.LEFT);
        this.hint      = hint;
        this.responder = responder;
        setBgColor(Layout.bgColor);
        updateLabel();
    }


    @Override
    protected void onValueChange() {
        if(responder != null) responder.accept(value);
        super.onValueChange();
    }

    @Override
    protected void updateLabel() {
        if(value.isEmpty() && !isFocused() && hint != null) setLabel(hint.get().copy().withStyle(HINT_STYLE));
        else setLabel(Component.literal(value));
    }
}