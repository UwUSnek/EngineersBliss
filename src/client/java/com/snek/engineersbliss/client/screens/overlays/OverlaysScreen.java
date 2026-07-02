package com.snek.engineersbliss.client.screens.overlays;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.screens.__base_Screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;




public class OverlaysScreen extends __base_Screen {
    private static final int BUTTON_WIDTH = 200;


    public OverlaysScreen() {
        super();
    }




    @Override
    protected void init() {

        // Power levels  //TODO add header
        addButton(getToggleText(OverlayFeature.COMPARATOR_POWER_LEVELS),       OverlayFeature.COMPARATOR_POWER_LEVELS      .getDetails(), b -> toggleFeature(OverlayFeature.COMPARATOR_POWER_LEVELS,       b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH);
        addButton(getToggleText(OverlayFeature.REDSTONE_WIRE_POWER_LEVELS),    OverlayFeature.REDSTONE_WIRE_POWER_LEVELS   .getDetails(), b -> toggleFeature(OverlayFeature.REDSTONE_WIRE_POWER_LEVELS,    b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH);
        addButton(getToggleText(OverlayFeature.RAIL_POWER_LEVELS),             OverlayFeature.RAIL_POWER_LEVELS            .getDetails(), b -> toggleFeature(OverlayFeature.RAIL_POWER_LEVELS,             b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH);

        // Logic
        addButton(getToggleText(OverlayFeature.COMPARATOR_LOGIC_SNIPPET),      OverlayFeature.COMPARATOR_LOGIC_SNIPPET     .getDetails(), b -> toggleFeature(OverlayFeature.COMPARATOR_LOGIC_SNIPPET,      b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH);
        addButton(getToggleText(OverlayFeature.REDSTONE_WIRE_POWER_SOURCE),    OverlayFeature.REDSTONE_WIRE_POWER_SOURCE   .getDetails(), b -> toggleFeature(OverlayFeature.REDSTONE_WIRE_POWER_SOURCE,    b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH);
        addButton(getToggleText(OverlayFeature.RAIL_POWER_SOURCE),             OverlayFeature.RAIL_POWER_SOURCE            .getDetails(), b -> toggleFeature(OverlayFeature.RAIL_POWER_SOURCE,             b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH);
    }


    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        if(tabPressed) return;
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }





    public static String getToggleText(final OverlayFeature feature, final boolean state) {
        return feature.getName() + ": " + (state ? "ON" : "OFF");
    }
    public static String getToggleText(final OverlayFeature feature) {
        return getToggleText(feature, OverlaysHandler.getFeature(feature));
    }


    public static void toggleFeature(final OverlayFeature feature, final Button b) {
        boolean newState = !OverlaysHandler.getFeature(feature);
        b.setMessage(Component.literal(getToggleText(feature, newState)));
        OverlaysHandler.setFeature(feature, newState);
    }
}