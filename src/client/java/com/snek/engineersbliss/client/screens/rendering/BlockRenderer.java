package com.snek.engineersbliss.client.screens.rendering;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;








public class BlockRenderer {
    private BlockRenderer() {}


    public static final Identifier MISSING_ITEM_SPRITE = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/gui/missing_item_sprite.png");
    public static final int DEFAULT_ITEM_SPRITE_SIZE = 16;

    public static final int SPRITE_FRAME_WIDTH  = 480;
    public static final int SPRITE_FRAME_HEIGHT = 480;
    public static final int SPRITE_FRAME_COUNT  = 72;
    public static final int SPRITE_FPS          = 24;
    public static final int SPRITE_COLS         = 9;
    public static final int SPRITE_ROWS         = 8;
    public static final int SPRITE_SHEET_WIDTH  = SPRITE_FRAME_WIDTH  * SPRITE_COLS;
    public static final int SPRITE_SHEET_HEIGHT = SPRITE_FRAME_HEIGHT * SPRITE_ROWS;




    /**
     * Renders an animated block spritesheet on the provided Graphics.
     * Spritesheets are 480x34560 vertical strips (72 frames × 480px).
     * The default size is 16px.
     * @param graphics  The output Graphics
     * @param block     The block whose spritesheet to render
     * @param x         The X position
     * @param y         The Y position
     * @param size      The rendered size in pixels
     */
    public static void renderBlockSpriteSheet(GuiGraphicsExtractor graphics, Block block, int x, int y, int size) {

        // Find the sprite sheet file
        Identifier id = block.builtInRegistryHolder().key().identifier();
        Identifier texture = Identifier.fromNamespaceAndPath(
            EngineerSBliss.MOD_ID,
            "textures/gui/block_renders/" + id.getPath() + ".webp"
        );

        // Fallback to renderBlockIcon if sprite sheet is absent
        if (Minecraft.getInstance().getResourceManager().getResource(texture).isEmpty()) {
            renderBlockIcon(graphics, block, x, y, size);
            return;
        }

        // If sprite sheet file exists, draw the current frame
        int frame = (int)((System.currentTimeMillis() / (1000L / SPRITE_FPS)) % SPRITE_FRAME_COUNT);
        int col   = frame % SPRITE_COLS;
        int row   = frame / SPRITE_COLS;
        float u0  = (col * SPRITE_FRAME_WIDTH)        / (float) SPRITE_SHEET_WIDTH;
        float u1  = ((col + 1) * SPRITE_FRAME_WIDTH)  / (float) SPRITE_SHEET_WIDTH;
        float v0  = (row * SPRITE_FRAME_HEIGHT)       / (float) SPRITE_SHEET_HEIGHT;
        float v1  = ((row + 1) * SPRITE_FRAME_HEIGHT) / (float) SPRITE_SHEET_HEIGHT;
        graphics.blit(texture, x, y, x + size, y + size, u0, u1, v0, v1);
    }

    /**
     * Renders an animated block spritesheet on the provided Graphics.
     * Spritesheets are 480x34560 vertical strips (72 frames × 480px).
     * The default size is 16px.
     * @param graphics  The output Graphics
     * @param block     The block whose spritesheet to render
     * @param x         The X position
     * @param y         The Y position
     */
    public static void renderBlockSpriteSheet(GuiGraphicsExtractor graphics, Block block, int x, int y) {
        renderBlockSpriteSheet(graphics, block, x, y, DEFAULT_ITEM_SPRITE_SIZE);
    }




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
        float scale = (float)size / DEFAULT_ITEM_SPRITE_SIZE;
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
