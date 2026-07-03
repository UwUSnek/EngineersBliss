package com.snek.engineersbliss.client.screens.alt_textures;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlayFeature;
import com.snek.engineersbliss.client.screens.__base_Screen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;




public class AltTexturesScreen extends __base_Screen {
    private static final int BUTTON_WIDTH = 200;


    public AltTexturesScreen() {
        super();
    }




    @Override
    protected void init() {
        // Visibility //TODO add header
        addButton(getToggleText(AltTextureFeature.MINIMAL_REDSTONE_WIRE),        AltTextureFeature.MINIMAL_REDSTONE_WIRE       .getDetails(), b -> toggleFeature(AltTextureFeature.MINIMAL_REDSTONE_WIRE,        b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH);
        //TODO add disable redstone wire particles here
        addButton(getToggleText(AltTextureFeature.TRANSPARENT_SLIME_BLOCK),      AltTextureFeature.TRANSPARENT_SLIME_BLOCK     .getDetails(), b -> toggleFeature(AltTextureFeature.TRANSPARENT_SLIME_BLOCK,      b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.TRANSPARENT_HONEY_BLOCK),      AltTextureFeature.TRANSPARENT_HONEY_BLOCK     .getDetails(), b -> toggleFeature(AltTextureFeature.TRANSPARENT_HONEY_BLOCK,      b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS), AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS.getDetails(), b -> toggleFeature(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS, b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 3, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING),    AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING   .getDetails(), b -> toggleFeature(AltTextureFeature.UNOBSTRUCTIVE_SCAFFOLDING,    b), BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 4, BUTTON_WIDTH);

        // Fixes //TODO add header
        addButton(               getToggleText(AltTextureFeature.CONSISTENT_SLOPED_RAILS),      AltTextureFeature.CONSISTENT_SLOPED_RAILS     .getDetails(), b ->                toggleFeature(AltTextureFeature.CONSISTENT_SLOPED_RAILS,    b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH);
        addButton(OverlaysScreen.getToggleText(OverlayFeature.BETTER_BARRIER_DISPLAY),          OverlayFeature.BETTER_BARRIER_DISPLAY         .getDetails(), b -> OverlaysScreen.toggleFeature(OverlayFeature.BETTER_BARRIER_DISPLAY,        b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH);
        addButton(OverlaysScreen.getToggleText(OverlayFeature.BETTER_STRUCTURE_VOID_DISPLAY),   OverlayFeature.BETTER_STRUCTURE_VOID_DISPLAY  .getDetails(), b -> OverlaysScreen.toggleFeature(OverlayFeature.BETTER_STRUCTURE_VOID_DISPLAY, b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH);
        addButton(OverlaysScreen.getToggleText(OverlayFeature.BETTER_LIGHT_BLOCK_DISPLAY),      OverlayFeature.BETTER_LIGHT_BLOCK_DISPLAY     .getDetails(), b -> OverlaysScreen.toggleFeature(OverlayFeature.BETTER_LIGHT_BLOCK_DISPLAY,    b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 1, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 3, BUTTON_WIDTH);


        // 3D //TODO add header
        addButton(getToggleText(AltTextureFeature.REDSTONE_WIRE_3D),             AltTextureFeature.REDSTONE_WIRE_3D            .getDetails(), b -> toggleFeature(AltTextureFeature.REDSTONE_WIRE_3D,             b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.RAILS_3D),                     AltTextureFeature.RAILS_3D                    .getDetails(), b -> toggleFeature(AltTextureFeature.RAILS_3D,                     b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.LADDERS_3D),                   AltTextureFeature.LADDERS_3D                  .getDetails(), b -> toggleFeature(AltTextureFeature.LADDERS_3D,                   b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.CHAINS_3D),                    AltTextureFeature.CHAINS_3D                   .getDetails(), b -> toggleFeature(AltTextureFeature.CHAINS_3D,                    b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 3, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.BARS_3D),                      AltTextureFeature.BARS_3D                     .getDetails(), b -> toggleFeature(AltTextureFeature.BARS_3D,                      b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 4, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.VINES_3D),                     AltTextureFeature.VINES_3D                    .getDetails(), b -> toggleFeature(AltTextureFeature.VINES_3D,                     b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 5, BUTTON_WIDTH);
        addButton(getToggleText(AltTextureFeature.GLOW_LICHEN_3D),               AltTextureFeature.GLOW_LICHEN_3D              .getDetails(), b -> toggleFeature(AltTextureFeature.GLOW_LICHEN_3D,               b), BORDER_WIDTH + (BORDER_WIDTH + BUTTON_WIDTH) * 2, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 6, BUTTON_WIDTH);
    }


    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        if(tabPressed) return;
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }





    public static Txt getToggleText(final AltTextureFeature feature, final boolean state) {
        return new UiTxt(feature.getName() + ": " + (state ? "ON" : "OFF"));
    }
    public static Txt getToggleText(final AltTextureFeature feature) {
        return getToggleText(feature, AltTexturesHandler.getFeature(feature));
    }


    public static void toggleFeature(final AltTextureFeature feature, final Button b) {
        boolean newState = !AltTexturesHandler.getFeature(feature);
        b.setMessage(getToggleText(feature, newState).get());
        AltTexturesHandler.setFeature(feature, newState);
        MinecraftUtils.refreshSectionsContaining(feature.getAffectedBlocks());
    }
}