package com.snek.engineersbliss.client.screens.creative_tweaks;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiButton;
import com.snek.engineersbliss.client.screens.parts.UiSpacer;
import com.snek.engineersbliss.client.screens.parts.UiSteppedSlider;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.avif_textures.AvifTextureTracker;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.Identifier;




public class CreativeTweaksScreen extends __base_Screen {

    // Elements and layout
    private static UiWidgetList leftSidebar;
    private static UiWidgetList rightSidebar;
    private static final float LEFT_SIDEBAR_WIDTH = 0.25f;
    private static final float RIGHT_SIDEBAR_WIDTH = 0.25f;
    private static final float PREVIEW_WIDTH = 0.25f;

    // Hover data cache
    private static Identifier[] hoveredPreviewAtlasIds = null;
    private static UiButton lastHoveredButton = null;



    public CreativeTweaksScreen() {
        super();
    }




    @Override
    protected void init() {


        leftSidebar = new UiWidgetList((int)(width * LEFT_SIDEBAR_WIDTH), height, 0, 0, BUTTON_HEIGHT); {
            final String titleString = "Creative Tweaks";
            leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, 2f).withBoldFont(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);

            // Player properties
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Player properties", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidget(new UiSteppedSlider<Float>(
                0, 0, 0, 0,
                "Flying speed", List.of(0.05f, 0.125f, 0.25f, 0.5f, 1f, 2f, 4f, 8f, 16f, 32f, 64f), 0, CreativeTweaksHandler::onFlyingSpeedChange
            ));
            leftSidebar.addWidget(new UiSteppedSlider<Float>(
                0, 0, 0, 0,
                "Reach distance", List.of(4.5f, 8f, 16f, 32f, 64f, 128f, 256f, 8192f), 0, CreativeTweaksHandler::onReachDistanceChange
            ));
            leftSidebar.addWidget(new UiSteppedSlider<Integer>(
                0, 0, 0, 0,
                "Interaction radius", List.of(1, 2, 3, 4, 5, 10, 20, 50), 0, CreativeTweaksHandler::onInteractionRadiusChanged
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
        addRenderableWidget(leftSidebar);








        final int rightSidebarWidth = (int)(width * RIGHT_SIDEBAR_WIDTH);
        rightSidebar = new UiWidgetList(rightSidebarWidth, height, width - rightSidebarWidth, 0, BUTTON_HEIGHT); {
            final String titleString = "TEST //TODO remove";
            rightSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, 2f).withBoldFont(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        }
        addRenderableWidget(rightSidebar);
    }


    public static UiButton createCreativeTweakFeatureButton(final CreativeTweakFeature feature, final @Nullable String spriteName) {
        return createButton(
            getToggleText(feature),
            feature.getDetails(),
            b -> toggleFeature(feature, b),
            '\0',
            "creative_tweaks/" + spriteName,
            feature.name().toLowerCase()
        );
    }


    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        // Handle tab and normal element rendering
        if(tabPressed) return;
        super.extractRenderState(graphics, mouseX, mouseY, a);


        // Calculate feature preview position
        final float ratio = 9f / 4f; //! Vertical 4:9 for 1080x480 (1920/4) resolution
        final int w = (int)(width * PREVIEW_WIDTH);
        final int h = (int)(w * ratio);
        final int xOff = (width  - w) / 2 - w / 2 ;
        final int xOn  = (width  - w) / 2 + w / 2 ;
        final int y    = (height - h) / 2;


        // Find the hovered feature and calculate the remaining preview data
        final @Nullable UiWidgetList.Entry entry = leftSidebar.getHoveredEntry();
        if(entry == null) {
            hoveredPreviewAtlasIds = null;
            lastHoveredButton = null;
        }
        else {
            final AbstractWidget widget = entry.getWidget();
            if(widget instanceof UiButton button) {
                if(button != lastHoveredButton) {
                    lastHoveredButton = button;
                    final String fatureId = button.getFeatureId();
                    final String atlasPathOff = String.format("textures/gui/feature_previews/creative_tweaks/%s_off_0.avif", fatureId); //FIXME indices
                    final String atlasPathOn  = String.format("textures/gui/feature_previews/creative_tweaks/%s_on_0.avif",  fatureId); //FIXME indices
                    hoveredPreviewAtlasIds = new Identifier[] {
                        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathOff),
                        Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, atlasPathOn)
                    };
                }


        //TODO name of the feature at the top. also ON/OFF
        //TODO description at the bottom
                // Render the feature preview
                final Identifier atlasIdOff = hoveredPreviewAtlasIds[0];
                final Identifier atlasIdOn  = hoveredPreviewAtlasIds[1];
                if(!AvifTextureTracker.isTextureReady(atlasIdOff)) {
                    graphics.blit(atlasIdOff, xOff, y, xOff + w, y + h, 0f, 1f, 0f, 1f);
                }
                else {
                    final float[] uvOff = AvifTextureTracker.getUV(atlasIdOff, 0, System.currentTimeMillis());
                    graphics.blit(atlasIdOff, xOff, y, xOff + w, y + h, uvOff[0], uvOff[1], uvOff[2], uvOff[3]);
                }
                if(!AvifTextureTracker.isTextureReady(atlasIdOn)) {
                    graphics.blit(atlasIdOn,  xOn, y, xOn + w, y + h, 0f, 1f, 0f, 1f);
                }
                else {
                    final float[] uvOn  = AvifTextureTracker.getUV(atlasIdOn,  0, System.currentTimeMillis());
                    graphics.blit(atlasIdOn,  xOn, y, xOn + w, y + h, uvOn[0], uvOn[1], uvOn[2], uvOn[3]);
                }
            }
        }
    }








    public static Txt getToggleText(final CreativeTweakFeature feature, final boolean state) {
        return feature.getName().cat(": " + (state ? "ON" : "OFF"));
    }
    public static Txt getToggleText(final CreativeTweakFeature feature) {
        return getToggleText(feature, CreativeTweaksHandler.clientPlayerHasFeature(Minecraft.getInstance().player, feature));
    }


    public static void toggleFeature(final CreativeTweakFeature feature, final Button b) {
        final boolean newState = !CreativeTweaksHandler.clientPlayerHasFeature(Minecraft.getInstance().player, feature);
        b.setMessage(getToggleText(feature, newState).get());
        CreativeTweaksHandler.setFeature(feature, newState);
    }
}