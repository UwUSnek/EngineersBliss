package com.snek.engineersbliss.client.screens.rendering;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;




public class BlockRenderer {
    public static final Identifier MISSING_ITEM_SPRITE = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/gui/missing_item_sprite.png");
    public static final int DEFAULT_ITEM_SPRITE_SIZE = 16;

    private BlockRenderer() {}



    /**
     * Renders the icon of the specified block on the provided Graphics.
     * The default size is 16px.
     * @param graphics The output Graphics
     * @param block The block to render
     * @param x The X position
     * @param y The Y position
     */
    public static void renderBlockIcon(GuiGraphicsExtractor graphics, Block block, int x, int y) {
        renderBlockIcon(graphics, block, x, y, DEFAULT_ITEM_SPRITE_SIZE);
    }




    /**
     * Renders the icon of the specified block on the provided Graphics.
     * The default size is 16px.
     * @param graphics The output Graphics
     * @param block The block to render
     * @param x The X position
     * @param y The Y position
     * @param size The size of the icon
     */
    public static void renderBlockIcon(GuiGraphicsExtractor graphics, Block block, int x, int y, int size) {

        // Set up pose
        float scale = size / DEFAULT_ITEM_SPRITE_SIZE;
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scale, scale);

        // Load sprite
        //! Blocks with no item form return AIR from .asItem()
        if(block.asItem() == Items.AIR) graphics.blit(MISSING_ITEM_SPRITE, 0, 0, DEFAULT_ITEM_SPRITE_SIZE, DEFAULT_ITEM_SPRITE_SIZE, 0f, 1f, 0f, 1f);
        else graphics.item(new ItemStack(block), 0, 0);

        // Pop pose
        graphics.pose().popMatrix();
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
