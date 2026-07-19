package com.snek.engineersbliss.client.screens.alt_textures;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesClientFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysClientFeatureSet;
import com.snek.engineersbliss.client.screens.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiFeatureButton;
import com.snek.engineersbliss.client.screens.parts.UiSpacer;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;








public class AltTexturesScreen extends __base_UiFeatureSetScreen {
    private static UiWidgetList leftSidebar;
    private static final float LEFT_SIDEBAR_WIDTH = 0.25f;


    public AltTexturesScreen() {
        super();
    }




    @Override
    protected void init() {


        leftSidebar = new UiWidgetList((int)(width * LEFT_SIDEBAR_WIDTH), height, 0, 0, BUTTON_HEIGHT); {
            final String titleString = "Alternative Textures";
            leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, 2f).withBoldFont(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);

            // Visibility
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Visibility", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.MINIMAL_REDSTONE_WIRE),                   Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.NO_REDSTONE_DUST_PARTICLES),              Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.NO_CAMPFIRE_PARTICLES),                   Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.NO_FIRE_PARTICLES),                       Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.NO_LAVA_PARTICLES),                       Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.NO_WATER_STREAM_PARTICLES),               Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.NO_DRIP_PARTICLES),                       Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.TRANSPARENT_SLIME_BLOCK),                 Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.TRANSPARENT_HONEY_BLOCK),                 Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.UNOBSTRUCTIVE_MANGROVE_ROOTS),            Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.UNOBSTRUCTIVE_SCAFFOLDING),               Layout.BORDER_HEIGHT);

            // Fixes
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Fixes & performance", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.CONSISTENT_SLOPED_RAILS),                 Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.   BETTER_BARRIER_DISPLAY),                  Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.   BETTER_STRUCTURE_VOID_DISPLAY),           Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(OverlaysClientFeatureSet.   BETTER_LIGHT_BLOCK_DISPLAY),              Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.STATIC_CHESTS),                           Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.STATIC_SIGNS),                            Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.STATIC_BANNERS),                          Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.STATIC_DECORATED_POTS),                   Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.STATIC_BELLS),                            Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.STATIC_COPPER_GOLEM_STATUES),             Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.STATIC_LECTERNS),                         Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.OPTIMIZED_SHELVES),                       Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.OPTIMIZED_CAMPFIRES),                     Layout.BORDER_HEIGHT);

            // 3D models
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("3D models", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.REDSTONE_WIRE_3D),                        Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.RAILS_3D),                                Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.LADDERS_3D),                              Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.CHAINS_3D),                               Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.BARS_3D),                                 Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.VINES_3D),                                Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(new UiFeatureButton(AltTexturesClientFeatureSet.GLOW_LICHEN_3D),                          Layout.BORDER_HEIGHT);
        }
        addRenderableWidget(leftSidebar);
    }
}