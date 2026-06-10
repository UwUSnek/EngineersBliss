package com.snek.engineersbliss.client.screens.rendering;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.lighting.SkyLightEngine;




public class RenderingScreen extends Screen {
    public static final int BORDER_WIDTH = 10;
    public static final int LIST_TOP = 32;

    private final Screen parent;
    private EditBox searchField;
    private BlockListWidget blockList;


    public RenderingScreen(Screen parent) {
        super(Component.literal(""));
        this.parent = parent;
    }


    @Override
    protected void init() {
        int panelWidthCenter = this.width / 2 - BORDER_WIDTH * 2;
        int panelWidthSide = (this.width - panelWidthCenter) / 2 - BORDER_WIDTH * 2;

        // Left sidebar
        searchField = new EditBox(this.font, BORDER_WIDTH, LIST_TOP, panelWidthSide, 20, Component.literal("Search..."));
        searchField.setResponder(searchString -> blockList.filter(searchString));
        searchField.setX(0 + BORDER_WIDTH);
        this.addRenderableWidget(searchField);

        // Main list
        blockList = new BlockListWidget(this.minecraft, panelWidthCenter, this.height - LIST_TOP, LIST_TOP, 24);
        blockList.setX(panelWidthSide + BORDER_WIDTH * 3);
        this.addRenderableWidget(blockList);
        blockList.filter("");

        // Right sidebar
        //TODO
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