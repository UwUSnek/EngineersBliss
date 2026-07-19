package com.snek.engineersbliss.client.screens.overlays;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysClientFeatureSet;
import com.snek.engineersbliss.client.screens.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiFeatureButton;
import com.snek.engineersbliss.client.screens.parts.UiSpacer;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;




public class OverlaysScreen extends __base_UiFeatureSetScreen {
    private static UiWidgetList leftSidebar;
    private static final float LEFT_SIDEBAR_WIDTH = 0.25f;


    public OverlaysScreen() {
        super();
    }




    @Override
    protected void init() {


        leftSidebar = new UiWidgetList((int)(width * LEFT_SIDEBAR_WIDTH), height, 0, 0, BUTTON_HEIGHT); {
            final String titleString = "Overlays";
            leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, 2f).withBoldFont(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);

            // Power levels
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Power levels", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.COMPARATOR_POWER_LEVELS),    Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.REDSTONE_WIRE_POWER_LEVELS), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.RAIL_POWER_LEVELS),          Layout.BORDER_HEIGHT);

            // Logic
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Block logic", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.COMPARATOR_LOGIC_SNIPPET),   Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.REDSTONE_WIRE_POWER_SOURCE), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.RAIL_POWER_SOURCE),          Layout.BORDER_HEIGHT);
        }
        addRenderableWidget(leftSidebar);
    }
}