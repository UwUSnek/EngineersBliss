package com.snek.engineersbliss.client.screens.rendering.widgets;

import com.snek.engineersbliss.client.screens.rendering.BlockRenderer;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.level.block.Block;




public class BlockTooltipComponent implements ClientTooltipComponent {
    private final Block block;

    BlockTooltipComponent(final Block block) {
        this.block = block;
    }


    @Override
    public int getHeight(final Font font) { return 20; }

    @Override
    public int getWidth(final Font font) { return 20; }

    @Override
    public void extractImage(final Font font, final int x, final int y, final int width, final int height, final GuiGraphicsExtractor graphics) {
        BlockRenderer.renderBlockIcon(graphics, block, x, y);
        BlockRenderer.renderBlockName(graphics, block, x + 20 + 4, y + 6, 0xFFFFFFFF);
    }
}