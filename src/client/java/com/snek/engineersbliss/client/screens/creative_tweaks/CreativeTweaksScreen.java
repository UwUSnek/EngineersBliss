package com.snek.engineersbliss.client.screens.creative_tweaks;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.UiFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.UiSteppedFeatureSlider;
import com.snek.engineersbliss.client.ui.widgets.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;








public class CreativeTweaksScreen extends __base_UiFeatureSetScreen {



    public CreativeTweaksScreen() {
        super(CreativeTweaksClientFeatureSet.INSTANCE);
    }




    @Override
    protected void init() {
        super.init();


        // Player properties
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(new UiTxt("Player properties", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (CreativeTweaksClientFeatureSet.WALKING_SPEED), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (CreativeTweaksClientFeatureSet.FLYING_SPEED,  CreativeTweaksClientHandler:: onFlyingSpeedChange), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Float>  (CreativeTweaksClientFeatureSet.INTERACTION_DISTANCE), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiSteppedFeatureSlider<Integer>(CreativeTweaksClientFeatureSet.INTERACTION_RADIUS),   Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.NO_SIGN_GUI),                Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.OPEN_OBSTRUCTED_CONTAINERS), Layout.BORDER_HEIGHT);



        // Player properties
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(new UiTxt("World interactions", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.PHASE_THROUGH_BLOCKS_FLY),        Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.PHASE_THROUGH_ENTITIES),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_FIRE_EFFECT),             Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_FREEZING_EFFECT),         Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.FIX_HONEY_JUMP),                  Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_HONEY_SLIDING),           Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_SLIME_BOUNCE),            Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_BED_BOUNCE),              Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_ICE_SLIDING),             Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_CURRENT_DRAG),            Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_BUBBLE_COLUMN_DRAG),      Layout.BORDER_HEIGHT);

        // World interations
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(new UiTxt("Speed debuffs", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_HONEY_SLOWDOWN),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_SLIME_SLOWDOWN),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_SOULSAND_SLOWDOWN),       Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_POWDER_SNOW_SLOWDOWN),    Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_WATER_SLOWDOWN),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_LAVA_SLOWDOWN),           Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_COBWEB_SLOWDOWN),         Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_LADDER_SLOWDOWN),         Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_VINES_SLOWDOWN),          Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_TWISTING_VINES_SLOWDOWN), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_WEEPING_VINES_SLOWDOWN),  Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_SWEET_BERRIES_SLOWDOWN),  Layout.BORDER_HEIGHT);

        // Visual clutter
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(new UiTxt("Visual clutter", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_ITEM_CHANGE_ANIMATION),   Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_HAND_SWING_ANIMATION),    Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_DIMENSION_CHANGE_SCREEN), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_WATER_FOV_CHANGE),        Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_WATER_OVERLAY),           Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_LAVA_OVERLAY),            Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiFeatureButton(CreativeTweaksClientFeatureSet.DISABLE_NETHER_PORTAL_OVERLAY),   Layout.BORDER_HEIGHT);




        {
            final String titleString = "TEST //TODO remove";
            rightSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, Fonts.ui.bold, 2f), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        }
    }
}