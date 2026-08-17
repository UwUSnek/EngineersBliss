//FIXME make this a subclass of __base_SidebarScreen
//FIXME make __base_FeatureSetScreen a subclass of __base_SidebarScreen

//FIXME move settings to the left sidebar
//FIXME move presets to the right sidebar





package com.snek.engineersbliss.client.screens.rendering;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.client.screens.rendering.widgets.BlockListWidget;
import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiButton;
import com.snek.engineersbliss.client.ui.widgets.misc.UiEditBox;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.LevelChunkSection;




public class RenderingScreen extends __base_UiScreen {
    @SuppressWarnings("java:S1450")
    private int panelWidthCenter;
    @SuppressWarnings("java:S1450")
    private int panelWidthSide;
    @SuppressWarnings("java:S1450")
    private int halfButtonWidth;


    private UiEditBox searchField;
    private BlockListWidget blockList;


    UiButton renderBlockOutlinesButton = null;
    UiButton renderBlocksButton = null;
    UiButton renderBlockEntitiesButton = null;
    UiButton renderFluidsButton = null;
    UiButton shadingFixButton = null;


    public RenderingScreen() {
        super();
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }


    @Override
    public boolean keyPressed(final KeyEvent event) {
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
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        searchField.setFocused(searchField.isHovered());
        return super.mouseClicked(event, doubleClick);
    }






    @Override
    protected void init() {

        this.panelWidthCenter = this.width / 2;
        this.panelWidthSide = (this.width - panelWidthCenter) / 2 - BORDER_WIDTH * 2;
        this.halfButtonWidth = (panelWidthSide - BORDER_WIDTH) / 2;




        // Left sidebar

        searchField = new UiEditBox(this, BORDER_WIDTH, LIST_TOP, panelWidthSide, 20, new UiTxt("Search...").get());
        searchField.setHint(new UiTxt("Search...").get());
        searchField.setMaxLength(Integer.MAX_VALUE);
        searchField.setResponder(searchString -> blockList.filter(searchString));
        searchField.setX(BORDER_WIDTH);
        this.addRenderableWidget(searchField);

        addButton(
            getToggleText_targetHiddenBlocks(RenderingFilterHandler.getTargetHiddenBlocks()),
            new UiTxt("Toggle targeting hidden blocks"),
            RenderingScreen::toggleTargetHiddenBlocks, BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT), panelWidthSide
        );




        // Right sidebar

        addButton(
            new UiTxt("Reset filters"),
            new UiTxt("Reset all rendering filters to their default state."),
            this::resetFilters, this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP, panelWidthSide
        );
        addButton(
            new UiTxt("Recalculate light"),
            new UiTxt("Recalculate all the light. This is a very resource intensive process that might take many seconds or minutes depending on your hardware."),
            RenderingScreen::recalculateLight, this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT), panelWidthSide
        );

        renderBlockOutlinesButton = addButton(
            getToggleText_renderBlockOutlines(RenderingFilterHandler.getRenderBlockOutlines()),
            new UiTxt("Toggle whether block outlines should be rendered at all"),
            RenderingScreen::toggleRenderBlockOutlines, this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 3, panelWidthSide
        );
        renderBlocksButton = addButton(
            getToggleText_renderBlocks(RenderingFilterHandler.getRenderBlocks()),
            new UiTxt("Toggle whether blocks without custom block entity rendering should be rendered at all."),
            RenderingScreen::toggleRenderBlocks,        this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 4, panelWidthSide
        );
        renderBlockEntitiesButton = addButton(
            getToggleText_renderBlockEntities(RenderingFilterHandler.getRenderBlockEntities()),
            new UiTxt("Toggle whether blocks with custom block entity rendering should be rendered at all."),
            RenderingScreen::toggleRenderBlockEntities, this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 5, panelWidthSide
        );
        renderFluidsButton = addButton(
            getToggleText_renderFluids(RenderingFilterHandler.getRenderFluids()),
            new UiTxt("Toggle whether fluids should be rendered at all."),
            RenderingScreen::toggleRenderFluids,        this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 6, panelWidthSide
        );
        shadingFixButton = addButton(
            getToggleText_shadingFix(RenderingFilterHandler.getFixShading()),
            new UiTxt("Fixes the weird shading Vanilla applies to certain blocks. This is most visible on Dirt Path and Farmland blocks."),
            RenderingScreen::toggleShadingFix,          this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 7, panelWidthSide
        );




        // Main list
        //! This needs to be rendered last to let tooltips show on top of right side buttons
        blockList = new BlockListWidget(this.minecraft, panelWidthCenter, this.height - LIST_TOP, LIST_TOP, 24);
        blockList.setX(panelWidthSide + BORDER_WIDTH * 2);
        this.addRenderableWidget(blockList);
        blockList.filter("");
    }




    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        final @NotNull ScaledFont scaledFont = Fonts.ui.regular.get(1f);
        final @NotNull Font font = scaledFont.getFont();
        final int lineBase = this.height;
        final int lineHeight = scaledFont.getLineHeight();
        if(tabPressed) return;

        super.extractRenderState(graphics, mouseX, mouseY, delta);


        // Draw find syntax instructions
        final String[] syntaxInstructions = {
            "@", "Search blocks in loaded chunks",
            "#", "Search block tag",
            "&", "Search multiple strings",
            "|", "Search either of two strings"
        };
        for(int i = 0; i < syntaxInstructions.length / 2; i++) {
            graphics.text(font, syntaxInstructions[i * 2    ], BORDER_WIDTH,      lineBase - lineHeight * (5 - i), 0xFFAAAAAA);
            graphics.text(font, syntaxInstructions[i * 2 + 1], BORDER_WIDTH + 16, lineBase - lineHeight * (5 - i), 0xFFAAAAAA);
        }


        // Draw render stats
        final ClientLevel level = Minecraft.getInstance().level;
        if(level != null) {
            final int loadedChunkNum = MinecraftUtils.getLoadedChunkNumber();
            final int rightTextX = this.width - panelWidthSide;
            final int lightProgress = RenderingFilterHandler.getLightRecalcProgress();
            final int lightMax = RenderingFilterHandler.getLightRecalcMax();
            final String[] renderStats = {
                "Light calculation: ", lightProgress == lightMax ? "Idle" : String.format("%,d / %,d", lightProgress, lightMax),
                "Loaded chunks: ", String.format("%,d", loadedChunkNum),
                "Loaded blocks: ", String.format("%,d", (loadedChunkNum * level.getHeight() * LevelChunkSection.SECTION_WIDTH * LevelChunkSection.SECTION_WIDTH))
            };

            graphics.text(font, renderStats[0], rightTextX, lineBase - lineHeight * 4, 0xFFAAAAAA);
            graphics.text(font, renderStats[2], rightTextX, lineBase - lineHeight * 3, 0xFFAAAAAA);
            graphics.text(font, renderStats[4], rightTextX, lineBase - lineHeight * 2, 0xFFAAAAAA);
            int rightTextPrefixWidth = 0;
            for(int i = 0; i < renderStats.length; i += 2) {
                final int w = scaledFont.calcWidth(renderStats[i]);
                if(w > rightTextPrefixWidth) rightTextPrefixWidth = w;
            }

            graphics.text(font, renderStats[1], rightTextX + rightTextPrefixWidth, lineBase - lineHeight * 4, 0xFFAAAAAA);
            graphics.text(font, renderStats[3], rightTextX + rightTextPrefixWidth, lineBase - lineHeight * 3, 0xFFAAAAAA);
            graphics.text(font, renderStats[5], rightTextX + rightTextPrefixWidth, lineBase - lineHeight * 2, 0xFFAAAAAA);
        }

        //TODO optimize stuff. put static elements in init()
    }




    public void resetFilters(final Button b) {

        // Reset settings
        RenderingFilterHandler.init(false, true, true, true, true);
        MinecraftUtils.refreshRendering();

        // Respawn the entire screen to update buttons and checkboxes. Manually restore search query
        final String searchQuery = searchField.getValue();
        final RenderingScreen newScreen = new RenderingScreen();
        minecraft.setScreen(newScreen);
        newScreen.searchField.setValue(searchQuery);
    }




    public static void recalculateLight(final Button b) {
        RenderingFilterHandler.recalculateLight();
    }



    public static UiTxt getToggleText_targetHiddenBlocks(final boolean state) {
        return new UiTxt("Target hidden blocks: " + (state ? "ON" : "OFF"));
    }
    public static void toggleTargetHiddenBlocks(final UiButton b) {
        final boolean newState = !RenderingFilterHandler.getTargetHiddenBlocks();
        RenderingFilterHandler.setTargetHiddenBlocks(newState);
        b.setLabel(getToggleText_targetHiddenBlocks(newState).get());
    }


    public static UiTxt getToggleText_renderBlockOutlines(final boolean state) {
        return new UiTxt("[O] Render block outlines: " + (state ? "ON" : "OFF"));
    }
    public static void toggleRenderBlockOutlines(final UiButton b) {
        final boolean newState = !RenderingFilterHandler.getRenderBlockOutlines();
        RenderingFilterHandler.setRenderBlockOutlines(newState);
        RenderingFilterHandler.recalculate();
        MinecraftUtils.refreshRendering();
        b.setLabel(getToggleText_renderBlockOutlines(newState).get());
    }


    public static UiTxt getToggleText_renderBlocks(final boolean state) {
        return new UiTxt("[B] Render blocks: " + (state ? "ON" : "OFF"));
    }
    public static void toggleRenderBlocks(final UiButton b) {
        final boolean newState = !RenderingFilterHandler.getRenderBlocks();
        RenderingFilterHandler.setRenderBlocks(newState);
        RenderingFilterHandler.recalculate();
        MinecraftUtils.refreshRendering();
        b.setLabel(getToggleText_renderBlocks(newState).get());
    }


    public static UiTxt getToggleText_renderBlockEntities(final boolean state) {
        return new UiTxt("[E] Render block entities: " + (state ? "ON" : "OFF"));
    }
    public static void toggleRenderBlockEntities(final UiButton b) {
        final boolean newState = !RenderingFilterHandler.getRenderBlockEntities();
        RenderingFilterHandler.setRenderBlockEntities(newState);
        RenderingFilterHandler.recalculate();
        MinecraftUtils.refreshRendering();
        b.setLabel(getToggleText_renderBlockEntities(newState).get());
    }


    public static UiTxt getToggleText_renderFluids(final boolean state) {
        return new UiTxt("[F] Render fluids: " + (state ? "ON" : "OFF"));
    }
    public static void toggleRenderFluids(final UiButton b) {
        final boolean newState = !RenderingFilterHandler.getRenderFluids();
        RenderingFilterHandler.setRenderFluids(newState);
        RenderingFilterHandler.recalculate();
        MinecraftUtils.refreshRendering();
        b.setLabel(getToggleText_renderFluids(newState).get());
    }


    public static UiTxt getToggleText_shadingFix(final boolean state) {
        return new UiTxt("Smooth Shading: " + (state ? "ON" : "OFF"));
    }
    public static void toggleShadingFix(final UiButton b) {
        final boolean newState = !RenderingFilterHandler.getFixShading();
        RenderingFilterHandler.setShadingFix(newState);
        MinecraftUtils.refreshRendering();
        b.setLabel(getToggleText_shadingFix(newState).get());
    }
}




//TODO add presets to the left
//TODO save and load buttons on the right of the preset name (which is editable)
//TODO storage them in the config folder of the client