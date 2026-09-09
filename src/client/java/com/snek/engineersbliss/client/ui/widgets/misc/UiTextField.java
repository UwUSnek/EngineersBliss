package com.snek.engineersbliss.client.ui.widgets.misc;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiTextHandlerWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.screens.Screen;








public class UiTextField extends __base_UiTextHandlerWidget {


    public UiTextField(final Screen screen, final FontFamily fontFamily, final UiTxt hint) {
        super(screen, fontFamily, hint, TextAlignment.LEFT, true);
        setBgColor(Layout.bgColor);
        updateLabel();
    }
}