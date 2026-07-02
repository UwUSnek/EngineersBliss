package com.snek.engineersbliss.client.screens;

import java.util.function.Consumer;

import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.client.screens.parts.UiTxt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;




public abstract class __base_Screen extends Screen {
    public static final int BORDER_WIDTH  = Layout.BORDER_WIDTH;
    public static final int BORDER_HEIGHT = Layout.BORDER_HEIGHT;
    public static final int LIST_TOP      = Layout.LIST_TOP;
    public static final int BUTTON_HEIGHT = Layout.BUTTON_HEIGHT;



    protected __base_Screen() {
        super(new UiTxt().get());
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
            return super.keyReleased(event);
        }
    }




    @Override
    public boolean isPauseScreen() {
        return false;
    }


    protected Button addButton(String label, String details, Consumer<Button> action, int x, int y, int width) {
        Button r =
            Button.builder(
                new UiTxt(label).get(),
                b -> { action.accept(b); b.setFocused(false); }
            )
            .size(width, BUTTON_HEIGHT)
            .pos(x, y)
            .tooltip(Tooltip.create(new UiTxt(details).get()))
            .build()
        ;
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
