package com.snek.engineersbliss.client.screens.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.lighting.LevelLightEngine;

import java.util.ArrayList;
import java.util.List;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.resources.Identifier;




final class BlockListWidget extends AbstractSelectionList<BlockListWidget.Entry> {
    final int rowItemHeight;
    private List<Block> allBlocks;

    BlockListWidget(Minecraft client, int width, int height, int top, int itemHeight) {
        super(client, width, height, top, itemHeight);
        this.rowItemHeight = itemHeight;

        // Create list of all blocks
        allBlocks = new ArrayList<>();
        BuiltInRegistries.BLOCK.forEach(block -> {
            if(block != Blocks.AIR) {
                allBlocks.add(block);
            }
        });
    }




    //! Required for correct scrollbar hitbox position
    //! overriding getRowWidth breaks the default scrollBarX
    @Override
    protected int scrollBarX() {
        return this.getX() + this.width - this.scrollbarWidth() - 2;
    }

	void clear() {
		this.clearEntries();
        setScrollAmount(0);
	}

    void filter(String query) {

        //TODO select tags #
        //TODO find ids
        //TODO && and
        //TODO || or
        //FIXME remove all spaces before parsing

        //TODO add "Rendering: 12 blocks"
        //TODO add "Search result: 5232 blocks"

        //TODO add "searching for:
        //TODO          ID or Name contains "hi"
        //TODO          Any of the tags contains "uwu"
        clear();
        for(Block block : allBlocks) {
            String name = block.getName().getString().toLowerCase();
            if(query.isEmpty() || name.contains(query)) {
                this.addEntry(new Entry(block));
            }
        }
    }


    @Override
    public int addEntry(Entry entry) {
        return super.addEntry(entry);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput arg) {
        // TODO seems to be possibly accessibility related
    }

    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);

        // draw header above list
        int headerY = this.getY() - 12;
        int rowLeft = this.getRowLeft();
        int rowWidth = this.getRowWidth();
        graphics.text(minecraft.font, Component.literal("Block"),   rowLeft, headerY, 0xFFAAAAAA);
        graphics.text(minecraft.font, Component.literal("Enable"),  rowLeft + rowWidth - 80, headerY, 0xFFAAAAAA);
        graphics.text(minecraft.font, Component.literal("Isolate"), rowLeft + rowWidth - 40, headerY, 0xFFAAAAAA);


        if (getHovered() != null) {
           setSelected(getHovered());
        }
    }

    @Override
    public int getRowWidth() {
        return this.width - RenderingScreen.BORDER_WIDTH * 2;
    }








    class Entry extends AbstractSelectionList.Entry<BlockListWidget.Entry> {
        private final Block block;
        private final Checkbox enableBox;
        private final Checkbox isolateBox;

        public Entry(Block block) {
            this.block = block;
            this.enableBox  = Checkbox.builder(Component.empty(), BlockListWidget.this.minecraft.font).pos(0, 0).selected(RenderFilterHandler.getEnabled(block)).build();
            this.isolateBox = Checkbox.builder(Component.empty(), BlockListWidget.this.minecraft.font).pos(0, 0).selected(RenderFilterHandler.getIsolated(block)).build();
        }


        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int rowWidth = BlockListWidget.this.getRowWidth();
            int midY = this.getY() + BlockListWidget.this.rowItemHeight / 2;


            // Block icon
            if(block.asItem() == Items.AIR) {
                //TODO add "?" image for missing item forms
            }
            else {
                ItemStack stack = new ItemStack(block);
                graphics.item(stack, this.getContentX(), midY - 8);
            }


            // Block name
            graphics.text(
                BlockListWidget.this.minecraft.font,
                block.getName(),
                this.getContentX() + 20,
                midY - 4,
                0xFFFFFFFF
            );


            // Checkboxes
            int checkboxY = this.getY() + (BlockListWidget.this.rowItemHeight - 20) / 2;

            enableBox.setX(this.getX() + rowWidth - 80);
            enableBox.setY(checkboxY);
            enableBox.extractRenderState(graphics, mouseX, mouseY, tickDelta);

            isolateBox.setX(this.getX() + rowWidth - 40);
            isolateBox.setY(checkboxY);
            isolateBox.extractRenderState(graphics, mouseX, mouseY, tickDelta);
        }


        @Override
        public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
            if(enableBox.mouseClicked(event, doubleClick)) {
                RenderFilterHandler.setEnabled(block, enableBox.selected());
                //FIXME call from confirm button
                RenderFilterHandler.recalculate();
                minecraft.levelRenderer.allChanged();
                BlockModelLighter.clearCache();

                return true;
            }
            if(isolateBox.mouseClicked(event, doubleClick)) {
                RenderFilterHandler.setIsolated(block, enableBox.selected());
                //FIXME call from confirm button
                RenderFilterHandler.recalculate();
                minecraft.levelRenderer.allChanged();
                BlockModelLighter.clearCache();

                return true;
            }
            return super.mouseClicked(event, doubleClick);
        }
    }
}