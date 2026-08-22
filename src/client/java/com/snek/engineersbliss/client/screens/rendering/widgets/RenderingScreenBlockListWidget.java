// package com.snek.engineersbliss.client.screens.rendering.widgets;

// import net.minecraft.client.Minecraft;
// import net.minecraft.client.gui.Font;
// import net.minecraft.client.gui.GuiGraphicsExtractor;
// import net.minecraft.client.gui.components.AbstractSelectionList;
// import net.minecraft.client.gui.components.Checkbox;
// import net.minecraft.core.registries.BuiltInRegistries;
// import net.minecraft.world.level.block.Block;
// import net.minecraft.world.level.block.Blocks;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.regex.Pattern;

// import org.jetbrains.annotations.NotNull;

// import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
// import com.snek.engineersbliss.client.utils.UiTxt;
// import com.snek.engineersbliss.client.screens.rendering.BlockRenderer;
// import com.snek.engineersbliss.client.ui.font.Fonts;
// import com.snek.engineersbliss.client.utils.Layout;
// import com.snek.engineersbliss.client.utils.MinecraftUtils;

// import net.minecraft.client.gui.narration.NarrationElementOutput;
// import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
// import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
// import net.minecraft.client.input.MouseButtonEvent;




// public class RenderingScreenBlockListWidget extends AbstractSelectionList<RenderingScreenBlockListWidget.Entry> {
//     public static final int CHECKBOX_AREA_WIDTH = 40;

//     final int rowItemHeight;
//     private final List<Block> allBlocks;    // All blocks in the game, vanilla order
//     private final List<Block> loadedBlocks; // Blocks in loaded chunks, vanilla order (manual). Reset when the UI is closed


//     public RenderingScreenBlockListWidget(final Minecraft client, final int width, final int height, final int top, final int itemHeight) {
//         super(client, width, height, top, itemHeight);
//         this.rowItemHeight = itemHeight;

//         // Create list of all blocks
//         allBlocks = new ArrayList<>();
//         BuiltInRegistries.BLOCK.forEach(block -> {
//             if(block != Blocks.AIR && block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR) {
//                 allBlocks.add(block);
//             }
//         });

//         // Loaded blocks are only calculated when needed
//         loadedBlocks = new ArrayList<>();
//     }




//     //! Required for correct scrollbar hitbox position
//     //! overriding getRowWidth breaks the default scrollBarX
//     @Override
//     protected int scrollBarX() {
//         return this.getX() + this.width - this.scrollbarWidth() - 2;
//     }

// 	public void clear() {
// 		this.clearEntries();
//         setScrollAmount(0);
// 	}


//     // Public access for getHovered()
//     public Entry getHoveredEntry() {
//         return super.getHovered();
//     }



//     private static final Pattern CLEAN_PATTERN = Pattern.compile("\\s*([&|#@])\\s*");
//     public void filter(final String query) {

//         // Remove spaces near operators and prefixes
//         final String cleanQuery = CLEAN_PATTERN.matcher(query).replaceAll("$1");


//         // Iterate over or groups first, so or operators naturally end up with lower priority
//         final List<Block> orResults = new ArrayList<>();
//         for(final String orGroup : cleanQuery.split("\\|")) {

//             // Iterate over inner and groups, then add the results to the or results
//             final List<Block> andResults = new ArrayList<>(allBlocks);
//             for(final String andGroup : orGroup.split("&")) {
//                 andResults.removeIf(block -> {

//                     // Filter tags
//                     if(andGroup.startsWith("#")) {
//                         final String tagQuery = andGroup.substring(1).toLowerCase();
//                         return BuiltInRegistries.BLOCK.wrapAsHolder(block).tags().noneMatch(tag -> tag.location().toString().toLowerCase().contains(tagQuery));
//                     }

//                     // Filter loaded blocks and recalculate list if needed
//                     if(andGroup.startsWith("@")) {
//                         if(loadedBlocks.isEmpty()) {
//                             loadedBlocks.addAll(MinecraftUtils.calcLoadedBlockList());
//                         }
//                         return !loadedBlocks.contains(block);
//                     }

//                     // Filter name and ID
//                     else {
//                         final String nameIdQuery = andGroup.toLowerCase();
//                         final String name = block.getName().getString().toLowerCase();
//                         final String id = BuiltInRegistries.BLOCK.getKey(block).toString().toLowerCase();
//                         return !name.contains(nameIdQuery) && !id.contains(nameIdQuery);
//                     }
//                 });
//             }
//             orResults.addAll(andResults);
//         }

//         //TODO add "Rendering: 12 blocks"
//         //TODO add "Search result: 5232 blocks"

//         //TODO add "searching for:
//         //TODO          ID or Name contains "hi"
//         //TODO          Any of the tags contains "uwu"

//         // Clear block list and load the filtered entries
//         clear();
//         for(final Block block : orResults) {
//             addEntry(new Entry(block));
//         }
//     }




//     @Override
//     public int addEntry(final Entry entry) {
//         return super.addEntry(entry);
//     }

//     @Override
//     public void updateWidgetNarration(final NarrationElementOutput arg) {
//         // TODO seems to be possibly accessibility related
//     }




//     @Override
//     public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
//         super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
//         final @NotNull Font font = Fonts.ui.regular.get(1f).getFont();

//         // draw header above list
//         final int headerY = this.getY() - 12;
//         final int rowLeft = this.getRowLeft();
//         final int rowWidth = this.getRowWidth();
//         graphics.text(font, "Block"  , rowLeft, headerY, 0xFFAAAAAA);
//         graphics.text(font, "Enable" , rowLeft + rowWidth - 80, headerY, 0xFFAAAAAA);
//         graphics.text(font, "Isolate", rowLeft + rowWidth - 40, headerY, 0xFFAAAAAA);


//         // Handle hover events
//         final var hoveredEntry = getHovered();
//         if(hoveredEntry != null) {
//             setSelected(hoveredEntry);

//             // If hovering on the left half of the entry, spawn block info tooltip
//             if(mouseX < hoveredEntry.getX() + getRowWidth() - CHECKBOX_AREA_WIDTH * 2) {
//                 final Block block = hoveredEntry.block;
//                 final List<ClientTooltipComponent> tooltipLines = new ArrayList<>();
//                 tooltipLines.add(0, new BlockTooltipComponent(block));
//                 tooltipLines.add(ClientTooltipComponent.create(new UiTxt(BuiltInRegistries.BLOCK.getKey(block).toString()).lightBlue().get().getVisualOrderText()));
//                 BuiltInRegistries.BLOCK.wrapAsHolder(block).tags().forEach(tag ->
//                     tooltipLines.add(ClientTooltipComponent.create(new UiTxt("#" + tag.location()).gray().get().getVisualOrderText()))
//                 );
//                 graphics.tooltip(font, tooltipLines, mouseX, mouseY + 4, DefaultTooltipPositioner.INSTANCE, null);
//             }
//         }
//     }




//     @Override
//     public int getRowWidth() {
//         return this.width - Layout.BORDER_WIDTH * 2;
//     }








//     public class Entry extends AbstractSelectionList.Entry<RenderingScreenBlockListWidget.Entry> {
//         private final Block block;
//         private final Checkbox enableBox;
//         private final Checkbox isolateBox;
//         public Block getBlock() { return block; }


//         public Entry(final Block block) {
//             final @NotNull Font font = Fonts.ui.regular.get(1f).getFont();
//             this.block = block;
//             this.enableBox  = Checkbox.builder(new UiTxt().get(), font).pos(0, 0).selected(RenderingFilterHandler.getEnabled(block)).build();
//             this.isolateBox = Checkbox.builder(new UiTxt().get(), font).pos(0, 0).selected(RenderingFilterHandler.getIsolated(block)).build();
//         }


//         @Override
//         public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float tickDelta) {
//             final int rowWidth = RenderingScreenBlockListWidget.this.getRowWidth();
//             final int midY = this.getY() + RenderingScreenBlockListWidget.this.rowItemHeight / 2;


//             // Block icon and name
//             BlockRenderer.extractBlockIcon(graphics, block, this.getContentX(), midY - 8);
//             BlockRenderer.extractBlockName(graphics, block, this.getContentX() + 20, midY - 4, 0xFFFFFFFF);


//             // Checkboxes
//             final int checkboxY = this.getY() + (RenderingScreenBlockListWidget.this.rowItemHeight - 20) / 2;

//             enableBox.setX(this.getX() + rowWidth - CHECKBOX_AREA_WIDTH * 2 + (CHECKBOX_AREA_WIDTH - enableBox.getWidth()) / 2);
//             enableBox.setY(checkboxY);
//             enableBox.extractRenderState(graphics, mouseX, mouseY, tickDelta);

//             isolateBox.setX(this.getX() + rowWidth - CHECKBOX_AREA_WIDTH + (CHECKBOX_AREA_WIDTH - isolateBox.getWidth()) / 2);
//             isolateBox.setY(checkboxY);
//             isolateBox.extractRenderState(graphics, mouseX, mouseY, tickDelta);
//         }


//         @Override
//         public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
//             if(enableBox.mouseClicked(event, doubleClick)) {
//                 RenderingFilterHandler.resetStateCache();
//                 RenderingFilterHandler.setEnabled(block, enableBox.selected());
//                 MinecraftUtils.refreshSectionsContaining(block);
//                 return true;
//             }
//             if(isolateBox.mouseClicked(event, doubleClick)) {
//                 RenderingFilterHandler.resetStateCache();
//                 RenderingFilterHandler.setIsolated(block, isolateBox.selected());
//                 MinecraftUtils.refreshRendering();
//                 return true;
//             }
//             return super.mouseClicked(event, doubleClick);
//         }
//     }
// }

// //TODO add a grid that shows each item in a selection and in which container it was found
// //TODO each cell has a teleport option
// //TODO different stacks appear in different rows, hovering shows the item as if in a UI




// //FIXME ------------ specify the type of block and keep both of them in the list, so filtering logic doesn't break

// //FIXME Fix heads and skulls appearing twice in the list (wall/floor variants)
// //FIXME Fix signs appearing twice in the list (wall/floor variants)
// //FIXME Fix hanging signs appearing twice in the list (wall/floor variants)

















package com.snek.engineersbliss.client.screens.rendering.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.screens.rendering.BlockRenderer;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.containers.UiWidgetList;
import com.snek.engineersbliss.client.ui.widgets.base.UiWidgetBase;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;




public class RenderingScreenBlockListWidget extends UiWidgetList {
    public static final int CHECKBOX_AREA_WIDTH = 40;

    private final List<Block> allBlocks;    // All blocks in the game, vanilla order
    private final List<Block> loadedBlocks; // Blocks in loaded chunks, vanilla order (manual). Reset when the UI is closed


    public RenderingScreenBlockListWidget(final Screen screen, final int itemHeight) {
        super(screen, itemHeight);

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




	public void clear() {
		this.clearEntries();
        setScrollAmount(0);
	}


    @Override
    public Entry getHoveredEntry() {
        return (Entry)super.getHovered();
    }



    private static final Pattern CLEAN_PATTERN = Pattern.compile("\\s*([&|#@])\\s*");
    public void filter(final String query) {

        // Remove spaces near operators and prefixes
        final String cleanQuery = CLEAN_PATTERN.matcher(query).replaceAll("$1");


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
                        return BuiltInRegistries.BLOCK.wrapAsHolder(block).tags().noneMatch(tag -> tag.location().toString().toLowerCase().contains(tagQuery));
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
            addEntry(new Entry(block));
        }
    }




    public int addEntry(final Entry entry) {
        return super.addEntry(entry);
    }




    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        final @NotNull Font font = Fonts.ui.regular.get(1f).getFont();

        // draw header above list
        final int headerY = this.getY() - 12;
        final int rowLeft = this.getRowLeft();
        final int rowWidth = this.getRowWidth();
        graphics.text(font, "Block"  , rowLeft, headerY, 0xFFAAAAAA);
        graphics.text(font, "Enable" , rowLeft + rowWidth - 80, headerY, 0xFFAAAAAA);
        graphics.text(font, "Isolate", rowLeft + rowWidth - 40, headerY, 0xFFAAAAAA);


        // Handle hover events
        final Entry hoveredEntry = getHoveredEntry();
        if(hoveredEntry != null) {
            setSelected(hoveredEntry);

            // If hovering on the left half of the entry, spawn block info tooltip
            if(mouseX < hoveredEntry.getX() + getRowWidth() - CHECKBOX_AREA_WIDTH * 2) {
                final Block block = hoveredEntry.block;
                final List<ClientTooltipComponent> tooltipLines = new ArrayList<>();
                tooltipLines.add(0, new BlockTooltipComponent(block));
                tooltipLines.add(ClientTooltipComponent.create(new UiTxt(BuiltInRegistries.BLOCK.getKey(block).toString()).lightBlue().get().getVisualOrderText()));
                BuiltInRegistries.BLOCK.wrapAsHolder(block).tags().forEach(tag ->
                    tooltipLines.add(ClientTooltipComponent.create(new UiTxt("#" + tag.location()).gray().get().getVisualOrderText()))
                );
                graphics.tooltip(font, tooltipLines, mouseX, mouseY + 4, DefaultTooltipPositioner.INSTANCE, null);
            }
        }
    }

    //! UiWidgetList forces this to false.
    @Override
    protected boolean entriesCanBeSelected() {
        return true;
    }








    public class Entry extends UiWidgetList.Entry implements UiWidgetBase {
        private final Block block;
        private final Checkbox enableBox;
        private final Checkbox isolateBox;
        public Block getBlock() { return block; }


        private final List<Object> children;
        @Override
        public @Nullable List<?> children() {
            return children;
        }
        @Override
        public Screen getScreen() {
            return RenderingScreenBlockListWidget.this.getScreen();
        }




        public Entry(final Block block) {
            super();
            final @NotNull Font font = Fonts.ui.regular.get(1f).getFont();
            this.block = block;
            children = new ArrayList<>();
            children.add(this.enableBox  = Checkbox.builder(new UiTxt().get(), font).selected(RenderingFilterHandler.getEnabled(block)).build()); //FIXME replace with UiCheckbox
            children.add(this.isolateBox = Checkbox.builder(new UiTxt().get(), font).selected(RenderingFilterHandler.getIsolated(block)).build()); //FIXME replace with UiCheckbox
        }


        @Override
        public void layoutWidgets() {
            final int rowWidth = RenderingScreenBlockListWidget.this.getRowWidth();
            final int checkboxY = this.getY() + (this.getHeight() - 20) / 2;
            enableBox.setX(this.getX() + rowWidth - CHECKBOX_AREA_WIDTH * 2 + (CHECKBOX_AREA_WIDTH - enableBox.getWidth()) / 2);
            enableBox.setY(checkboxY);
            isolateBox.setX(this.getX() + rowWidth - CHECKBOX_AREA_WIDTH + (CHECKBOX_AREA_WIDTH - isolateBox.getWidth()) / 2);
            isolateBox.setY(checkboxY);
            UiWidgetBase.super.layoutWidgets();
        }


        @Override
        public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float tickDelta) {
            final int midY = this.getY() + this.getHeight() / 2;

            // Block icon and name
            BlockRenderer.extractBlockIcon(graphics, block, this.getContentX(), midY - 8);
            BlockRenderer.extractBlockName(graphics, block, this.getContentX() + 20, midY - 4, 0xFFFFFFFF);

            // Checkboxes
            enableBox. extractRenderState(graphics, mouseX, mouseY, tickDelta);
            isolateBox.extractRenderState(graphics, mouseX, mouseY, tickDelta);
        }




        @Override
        public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
            if(enableBox.mouseClicked(event, doubleClick)) {
                RenderingFilterHandler.resetStateCache();
                RenderingFilterHandler.setEnabled(block, enableBox.selected());
                MinecraftUtils.refreshSectionsContaining(block);
                return true;
            }
            if(isolateBox.mouseClicked(event, doubleClick)) {
                RenderingFilterHandler.resetStateCache();
                RenderingFilterHandler.setIsolated(block, isolateBox.selected());
                MinecraftUtils.refreshRendering();
                return true;
            }
            return super.mouseClicked(event, doubleClick);
        }
    }
}