package com.snek.engineersbliss.client.screens.rendering.widgets;

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
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.screens.Layout;
import com.snek.engineersbliss.client.screens.rendering.BlockRenderer;
import com.snek.engineersbliss.client.screens.rendering.RenderingScreen;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.fabricmc.fabric.mixin.client.gametest.ClientChunkCacheAccessor;
import net.fabricmc.fabric.mixin.client.gametest.ClientChunkCacheStorageAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;




public class BlockListWidget extends AbstractSelectionList<BlockListWidget.Entry> {
    final int rowItemHeight;
    private final List<Block> allBlocks;    // All blocks in the game, vanilla order
    private final List<Block> loadedBlocks; // Blocks in loaded chunks, vanilla order (manual). Reset when the UI is closed
    private final RenderingScreen screen;


    public BlockListWidget(final Minecraft client, final RenderingScreen screen, final int width, final int height, final int top, final int itemHeight) {
        super(client, width, height, top, itemHeight);
        this.screen = screen;
        this.rowItemHeight = itemHeight;

        // Create list of all blocks
        allBlocks = new ArrayList<>();
        BuiltInRegistries.BLOCK.forEach(block -> {
            if(block != Blocks.AIR && block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR) {
                allBlocks.add(block);
            }
        });

        // Loaded blocks are only calculated when needed
        loadedBlocks = new ArrayList<>();
    }




    //! Required for correct scrollbar hitbox position
    //! overriding getRowWidth breaks the default scrollBarX
    @Override
    protected int scrollBarX() {
        return this.getX() + this.width - this.scrollbarWidth() - 2;
    }

	public void clear() {
		this.clearEntries();
        setScrollAmount(0);
	}


    // Public access for getHovered()
    public Entry getHoveredEntry() {
        return super.getHovered();
    }




    public void filter(final String query) {

        // Remove spaces near operators and prefixes
        final String cleanQuery = query.replaceAll("\\s*([&|#@])\\s*", "$1");


        // Iterate over or groups first, so or operators naturally end up with lower priority
        final List<Block> orResults = new ArrayList<>();
        for(final String orGroup : cleanQuery.split("\\|")) {

            // Iterate over inner and groups, then add the results to the or results
            final List<Block> andResults = new ArrayList<>(allBlocks);
            for(final String andGroup : orGroup.split("&")) {
                andResults.removeIf(block -> {

                    // Filter tags
                    if(andGroup.startsWith("#")) {
                        final String tagQuery = andGroup.substring(1).toLowerCase();
                        return !block.builtInRegistryHolder().tags().anyMatch(tag -> tag.location().toString().toLowerCase().contains(tagQuery));
                    }

                    // Filter loaded blocks and recalculate list if needed
                    if(andGroup.startsWith("@")) {
                        if(loadedBlocks.isEmpty()) {
                            loadedBlocks.addAll(MinecraftUtils.calcLoadedBlockList());
                        }
                        return !loadedBlocks.contains(block);
                    }

                    // Filter name and ID
                    else {
                        final String nameIdQuery = andGroup.toLowerCase();
                        final String name = block.getName().getString().toLowerCase();
                        final String id = BuiltInRegistries.BLOCK.getKey(block).toString().toLowerCase();
                        return !name.contains(nameIdQuery) && !id.contains(nameIdQuery);
                    }
                });
            }
            orResults.addAll(andResults);
        }

        //TODO add "Rendering: 12 blocks"
        //TODO add "Search result: 5232 blocks"

        //TODO add "searching for:
        //TODO          ID or Name contains "hi"
        //TODO          Any of the tags contains "uwu"

        // Clear block list and load the filtered entries
        clear();
        for(final Block block : orResults) {
            addEntry(new Entry(block, screen));
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


        // Handle hover events and draw tooltips
        final var hoveredEntry = getHovered();
        if(hoveredEntry != null) {
            setSelected(hoveredEntry);
            final Block block = hoveredEntry.block;
            final List<ClientTooltipComponent> tooltipLines = new ArrayList<>();
            tooltipLines.add(0, new BlockTooltipComponent(block));
            tooltipLines.add(ClientTooltipComponent.create(Component.literal(BuiltInRegistries.BLOCK.getKey(block).toString()).withStyle(ChatFormatting.BLUE).getVisualOrderText()));
            block.builtInRegistryHolder().tags().forEach(tag ->
                tooltipLines.add(ClientTooltipComponent.create(Component.literal("#" + tag.location()).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText()))
            );
            graphics.tooltip(minecraft.font, tooltipLines, mouseX, mouseY + 4, DefaultTooltipPositioner.INSTANCE, null);
        }
    }




    @Override
    public int getRowWidth() {
        return this.width - Layout.BORDER_WIDTH * 2;
    }








    public class Entry extends AbstractSelectionList.Entry<BlockListWidget.Entry> {
        private final Block block;
        private final Checkbox enableBox;
        private final Checkbox isolateBox;
        private final RenderingScreen screen;
        public Block getBlock() { return block; }


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


            // Block icon and name
            BlockRenderer.renderBlockIcon(graphics, block, this.getContentX(), midY - 8);
            BlockRenderer.renderBlockName(graphics, block, this.getContentX() + 20, midY - 4, 0xFFFFFFFF);


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
                RenderFilterHandler.setEnabled(block, enableBox.selected());
                RenderFilterHandler.recalculate();
                MinecraftUtils.refreshSectionsContaining(block);
                return true;
            }
            if(isolateBox.mouseClicked(event, doubleClick)) {
                RenderFilterHandler.setIsolated(block, isolateBox.selected());
                RenderFilterHandler.recalculate();
                MinecraftUtils.refreshSectionsContaining(block);
                return true;
            }
            return super.mouseClicked(event, doubleClick);
        }
    }
}

//TODO add a grid that shows each item in a selection and in which container it was found
//TODO each cell has a teleport option
//TODO different stacks appear in different rows, hovering shows the item as if in a UI




//FIXME ------------ specify the type of block and keep both of them in the list, so filtering logic doesn't break

//FIXME Fix heads and skulls appearing twice in the list (wall/floor variants)
//FIXME Fix signs appearing twice in the list (wall/floor variants)
//FIXME Fix hanging signs appearing twice in the list (wall/floor variants)
