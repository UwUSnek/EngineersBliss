package com.snek.engineersbliss.client.screens.creative_tweaks;

import java.text.DecimalFormat;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.base.ValueFormatter;
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
    private static final ValueFormatter<Integer> tickFormatter = (n, u) -> {
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
    private static final ValueFormatter<Float> multiplierFormatter = (n, u) -> {
        return decimalFormatter.format(n) + "x";
    };
    private static final ValueFormatter<Integer> intMultiplierFormatter = (n, u) -> {
        return String.format("%dx", n);
    };
    private static final ValueFormatter<Float> blockFormatter = (n, u) -> {
        final String unit = u ? "b" : (Utils.floatEquals(n, 1, 1e-5f) ? "block" : "blocks");
        return String.format("%s%s", decimalFormatter.format(n), unit);
    };
    private static final ValueFormatter<Integer> intBlockFormatter = (n, u) -> {
        final String unit = u ? "b" : (n == 1 ? "block" : "blocks");
        return String.format("%d%s", n, unit);
    };



    public CreativeTweaksScreen() {
        super(CreativeTweaksClientFeatureSet.INSTANCE);
    }




    @Override
    protected void init() {
        super.init();


        // Player properties
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Player properties", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>(
            this, CreativeTweaksClientFeatureSet.WALKING_SPEED,
            null, multiplierFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>(
            this, CreativeTweaksClientFeatureSet.FLYING_SPEED,
            CreativeTweaksClientHandler::onFlyingSpeedChange, multiplierFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>(
            this, CreativeTweaksClientFeatureSet.PLAYER_SCALE,
            null, multiplierFormatter,
            0, 0 //FIXME fix preview text indices
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>(
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
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.AUTOCLICKER,                null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.TOGGLE_CLICKS,              null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.NO_SIGN_GUI,                null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.DISABLE_PICKING_UP_ITEMS,   null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton          (this, CreativeTweaksClientFeatureSet.OPEN_OBSTRUCTED_CONTAINERS, null), Layout.BORDER_HEIGHT);


        // Player properties
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("World interactions", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.PHASE_THROUGH_BLOCKS_FLY,             null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.PHASE_THROUGH_ENTITIES,               null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_FIRE_EFFECT,                  null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_FREEZING_EFFECT,              null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.FIX_HONEY_JUMP,                       null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_HONEY_SLIDING,                null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_SLIME_BOUNCE,                 null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_BED_BOUNCE,                   null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_ICE_SLIDING,                  null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_CURRENT_DRAG,                 null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_BUBBLE_COLUMN_DRAG,           null), Layout.BORDER_HEIGHT);


        // World interations
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Speed debuffs", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_HONEY_SLOWDOWN,               null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_SLIME_SLOWDOWN,               null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_SOULSAND_SLOWDOWN,            null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_POWDER_SNOW_SLOWDOWN,         null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_WATER_SLOWDOWN,               null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_LAVA_SLOWDOWN,                null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_COBWEB_SLOWDOWN,              null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_LADDER_SLOWDOWN,              null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_VINES_SLOWDOWN,               null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_TWISTING_VINES_SLOWDOWN,      null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_WEEPING_VINES_SLOWDOWN,       null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_SWEET_BERRIES_SLOWDOWN,       null), Layout.BORDER_HEIGHT);


        // Visual clutter
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Visual clutter", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_BLOCK_BREAK_PARTICLES,   null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_ITEM_CHANGE_ANIMATION,   null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_HAND_SWING_ANIMATION,    null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_DIMENSION_CHANGE_SCREEN, null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_WATER_FOV_CHANGE,        null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_WATER_OVERLAY,           null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_LAVA_OVERLAY,            null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, CreativeTweaksClientFeatureSet.DISABLE_NETHER_PORTAL_OVERLAY,   null), Layout.BORDER_HEIGHT);




        {
            final String titleString = "TEST //TODO remove";
            rightSidebar.addWidget(new UiTextWidget(this, new UiTxt(titleString, Fonts.ui.bold, 2f), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        }
    }
}