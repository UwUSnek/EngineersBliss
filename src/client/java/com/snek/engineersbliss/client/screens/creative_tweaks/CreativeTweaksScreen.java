package com.snek.engineersbliss.client.screens.creative_tweaks;

import java.text.DecimalFormat;
import java.util.function.Function;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiToggleFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.ui.widgets.sliders.UiSteppedFeatureSlider;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Utils;








public class CreativeTweaksScreen extends __base_UiFeatureSetScreen {
    private static final DecimalFormat decimalFormatter = new DecimalFormat("0.#####");


    @SuppressWarnings("unchecked")
    private static final Function<Integer, String> tickFormatter = n -> {
        final int seconds = n / 20;
        final int ticks   = n % 20;
        return seconds == 0
            ? ticks == 0
                ? "0"
                : String.format("%st", ticks)
            : ticks == 0
                ? String.format("%ss", seconds)
                : String.format("%ss%st", seconds, ticks)
        ;
    };
    private static final Function<Float, String> multiplierFormatter = n -> {
        return decimalFormatter.format(n) + "x";
    };
    private static final Function<Integer, String> intMultiplierFormatter = n -> {
        return String.format("%dx", n);
    };
    private static final Function<Float, String> blockFormatter = n -> {
        return String.format("%s block%s", decimalFormatter.format(n), (Utils.floatEquals(n, 1, 1e-5f) ? "" : "s"));
    };
    private static final Function<Integer, String> intBlockFormatter = n -> {
        return String.format("%d block%s", n, (n == 1 ? "" : "s"));
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
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (
            this, CreativeTweaksClientFeatureSet.WALKING_SPEED,
            null, multiplierFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (
            this, CreativeTweaksClientFeatureSet.FLYING_SPEED,
            CreativeTweaksClientHandler::onFlyingSpeedChange, multiplierFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (
            this, CreativeTweaksClientFeatureSet.PLAYER_SCALE,
            null, multiplierFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (
            this, CreativeTweaksClientFeatureSet.INTERACTION_DISTANCE,
            null, blockFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(
            this, CreativeTweaksClientFeatureSet.INTERACTION_RADIUS,
            null, intBlockFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(
            this, CreativeTweaksClientFeatureSet.INTERACTION_COUNT,
            null, intMultiplierFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(
            this, CreativeTweaksClientFeatureSet.PLACEMENT_DELAY,
            null, tickFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(
            this, CreativeTweaksClientFeatureSet.AUTOCLICKER_DELAY,
            null, tickFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.AUTOCLICKER),                      Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.TOGGLE_CLICKS),                    Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.NO_SIGN_GUI),                      Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.DISABLE_PICKING_UP_ITEMS),         Layout.BORDER_HEIGHT);
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