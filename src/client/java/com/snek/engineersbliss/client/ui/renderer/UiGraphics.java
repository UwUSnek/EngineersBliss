package com.snek.engineersbliss.client.ui.renderer;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.platform.cursor.CursorType;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.screens.rendering.BlockSpriteFileNames;
import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.textures.atlases.TextureAtlasTracker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor.ScissorStack;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;








/**
 * A wrapper for Minecraft's GuiGraphicsExtractor which adds support for UiTxt rendering and antialiased operations.
 */
public class UiGraphics {
    GuiGraphicsExtractor raw;
    UiScreen screen;







    public UiGraphics(final GuiGraphicsExtractor raw, UiScreen screen) {
        this.raw = raw;
        this.screen = screen;
    }

    public void requestCursor(final CursorType cursorType) {
        raw.requestCursor(cursorType);
    }

    private int applyAlpha(final int color, final float alpha) {
        return (color & 0x00FFFFFF) | Math.round((color & 0xFF000000) * alpha);
    }






    // Scissors

    public void enableScissor(final int x0, final int y0, final int x1, final int y1) {
        raw.enableScissor(x0, y0, x1, y1);
    }
    public void disableScissor() {
        raw.disableScissor();
    }
    public boolean containsPointInScissor(final int x, final int y) {
        return raw.containsPointInScissor(x, y);
    }
    public ScissorStack getScissorStack() {
        return raw.scissorStack;
    }




    public void blurBeforeThisStratum() {
        raw.blurBeforeThisStratum();
    }








    // Floating point fills

    public void fill(final float x0, final float y0, final float x1, final float y1, final int col, final float alpha) {
        fill(x0, y0, x1, y1, applyAlpha(col, alpha));
    }
    public void fill(float x0, float y0, float x1, float y1, final int col) {
        if(x0 > x1) { final float tmp = x0; x0 = x1; x1 = tmp; }
        if(y0 > y1) { final float tmp = y0; y0 = y1; y1 = tmp; }
        raw.guiRenderState.addGuiElement(new AaFillRenderState(
            UiRenderPipelines.AA_FILL, TextureSetup.noTexture(), new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1, col, getScissorStack().peek()
        ));
    }








//TODO add float coords support for text
    // Text rendering

    public void text(
        final FormattedCharSequence text,
        final int textWidth,
        final ScaledFont scaledFont,
        final int x, final int y,
        final int color,
        final TextAlignment textAlignment,
        final float elmWidth, //! Only used by alignment CENTER and RIGHT
        final float shiftX, final float shiftY //! Text shift in real screen pixels. This doesn't depend on the text size.
    ) {

        // Retrieve font and text scale
        final float guiScaleMultiplier = scaledFont.isScaleInvariant() ? 1f : screen.getGuiScale();
        final float textScale = scaledFont.getSize() * guiScaleMultiplier;

        // Compute x and y positions
        final int _x = (int)(switch(textAlignment) {
            case LEFT            -> x;
            case CENTER          -> x + (elmWidth - textWidth) / 2f;
            case RIGHT           -> x +  elmWidth - textWidth;
            case CENTER_ANCHORED -> x -             textWidth  / 2f;
        } / textScale);
        final int _y = (int)(y / textScale);

        // Draw scaled text
        raw.pose().pushMatrix();
        raw.pose().translate(shiftX, shiftY);
        raw.pose().scale(textScale);
        raw.text(scaledFont.getFont(), text, _x, _y, color);
        raw.pose().popMatrix();
    }


    public void text(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final float elmWidth, final boolean dropShadow) {
        text(text, x, y, color, textAlignment, elmWidth, dropShadow, 0f, 0f);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final float elmWidth, final boolean dropShadow, final float shiftX, final float shiftY) {
        //! All overloads go through this which calls the true extractTxt.
        //! Using toRawVisualOrder() is required in order to render '§' properly.
        final ScaledFont scaledFont = (text instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont(false);
        text((dropShadow ? text : text.noShadow()).toRawVisualOrder(), text.getWidth(), scaledFont, x, y, color, textAlignment, elmWidth, shiftX, shiftY);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final boolean dropShadow) {
        text(text, x, y, color, dropShadow, 0f, 0f);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final boolean dropShadow, final float shiftX, final float shiftY) {
        text(text, x, y, color, TextAlignment.LEFT, 0, dropShadow, shiftX, shiftY);
    }


    public void text(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final float elmWidth) {
        text(text, x, y, color, textAlignment, elmWidth, 0f, 0f);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final float elmWidth, final float shiftX, final float shiftY) {
        text(text, x, y, color, textAlignment, elmWidth, false, shiftX, shiftY);
    }
    public void text(final UiTxt text, final int x, final int y, final int color) {
        text(text, x, y, color, 0f, 0f);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final float shiftX, final float shiftY) {
        text(text, x, y, color, false, shiftX, shiftY);
    }


    public void text(final UiTxt text, final int x, final int y, final int color, final float alpha, final TextAlignment textAlignment, final float elmWidth, final boolean dropShadow) {
        text(text, x, y, applyAlpha(color, alpha), textAlignment, elmWidth, dropShadow);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final float alpha, final TextAlignment textAlignment, final float elmWidth, final boolean dropShadow, final float shiftX, final float shiftY) {
        text(text, x, y, applyAlpha(color, alpha), textAlignment, elmWidth, dropShadow, shiftX, shiftY);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final float alpha, final boolean dropShadow) {
        text(text, x, y, applyAlpha(color, alpha), dropShadow);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final float alpha, final boolean dropShadow, final float shiftX, final float shiftY) {
        text(text, x, y, applyAlpha(color, alpha), dropShadow, shiftX, shiftY);
    }


    public void text(final UiTxt text, final int x, final int y, final int color, final float alpha, final TextAlignment textAlignment, final float elmWidth) {
        text(text, x, y, applyAlpha(color, alpha), textAlignment, elmWidth);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final float alpha, final TextAlignment textAlignment, final float elmWidth, final float shiftX, final float shiftY) {
        text(text, x, y, applyAlpha(color, alpha), textAlignment, elmWidth, shiftX, shiftY);
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final float alpha) {
        text(text, x, y, applyAlpha(color, alpha));
    }
    public void text(final UiTxt text, final int x, final int y, final int color, final float alpha, final float shiftX, final float shiftY) {
        text(text, x, y, applyAlpha(color, alpha), shiftX, shiftY);
    }








    // Internal blit methods
    private static TextureSetup textureSetupFor(final Identifier location) {
        final @NotNull AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location);

        return TextureSetup.singleTexture(texture.getTextureView(), texture.getSampler());
    }
    private void __internal_blit(final TextureSetup setup, final float x0, final float y0, final float x1, final float y1, final float u0, final float v0, final float u1, final float v1, final float alpha) {
        raw.guiRenderState.addGuiElement(new AaBlitRenderState(
            UiRenderPipelines.AA_BLIT, setup, new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1,
            u0, v0, u1, v1,
            alpha, getScissorStack().peek()
        ));
    }


    // Floating point blit
    public void blit(final Identifier texture, final float x, final float y, final float u, final float v, final float width, final float height, final int textureWidth, final int textureHeight) {
        blit(texture, x, y, u, v, width, height, textureWidth, textureHeight, 1.0f);
    }
    public void blit(final Identifier texture, final float x, final float y, final float u, final float v, final float width, final float height, final int textureWidth, final int textureHeight, final float alpha) {
        __internal_blit(textureSetupFor(texture), x, y, x + width, y + height, u / textureWidth, v / textureHeight, (u + width) / textureWidth, (v + height) / textureHeight, alpha);
    }
    public void blit(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float u0, final float u1, final float v0, final float v1) {
        blit(location, x0, y0, x1, y1, u0, u1, v0, v1, 1.0f);
    }
    public void blit(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float u0, final float u1, final float v0, final float v1, final float alpha) {
        __internal_blit(textureSetupFor(location), x0, y0, x1, y1, u0, v0, u1, v1, alpha);
    }


    // Floating point blitSprite
    public void blitSprite(final Identifier location, final float x, final float y, final float width, final float height) {
        blitSprite(location, x, y, width, height, 1.0f);
    }
    public void blitSprite(final Identifier location, final float x, final float y, final float width, final float height, final float alpha) {
        final TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.GUI).getSprite(location);
        blitSprite(sprite, x, y, width, height, alpha);
    }
    public void blitSprite(final TextureAtlasSprite sprite, final float x, final float y, final float width, final float height) {
        blitSprite(sprite, x, y, width, height, 1.0f);
    }
    public void blitSprite(final TextureAtlasSprite sprite, final float x, final float y, final float width, final float height, final float alpha) {
        __internal_blit(textureSetupFor(sprite.atlasLocation()), x, y, x + width, y + height, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), alpha);
    }








    // Multilines
    public void multiLine(final float x0, final float y0, final float x1, final float y1, float[] xs, float[] ys, float thickness, int color) {
        raw.guiRenderState.addGuiElement(new AaMultilineRenderState(
            UiRenderPipelines.AA_MULTILINE, new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1,
            xs, ys, thickness, color,
            getScissorStack().peek()
        ));
    }
    public void multiLineArea(final float x0, final float y0, final float x1, final float y1, float[] xs, float[] ys, int color) {
        raw.guiRenderState.addGuiElement(new MultilineAreaRenderState(
            UiRenderPipelines.MULTILINE_AREA, new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1,
            xs, ys, color,
            getScissorStack().peek()
        ));
    }







    // Block icons and sprites

    public static final Identifier MISSING_ITEM_SPRITE = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/gui/missing_item_sprite.png");
    public static final int DEFAULT_ITEM_SPRITE_SIZE = 16;

    private static final int ATLAS_COLS = 8;
    private static final int ATLAS_ROWS = 8;
    private static final int SHEETS_PER_ATLAS = ATLAS_COLS * ATLAS_ROWS;
    //TODO this stuff could be moved to the atlas tracker using a suffix _n system but that's kinda complicated
    //TODO and also large sprite sheets are supposed to use that? these are not large sprite sheets but atlases of sprite sheets which is different.
    //TODO different math? probably?


    /**
     * Renders an animated block spritesheet.
     * The default size is 16px.
     * @param block     The block whose spritesheet to render
     * @param x         The X position
     * @param y         The Y position
     * @param size      The rendered size in pixels
     */
    public void blockSpriteSheet(final Block block, final float x, final float y, final float size) {

        // Get block index, fallback to default icon if absent
        final Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        final int blockIdx = BlockSpriteFileNames.getIdList().indexOf(id.getPath());
        if(blockIdx == -1) {
            blockIcon(block, x, y, size);
            return;
        }

        final Identifier textureId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/gui/block_renders/atlas_0.png");
        if(!TextureAtlasTracker.isTextureReady(textureId)) {
            blit(textureId, x, y, x + size, y + size, 0f, 1f, 0f, 1f);
        }
        else {
            final float[] uv = TextureAtlasTracker.getUV(textureId, blockIdx, System.currentTimeMillis());
            blit(textureId, x, y, x + size, y + size, uv[0], uv[1], uv[2], uv[3]);
        }
    }

    /**
     * Renders an animated block spritesheet.
     * The default size is 16px.
     * @param block     The block whose spritesheet to render
     * @param x         The X position
     * @param y         The Y position
     */
    public void blockSpriteSheet(final Block block, final float x, final float y) {
        blockSpriteSheet(block, x, y, DEFAULT_ITEM_SPRITE_SIZE);
    }

    /**
     * Renders the icon of the specified block.
     * The default size is 16px.
     * @param block The block to render
     * @param x The X position
     * @param y The Y position
     */
    public void blockIcon(final Block block, final float x, final float y) {
        blockIcon(block, x, y, DEFAULT_ITEM_SPRITE_SIZE);
    }

    /**
     * Renders the icon of the specified block.
     * The default size is 16px.
     * @param block The block to render
     * @param x The X position
     * @param y The Y position
     * @param size The size of the icon
     */
    public void blockIcon(final Block block, final float x, final float y, final float size) {

        // Set up pose
        final float scale = size / DEFAULT_ITEM_SPRITE_SIZE;
        raw.pose().pushMatrix();
        raw.pose().translate(x, y);
        raw.pose().scale(scale, scale);

        // Load sprite
        //! Blocks with no item form return AIR from .asItem()
        if(block.asItem() == Items.AIR) {
            blit(MISSING_ITEM_SPRITE, 0, 0, DEFAULT_ITEM_SPRITE_SIZE, DEFAULT_ITEM_SPRITE_SIZE, 0f, 1f, 0f, 1f);
        }
        else {
            raw.item(new ItemStack(block), 0, 0);
        }

        // Pop pose
        raw.pose().popMatrix();
    }








    // Text cursors
    public static final float CURSOR_INSERT_WIDTH = 1f;
    private static final String CURSOR_APPEND_CHARACTER = "_";

    public void textInsertCursor(final int x, final float y, final int color, final float lineHeight) {
        fill(x, y - 1f, x + 1f, y + lineHeight, color);
    }

    public void textAppendCursor(final Font font, final int x, final int y, final int color, final boolean shadow) {
        raw.text(font, CURSOR_APPEND_CHARACTER, x, y, color, shadow);
    }

    public void textSelection(final int x0, final int y0, final int x1, final int y1, final boolean invert){
        raw.textHighlight(x0, y0, x1, y1, invert);
    }







    // Entities //TODO this is just a copy of vanilla's stuff. idk if it needs changes

    /**
     * Renders the specified entity's model, making it face the cursor at all times.
     */
    public void entity(
        final float x0,
        final float y0,
        final float x1,
        final float y1,
        final float size,
        final float offsetY,
        final float mouseX,
        final float mouseY,
        final LivingEntity entity
    ) {
        final float vanillaGuiScale = screen.getVanillaGuiScale();
        float centerX = (x0 + x1) / 2f;
        float centerY = (y0 + y1) / 2f;
        float xAngle = (float)Math.atan((centerX - mouseX) / 40f);
        float yAngle = (float)Math.atan((centerY - mouseY) / 40f);
        Quaternionf rotation = new Quaternionf().rotateZ((float)Math.PI);
        Quaternionf xRotation = new Quaternionf().rotateX(yAngle * 20f * (float)(Math.PI / 180.0));
        rotation.mul(xRotation);
        EntityRenderState renderState = extractEntityRenderState(entity);
        if(renderState instanceof LivingEntityRenderState livingRenderState) {
            livingRenderState.bodyRot = 180f + xAngle * 20f;
            livingRenderState.yRot = xAngle * 20f;
            if(livingRenderState.pose != Pose.FALL_FLYING) {
                livingRenderState.xRot = -yAngle * 20f;
            }
            else {
                livingRenderState.xRot = 0f;
            }

            livingRenderState.boundingBoxWidth  = livingRenderState.boundingBoxWidth  / livingRenderState.scale;
            livingRenderState.boundingBoxHeight = livingRenderState.boundingBoxHeight / livingRenderState.scale;
            livingRenderState.scale = 1f;
        }

        Vector3f translation = new Vector3f(0f, renderState.boundingBoxHeight / 2f + offsetY, 0f);
        final int _size = Math.round(size / vanillaGuiScale);
        final int _x0   = Math.round(x0   / vanillaGuiScale);
        final int _y0   = Math.round(y0   / vanillaGuiScale);
        final int _x1   = Math.round(x1   / vanillaGuiScale);
        final int _y1   = Math.round(y1   / vanillaGuiScale);
        raw.entity(renderState, _size, translation, rotation, xRotation, _x0, _y0, _x1, _y1);
    }


    private static EntityRenderState extractEntityRenderState(final LivingEntity entity) {
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> renderer = entityRenderDispatcher.getRenderer(entity);
        EntityRenderState renderState = renderer.createRenderState(entity, 1f);
        renderState.shadowPieces.clear();
        renderState.outlineColor = 0;
        return renderState;
    }
}
