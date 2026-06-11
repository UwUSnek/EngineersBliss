package com.snek.engineersbliss.client.screens.rendering;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;




public class RenderingScreen extends Screen {
    public static final int BORDER_WIDTH = 10;
    public static final int BORDER_HEIGHT = 4;
    public static final int LIST_TOP = 32;


    public static final int BUTTON_HEIGHT = 20;
    private int panelWidthCenter;
    private int panelWidthSide;
    private int halfButtonWidth;


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
    public boolean isPauseScreen() {
        return false;
    }




    @Override
    protected void init() {

        this.panelWidthCenter = this.width / 2;
        this.panelWidthSide = (this.width - panelWidthCenter) / 2 - BORDER_WIDTH * 2;
        this.halfButtonWidth = (panelWidthSide - BORDER_WIDTH) / 2;



        // Left sidebar

        searchField = new EditBox(this.font, BORDER_WIDTH, LIST_TOP, panelWidthSide, 20, Component.literal("Search..."));
        searchField.setResponder(searchString -> blockList.filter(searchString));
        searchField.setX(BORDER_WIDTH);
        this.addRenderableWidget(searchField);

        Button targetButton = Button.builder(Component.literal("Target hidden blocks: " + (RenderFilterHandler.getTargetHiddenBlocks() ? "YES" : "NO")), b -> {
            boolean newState = !RenderFilterHandler.getTargetHiddenBlocks();
            RenderFilterHandler.setTargetHiddenBlocks(newState);
            b.setMessage(Component.literal("Target hidden blocks: " + (newState ? "YES" : "NO")));
            b.setFocused(false);
            System.out.println("NEW STATE: " + newState);
        }).size(panelWidthSide, BUTTON_HEIGHT).build();
        targetButton.setX(BORDER_WIDTH);
        targetButton.setY(LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT));
        this.addRenderableWidget(targetButton);




        // Main list
        blockList = new BlockListWidget(this.minecraft, this, panelWidthCenter, this.height - LIST_TOP, LIST_TOP, 24);
        blockList.setX(panelWidthSide + BORDER_WIDTH * 2);
        this.addRenderableWidget(blockList);
        blockList.filter("");




        // Right sidebar

        Button resetButton = Button.builder(Component.literal("Reset filters"), b -> { resetFilters(); b.setFocused(false); }).size(panelWidthSide, BUTTON_HEIGHT).build();
        resetButton.setX(this.width - panelWidthSide - BORDER_WIDTH);
        resetButton.setY(LIST_TOP);
        this.addRenderableWidget(resetButton);

        Button lightButton = Button.builder(Component.literal("Recalculate light"), b -> { recalculateLight(); b.setFocused(false); }).size(panelWidthSide, BUTTON_HEIGHT).build();
        lightButton.setX(this.width - panelWidthSide - BORDER_WIDTH);
        lightButton.setY(LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT));
        this.addRenderableWidget(lightButton);

        Button applyButton = Button.builder(Component.literal("Apply"), b -> { apply(); b.setFocused(false); }).size(halfButtonWidth, BUTTON_HEIGHT).build();
        applyButton.setX(panelWidthSide + panelWidthCenter + BORDER_WIDTH * 3);
        applyButton.setY(LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2);
        this.addRenderableWidget(applyButton);

        Button doneButton = Button.builder(Component.literal("Done"), b -> { done(); }).size(halfButtonWidth, BUTTON_HEIGHT).build();
        doneButton.setX(this.width - BORDER_WIDTH - halfButtonWidth);
        doneButton.setY(LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2);
        this.addRenderableWidget(doneButton);
    }




    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        final int lineBase = this.height;
        final int lineHeight = this.font.lineHeight;

        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);


        // Draw find syntax instructions
        final String[] syntaxInstructions = {
            "@", "Search blocks in loaded chunks",
            "#", "Search block tag",
            "&", "Search multiple strings",
            "|", "Search either of two strings"
        };

        graphics.text(this.font, syntaxInstructions[0], BORDER_WIDTH,      lineBase - lineHeight * 5, 0xFFAAAAAA);
        graphics.text(this.font, syntaxInstructions[2], BORDER_WIDTH,      lineBase - lineHeight * 4, 0xFFAAAAAA);
        graphics.text(this.font, syntaxInstructions[4], BORDER_WIDTH,      lineBase - lineHeight * 3, 0xFFAAAAAA);
        graphics.text(this.font, syntaxInstructions[6], BORDER_WIDTH,      lineBase - lineHeight * 2, 0xFFAAAAAA);

        graphics.text(this.font, syntaxInstructions[1], BORDER_WIDTH + 16, lineBase - lineHeight * 5, 0xFFAAAAAA);
        graphics.text(this.font, syntaxInstructions[3], BORDER_WIDTH + 16, lineBase - lineHeight * 4, 0xFFAAAAAA);
        graphics.text(this.font, syntaxInstructions[5], BORDER_WIDTH + 16, lineBase - lineHeight * 3, 0xFFAAAAAA);
        graphics.text(this.font, syntaxInstructions[7], BORDER_WIDTH + 16, lineBase - lineHeight * 2, 0xFFAAAAAA);


        // Draw render stats
        final ClientLevel level = Minecraft.getInstance().level;
        final int loadedChunkNum = MinecraftUtils.getLoadedChunkNumber();
        final int rightTextX = this.width - panelWidthSide;
        final String[] renderStats = {
            "Loaded chunks: ", String.format("%,d", loadedChunkNum),
            "Loaded blocks: ", String.format("%,d", (loadedChunkNum * level.getHeight() * 16 * 16))
        };

        graphics.text(this.font, renderStats[0], rightTextX, lineBase - lineHeight * 3, 0xFFAAAAAA);
        graphics.text(this.font, renderStats[2], rightTextX, lineBase - lineHeight * 2, 0xFFAAAAAA);
        final int rightTextPrefixWidth = Math.max(this.font.width(renderStats[0]), this.font.width(renderStats[2]));

        graphics.text(this.font, renderStats[1], rightTextX + rightTextPrefixWidth, lineBase - lineHeight * 3, 0xFFAAAAAA);
        graphics.text(this.font, renderStats[3], rightTextX + rightTextPrefixWidth, lineBase - lineHeight * 2, 0xFFAAAAAA);
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



    public void resetFilters() {
        markChanged();
        RenderFilterHandler.init(RenderFilterHandler.getTargetHiddenBlocks());
        blockList.filter(searchField.getValue());
        apply();
    }


    public void recalculateLight() {
        RenderFilterHandler.recalculateLight();
    }
}