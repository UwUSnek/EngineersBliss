package com.snek.engineersbliss.client.screens.rendering;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;




public class RenderingScreen extends Screen {
    private final Screen parent;


    public RenderingScreen(Screen parent) {
        super(Component.literal(""));
        this.parent = parent;
    }


    @Override
    protected void init() {
        this.addRenderableWidget(new BlockListWidget(
            this.minecraft,
            this.width,
            this.height - 64, // leave room for header/footer
            32,               // y start
            18                // item height
        ));
    }


    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);
    }


    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        //TODO custom background
    }


    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}