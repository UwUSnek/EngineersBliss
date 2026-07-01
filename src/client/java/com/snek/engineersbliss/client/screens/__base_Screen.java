package com.snek.engineersbliss.client.screens;

import java.util.function.Consumer;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;




public abstract class __base_Screen extends Screen {
    public static final int BORDER_WIDTH = 10;
    public static final int BORDER_HEIGHT = 4;
    public static final int LIST_TOP = 32;
    public static final int BUTTON_HEIGHT = 20;



    protected __base_Screen() {
        super(Component.literal(""));
    }



    protected boolean tabPressed = false;
    @Override
    public boolean keyPressed(KeyEvent event) {
        if(event.key() == InputConstants.KEY_TAB) {
            tabPressed = true;
            return true;
        }
        else {
            return super.keyPressed(event);
        }
    }
    @Override
    public boolean keyReleased(KeyEvent event) {
        if(event.key() == InputConstants.KEY_TAB) {
            tabPressed = false;
            return true;
        }
        else {
            return super.keyPressed(event);
        }
    }




    @Override
    public boolean isPauseScreen() {
        return false;
    }


    protected Button addButton(String label, String details, Consumer<Button> action, int x, int y, int width) {
        Button r = Button.builder(Component.literal(label), b -> { action.accept(b); b.setFocused(false); }).size(width, Layout.BUTTON_HEIGHT).pos(x, y).build();
        r.setTooltip(Tooltip.create(Component.literal(details)));
        this.addRenderableWidget(r);
        return r;
    }


    @Override
    public void extractBlurredBackground(final GuiGraphicsExtractor graphics) {
        //! No blurred background
    }


    @Override
	public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        //! No background
    }


    @Override
    public void onClose() {
        this.minecraft.setScreen(null); // Close screen and go back to game
    }
}
