package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiTextHandlerWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;








public class UiTextField extends __base_UiTextHandlerWidget {


    // public UiTextField(final net.minecraft.client.gui.screens.Screen screen, final UiTxt hint, final Consumer<String> responder) { //TODO prob remove
    public UiTextField(final net.minecraft.client.gui.screens.Screen screen, final UiTxt hint) {
        super(screen, new UiTxt(Component.empty()), TextAlignment.LEFT, true);
        // this.hint        = hint; //TODO remove
        // this.responder   = responder; //TODO remove
        // this.renderLines = new ArrayList<>(); //TODO remove
        setBgColor(Layout.bgColor);
        updateLabel();
    }


    // @Override //TODO remove
    // protected void onValueChange() {
    //     if(responder != null) responder.accept(getValue());
    //     super.onValueChange();
    // }
}