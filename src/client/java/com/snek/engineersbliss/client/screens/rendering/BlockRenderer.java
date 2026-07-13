package com.snek.engineersbliss.client.screens.rendering;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.screens.AvifTextureTracker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;








public class BlockRenderer {
    private BlockRenderer() {}


    public static final Identifier MISSING_ITEM_SPRITE = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/gui/missing_item_sprite.png");
    public static final int DEFAULT_ITEM_SPRITE_SIZE = 16;

    private static final int ATLAS_COLS = 8;
    private static final int ATLAS_ROWS = 8;
    private static final int SHEETS_PER_ATLAS = ATLAS_COLS * ATLAS_ROWS;

    public static final int SPRITE_FRAME_WIDTH  = 64;
    public static final int SPRITE_FRAME_HEIGHT = 64;
    public static final int SPRITE_FRAME_COUNT  = 36;
    public static final int SPRITE_FPS          = 12;
    public static final int SPRITE_COLS         = 6;
    public static final int SPRITE_ROWS         = 6;
    public static final int SPRITE_SHEET_WIDTH  = SPRITE_FRAME_WIDTH  * SPRITE_COLS;
    public static final int SPRITE_SHEET_HEIGHT = SPRITE_FRAME_HEIGHT * SPRITE_ROWS;



//FIXME use new avif system
    /**
     * Renders an animated block spritesheet on the provided Graphics.
     * The default size is 16px.
     * @param graphics  The output Graphics
     * @param block     The block whose spritesheet to render
     * @param x         The X position
     * @param y         The Y position
     * @param size      The rendered size in pixels
     */
    public static void renderBlockSpriteSheet(final GuiGraphicsExtractor graphics, final Block block, final int x, final int y, final int size) {

        // Get block index, fallback to default icon if absent
        @NotNull
        final Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        final int blockIdx = BlockSpriteFileNames.getIdList().indexOf(id.getPath());
        if(blockIdx == -1) {
            renderBlockIcon(graphics, block, x, y, size);
            return;
        }

        final int atlasIdx = blockIdx / SHEETS_PER_ATLAS;
        final int localIdx = blockIdx % SHEETS_PER_ATLAS;
        final int sheetCol = localIdx % ATLAS_COLS;
        final int sheetRow = localIdx / ATLAS_COLS;
        final Identifier textureId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/gui/block_renders/atlas_" + atlasIdx + ".avif");


        if(!AvifTextureTracker.isTextureReady(textureId)) {
            graphics.blit(textureId, x, y, x + size, y + size, 0f, 1f, 0f, 1f);
        }
        else {

            // Atlas dimensions in sheets
            final float atlasW = ATLAS_COLS * (float)SPRITE_SHEET_WIDTH;
            final float atlasH = ATLAS_ROWS * (float)SPRITE_SHEET_HEIGHT;

            // Current animation frame
            final int frame = (int)((System.currentTimeMillis() / (1000L / SPRITE_FPS)) % SPRITE_FRAME_COUNT);
            final int frameCol = frame % SPRITE_COLS;
            final int frameRow = frame / SPRITE_COLS;

            // Pixel offsets of this sheet within the atlas
            final float sheetOffsetX = sheetCol * (float)SPRITE_SHEET_WIDTH;
            final float sheetOffsetY = sheetRow * (float)SPRITE_SHEET_HEIGHT;

            final float u0 = (sheetOffsetX +  frameCol      * SPRITE_FRAME_WIDTH)  / atlasW;
            final float u1 = (sheetOffsetX + (frameCol + 1) * SPRITE_FRAME_WIDTH)  / atlasW;
            final float v0 = (sheetOffsetY +  frameRow      * SPRITE_FRAME_HEIGHT) / atlasH;
            final float v1 = (sheetOffsetY + (frameRow + 1) * SPRITE_FRAME_HEIGHT) / atlasH;

            graphics.blit(textureId, x, y, x + size, y + size, u0, u1, v0, v1);
        }
    }

    /**
     * Renders an animated block spritesheet on the provided Graphics.
     * The default size is 16px.
     * @param graphics  The output Graphics
     * @param block     The block whose spritesheet to render
     * @param x         The X position
     * @param y         The Y position
     */
    public static void renderBlockSpriteSheet(final GuiGraphicsExtractor graphics, final Block block, final int x, final int y) {
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
    public static void renderBlockIcon(final GuiGraphicsExtractor graphics, final Block block, final int x, final int y) {
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
    public static void renderBlockIcon(final GuiGraphicsExtractor graphics, final Block block, final int x, final int y, final int size) {

        // Set up pose
        final float scale = (float)size / DEFAULT_ITEM_SPRITE_SIZE;
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
     * Default height is 16px.
     * @param graphics The output Graphics
     * @param block The block to render the name of
     * @param x The X position
     * @param y The Y position
     * @param height The height of each line
     */
    public static void renderBlockName(final GuiGraphicsExtractor graphics, final Block block, final int x, final int y, final int color, final int height) {

        // Set up pose
        final float scale = (float)height / DEFAULT_ITEM_SPRITE_SIZE;
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scale, scale);

        graphics.text(Minecraft.getInstance().font, new UiTxt(block.getName(), scale).get().getVisualOrderText(), 0, 0, color);

        // Pop pose
        graphics.pose().popMatrix();
    }




    /**
     * Renders the name of the specified block on the provided Graphics.
     * Default height is 16px.
     * @param graphics The output Graphics
     * @param block The block to render the name of
     * @param x The X position
     * @param y The Y position
     */
    public static void renderBlockName(final GuiGraphicsExtractor graphics, final Block block, final int x, final int y, final int color) {
        renderBlockName(graphics, block, x, y, color, DEFAULT_ITEM_SPRITE_SIZE);
    }
}
