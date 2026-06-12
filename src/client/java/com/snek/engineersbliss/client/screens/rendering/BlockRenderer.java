package com.snek.engineersbliss.client.screens.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;




public class BlockRenderer {
    private BlockRenderer() {}




    /**
     * Renders the icon of the specified block on the provided Graphics.
     * @param graphics The output Graphics
     * @param block The block to render
     * @param x The X position
     * @param y The Y position
     */
    public static void renderBlockIcon(GuiGraphicsExtractor graphics, Block block, int x, int y) {
        if(block.asItem() == Items.AIR) {
            //TODO add "?" image for missing item forms
        }
        else {
            graphics.item(new ItemStack(block), x, y);
        }
    }




    /**
     * Renders the name of the specified block on the provided Graphics.
     * @param graphics The output Graphics
     * @param block The block to render the name of
     * @param x The X position
     * @param y The Y position
     */
    public static void renderBlockName(GuiGraphicsExtractor graphics, Block block, int x, int y, int color) {
        graphics.text(Minecraft.getInstance().font, block.getName().getVisualOrderText(), x, y, color);
    }
}
