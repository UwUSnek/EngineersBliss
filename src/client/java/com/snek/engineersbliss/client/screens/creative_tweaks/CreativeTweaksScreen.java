package com.snek.engineersbliss.client.screens.creative_tweaks;

import java.util.function.Function;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiToggleFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.ui.widgets.sliders.UiSlider;
import com.snek.engineersbliss.client.ui.widgets.sliders.UiSteppedFeatureSlider;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;








public class CreativeTweaksScreen extends __base_UiFeatureSetScreen {

    @SuppressWarnings("unchecked")
    private static final Function<UiSlider, UiTxt> tickFormatter = s -> {
        final int total = ((UiSteppedFeatureSlider<Integer>)s).getSelectedValue();
        final int seconds = total / 20;
        final int ticks   = total % 20;
        return new UiTxt(seconds == 0
            ? ticks == 0
                ? "0"
                : String.format("%st", ticks)
            : ticks == 0
                ? String.format("%ss", seconds)
                : String.format("%ss%st", seconds, ticks)
        );
    };



    public CreativeTweaksScreen() {
        super(CreativeTweaksClientFeatureSet.INSTANCE);
    }




    @Override
    protected void init() {
        super.init();


        // Player properties
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Player properties", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (this, CreativeTweaksClientFeatureSet.WALKING_SPEED),                    Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (this, CreativeTweaksClientFeatureSet.FLYING_SPEED,  CreativeTweaksClientHandler:: onFlyingSpeedChange), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (this, CreativeTweaksClientFeatureSet.INTERACTION_DISTANCE),             Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(this, CreativeTweaksClientFeatureSet.INTERACTION_RADIUS),               Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(this, CreativeTweaksClientFeatureSet.INTERACTION_COUNT),                Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(this, CreativeTweaksClientFeatureSet.PLACEMENT_DELAY,   tickFormatter), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(this, CreativeTweaksClientFeatureSet.AUTOCLICKER_DELAY, tickFormatter), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.AUTOCLICKER),                      Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.TOGGLE_CLICKS),                    Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.NO_SIGN_GUI),                      Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.OPEN_OBSTRUCTED_CONTAINERS),       Layout.BORDER_HEIGHT);


        // Player properties
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("World interactions", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.PHASE_THROUGH_BLOCKS_FLY),        Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.PHASE_THROUGH_ENTITIES),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_FIRE_EFFECT),             Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_FREEZING_EFFECT),         Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.FIX_HONEY_JUMP),                  Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_HONEY_SLIDING),           Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_SLIME_BOUNCE),            Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_BED_BOUNCE),              Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_ICE_SLIDING),             Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_CURRENT_DRAG),            Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_BUBBLE_COLUMN_DRAG),      Layout.BORDER_HEIGHT);


        // World interations
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Speed debuffs", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_HONEY_SLOWDOWN),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_SLIME_SLOWDOWN),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_SOULSAND_SLOWDOWN),       Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_POWDER_SNOW_SLOWDOWN),    Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_WATER_SLOWDOWN),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_LAVA_SLOWDOWN),           Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_COBWEB_SLOWDOWN),         Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_LADDER_SLOWDOWN),         Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_VINES_SLOWDOWN),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_TWISTING_VINES_SLOWDOWN), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_WEEPING_VINES_SLOWDOWN),  Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_SWEET_BERRIES_SLOWDOWN),  Layout.BORDER_HEIGHT);


        // Visual clutter
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Visual clutter", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_BLOCK_BREAK_PARTICLES),   Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_ITEM_CHANGE_ANIMATION),   Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_HAND_SWING_ANIMATION),    Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_DIMENSION_CHANGE_SCREEN), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_WATER_FOV_CHANGE),        Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_WATER_OVERLAY),           Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_LAVA_OVERLAY),            Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_NETHER_PORTAL_OVERLAY),   Layout.BORDER_HEIGHT);




        {
            final String titleString = "TEST //TODO remove";
            rightSidebar.addWidget(new UiTextWidget(this, new UiTxt(titleString, Fonts.ui.bold, 2f), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        }
    }
}