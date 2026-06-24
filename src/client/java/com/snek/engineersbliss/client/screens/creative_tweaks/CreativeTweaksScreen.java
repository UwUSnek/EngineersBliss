package com.snek.engineersbliss.client.screens.creative_tweaks;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.parts.__base_Slider;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;




public class CreativeTweaksScreen extends __base_Screen {
    private static final int BUTTON_WIDTH = 200;
    private double flySpeed = 0.1;
    private double reachDistance = 4.5;
    private double interactionRadius = 1.0;


    public CreativeTweaksScreen() { }


    @Override
    protected void init() {

        addRenderableWidget(new __base_Slider(BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH, BUTTON_HEIGHT, "Flying Speed", 0.05, 1.0, flySpeed) {
            @Override protected void applyValue() {
                final Player player = minecraft.player;
                if(player != null && player.getAbilities().instabuild) {
                    player.getAbilities().setFlyingSpeed((float)flySpeed);
                }
            }
        });

        addRenderableWidget(new __base_Slider(BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH, BUTTON_HEIGHT, "Reach Distance", 1.0, 20.0, reachDistance) {
            @Override protected void applyValue() {
                final Player player = minecraft.player;
                if(player != null && player.getAbilities().instabuild) {
                    // var attr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
                    // if (attr != null) {
                    //     attr.removeModifier(REACH_MODIFIER_ID);
                    //     attr.addOrUpdateTransientModifier(new AttributeModifier(
                    //         REACH_MODIFIER_ID,
                    //         reachDistance - 4.5, // offset from default
                    //         AttributeModifier.Operation.ADD_VALUE
                    //     ));
                    // }
                }
            }
        });

        addRenderableWidget(new __base_Slider(BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH, BUTTON_HEIGHT, "Interaction Radius", 1.0, 10.0, interactionRadius) {
            @Override protected void applyValue() {
                interactionRadius = value;
                //TODO
            }
        });
    }




    public String getToggleText(final AltTextureFeature feature, final boolean state) {
        return feature.getName() + ": " + (state ? "ON" : "OFF");
    }


    public void toggleFeature(final AltTextureFeature feature, final Button b) {
        boolean newState = !AltTexturesHandler.getFeature(feature);
        b.setMessage(Component.literal(getToggleText(feature, newState)));
        AltTexturesHandler.setFeature(feature, newState);
        MinecraftUtils.refreshSectionsContaining(feature.getAffectedBlocks());
    }
}