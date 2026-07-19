package com.snek.engineersbliss.client.screens.overlays;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.screens.base.__base_UiScreen;
import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.client.screens.parts.UiButton;
import com.snek.engineersbliss.client.screens.parts.UiSpacer;
import com.snek.engineersbliss.client.screens.parts.UiTextWidget;
import com.snek.engineersbliss.client.screens.parts.UiWidgetList;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;




public class OverlaysScreen extends __base_UiScreen {
    private static UiWidgetList leftSidebar;
    private static final float LEFT_SIDEBAR_WIDTH = 0.25f;


    public OverlaysScreen() {
        super();
    }




    @Override
    protected void init() {


        leftSidebar = new UiWidgetList((int)(width * LEFT_SIDEBAR_WIDTH), height, 0, 0, BUTTON_HEIGHT); {
            final String titleString = "Overlays";
            leftSidebar.addWidget(new UiTextWidget(new UiTxt(titleString, 2f).withBoldFont(), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);

            // Power levels
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Power levels", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createOverlayFeatureButton(OverlayFeature.COMPARATOR_POWER_LEVELS,    "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createOverlayFeatureButton(OverlayFeature.REDSTONE_WIRE_POWER_LEVELS, "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createOverlayFeatureButton(OverlayFeature.RAIL_POWER_LEVELS,          "test"), Layout.BORDER_HEIGHT);

            // Logic
            leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
            leftSidebar.addWidget(new UiTextWidget(new UiTxt("Block logic", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createOverlayFeatureButton(OverlayFeature.COMPARATOR_LOGIC_SNIPPET,   "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createOverlayFeatureButton(OverlayFeature.REDSTONE_WIRE_POWER_SOURCE, "test"), Layout.BORDER_HEIGHT);
            leftSidebar.addWidgetAndSpacer(createOverlayFeatureButton(OverlayFeature.RAIL_POWER_SOURCE,          "test"), Layout.BORDER_HEIGHT);
        }
        addRenderableWidget(leftSidebar);
    }


    public static UiButton createOverlayFeatureButton(final OverlayFeature feature, final @Nullable String spriteName) {
        return createButton(
            getToggleText(feature),
            feature.getDetails(),
            b -> toggleFeature(feature, b),
            '\0',
            "overlays/" + spriteName,
            feature.name().toLowerCase()
        );
    }








    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        if(tabPressed) return;
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }





    public static Txt getToggleText(final OverlayFeature feature, final boolean state) {
        return feature.getName().cat(": " + (state ? "ON" : "OFF"));
    }
    public static Txt getToggleText(final OverlayFeature feature) {
        return getToggleText(feature, OverlaysHandler.getFeature(feature));
    }


    public static void toggleFeature(final OverlayFeature feature, final Button b) {
        final boolean newState = !OverlaysHandler.getFeature(feature);
        b.setMessage(getToggleText(feature, newState).get());
        OverlaysHandler.setFeature(feature, newState);
    }
}