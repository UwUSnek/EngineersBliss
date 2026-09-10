package com.snek.engineersbliss.client.screens.action_history;

import com.snek.engineersbliss.client.ui.base.__base_UiSidebarScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;




public class ActionHistoryScreen extends __base_UiSidebarScreen {

    public ActionHistoryScreen() {
        super(DEFAULT_SIDEBAR_WIDTH, null);
    }




    @Override
    protected void init() {

        //! test //TODO remove
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("test //TODO remove", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        // leftSidebar.addWidget(createButton(new UiTxt("undo"), new UiTxt(""), b -> {}, '\0', null, null));
        // leftSidebar.addWidget(createButton(new UiTxt("redo"), new UiTxt(""), b -> {}, '\0', null, null));
    }
}