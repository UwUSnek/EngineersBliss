package com.snek.engineersbliss.client.screens.overlays;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysClientFeatureSet;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreenWithPreview;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiToggleFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;




public class OverlaysScreen extends __base_UiFeatureSetScreenWithPreview {


    public OverlaysScreen() {
        super(OverlaysClientFeatureSet.INSTANCE);
    }




    @Override
    protected void init() {
        super.init();


        // Power levels
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Power levels", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, OverlaysClientFeatureSet.COMPARATOR_POWER_LEVELS,    null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, OverlaysClientFeatureSet.REDSTONE_WIRE_POWER_LEVELS, null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, OverlaysClientFeatureSet.RAIL_POWER_LEVELS,          null), Layout.BORDER_HEIGHT);

        // Logic
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Block logic", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, OverlaysClientFeatureSet.COMPARATOR_LOGIC_SNIPPET,   null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, OverlaysClientFeatureSet.REDSTONE_WIRE_POWER_SOURCE, null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, OverlaysClientFeatureSet.RAIL_POWER_SOURCE,          null), Layout.BORDER_HEIGHT);
    }
}