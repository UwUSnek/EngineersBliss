package com.snek.engineersbliss.client.screens.rendering;

import java.util.function.Consumer;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.screens.rendering.widgets.BlockListWidget;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
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



    private boolean changedRenderBlockOutlines = false; //! Initialized by the screen's init function, also changed by buttons
    private boolean changedRenderBlocks        = false; //! Initialized by the screen's init function, also changed by buttons
    private boolean changedRenderBlockEntities = false; //! Initialized by the screen's init function, also changed by buttons
    private boolean changedRenderFluids        = false; //! Initialized by the screen's init function, also changed by buttons
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


    private Button addButton(String label, Consumer<Button> action, int x, int y, int width) {
        Button r = Button.builder(Component.literal(label), b -> { action.accept(b); b.setFocused(false); }).size(width, BUTTON_HEIGHT).pos(x, y).build();
        this.addRenderableWidget(r);
        return r;
    }




    @Override
    protected void init() {

        this.panelWidthCenter = this.width / 2;
        this.panelWidthSide = (this.width - panelWidthCenter) / 2 - BORDER_WIDTH * 2;
        this.halfButtonWidth = (panelWidthSide - BORDER_WIDTH) / 2;




        // Left sidebar

        searchField = new EditBox(this.font, BORDER_WIDTH, LIST_TOP, panelWidthSide, 20, Component.literal("Search..."));
        searchField.setMaxLength(Integer.MAX_VALUE);
        searchField.setResponder(searchString -> blockList.filter(searchString));
        searchField.setX(BORDER_WIDTH);
        this.addRenderableWidget(searchField);

        addButton(getToggleText_targetHiddenBlocks(RenderFilterHandler.getTargetHiddenBlocks()), this::toggleTargetHiddenBlocks, BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT), panelWidthSide);




        // Right sidebar

        addButton("Reset filters",     this::resetFilters,     this.width - panelWidthSide - BORDER_WIDTH,           LIST_TOP,                                       panelWidthSide);
        addButton("Recalculate light", this::recalculateLight, this.width - panelWidthSide - BORDER_WIDTH,           LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT),     panelWidthSide);
        addButton("Apply",             this::apply,            panelWidthSide + panelWidthCenter + BORDER_WIDTH * 3, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, halfButtonWidth);
        addButton("Done",              this::done,             this.width - BORDER_WIDTH - halfButtonWidth,          LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 2, halfButtonWidth);

        changedRenderBlockOutlines = RenderFilterHandler.getRenderBlockOutlines();
        changedRenderBlocks        = RenderFilterHandler.getRenderBlocks();
        changedRenderBlockEntities = RenderFilterHandler.getRenderBlockEntities();
        changedRenderFluids        = RenderFilterHandler.getRenderFluids();
        addButton(getToggleText_renderBlockOutlines(changedRenderBlockOutlines), this::toggleRenderBlockOutlines, this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 4, panelWidthSide);
        addButton(getToggleText_renderBlocks       (changedRenderBlocks),        this::toggleRenderBlocks,        this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 5, panelWidthSide);
        addButton(getToggleText_renderBlockEntities(changedRenderBlockEntities), this::toggleRenderBlockEntities, this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 6, panelWidthSide);
        addButton(getToggleText_renderFluids       (changedRenderFluids),        this::toggleRenderFluids,        this.width - panelWidthSide - BORDER_WIDTH, LIST_TOP + (BUTTON_HEIGHT + BORDER_HEIGHT) * 7, panelWidthSide);




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
            "Loaded blocks: ", String.format("%,d", (loadedChunkNum * level.getHeight() * 16 * 16))
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




    @Override
    public void extractBlurredBackground(final GuiGraphicsExtractor graphics) {
        //! No blurred background
    }



    @Override
    public void onClose() {
        this.minecraft.setScreen(null); // Close screen and go back to game
    }




    public void apply(final Button b) {
        if(!applied) {
            blockList.flushChanges();
            RenderFilterHandler.setRenderBlockOutlines(changedRenderBlockOutlines);
            RenderFilterHandler.setRenderBlocks       (changedRenderBlocks);
            RenderFilterHandler.setRenderBlockEntities(changedRenderBlockEntities);
            RenderFilterHandler.setRenderFluids       (changedRenderFluids);

            RenderFilterHandler.recalculate();
            RenderFilterHandler.refreshRendering();

            applied = true;
        }
    }




    public void done(final Button b) {
        apply(b);
        onClose();
    }

    public void resetFilters(final Button b) {
        markChanged();
        RenderFilterHandler.init(
            RenderFilterHandler.getTargetHiddenBlocks(),
            RenderFilterHandler.getRenderBlockOutlines(),
            RenderFilterHandler.getRenderBlocks(),
            RenderFilterHandler.getRenderBlockEntities(),
            RenderFilterHandler.getRenderFluids()
        );
        blockList.filter(searchField.getValue());
        apply(b);
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
        return "Render block outlines: " + (state ? "YES" : "NO");
    }
    public void toggleRenderBlockOutlines(final Button b) {
        boolean newState = !changedRenderBlockOutlines;
        changedRenderBlockOutlines = newState;
        markChanged(); //! Flushed on application
        b.setMessage(Component.literal(getToggleText_renderBlockOutlines(newState)));
    }


    public String getToggleText_renderBlocks(final boolean state) {
        return "Render blocks: " + (state ? "YES" : "NO");
    }
    public void toggleRenderBlocks(final Button b) {
        boolean newState = !changedRenderBlocks;
        changedRenderBlocks = newState;
        markChanged(); //! Flushed on application
        b.setMessage(Component.literal(getToggleText_renderBlocks(newState)));
    }


    public String getToggleText_renderBlockEntities(final boolean state) {
        return "Render block entities: " + (state ? "YES" : "NO");
    }
    public void toggleRenderBlockEntities(final Button b) {
        boolean newState = !changedRenderBlockEntities;
        changedRenderBlockEntities = newState;
        markChanged(); //! Flushed on application
        b.setMessage(Component.literal(getToggleText_renderBlockEntities(newState)));
    }


    public String getToggleText_renderFluids(final boolean state) {
        return "Render fluids: " + (state ? "YES" : "NO");
    }
    public void toggleRenderFluids(final Button b) {
        boolean newState = !changedRenderFluids;
        changedRenderFluids = newState;
        markChanged(); //! Flushed on application
        b.setMessage(Component.literal(getToggleText_renderFluids(newState)));
    }
}




//TODO add presets to the left
//TODO save and load buttons on the right of the preset name (which is editable)
//TODO storage them in the config folder of the client