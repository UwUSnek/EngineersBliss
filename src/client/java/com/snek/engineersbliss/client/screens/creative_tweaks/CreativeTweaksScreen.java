package com.snek.engineersbliss.client.screens.creative_tweaks;

import static com.snek.engineersbliss.client.screens.base.__base_UiFeatureSetScreen.hoveredPreviewAtlasIds;
import static com.snek.engineersbliss.client.screens.base.__base_UiFeatureSetScreen.lastHoveredButton;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
import com.snek.engineersbliss.client.screens.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.screens.base.__base_UiScreen;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiButton;
import com.snek.engineersbliss.client.screens.parts.UiSpacer;
import com.snek.engineersbliss.client.screens.parts.UiSteppedSlider;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.texture_atlases.TextureAtlasTracker;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.Identifier;




public class CreativeTweaksScreen extends __base_UiFeatureSetScreen {



    public CreativeTweaksScreen() {
        super();
    }




    @Override
    protected void init() {
        super.init();



        {
            final String titleString = "Creative Tweaks";
            leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, 2f).withBoldFont(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);

            // Player properties
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Player properties", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidget(new UiSteppedSlider<Float>(
                0, 0, 0, 0,
                "Flying speed", CreativeTweaksServerFeatureSet.FLYING_SPEED.getValues(),
                CreativeTweaksServerFeatureSet.FLYING_SPEED.getDefault(),
                CreativeTweaksClientHandler::onFlyingSpeedChange
            ));
            leftSidebar.addWidget(new UiSteppedSlider<Float>(
                0, 0, 0, 0,
                "Reach distance",
                CreativeTweaksServerFeatureSet.INTERACTION_DISTANCE.getValues(),
                CreativeTweaksServerFeatureSet.INTERACTION_DISTANCE.getDefault(),
                CreativeTweaksClientHandler::onReachDistanceChange
            ));
            leftSidebar.addWidget(new UiSteppedSlider<Integer>(
                0, 0, 0, 0,
                "Interaction radius",
                CreativeTweaksServerFeatureSet.INTERACTION_RADIUS.getValues(),
                CreativeTweaksServerFeatureSet.INTERACTION_RADIUS.getDefault(),
                CreativeTweaksClientHandler::onInteractionRadiusChanged
            ));

            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.NO_SIGN_GUI,                "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.OPEN_OBSTRUCTED_CONTAINERS, "test"), Layout.BORDER_HEIGHT);



            // Player properties
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("World interactions", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.PHASE_THROUGH_BLOCKS_FLY,        "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.PHASE_THROUGH_ENTITIES,          "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_FIRE_EFFECT,             "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_FREEZING_EFFECT,         "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.FIX_HONEY_JUMP,              "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_HONEY_SLIDING,           "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_SLIME_BOUNCE,            "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_BED_BOUNCE,              "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_ICE_SLIDING,             "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_CURRENT_DRAG,            "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_BUBBLE_COLUMN_DRAG,      "test"), Layout.BORDER_HEIGHT);

            // World interations
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Speed debuffs", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_HONEY_SLOWDOWN,          "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN,          "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_SOULSAND_SLOWDOWN,       "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_POWDER_SNOW_SLOWDOWN,    "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_WATER_SLOWDOWN,          "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_LAVA_SLOWDOWN,           "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_COBWEB_SLOWDOWN,         "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_LADDER_SLOWDOWN,         "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_VINES_SLOWDOWN,          "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_TWISTING_VINES_SLOWDOWN, "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_WEEPING_VINES_SLOWDOWN,  "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_SWEET_BERRIES_SLOWDOWN,  "test"), Layout.BORDER_HEIGHT);

            // Visual clutter
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Visual clutter", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_ITEM_CHANGE_ANIMATION,   "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_HAND_SWING_ANIMATION,    "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_DIMENSION_CHANGE_SCREEN, "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_WATER_FOV_CHANGE,        "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_WATER_OVERLAY,           "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_LAVA_OVERLAY,            "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createCreativeTweakFeatureButton(CreativeTweakFeature.DISABLE_NETHER_PORTAL_OVERLAY,   "test"), Layout.BORDER_HEIGHT);
        }




        {
            final String titleString = "TEST //TODO remove";
            rightSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, 2f).withBoldFont(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        }
    }
}