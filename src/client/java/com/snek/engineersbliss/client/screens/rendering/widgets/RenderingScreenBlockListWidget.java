package com.snek.engineersbliss.client.screens.rendering.widgets;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.ServerMinecraftUtils;
import com.snek.engineersbliss.client.ui.UiGraphics;
import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.containers.UiWidgetList;
import com.snek.engineersbliss.client.utils.MinecraftUtils;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;








public class RenderingScreenBlockListWidget extends UiWidgetList {
    public static final int CHECKBOX_AREA_WIDTH = 40;
    public static final float LIST_MARGIN = 0.2f;

    private final List<Block> allBlocks;    // All blocks in the game, vanilla order
    private final List<Block> loadedBlocks; // Blocks in loaded chunks, vanilla order (manual)


    public RenderingScreenBlockListWidget(final Screen screen, final int itemHeight) {
        super(screen, itemHeight, LIST_MARGIN);

        // Create list of all blocks
        allBlocks = ServerMinecraftUtils.fetchAllBlocks();

        // Loaded blocks are only calculated when needed
        loadedBlocks = new ArrayList<>();
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
        clearEntries();
        disableRelayout();
        for(final Block block : orResults) {
            addWidget(new BlockEntryContents(this, block));
        }
        enableRelayout();
        relayout();
    }




    @Override
    public void extractWidgetRenderState(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        final FontFamily fontFamily = Fonts.ui.regular;

        // draw header above list
        final int headerY = getY() - 12;
        final int rowLeft = (int)getRowLeft();
        final int rowWidth = (int)getRowWidth();
        graphics.extractTxt(new UiTxt("Block",   fontFamily), rowLeft,                 headerY, 0xFFAAAAAA);
        graphics.extractTxt(new UiTxt("Enable",  fontFamily), rowLeft + rowWidth - 80, headerY, 0xFFAAAAAA);
        graphics.extractTxt(new UiTxt("Isolate", fontFamily), rowLeft + rowWidth - 40, headerY, 0xFFAAAAAA);


        // Handle hover events
        final GuiEventListener hovered = ((__base_UiScreen)getScreen()).getHoveredOrDraggedElm(); //FIXME remove blind cast
        if(hovered != null && hovered instanceof BlockEntryContents contents) {

            // If hovering on the left half of the entry, spawn block info tooltip //! Checkboxes are on the right half.
            if(mouseX < contents.getWidthCenter()) {
                final Block block = contents.getBlock();
                final List<ClientTooltipComponent> tooltipLines = new ArrayList<>();
                tooltipLines.add(0, new BlockTooltipComponent(block));
                tooltipLines.add(ClientTooltipComponent.create(new UiTxt(BuiltInRegistries.BLOCK.getKey(block).toString()).lightBlue().get().getVisualOrderText()));
                BuiltInRegistries.BLOCK.wrapAsHolder(block).tags().forEach(tag ->
                    tooltipLines.add(ClientTooltipComponent.create(new UiTxt("#" + tag.location()).gray().get().getVisualOrderText()))
                );
                // graphics.tooltip(font, tooltipLines, mouseX, mouseY + 4, DefaultTooltipPositioner.INSTANCE, null);
                //FIXME add tooltip rendering to UiGraphics
            }
        }
    }
}