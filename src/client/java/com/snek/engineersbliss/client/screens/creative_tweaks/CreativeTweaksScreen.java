package com.snek.engineersbliss.client.screens.creative_tweaks;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.parts.SteppedSlider;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;




public class CreativeTweaksScreen extends __base_Screen {
    private static final int BUTTON_WIDTH = 200;



    public CreativeTweaksScreen() {
        super();
    }




    @Override
    protected void init() {



        addRenderableWidget(new SteppedSlider<Float>(
            BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH, BUTTON_HEIGHT,
            "Flying speed", List.of(0.05f, 0.125f, 0.25f, 0.5f, 1f, 2f, 4f, 8f, 16f, 32f, 64f), 0, CreativeTweaksHandler::onFlyingSpeedChange
        ));
        addRenderableWidget(new SteppedSlider<Float>(
            BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH, BUTTON_HEIGHT,
            "Reach distance", List.of(4.5f, 8f, 16f, 32f, 64f, 128f, 256f, 8192f), 0, CreativeTweaksHandler::onReachDistanceChange
        ));
        addRenderableWidget(new SteppedSlider<Integer>(
            BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH, BUTTON_HEIGHT,
            "Interaction radius", List.of(1, 2, 3, 4, 5, 10, 20), 0, CreativeTweaksHandler::onInteractionRadiusChanged
        ));


        addButton(getToggleText(CreativeTweakFeature.PHASE_THROUGH_BLOCKS),          b -> toggleFeature(CreativeTweakFeature.PHASE_THROUGH_BLOCKS,          b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  0, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.PHASE_THROUGH_ENTITIES),        b -> toggleFeature(CreativeTweakFeature.PHASE_THROUGH_ENTITIES,        b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  1, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_SLIME_BOUNCE),          b -> toggleFeature(CreativeTweakFeature.DISABLE_SLIME_BOUNCE,          b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  2, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_HONEY_JUMP),            b -> toggleFeature(CreativeTweakFeature.DISABLE_HONEY_JUMP,            b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  3, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_HONEY_SLIDING),         b -> toggleFeature(CreativeTweakFeature.DISABLE_HONEY_SLIDING,         b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  4, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN),        b -> toggleFeature(CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN,        b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  5, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_HONEY_SLOWDOWN),        b -> toggleFeature(CreativeTweakFeature.DISABLE_HONEY_SLOWDOWN,        b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  6, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_SOULSAND_SLOWDOWN),     b -> toggleFeature(CreativeTweakFeature.DISABLE_SOULSAND_SLOWDOWN,     b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  7, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_ICE_SLIDING),           b -> toggleFeature(CreativeTweakFeature.DISABLE_ICE_SLIDING,           b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  8, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_CURRENT_DRAG),          b -> toggleFeature(CreativeTweakFeature.DISABLE_CURRENT_DRAG,          b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) *  9, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_FIRE_EFFECT),           b -> toggleFeature(CreativeTweakFeature.DISABLE_FIRE_EFFECT,           b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) * 10, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_FREEZING_EFFECT),       b -> toggleFeature(CreativeTweakFeature.DISABLE_FREEZING_EFFECT,       b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) * 11, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_ITEM_CHANGE_ANIMATION), b -> toggleFeature(CreativeTweakFeature.DISABLE_ITEM_CHANGE_ANIMATION, b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) * 12, BUTTON_WIDTH);
        addButton(getToggleText(CreativeTweakFeature.DISABLE_HAND_SWING_ANIMATION),  b -> toggleFeature(CreativeTweakFeature.DISABLE_HAND_SWING_ANIMATION,  b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), BORDER_HEIGHT + (BORDER_HEIGHT + BUTTON_HEIGHT) * 13, BUTTON_WIDTH);
    }




    public String getToggleText(final CreativeTweakFeature feature, final boolean state) {
        return feature.getName() + ": " + (state ? "ON" : "OFF");
    }
    public String getToggleText(final CreativeTweakFeature feature) {
        return getToggleText(feature, CreativeTweaksHandler.getFeature(feature));
    }


    public void toggleFeature(final CreativeTweakFeature feature, final Button b) {
        boolean newState = !CreativeTweaksHandler.getFeature(feature);
        b.setMessage(Component.literal(getToggleText(feature, newState)));
        CreativeTweaksHandler.setFeature(feature, newState);
    }
}