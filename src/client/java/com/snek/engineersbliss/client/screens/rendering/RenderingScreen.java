package com.snek.engineersbliss.client.screens.rendering;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingClientFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.client.screens.rendering.widgets.BlockListWidget;
import com.snek.engineersbliss.client.ui.base.__base_UiSidebarScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiButton;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiToggleFeatureButton;
import com.snek.engineersbliss.client.ui.widgets.misc.UiEditBox;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiTextWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.LevelChunkSection;




public class RenderingScreen extends __base_UiSidebarScreen {
    private UiEditBox searchField;
    private BlockListWidget blockList;


    public RenderingScreen() {
        super();
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }


    //! Manually focus search bar bc for some reason Minecraft doesn't do that on its own
    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        searchField.setFocused(searchField.isHovered());
        return super.mouseClicked(event, doubleClick);
    }





    @Override
    protected void init() {
        super.init();


        // Add left sidebar title //TODO move to a generic featureSetScreen, rename old featureSetScreen to featureSetScreenWithPreview
        final UiTxt titleText = RenderingClientFeatureSet.INSTANCE.calcName();
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt(titleText.get(), 2f), TextAlignment.LEFT, Layout.fgColor), titleText.getScaledFont().getLineHeight());


        // Rendering filter
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Rendering Filter", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(searchField = new UiEditBox(this, new UiTxt("Search..."), searchString -> blockList.filter(searchString)), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, RenderingClientFeatureSet.RENDER_BLOCK_OUTLINES, null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, RenderingClientFeatureSet.RENDER_BLOCKS,         null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, RenderingClientFeatureSet.RENDER_FLUIDS,         null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, RenderingClientFeatureSet.RENDER_BLOCK_ENTITIES, null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, RenderingClientFeatureSet.RENDER_ENTITIES,       null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, RenderingClientFeatureSet.RENDER_PARTICLES,      null), Layout.BORDER_HEIGHT);


        // Misc
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Misc", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, RenderingClientFeatureSet.TARGET_HIDDEN_BLOCKS,  null), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiToggleFeatureButton(this, RenderingClientFeatureSet.SMOOTH_SHADING,        null), Layout.BORDER_HEIGHT);




        // Actions
        leftSidebar.addWidget(new UiSpacer(), Layout.BIG_SEPARATOR_HEIGHT);
        leftSidebar.addWidget(new UiTextWidget(this, new UiTxt("Actions", Layout.HEADER_SCALE), TextAlignment.LEFT, Layout.fgColor), Layout.HEADER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiButton(
            this,
            new UiTxt("Reset filters"),
            b -> {
                final String query = searchField.getValue();
                RenderingFilterHandler.init();
                rebuildWidgets();
                searchField.setValue(query);
            }
        ), Layout.BORDER_HEIGHT);
        leftSidebar.addWidgetAndSpacer(new UiButton(
            this,
            new UiTxt("Recalculate light"),
            b -> {
                final String query = searchField.getValue();
                RenderingFilterHandler.recalculateLight();
                rebuildWidgets();
                searchField.setValue(query);
            }
        ), Layout.BORDER_HEIGHT);




        // Main list
        //! This needs to be rendered last to let tooltips show on top of right side buttons
        blockList = new BlockListWidget(this.minecraft, 0, 0, LIST_TOP, 24);
        this.addRenderableWidget(blockList);
        blockList.filter("");
    }



    @Override
    public void layoutWidgets() {
        final int leftSidebarWidthPx  = (int)(width * leftSidebarWidth);
        final int rightSidebarWidthPx = (int)(width * rightSidebarWidth);

        // Main list
        blockList.setSize(width - leftSidebarWidthPx - rightSidebarWidthPx, this.height - LIST_TOP);
        blockList.setPosition(leftSidebarWidthPx, 0);

        super.layoutWidgets();
    }








    @Override
    public void _extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta) {
        final @NotNull ScaledFont scaledFont = Fonts.ui.regular.get(1f);
        final @NotNull Font font = scaledFont.getFont();
        final int lineBase = this.height;
        final int lineHeight = scaledFont.getLineHeight();
        if(tabPressed) return;

        super._extractRenderState(graphics, mouseX, mouseY, delta);


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
            final int rightTextX = this.width - (int)(width * rightSidebarWidth);
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
}