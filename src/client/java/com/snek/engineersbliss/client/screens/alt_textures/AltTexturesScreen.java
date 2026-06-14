package com.snek.engineersbliss.client.screens.alt_textures;

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
        addButton(getToggleText_transparentSlimeBlock     (AltTexturesHandler.getTransparentSlimeBlock     ()), this::toggleTransparentSlimeBlock,      BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 0, 200);
        addButton(getToggleText_transparentHoneyBlock     (AltTexturesHandler.getTransparentHoneyBlock     ()), this::toggleTransparentHoneyBlock,      BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 1, 200);
        addButton(getToggleText_unobstructiveMangroveRoots(AltTexturesHandler.getUnobstructiveMangroveRoots()), this::toggleUnobstructiveMangroveRoots, BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, 200);
        addButton(getToggleText_unobstructiveScaffolding  (AltTexturesHandler.getUnobstructiveScaffolding  ()), this::toggleUnobstructiveScaffolding,   BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 3, 200);
        addButton(getToggleText_lineRedstoneDust          (AltTexturesHandler.getLineRedstoneDust          ()), this::toggleLineRedstoneDust,           BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 4, 200);
    }


    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        //TODO stuff
    }






    public String getToggleText_transparentSlimeBlock(final boolean state) {
        return "TransparentSlimeBlock" + (state ? "ON" : "OFF");
    }
    public void toggleTransparentSlimeBlock(final Button b) {
        boolean newState = !AltTexturesHandler.getTransparentSlimeBlock();
        b.setMessage(Component.literal(getToggleText_transparentSlimeBlock(newState)));
        AltTexturesHandler.setTransparentSlimeBlock(newState);
        MinecraftUtils.refreshRendering();
    }


    public String getToggleText_transparentHoneyBlock(final boolean state) {
        return "TransparentHoneyBlock" + (state ? "ON" : "OFF");
    }
    public void toggleTransparentHoneyBlock(final Button b) {
        boolean newState = !AltTexturesHandler.getTransparentHoneyBlock();
        b.setMessage(Component.literal(getToggleText_transparentHoneyBlock(newState)));
        AltTexturesHandler.setTransparentHoneyBlock(newState);
        MinecraftUtils.refreshRendering();
    }


    public String getToggleText_unobstructiveMangroveRoots(final boolean state) {
        return "UnobstructiveMangroveRoots" + (state ? "ON" : "OFF");
    }
    public void toggleUnobstructiveMangroveRoots(final Button b) {
        boolean newState = !AltTexturesHandler.getUnobstructiveMangroveRoots();
        b.setMessage(Component.literal(getToggleText_unobstructiveMangroveRoots(newState)));
        AltTexturesHandler.setUnobstructiveMangroveRoots(newState);
        MinecraftUtils.refreshRendering();
    }


    public String getToggleText_unobstructiveScaffolding(final boolean state) {
        return "UnobstructiveScaffolding" + (state ? "ON" : "OFF");
    }
    public void toggleUnobstructiveScaffolding(final Button b) {
        boolean newState = !AltTexturesHandler.getUnobstructiveScaffolding();
        b.setMessage(Component.literal(getToggleText_unobstructiveScaffolding(newState)));
        AltTexturesHandler.setUnobstructiveScaffolding(newState);
        MinecraftUtils.refreshRendering();
    }


    public String getToggleText_lineRedstoneDust(final boolean state) {
        return "LineRedstoneDust" + (state ? "ON" : "OFF");
    }
    public void toggleLineRedstoneDust(final Button b) {
        boolean newState = !AltTexturesHandler.getLineRedstoneDust();
        b.setMessage(Component.literal(getToggleText_lineRedstoneDust(newState)));
        AltTexturesHandler.setLineRedstoneDust(newState);
        MinecraftUtils.refreshRendering();
    }
}