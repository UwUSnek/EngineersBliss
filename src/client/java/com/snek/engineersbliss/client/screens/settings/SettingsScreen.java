package com.snek.engineersbliss.client.screens.settings;

import com.snek.engineersbliss.client.feature_handlers.settings.SettingsClientFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.status_bar.StatusBarHandler;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiToggleFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.ui.widgets.sliders.UiSteppedFeatureSlider;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;








public class SettingsScreen extends __base_UiFeatureSetScreen {
    private static final ValueFormatter<Float> statusBarHeightFormatter = (n, u) -> {
        return String.format("%dpx", (int)(StatusBarHandler.DEFAULT_BAR_HEIGHT_PX * n));
    };
    private static final ValueFormatter<Integer> blockShaderLimitFormatter = (n, u) -> {
        if(n == SettingsServerFeatureSet.BLOCK_SHADER_LIMIT_INFINITE) return "Unlimited";
        else return String.format("%d", n);
    };




    public SettingsScreen() {
        super(SettingsClientFeatureSet.INSTANCE);
    }




    @Override
    protected void init() {
        super.init();


        // Screens
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Misc", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, SettingsClientFeatureSet.PAUSE_GAME_IN_PAUSE_MENU,  null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, SettingsClientFeatureSet.PAUSE_GAME_IN_MOD_SCREENS, null), Layout.BORDER_HEIGHT);


        // Status bar
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Status bar", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>(
            this, SettingsClientFeatureSet.STATUS_BAR_HEIGHT,
            null, statusBarHeightFormatter, 0, 0
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(
            this, SettingsClientFeatureSet.STATUS_BAR_POSITION,
            null, (n, u) -> n.booleanValue() ? "Top" : "Bottom"
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, SettingsClientFeatureSet.CHAT_HIDES_STATUS_BAR, null), Layout.BORDER_HEIGHT);


        // Rendering
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Rendering", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(
            this, SettingsClientFeatureSet.PLAYER_MODEL_IN_PAUSE_SCREEN,
            null
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(
            this, SettingsClientFeatureSet.BLOCK_SHADERS,
            null
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(
            this, SettingsClientFeatureSet.BLOCK_SHADER_LIMIT,
            null, blockShaderLimitFormatter, 0, 0
        ), Layout.BORDER_HEIGHT);
    }
}