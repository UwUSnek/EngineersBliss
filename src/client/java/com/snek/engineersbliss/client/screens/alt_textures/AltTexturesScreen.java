package com.snek.engineersbliss.client.screens.alt_textures;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesClientFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysClientFeatureSet;
import com.snek.engineersbliss.client.ui.base.__base_UiFeatureSetScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiToggleFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;








public class AltTexturesScreen extends __base_UiFeatureSetScreen {


    public AltTexturesScreen() {
        super(AltTexturesClientFeatureSet.INSTANCE);
    }




    @Override
    protected void init() {
        super.init();


        // Visibility
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Visibility", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.MINIMAL_REDSTONE_WIRE,         null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.NO_REDSTONE_DUST_PARTICLES,    null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.NO_CAMPFIRE_PARTICLES,         null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.NO_FIRE_PARTICLES,             null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.NO_LAVA_PARTICLES,             null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.NO_WATER_STREAM_PARTICLES,     null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.NO_DRIP_PARTICLES,             null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.TRANSPARENT_SLIME_BLOCK,       null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.TRANSPARENT_HONEY_BLOCK,       null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.UNOBSTRUCTIVE_MANGROVE_ROOTS,  null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.UNOBSTRUCTIVE_SCAFFOLDING,     null), Layout.BORDER_HEIGHT);


        // Fixes
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Fixes & performance", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.CONSISTENT_SLOPED_RAILS,       null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, OverlaysClientFeatureSet.   BETTER_BARRIER_DISPLAY,        null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, OverlaysClientFeatureSet.   BETTER_STRUCTURE_VOID_DISPLAY, null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, OverlaysClientFeatureSet.   BETTER_LIGHT_BLOCK_DISPLAY,    null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.STATIC_CHESTS,                 null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.STATIC_SIGNS,                  null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.STATIC_BANNERS,                null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.STATIC_DECORATED_POTS,         null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.STATIC_BELLS,                  null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.STATIC_COPPER_GOLEM_STATUES,   null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.STATIC_LECTERNS,               null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.STATIC_BEDS,                   null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.OPTIMIZED_SHELVES,             null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.OPTIMIZED_CAMPFIRES,           null), Layout.BORDER_HEIGHT);


        // 3D models
        leftSidebar.addWidget(new UiSpacer(this), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("3D models", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.REDSTONE_WIRE_3D,              null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.RAILS_3D,                      null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.LADDERS_3D,                    null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.CHAINS_3D,                     null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.BARS_3D,                       null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.VINES_3D,                      null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, AltTexturesClientFeatureSet.GLOW_LICHEN_3D,                null), Layout.BORDER_HEIGHT);
    }
}