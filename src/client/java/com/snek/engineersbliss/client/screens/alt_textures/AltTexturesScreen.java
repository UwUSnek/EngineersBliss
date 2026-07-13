package com.snek.engineersbliss.client.screens.alt_textures;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiButton;
import com.snek.engineersbliss.client.screens.parts.UiSpacer;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;




public class AltTexturesScreen extends __base_Screen {
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
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.MINIMAL_REDSTONE_WIRE,        "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.NO_REDSTONE_DUST_PARTICLES,   "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.NO_CAMPFIRE_PARTICLES,        "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.NO_FIRE_PARTICLES,            "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.NO_LAVA_PARTICLES,            "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.NO_WATER_STREAM_PARTICLES,    "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.NO_DRIP_PARTICLES,            "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.TRANSPARENT_SLIME_BLOCK,      "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.TRANSPARENT_HONEY_BLOCK,      "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS, "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING,    "test"), Layout.BORDER_HEIGHT);

            // Fixes
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Fixes & performance", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.CONSISTENT_SLOPED_RAILS,    "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(OverlaysScreen.createOverlayFeatureButton(OverlayFeature.BETTER_BARRIER_DISPLAY,        "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(OverlaysScreen.createOverlayFeatureButton(OverlayFeature.BETTER_STRUCTURE_VOID_DISPLAY, "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(OverlaysScreen.createOverlayFeatureButton(OverlayFeature.BETTER_LIGHT_BLOCK_DISPLAY,    "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.STATIC_CHESTS,              "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.STATIC_SIGNS,               "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.STATIC_BANNERS,             "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.STATIC_DECORATED_POTS,      "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.STATIC_BELLS,               "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.STATIC_COPPER_GOLEM_STATUES,"test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.STATIC_LECTERNS,            "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.OPTIMIZED_SHELVES,          "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(            createAltTextureFeatureButton(AltTextureFeature.OPTIMIZED_CAMPFIRES,        "test"), Layout.BORDER_HEIGHT);

            // 3D models
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("3D models", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.REDSTONE_WIRE_3D,                          "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.RAILS_3D,                                  "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.LADDERS_3D,                                "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.CHAINS_3D,                                 "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.BARS_3D,                                   "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.VINES_3D,                                  "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createAltTextureFeatureButton(AltTextureFeature.GLOW_LICHEN_3D,                            "test"), Layout.BORDER_HEIGHT);
        }
        addRenderableWidget(leftSidebar);
    }


    public static UiButton createAltTextureFeatureButton(final AltTextureFeature feature, final @Nullable String spriteName) {
        return createButton(
            getToggleText(feature),
            feature.getDetails(),
            b -> toggleFeature(feature, b),
            '\0',
            "alt_textures/" + spriteName
        );
    }








    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        if(tabPressed) return;
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }





    public static Txt getToggleText(final AltTextureFeature feature, final boolean state) {
        return feature.getName().cat(": " + (state ? "ON" : "OFF"));
    }
    public static Txt getToggleText(final AltTextureFeature feature) {
        return getToggleText(feature, AltTexturesHandler.getFeature(feature));
    }


    public static void toggleFeature(final AltTextureFeature feature, final Button b) {
        final boolean newState = !AltTexturesHandler.getFeature(feature);
        b.setMessage(getToggleText(feature, newState).get());
        AltTexturesHandler.setFeature(feature, newState);
        MinecraftUtils.refreshSectionsContaining(feature.getAffectedBlocks());
    }
}