package com.snek.engineersbliss.client.screens.rendering;

import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.screens.__base_PauseScreen;
import com.snek.engineersbliss.client.screens.rendering.widgets.BlockListWidget;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunkSection;




public class RenderingScreen extends __base_PauseScreen {
    private int panelWidthCenter;
    private int panelWidthSide;
    private int halfButtonWidth;


    private EditBox searchField;
    private BlockListWidget blockList;


    Button renderBlockOutlinesButton = null;
    Button renderBlocksButton = null;
    Button renderBlockEntitiesButton = null;
    Button renderFluidsButton = null;


    public RenderingScreen() {
        super();
    }


    @Override
    public boolean keyPressed(KeyEvent event) {
        if(!searchField.isFocused()) {
            if(event.key() == InputConstants.KEY_O) {
                toggleRenderBlockOutlines(renderBlockOutlinesButton);
                return true;
            }
            if(event.key() == InputConstants.KEY_E) {
                toggleRenderBlockEntities(renderBlockEntitiesButton);
                return true;
            }
            if(event.key() == InputConstants.KEY_F) {
                toggleRenderFluids(renderFluidsButton);
                return true;
            }
            if(event.key() == InputConstants.KEY_B) {
                toggleRenderBlocks(renderBlocksButton);
                return true;
            }
        }
        return super.keyPressed(event);
    }


    //! Manually focus search bar bc for some reason Minecraft doesn't do that on its own
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        searchField.setFocused(searchField.isHovered());
        return super.mouseClicked(event, doubleClick);
    }






    @Override
    protected void init() {

        this.panelWidthCenter = this.width / 2;
        this.panelWidthSide = (this.width - panelWidthCenter) / 2 - BORDER_WIDTH * 2;
        this.halfButtonWidth = (panelWidthSide - BORDER_WIDTH) / 2;




        // Left sidebar

        searchField = new EditBox(this.font, BORDER_WIDTH, LIST_TOP, panelWidthSide, 20, Component.literal("Search..."));
        searchField.setHint(Component.literal("Search..."));
        searchField.setMaxLength(Integer.MAX_VALUE);
        searchField.setResponder(searchString -> blockList.filter(searchString));
        searchField.setX(BORDER_WIDTH);
        this.addRenderableWidget(searchField);

        addButton(getToggleText_targetHiddenBlocks(RenderFilterHandler.getTargetHiddenBlocks()), this::toggleTargetHiddenBlocks, BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT), panelWidthSide);




        // Right sidebar

        addButton("Reset filters",     this::resetFilters,     this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP,                                   panelWidthSide);
        addButton("Recalculate light", this::recalculateLight, this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT), panelWidthSide);

        renderBlockOutlinesButton = addButton(getToggleText_renderBlockOutlines(RenderFilterHandler.getRenderBlockOutlines()), this::toggleRenderBlockOutlines, this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 3, panelWidthSide);
        renderBlocksButton        = addButton(getToggleText_renderBlocks       (RenderFilterHandler.getRenderBlocks()),        this::toggleRenderBlocks,        this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 4, panelWidthSide);
        renderBlockEntitiesButton = addButton(getToggleText_renderBlockEntities(RenderFilterHandler.getRenderBlockEntities()), this::toggleRenderBlockEntities, this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 5, panelWidthSide);
        renderFluidsButton        = addButton(getToggleText_renderFluids       (RenderFilterHandler.getRenderFluids()),        this::toggleRenderFluids,        this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 6, panelWidthSide);




        // Main list
        //! This needs to be rendered last to let tooltips show on top of right side buttons
        blockList = new BlockListWidget(this.minecraft, this, panelWidthCenter, this.height - LIST_TOP, LIST_TOP, 24);
        blockList.setX(panelWidthSide + BORDER_WIDTH * 2);
        this.addRenderableWidget(blockList);
        blockList.filter("");
    }




    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        final int lineBase = this.height;
        final int lineHeight = this.font.lineHeight;
        if(tabPressed) return;

        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);


        // Draw find syntax instructions
        final String[] syntaxInstructions = {
            "@", "Search blocks in loaded chunks",
            "#", "Search block tag",
            "&", "Search multiple strings",
            "|", "Search either of two strings"
        };
        for(int i = 0; i < syntaxInstructions.length / 2; i++) {
            graphics.text(this.font, syntaxInstructions[i * 2],     BORDER_WIDTH,      lineBase - lineHeight * (5 - i), 0xFFAAAAAA);
            graphics.text(this.font, syntaxInstructions[i * 2 + 1], BORDER_WIDTH + 16, lineBase - lineHeight * (5 - i), 0xFFAAAAAA);
        }


        // Draw render stats
        final ClientLevel level = Minecraft.getInstance().level;
        final int loadedChunkNum = MinecraftUtils.getLoadedChunkNumber();
        final int rightTextX = this.width - panelWidthSide;
        final int lightProgress = RenderFilterHandler.getLightRecalcProgress();
        final int lightMax = RenderFilterHandler.getLightRecalcMax();
        final String[] renderStats = {
            "Light calculation: ", lightProgress == lightMax ? "Idle" : String.format("%,d / %,d", lightProgress, lightMax),
            "Loaded chunks: ", String.format("%,d", loadedChunkNum),
            "Loaded blocks: ", String.format("%,d", (loadedChunkNum * level.getHeight() * LevelChunkSection.SECTION_WIDTH * LevelChunkSection.SECTION_WIDTH))
        };

        graphics.text(this.font, renderStats[0], rightTextX, lineBase - lineHeight * 4, 0xFFAAAAAA);
        graphics.text(this.font, renderStats[2], rightTextX, lineBase - lineHeight * 3, 0xFFAAAAAA);
        graphics.text(this.font, renderStats[4], rightTextX, lineBase - lineHeight * 2, 0xFFAAAAAA);
        int rightTextPrefixWidth = 0;
        for(int i = 0; i < renderStats.length; i += 2) {
            final int w = this.font.width(renderStats[i]);
            if(w > rightTextPrefixWidth) rightTextPrefixWidth = w;
        }

        graphics.text(this.font, renderStats[1], rightTextX + rightTextPrefixWidth, lineBase - lineHeight * 4, 0xFFAAAAAA);
        graphics.text(this.font, renderStats[3], rightTextX + rightTextPrefixWidth, lineBase - lineHeight * 3, 0xFFAAAAAA);
        graphics.text(this.font, renderStats[5], rightTextX + rightTextPrefixWidth, lineBase - lineHeight * 2, 0xFFAAAAAA);
    }




    public void resetFilters(final Button b) {

        // Reset settings
        RenderFilterHandler.init(false, true, true, true, true);
        MinecraftUtils.refreshRendering();

        // Respawn the entire screen to update buttons and checkboxes. Manually restore search query
        final String searchQuery = searchField.getValue();
        final RenderingScreen newScreen = new RenderingScreen();
        minecraft.setScreen(newScreen);
        newScreen.searchField.setValue(searchQuery);
    }




    public void recalculateLight(final Button b) {
        RenderFilterHandler.recalculateLight();
    }



    public String getToggleText_targetHiddenBlocks(final boolean state) {
        return "Target hidden blocks: " + (state ? "YES" : "NO");
    }
    public void toggleTargetHiddenBlocks(final Button b) {
        boolean newState = !RenderFilterHandler.getTargetHiddenBlocks();
        RenderFilterHandler.setTargetHiddenBlocks(newState);
        b.setMessage(Component.literal(getToggleText_targetHiddenBlocks(newState)));
    }


    public String getToggleText_renderBlockOutlines(final boolean state) {
        return "[O] Render block outlines: " + (state ? "YES" : "NO");
    }
    public void toggleRenderBlockOutlines(final Button b) {
        boolean newState = !RenderFilterHandler.getRenderBlockOutlines();
        RenderFilterHandler.setRenderBlockOutlines(newState);
        RenderFilterHandler.recalculate();
        MinecraftUtils.refreshRendering();
        b.setMessage(Component.literal(getToggleText_renderBlockOutlines(newState)));
    }


    public String getToggleText_renderBlocks(final boolean state) {
        return "[B] Render blocks: " + (state ? "YES" : "NO");
    }
    public void toggleRenderBlocks(final Button b) {
        boolean newState = !RenderFilterHandler.getRenderBlocks();
        RenderFilterHandler.setRenderBlocks(newState);
        RenderFilterHandler.recalculate();
        MinecraftUtils.refreshRendering();
        b.setMessage(Component.literal(getToggleText_renderBlocks(newState)));
    }


    public String getToggleText_renderBlockEntities(final boolean state) {
        return "[E] Render block entities: " + (state ? "YES" : "NO");
    }
    public void toggleRenderBlockEntities(final Button b) {
        boolean newState = !RenderFilterHandler.getRenderBlockEntities();
        RenderFilterHandler.setRenderBlockEntities(newState);
        RenderFilterHandler.recalculate();
        MinecraftUtils.refreshRendering();
        b.setMessage(Component.literal(getToggleText_renderBlockEntities(newState)));
    }


    public String getToggleText_renderFluids(final boolean state) {
        return "[F] Render fluids: " + (state ? "YES" : "NO");
    }
    public void toggleRenderFluids(final Button b) {
        boolean newState = !RenderFilterHandler.getRenderFluids();
        RenderFilterHandler.setRenderFluids(newState);
        RenderFilterHandler.recalculate();
        MinecraftUtils.refreshRendering();
        b.setMessage(Component.literal(getToggleText_renderFluids(newState)));
    }
}




//TODO add presets to the left
//TODO save and load buttons on the right of the preset name (which is editable)
//TODO storage them in the config folder of the client