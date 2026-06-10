package com.snek.engineersbliss.client.screens.rendering;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;




public class RenderingScreen extends Screen {
    public static final int BORDER_WIDTH = 10;
    public static final int BORDER_HEIGHT = 4;
    public static final int LIST_TOP = 32;

    private final Screen parent;
    private EditBox searchField;
    private BlockListWidget blockList;

    private boolean applied = false;
    public void markChanged() { applied = false; }


    public RenderingScreen(final Screen parent) {
        super(Component.literal(""));
        this.parent = parent;
    }




    @Override
    protected void init() {
        final int panelWidthCenter = this.width / 2;
        final int panelWidthSide = (this.width - panelWidthCenter) / 2 - BORDER_WIDTH * 2;

        final int buttonHeight = 20;
        final int halfButtonWidth = (panelWidthSide - BORDER_WIDTH) / 2;


        // Left sidebar
        searchField = new EditBox(this.font, BORDER_WIDTH, LIST_TOP, panelWidthSide, 20, Component.literal("Search..."));
        searchField.setResponder(searchString -> blockList.filter(searchString));
        searchField.setX(0 + BORDER_WIDTH);
        this.addRenderableWidget(searchField);


        // Main list
        blockList = new BlockListWidget(this.minecraft, this, panelWidthCenter, this.height - LIST_TOP, LIST_TOP, 24);
        blockList.setX(panelWidthSide + BORDER_WIDTH * 2);
        this.addRenderableWidget(blockList);
        blockList.filter("");


        // Right sidebar

        Button resetButton = Button.builder(Component.literal("Reset filter"), b -> { resetFilter(); b.setFocused(false); }).size(panelWidthSide, buttonHeight).build();
        resetButton.setX(this.width - panelWidthSide - BORDER_WIDTH);
        resetButton.setY(LIST_TOP);
        this.addRenderableWidget(resetButton);

        Button lightButton = Button.builder(Component.literal("Recalculate light"), b -> { recalculateLight(); b.setFocused(false); }).size(panelWidthSide, buttonHeight).build();
        lightButton.setX(this.width - panelWidthSide - BORDER_WIDTH);
        lightButton.setY(LIST_TOP + (buttonHeight + BORDER_HEIGHT));
        this.addRenderableWidget(lightButton);

        Button applyButton = Button.builder(Component.literal("Apply"), b -> { apply(); b.setFocused(false); }).size(halfButtonWidth, buttonHeight).build();
        applyButton.setX(panelWidthSide + panelWidthCenter + BORDER_WIDTH * 3);
        applyButton.setY(LIST_TOP + (buttonHeight + BORDER_HEIGHT) * 2);
        this.addRenderableWidget(applyButton);

        Button doneButton = Button.builder(Component.literal("Done"), b -> { done(); }).size(halfButtonWidth, buttonHeight).build();
        doneButton.setX(this.width - BORDER_WIDTH - halfButtonWidth);
        doneButton.setY(LIST_TOP + (buttonHeight + BORDER_HEIGHT) * 2);
        this.addRenderableWidget(doneButton);
    }




    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);
    }


    @Override
    public void extractBlurredBackground(final GuiGraphicsExtractor graphics) {
        //! No blurred background
    }



    @Override
    public void onClose() {
        this.minecraft.setScreen(null); // Close screen and go back to game
    }


    public void apply() {
        if(!applied) {
            blockList.flushChanges();
            RenderFilterHandler.recalculate();
            RenderFilterHandler.refreshRendering();
            applied = true;
        }
    }


    public void done() {
        apply();
        onClose();
    }



    public void resetFilter() {
        markChanged();
        RenderFilterHandler.init();
        blockList.filter(searchField.getValue());
        apply();
    }


    public void recalculateLight() {
        RenderFilterHandler.recalculateLight();
    }
}