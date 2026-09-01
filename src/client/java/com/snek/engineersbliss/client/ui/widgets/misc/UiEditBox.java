package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.function.Consumer;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiTextHandlerWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.screens.Screen;








public class UiEditBox extends __base_UiTextHandlerWidget {
    // private static final Style HINT_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY); //TODO remove

    private final Consumer<String> responder; //FIXME pass to superclass
    private String valueCache;


    public UiEditBox(final Screen screen, final FontFamily fontFamily, final UiTxt hint, final Consumer<String> responder) {
        super(screen, fontFamily, hint, TextAlignment.LEFT, false);
        this.responder = responder;
        this.valueCache = "";
        setBgColor(Layout.bgColor);
        updateLabel();
    }

    public String getValue() {
        return valueCache;
    }

    @Override
    protected void onValueChange() {
        super.onValueChange();
        valueCache = lines.get(0).toString();
        if(responder != null) responder.accept(getValue());
    }

    // @Override //TODO remove
    // protected void updateLabel() {
    //     if(value.isEmpty() && !isFocused() && hint != null) setLabel(hint.get().copy().withStyle(HINT_STYLE));
    //     else setLabel(Component.literal(value));
    // }
}