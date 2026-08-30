package com.snek.engineersbliss.client.screens.macros;

import com.snek.engineersbliss.client.ui.base.__base_UiSidebarScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextField;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;




public class MacrosScreen extends __base_UiSidebarScreen {
    private UiTextField textField;



    public MacrosScreen() {
        super(DEFAULT_SIDEBAR_WIDTH, null);
    }




    @Override
    protected void init() {
        super.init();
        //TODO left sidebar

        textField = new UiTextField(this, new UiTxt(""));
        addRenderableWidget(textField);
    }


    @Override
    public void relayoutSelf() {
        super.relayoutSelf();
        final int textFieldX = (int)(width * leftSidebarWidth);
        final int textFieldWidth = (int)(width * (1 - leftSidebarWidth));
        textField.setSize(textFieldWidth, height);
        textField.setPosition(textFieldX, 0);
    }
}