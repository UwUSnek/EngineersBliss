package com.snek.engineersbliss.client.screens.settings;

import java.util.function.Function;

import com.snek.engineersbliss.client.feature_handlers.settings.SettingsClientFeatureSet;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiToggleFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.ui.widgets.sliders.UiSteppedFeatureSlider;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;








public class SettingsScreen extends __base_UiFeatureSetScreen {

    private static final ValueFormatter<Integer> pixelFormatter = (n, u) -> {
        return String.format("%dpx", n);
    };


    public SettingsScreen() {
        super(SettingsClientFeatureSet.INSTANCE);
    }




    @Override
    protected void init() {
        super.init();


        // Rendering
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Rendering", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(
            this, SettingsClientFeatureSet.STATUS_BAR_HEIGHT,
            null, pixelFormatter, 0, 0
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(
            this, SettingsClientFeatureSet.STATUS_BAR_POSITION,
            null
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(
            this, SettingsClientFeatureSet.PLAYER_MODEL_IN_PAUSE_SCREEN,
            null
        ), Layout.BORDER_HEIGHT);
    }
}