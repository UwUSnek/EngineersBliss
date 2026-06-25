package com.snek.engineersbliss.client.screens.creative_tweaks;

import java.util.List;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.parts.SteppedSlider;
import com.snek.engineersbliss.client.screens.parts.__base_AnalogueSlider;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;




public class CreativeTweaksScreen extends __base_Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final float DEFAULT_FLYING_SPEED = new Abilities().getFlyingSpeed();
    private static final float DEFAULT_REACH = 4.5f; //FIXME get this from somewhere instead of hard coding it

    private static final Identifier REACH_MODIFIER_ID = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "creative_tweaks.reach");


    private static int interactionRadius = 1;




    public CreativeTweaksScreen() {
        super();
    }




    @Override
    protected void init() {



        addRenderableWidget(new SteppedSlider<Float>(
            BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH, BUTTON_HEIGHT,
            "Flying speed", List.of(0.05f, 0.125f, 0.25f, 0.5f, 1f, 2f, 4f, 8f, 16f, 32f, 64f), 0, CreativeTweaksScreen::onFlyingSpeedChange
        ));


        addRenderableWidget(new SteppedSlider<Float>(
            BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH, BUTTON_HEIGHT,
            "Reach distance", List.of(4.5f, 8f, 16f, 32f, 64f, 128f, 256f, 8192f), 0, CreativeTweaksScreen::onReachDistanceChange
        ));


        addRenderableWidget(new SteppedSlider<Integer>(
            BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH, BUTTON_HEIGHT,
            "Interaction radius", List.of(1, 2, 3, 4, 5, 10, 20), 0, CreativeTweaksScreen::onInteractionRadiusChanged
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










    private static void onFlyingSpeedChange(final SteppedSlider<Float> slider, final Float value) {
        final Player player = Minecraft.getInstance().player;
        if(player != null && player.getAbilities().instabuild) {
            player.getAbilities().setFlyingSpeed(value * DEFAULT_FLYING_SPEED);
        }
    }


    private static void onReachDistanceChange(final SteppedSlider<Float> slider, final Float value) {
        final Player player = Minecraft.getInstance().player;
        if(player != null && player.getAbilities().instabuild) {
            var attr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
            if(attr != null) {
                attr.addOrUpdateTransientModifier(new AttributeModifier(
                    REACH_MODIFIER_ID,
                    value - DEFAULT_REACH,
                    AttributeModifier.Operation.ADD_VALUE
                ));
            }
        }
    }


    private static void onInteractionRadiusChanged(final SteppedSlider<Integer> slider, final Integer value) {
        interactionRadius = value;
    }
}