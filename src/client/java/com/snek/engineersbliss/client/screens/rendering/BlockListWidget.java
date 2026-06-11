package com.snek.engineersbliss.client.screens.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.lighting.LightEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.client.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.fabricmc.fabric.mixin.client.gametest.ClientChunkCacheAccessor;
import net.fabricmc.fabric.mixin.client.gametest.ClientChunkCacheStorageAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.resources.Identifier;




final class BlockListWidget extends AbstractSelectionList<BlockListWidget.Entry> {
    final int rowItemHeight;
    private final List<Block> allBlocks;
    private final RenderingScreen screen;

    BlockListWidget(final Minecraft client, final RenderingScreen screen, final int width, final int height, final int top, final int itemHeight) {
        super(client, width, height, top, itemHeight);
        this.screen = screen;
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

    void filter(final String query) {

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
        // for(final Block block : allBlocks) {
        for(final Block block : MinecraftUtils.calcLoadedBlockList()) { //FIXME only refresh loaded blocks list after cache expires, like 10s, or link cache lifetime to UI lifetime
            final String name = block.getName().getString().toLowerCase();
            if(query.isEmpty() || name.contains(query)) {
                this.addEntry(new Entry(block, screen));
            }
        }
    }


    @Override
    public int addEntry(final Entry entry) {
        return super.addEntry(entry);
    }

    @Override
    public void updateWidgetNarration(final NarrationElementOutput arg) {
        // TODO seems to be possibly accessibility related
    }

    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);

        // draw header above list
        final int headerY = this.getY() - 12;
        final int rowLeft = this.getRowLeft();
        final int rowWidth = this.getRowWidth();
        graphics.text(minecraft.font, Component.literal("Block"),   rowLeft, headerY, 0xFFAAAAAA);
        graphics.text(minecraft.font, Component.literal("Enable"),  rowLeft + rowWidth - 80, headerY, 0xFFAAAAAA);
        graphics.text(minecraft.font, Component.literal("Isolate"), rowLeft + rowWidth - 40, headerY, 0xFFAAAAAA);


        if(getHovered() != null) {
           setSelected(getHovered());
        }
    }

    @Override
    public int getRowWidth() {
        return this.width - RenderingScreen.BORDER_WIDTH * 2;
    }

    // Flushes changes to persistent render settings
    public void flushChanges() {
        for(Entry e : children()) {
            if(e.isChanged) {
                RenderFilterHandler.setEnabled(e.block, e.enableBox.selected());
                RenderFilterHandler.setIsolated(e.block, e.isolateBox.selected());
            }
        }
    }








    class Entry extends AbstractSelectionList.Entry<BlockListWidget.Entry> {
        private final Block block;
        private final Checkbox enableBox;
        private final Checkbox isolateBox;
        private final RenderingScreen screen;
        private boolean isChanged = false;


        public Entry(final Block block, final RenderingScreen screen) {
            this.block = block;
            this.screen = screen;
            this.enableBox  = Checkbox.builder(Component.empty(), BlockListWidget.this.minecraft.font).pos(0, 0).selected(RenderFilterHandler.getEnabled(block)).build();
            this.isolateBox = Checkbox.builder(Component.empty(), BlockListWidget.this.minecraft.font).pos(0, 0).selected(RenderFilterHandler.getIsolated(block)).build();
        }


        @Override
        public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float tickDelta) {
            final int rowWidth = BlockListWidget.this.getRowWidth();
            final int midY = this.getY() + BlockListWidget.this.rowItemHeight / 2;


            // Block icon
            if(block.asItem() == Items.AIR) {
                //TODO add "?" image for missing item forms
            }
            else {
                final ItemStack stack = new ItemStack(block);
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
            final int checkboxY = this.getY() + (BlockListWidget.this.rowItemHeight - 20) / 2;

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
                isChanged = true;
                screen.markChanged();
                return true;
            }
            if(isolateBox.mouseClicked(event, doubleClick)) {
                isChanged = true;
                screen.markChanged();
                return true;
            }
            return super.mouseClicked(event, doubleClick);
        }
    }
}