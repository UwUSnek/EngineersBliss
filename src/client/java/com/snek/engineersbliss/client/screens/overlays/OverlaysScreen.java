package com.snek.engineersbliss.client.screens.overlays;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysClientFeatureSet;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.UiFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;




public class OverlaysScreen extends __base_UiFeatureSetScreen {


    public OverlaysScreen() {
        super(OverlaysClientFeatureSet.INSTANCE);
    }




    @Override
    protected void init() {
        super.init();


        // Power levels
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(new UiTxt("Power levels", Fonts.ui.bold, Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.COMPARATOR_POWER_LEVELS),    Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.REDSTONE_WIRE_POWER_LEVELS), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.RAIL_POWER_LEVELS),          Layout.BORDER_HEIGHT);

        // Logic
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(new UiTxt("Block logic", Fonts.ui.bold, Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.COMPARATOR_LOGIC_SNIPPET),   Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.REDSTONE_WIRE_POWER_SOURCE), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.RAIL_POWER_SOURCE),          Layout.BORDER_HEIGHT);
    }
}