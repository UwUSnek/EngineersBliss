package com.snek.engineersbliss.client.screens.rendering.widgets;

import com.snek.engineersbliss.client.screens.rendering.BlockRenderer;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.level.block.Block;




public class BlockTooltipComponent implements ClientTooltipComponent {
    public static final int ICON_SIZE = 32;
    public static final int TEXT_SIZE = 24;
    private final Block block;

    BlockTooltipComponent(final Block block) {
        this.block = block;
    }


    @Override
    public int getHeight(final Font font) { return ICON_SIZE + 4; } //TODO should be float?

    @Override
    public int getWidth(final Font font) { //TODO should be float?
        final int textWidth = (int)(font.width(block.getName()) * ((float)TEXT_SIZE / BlockRenderer.DEFAULT_ITEM_SPRITE_SIZE));
        return ICON_SIZE + 8 + textWidth + 4;
    }

    @Override
    public void extractImage(final Font font, final int x, final int y, final int width, final int height, final GuiGraphicsExtractor graphics) {
        // BlockRenderer.extractBlockSpriteSheet(graphics, block, x, y, ICON_SIZE);
        // BlockRenderer.extractBlockName(graphics, block, x + ICON_SIZE + 8, y + ICON_SIZE - TEXT_SIZE, 0xFFFFFFFF, TEXT_SIZE);
        //FIXME this needs a UiGraphics but using UiGraphics doesnt allow extending ClientTooltipComponent
        //FIXME USE A CUSTOM TOOLTIP ITEM
    }
}