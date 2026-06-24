package com.snek.engineersbliss.client.screens.alt_textures;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;




public class AltTexturesScreen extends __base_Screen {
    private static final int BUTTON_WIDTH = 200;


    public AltTexturesScreen() {
        super();
    }




    @Override
    protected void init() {
        // Visibility //TODO add header
        addButton(getToggleText(AltTextureFeature.TRANSPARENT_SLIME_BLOCK,      AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_SLIME_BLOCK)),      b -> toggleFeature(AltTextureFeature.TRANSPARENT_SLIME_BLOCK,      b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.TRANSPARENT_HONEY_BLOCK,      AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_HONEY_BLOCK)),      b -> toggleFeature(AltTextureFeature.TRANSPARENT_HONEY_BLOCK,      b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS, AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS)), b -> toggleFeature(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS, b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING,    AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING)),    b -> toggleFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING,    b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 3, BUTTON_WIDTH);

        // Misc //TODO add header
        addButton(getToggleText(AltTextureFeature.MINIMAL_REDSTONE_WIRE,           AltTexturesHandler.getFeature(AltTextureFeature.MINIMAL_REDSTONE_WIRE)),           b -> toggleFeature(AltTextureFeature.MINIMAL_REDSTONE_WIRE,           b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.CONSISTENT_SLOPED_RAILS,      AltTexturesHandler.getFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS)),      b -> toggleFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS,      b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH), LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH);

        // 3D //TODO add header
        addButton(getToggleText(AltTextureFeature.REDSTONE_WIRE_3D,             AltTexturesHandler.getFeature(AltTextureFeature.REDSTONE_WIRE_3D)),             b -> toggleFeature(AltTextureFeature.REDSTONE_WIRE_3D,             b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.RAILS_3D,                     AltTexturesHandler.getFeature(AltTextureFeature.RAILS_3D)),                     b -> toggleFeature(AltTextureFeature.RAILS_3D,                     b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH);
    }


    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        if(tabPressed) return;
        super.extractRenderState(graphics, mouseX, mouseY, delta);
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