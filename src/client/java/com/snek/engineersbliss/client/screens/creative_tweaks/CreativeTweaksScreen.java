package com.snek.engineersbliss.client.screens.creative_tweaks;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksHandler;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.parts.SteppedSlider;




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
    }




    // public String getToggleText(final AltTextureFeature feature, final boolean state) {
    //     return feature.getName() + ": " + (state ? "ON" : "OFF");
    // }


    // public void toggleFeature(final AltTextureFeature feature, final Button b) {
    //     boolean newState = !AltTexturesHandler.getFeature(feature);
    //     b.setMessage(Component.literal(getToggleText(feature, newState)));
    //     AltTexturesHandler.setFeature(feature, newState);
    //     MinecraftUtils.refreshSectionsContaining(feature.getAffectedBlocks());
    // }
}