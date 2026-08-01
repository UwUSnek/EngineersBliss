package com.snek.engineersbliss.client.screens.settings;

import java.util.function.Function;

import com.snek.engineersbliss.client.feature_handlers.settings.SettingsClientFeatureSet;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiToggleFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.ui.widgets.sliders.UiSlider;
import com.snek.engineersbliss.client.ui.widgets.sliders.UiSteppedFeatureSlider;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;








public class SettingsScreen extends __base_UiFeatureSetScreen {

    @SuppressWarnings("unchecked")
    private static final Function<UiSlider, UiTxt> pixelFormatter = s -> {
        final int total = ((UiSteppedFeatureSlider<Integer>)s).getSelectedValue();
        return new UiTxt(String.format("%dpx", total));
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
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(this, SettingsClientFeatureSet.STATUS_BAR_HEIGHT, pixelFormatter), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, SettingsClientFeatureSet.STATUS_BAR_POSITION),               Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, SettingsClientFeatureSet.PLAYER_MODEL_IN_PAUSE_SCREEN),      Layout.BORDER_HEIGHT);
    }
}