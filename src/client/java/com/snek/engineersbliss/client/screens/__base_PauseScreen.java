package com.snek.engineersbliss.client.screens;

import java.util.function.Consumer;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public abstract class __base_PauseScreen extends Screen {

    protected __base_PauseScreen() {
        super(Component.literal(""));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


    protected Button addButton(String label, Consumer<Button> action, int x, int y, int width) {
        Button r = Button.builder(Component.literal(label), b -> { action.accept(b); b.setFocused(false); }).size(width, Layout.BUTTON_HEIGHT).pos(x, y).build();
        this.addRenderableWidget(r);
        return r;
    }


    @Override
    public void extractBlurredBackground(final GuiGraphicsExtractor graphics) {
        //! No blurred background
    }


    @Override
    public void onClose() {
        this.minecraft.setScreen(null); // Close screen and go back to game
    }
}
