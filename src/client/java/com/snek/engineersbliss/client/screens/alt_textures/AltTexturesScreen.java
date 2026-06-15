package com.snek.engineersbliss.client.screens.alt_textures;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.screens.__base_PauseScreen;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;




public class AltTexturesScreen extends __base_PauseScreen {


    public AltTexturesScreen() {
        super();
    }

    @Override
    protected void init() {
        addButton(getToggleText(AltTextureFeature.TRANSPARENT_SLIME_BLOCK,      AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_SLIME_BLOCK)),      b -> toggleFeature(AltTextureFeature.TRANSPARENT_SLIME_BLOCK,      b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, 200);
        addButton(getToggleText(AltTextureFeature.TRANSPARENT_HONEY_BLOCK,      AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_HONEY_BLOCK)),      b -> toggleFeature(AltTextureFeature.TRANSPARENT_HONEY_BLOCK,      b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, 200);
        addButton(getToggleText(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS, AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS)), b -> toggleFeature(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS, b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, 200);
        addButton(getToggleText(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING,    AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING)),    b -> toggleFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING,    b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 3, 200);
        addButton(getToggleText(AltTextureFeature.LINE_REDSTONE_WIRE,           AltTexturesHandler.getFeature(AltTextureFeature.LINE_REDSTONE_WIRE)),           b -> toggleFeature(AltTextureFeature.LINE_REDSTONE_WIRE,           b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 4, 200);
        addButton(getToggleText(AltTextureFeature.REDSTONE_WIRE_POWER_LEVELS,   AltTexturesHandler.getFeature(AltTextureFeature.REDSTONE_WIRE_POWER_LEVELS)),   b -> toggleFeature(AltTextureFeature.REDSTONE_WIRE_POWER_LEVELS,   b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 5, 200);
        addButton(getToggleText(AltTextureFeature.COMPARATOR_POWER_LEVELS,      AltTexturesHandler.getFeature(AltTextureFeature.COMPARATOR_POWER_LEVELS)),      b -> toggleFeature(AltTextureFeature.COMPARATOR_POWER_LEVELS,      b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 6, 200);
        addButton(getToggleText(AltTextureFeature.CONSISTENT_SLOPED_RAILS,      AltTexturesHandler.getFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS)),      b -> toggleFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS,      b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 7, 200);
    }


    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        //TODO stuff
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