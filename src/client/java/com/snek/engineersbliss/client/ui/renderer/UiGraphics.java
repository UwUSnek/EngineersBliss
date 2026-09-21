package com.snek.engineersbliss.client.ui.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.screens.rendering.BlockSpriteFileNames;
import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.renderer.render_states.AaFillRenderState;
import com.snek.engineersbliss.client.ui.renderer.render_states.AaBlitRenderState;
import com.snek.engineersbliss.client.ui.renderer.render_states.RawBlitRenderState;
import com.snek.engineersbliss.client.ui.renderer.render_states.AaMultilineRenderState;
import com.snek.engineersbliss.client.ui.renderer.render_states.MultilineAreaRenderState;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.client.utils.textures.mp4.Mp4TextureTracker;
import com.snek.engineersbliss.client.utils.textures.svg.SvgTextureTracker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor.ScissorStack;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.registries.BuiltInRegistries;
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
    private final GuiGraphicsExtractor raw;
    private final Supplier<Integer> widthGetter;
    private final Supplier<Integer> heightGetter;
    public int getWidth () { return  widthGetter.get(); }
    public int getHeight() { return heightGetter.get(); }







    public UiGraphics(final GuiGraphicsExtractor raw, UiScreen screen) {
        this(raw, () -> screen.width, () -> screen.height);
    }
    public UiGraphics(final GuiGraphicsExtractor raw, final Supplier<Integer> widthGetter, final Supplier<Integer> heightGetter) {
        this.raw = raw;
        this.widthGetter = widthGetter;
        this.heightGetter = heightGetter;

        // Add full screen scissor to clip out of bounds widgets. Not strictly needed but makes the rendering more reliable.
        //! GuiGraphicsExtractor already does this by default in 26.2+, but im keeping this in all versions because I don't trust Miencraft's
        //! code enough. It might change in the future or something, having this always on is easier and doesn't rly affect performance anyway.
        raw.enableScissor(0, 0, getWidth(), getHeight());
    }


    public void requestCursor(final CursorType cursorType) {
        raw.requestCursor(cursorType);
    }

    public static int applyAlpha(final int color, final float alpha) {
        final int c = color & 0x00FFFFFF;
        final int a = (color >>> 24) & 0xFF;
        return c | ((Math.round(a * alpha) & 0xFF) << 24);
    }






    // Scissors
    public Scissor scissor = new Scissor();
    public class Scissor {
        public void enable(final int x0, final int y0, final int x1, final int y1) {
            raw.enableScissor(x0, y0, x1, y1);
        }
        public void disable() {
            raw.disableScissor();
        }
        public boolean containsPoint(final int x, final int y) {
            return raw.containsPointInScissor(x, y);
        }
        public ScissorStack getStack() {
            return raw.scissorStack;
        }
    }








    // Plain fill

    public void fill(final float x0, final float y0, final float x1, final float y1, final int col, final float alpha) {
        fill(x0, y0, x1, y1, applyAlpha(col, alpha));
    }
    public void fill(float x0, float y0, float x1, float y1, final int col) {
        gradient.quad(x0, y0, x1, y1, col, col, col, col);
    }








    // Gradient fills
    public Gradient gradient = new Gradient();
    public class Gradient {
        public void horizontal(final float x0, final float y0, final float x1, final float y1, final int colA, final int colB, float alpha) {
            horizontal(x0, y0, x1, y1, applyAlpha(colA, alpha), applyAlpha(colB, alpha));
        }
        public void horizontal(final float x0, final float y0, final float x1, final float y1, final int colA, final int colB) {
            __internal_horizontal(Math.min(x0, x1), y0, Math.max(x0, x1), y1, colA, colB);
        }
        private void __internal_horizontal(float x0, float y0, float x1, float y1, final int colA, final int colB) {
            if(y0 > y1) { final float tmp = y0; y0 = y1; y1 = tmp; }
            raw.guiRenderState.addGuiElement(new AaFillRenderState(
                UiRenderPipelines.AA_FILL, TextureSetup.noTexture(), new Matrix3x2f(raw.pose()),
                x0, y0, x1, y1,
                colB, colB, colA, colA, // tr, br, bl, tl
                scissor.getStack().peek()
            ));
        }


        public void vertical(final float x0, final float y0, final float x1, final float y1, final int colA, final int colB, float alpha) {
            vertical(x0, y0, x1, y1, applyAlpha(colA, alpha), applyAlpha(colB, alpha));
        }
        public void vertical(final float x0, final float y0, final float x1, final float y1, final int colA, final int colB) {
            __internal_vertical(x0, Math.min(y0, y1), x1, Math.max(y0, y1), colA, colB);
        }
        private void __internal_vertical(float x0, float y0, float x1, float y1, final int colA, final int colB) {
            if(x0 > x1) { final float tmp = x0; x0 = x1; x1 = tmp; }
            raw.guiRenderState.addGuiElement(new AaFillRenderState(
                UiRenderPipelines.AA_FILL, TextureSetup.noTexture(), new Matrix3x2f(raw.pose()),
                x0, y0, x1, y1,
                colA, colB, colB, colA, // tr, br, bl, tl
                scissor.getStack().peek()
            ));
        }


        public void quad(final float x0, final float y0, final float x1, final float y1, final int colTL, final int colTR, final int colBR, final int colBL, final float alpha) {
            quad(x0, y0, x1, y1, applyAlpha(colTL, alpha), applyAlpha(colTR, alpha), applyAlpha(colBR, alpha), applyAlpha(colBL, alpha));
        }
        public void quad(float x0, float y0, float x1, float y1, final int colTL, final int colTR, final int colBR, final int colBL) {
            if(x0 > x1) { final float tmp = x0; x0 = x1; x1 = tmp; }
            if(y0 > y1) { final float tmp = y0; y0 = y1; y1 = tmp; }
            raw.guiRenderState.addGuiElement(new AaFillRenderState(
                UiRenderPipelines.AA_FILL, TextureSetup.noTexture(), new Matrix3x2f(raw.pose()),
                x0, y0, x1, y1,
                colTR, colBR, colBL, colTL, // tr, br, bl, tl
                scissor.getStack().peek()
            ));
        }
    }







//TODO add float coords support for text
    // Text rendering
    public Text text = new Text();
    public class Text {
        public void __internal_draw(
            final FormattedCharSequence text,
            final int textWidth,
            final ScaledFont scaledFont,
            final int x, final int y,
            final int color,
            final TextAlignment textAlignment,
            final float elmWidth, //! Only used by alignment CENTER and RIGHT
            final float shiftX, final float shiftY //! Text shift in real screen pixels. This doesn't depend on the text size.
        ) {

            // Retrieve text scale
            final @Nullable Screen screen = MinecraftUtils.getScreen();
            final float guiScale = screen != null && (screen instanceof UiScreen s) ? s.getGuiScale() : SettingsFeatureHandler.getCurrentGuiScale();
            final float textScale = scaledFont.getSizeForGuiScale(guiScale);

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


        public void draw(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final float elmWidth, final boolean dropShadow) {
            drawShifted(text, x, y, color, textAlignment, elmWidth, dropShadow, 0f, 0f);
        }
        public void drawShifted(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final float elmWidth, final boolean dropShadow, final float shiftX, final float shiftY) {
            //! All overloads go through this which calls the true extractTxt.
            //! Using toRawVisualOrder() is required in order to render '§' properly.
            final ScaledFont scaledFont = (text instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont(false);
            __internal_draw((dropShadow ? text : text.noShadow()).toRawVisualOrder(), text.getWidth(), scaledFont, x, y, color, textAlignment, elmWidth, shiftX, shiftY);
        }
        public void draw(final UiTxt text, final int x, final int y, final int color, final boolean dropShadow) {
            drawShifted(text, x, y, color, dropShadow, 0f, 0f);
        }
        public void drawShifted(final UiTxt text, final int x, final int y, final int color, final boolean dropShadow, final float shiftX, final float shiftY) {
            drawShifted(text, x, y, color, TextAlignment.LEFT, 0, dropShadow, shiftX, shiftY);
        }


        public void draw(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final float elmWidth) {
            drawShifted(text, x, y, color, textAlignment, elmWidth, 0f, 0f);
        }
        public void drawShifted(final UiTxt text, final int x, final int y, final int color, final TextAlignment textAlignment, final float elmWidth, final float shiftX, final float shiftY) {
            drawShifted(text, x, y, color, textAlignment, elmWidth, false, shiftX, shiftY);
        }
        public void draw(final UiTxt text, final int x, final int y, final int color) {
            drawShifted(text, x, y, color, 0f, 0f);
        }
        public void drawShifted(final UiTxt text, final int x, final int y, final int color, final float shiftX, final float shiftY) {
            drawShifted(text, x, y, color, false, shiftX, shiftY);
        }


        public void draw(final UiTxt text, final int x, final int y, final int color, final float alpha, final TextAlignment textAlignment, final float elmWidth, final boolean dropShadow) {
            draw(text, x, y, applyAlpha(color, alpha), textAlignment, elmWidth, dropShadow);
        }
        public void drawShifted(final UiTxt text, final int x, final int y, final int color, final float alpha, final TextAlignment textAlignment, final float elmWidth, final boolean dropShadow, final float shiftX, final float shiftY) {
            drawShifted(text, x, y, applyAlpha(color, alpha), textAlignment, elmWidth, dropShadow, shiftX, shiftY);
        }
        public void draw(final UiTxt text, final int x, final int y, final int color, final float alpha, final boolean dropShadow) {
            draw(text, x, y, applyAlpha(color, alpha), dropShadow);
        }
        public void drawShifted(final UiTxt text, final int x, final int y, final int color, final float alpha, final boolean dropShadow, final float shiftX, final float shiftY) {
            drawShifted(text, x, y, applyAlpha(color, alpha), dropShadow, shiftX, shiftY);
        }


        public void draw(final UiTxt text, final int x, final int y, final int color, final float alpha, final TextAlignment textAlignment, final float elmWidth) {
            draw(text, x, y, applyAlpha(color, alpha), textAlignment, elmWidth);
        }
        public void drawShifted(final UiTxt text, final int x, final int y, final int color, final float alpha, final TextAlignment textAlignment, final float elmWidth, final float shiftX, final float shiftY) {
            drawShifted(text, x, y, applyAlpha(color, alpha), textAlignment, elmWidth, shiftX, shiftY);
        }
        public void draw(final UiTxt text, final int x, final int y, final int color, final float alpha) {
            draw(text, x, y, applyAlpha(color, alpha));
        }
        public void drawShifted(final UiTxt text, final int x, final int y, final int color, final float alpha, final float shiftX, final float shiftY) {
            drawShifted(text, x, y, applyAlpha(color, alpha), shiftX, shiftY);
        }


        public static final float CURSOR_INSERT_WIDTH = 1f;
        private static final String CURSOR_APPEND_CHARACTER = "_";
        public void drawInsertCursor(final int x, final float y, final int color, final float lineHeight) {
            fill(x, y - 1f, x + 1f, y + lineHeight, color);
        }
        public void drawAppendCursor(final Font font, final int x, final int y, final int color, final boolean shadow) {
            raw.text(font, CURSOR_APPEND_CHARACTER, x, y, color, shadow);
        }
        public void drawSelection(final int x0, final int y0, final int x1, final int y1, final boolean invert){
            raw.textHighlight(x0, y0, x1, y1, invert);
        }



        /**
         * Wraps the provided UiTxt so each line never goes past the width limit.
         * @param text The text to wrap.
         * @param maxWidth The maximum width of a line.
         * @return A list of UiTxt, each containing the formatted characters in a line.
         */
        public static List<UiTxt> wrapLines(final UiTxt text, final float maxWidth) {

            // Create line list and calculate data
            final @NotNull ScaledFont scaledFont = text.getScaledFont();
            final @NotNull List<UiTxt> lines = new ArrayList<>();
            final @NotNull String raw = text.getString();
            final int len = raw.length();
            int lineStart = 0;
            int lastSpace = -1;

            // Split lines
            for(int i = 0; i < len; i++) {
                final char c = raw.charAt(i);
                if(c == '\n') {
                    lines.add((UiTxt)text.substring(lineStart, i));
                    lineStart = i + 1;
                    lastSpace = -1;
                    continue;
                }
                if(c == ' ') {
                    lastSpace = i;
                }
                if(scaledFont.calcWidth(raw.substring(lineStart, i + 1)) > maxWidth) { //TODO this is prob inefficient
                    if(lastSpace >= lineStart) {
                        lines.add((UiTxt)text.substring(lineStart, lastSpace));
                        lineStart = lastSpace + 1;
                    }
                    else {
                        lines.add((UiTxt)text.substring(lineStart, i));
                        lineStart = i;
                    }
                    lastSpace = -1;
                }
            }
            if(lineStart < len) {
                lines.add((UiTxt)text.substring(lineStart, len));
            }
            return lines;
        }
    }








    // Floating point blit
    public final Blit blit = new Blit();
    public class Blit {
        private static TextureSetup textureSetupFor(final Identifier location) {
            final @NotNull AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location);
            return TextureSetup.singleTexture(texture.getTextureView(), texture.getSampler());
        }
        private void __internal_blit(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float u0, final float v0, final float u1, final float v1, final float alpha) {
            final boolean isSvg = SvgTextureTracker.isRegistered(location);
            if(isSvg) {
                final int x = Math.round(x0);
                final int y = Math.round(y0);
                final int w = Math.round(Math.abs(x1 - x0));
                final int h = Math.round(Math.abs(y1 - y0));
                final @Nullable Screen screen = MinecraftUtils.getScreen();
                final boolean isTransitioning = screen != null && (screen instanceof UiScreen s) && s.isGuiScaleTransitioning();
                final Identifier dataId = SvgTextureTracker.requestForSize(location, w, h, !isTransitioning);
                raw.guiRenderState.addGuiElement(new RawBlitRenderState(
                    UiRenderPipelines.RAW_BLIT,
                    textureSetupFor(dataId),
                    new Matrix3x2f(raw.pose()),
                    x, y, x + w, y + h,
                    u0, v0, u1, v1,
                    alpha, scissor.getStack().peek()
                ));
            }
            else {
                final Identifier dataId = location.withPath("textures/" + location.getPath() + ".png");
                raw.guiRenderState.addGuiElement(new AaBlitRenderState(
                    UiRenderPipelines.AA_BLIT,
                    textureSetupFor(dataId),
                    new Matrix3x2f(raw.pose()),
                    x0, y0, x1, y1,
                    u0, v0, u1, v1,
                    alpha, scissor.getStack().peek()
                ));
            }
        }


        public void wh(final Identifier location, final float x, final float y, final float w, final float h) {
            wh(location, x, y, w, h, 0f, 1f, 0f, 1f);
        }
        public void wh(final Identifier location, final float x, final float y, final float w, final float h, final float alpha) {
            wh(location, x, y, w, h, 0f, 1f, 0f, 1f, alpha);
        }
        public void wh(final Identifier location, final float x, final float y, final float w, final float h, final float u0, final float u1, final float v0, final float v1) {
            xy(location, x, y, x + w, y + h, u0, u1, v0, v1);
        }
        public void wh(final Identifier location, final float x, final float y, final float w, final float h, final float u0, final float u1, final float v0, final float v1, final float alpha) {
            xy(location, x, y, x + w, y + h, u0, u1, v0, v1, alpha);
        }


        public void xy(final Identifier location, final float x0, final float y0, final float x1, final float y1) {
            xy(location, x0, y0, x1, y1, 0f, 1f, 0f, 1f);
        }
        public void xy(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float alpha) {
            xy(location, x0, y0, x1, y1, 0f, 1f, 0f, 1f, alpha);
        }
        public void xy(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float u0, final float u1, final float v0, final float v1) {
            xy(location, x0, y0, x1, y1, u0, u1, v0, v1, 1.0f);
        }
        public void xy(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float u0, final float u1, final float v0, final float v1, final float alpha) {
            __internal_blit(location, x0, y0, x1, y1, u0, v0, u1, v1, alpha);
        }
    }








    // Video blit
    public final Video video = new Video();
    public class Video {
        private static TextureSetup textureSetupFor(final Identifier location) {
            final @NotNull AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location);
            return TextureSetup.singleTexture(texture.getTextureView(), texture.getSampler());
        }
        private void __internal_video(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float alpha) {
            final Identifier dataId = Mp4TextureTracker.getCurrentTexture(location);
            if(dataId == null) return;
            if(dataId == Layout.PLACEHOLDER_TEXTURE_ID) {
                float side = Math.min(y1 - y0, x1 - x0);
                float cx = (x0 + x1) / 2f;
                float cy = (y0 + y1) / 2f;
                final float sx0 = cx - side / 2f;
                final float sy0 = cy - side / 2f;
                final float sx1 = sx0 + side;
                final float sy1 = sy0 + side;
                blit.xy(dataId, sx0, sy0, sx1, sy1, 0.25f);
            }
            else raw.guiRenderState.addGuiElement(new AaBlitRenderState(
                UiRenderPipelines.AA_BLIT,
                textureSetupFor(dataId),
                new Matrix3x2f(raw.pose()),
                x0, y0, x1, y1,
                0f, 0f, 1f, 1f,
                alpha, scissor.getStack().peek()
            ));
        }


        public void wh(final Identifier location, final float x, final float y, final float w, final float h) {
            wh(location, x, y, w, h, 1f);
        }
        public void wh(final Identifier location, final float x, final float y, final float w, final float h, final float alpha) {
            xy(location, x, y, x + w, y + h, alpha);
        }


        public void xy(final Identifier location, final float x0, final float y0, final float x1, final float y1) {
            xy(location, x0, y0, x1, y1, 1f);
        }
        public void xy(final Identifier location, final float x0, final float y0, final float x1, final float y1, final float alpha) {
            __internal_video(location, x0, y0, x1, y1, alpha);
        }
    }








    // Multilines
    public void multiLine(final float x0, final float y0, final float x1, final float y1, float[] xs, float[] ys, float thickness, int color) {
        raw.guiRenderState.addGuiElement(new AaMultilineRenderState(
            UiRenderPipelines.AA_MULTILINE, new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1,
            xs, ys, thickness, color,
            scissor.getStack().peek()
        ));
    }
    public void multiLineArea(final float x0, final float y0, final float x1, final float y1, float[] xs, float[] ys, int color) {
        raw.guiRenderState.addGuiElement(new MultilineAreaRenderState(
            UiRenderPipelines.MULTILINE_AREA, new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1,
            xs, ys, color,
            scissor.getStack().peek()
        ));
    }







    // Block icons and sprites

    public static final Identifier MISSING_ITEM_SPRITE = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "gui/missing_item_sprite");
    public static final int DEFAULT_ITEM_SPRITE_SIZE = 16;

    //TODO REMOVE
    // private static final int ATLAS_COLS = 8;
    // private static final int ATLAS_ROWS = 8;
    // private static final int SHEETS_PER_ATLAS = ATLAS_COLS * ATLAS_ROWS;
    // //TODO this stuff could be moved to the atlas tracker using a suffix _n system but that's kinda complicated
    // //TODO and also large sprite sheets are supposed to use that? these are not large sprite sheets but atlases of sprite sheets which is different.
    // //TODO different math? probably?


    // /**
    //  * Renders an animated block spritesheet.
    //  * The default size is 16px.
    //  * @param block     The block whose spritesheet to render
    //  * @param x         The X position
    //  * @param y         The Y position
    //  * @param size      The rendered size in pixels
    //  */
    // public void blockSpriteSheet(final Block block, final float x, final float y, final float size) {

    //     // Get block index, fallback to default icon if absent
    //     final Identifier id = BuiltInRegistries.BLOCK.getKey(block);
    //     final int blockIdx = BlockSpriteFileNames.getIdList().indexOf(id.getPath());
    //     if(blockIdx == -1) {
    //         blockIcon(block, x, y, size);
    //         return;
    //     }

    //     final Identifier textureId = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "textures/gui/block_renders/atlas_0.png");
    //     if(!TextureAtlasTracker.isTextureReady(textureId)) {
    //         blit.wh(textureId, x, y, size, size);
    //     }
    //     else {
    //         final float[] uv = TextureAtlasTracker.getUV(textureId, blockIdx, System.currentTimeMillis());
    //         blit.wh(textureId, x, y, size, size, uv[0], uv[1], uv[2], uv[3]);
    //     }
    // }

    // /**
    //  * Renders an animated block spritesheet.
    //  * The default size is 16px.
    //  * @param block     The block whose spritesheet to render
    //  * @param x         The X position
    //  * @param y         The Y position
    //  */
    // public void blockSpriteSheet(final Block block, final float x, final float y) {
    //     blockSpriteSheet(block, x, y, DEFAULT_ITEM_SPRITE_SIZE);
    // }

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
            blit.wh(MISSING_ITEM_SPRITE, 0, 0, DEFAULT_ITEM_SPRITE_SIZE, DEFAULT_ITEM_SPRITE_SIZE);
        }
        else {
            raw.item(new ItemStack(block), 0, 0);
        }

        // Pop pose
        raw.pose().popMatrix();
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

        final float vanillaGuiScale = MinecraftUtils.getVanillaGuiScale();
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







    // Blur

    public void markBlurStart() { //FIXME
        //BUG this shouldn't be needed byt the new mixin thing system requires it in order to work.
        //BUG old mixin system thing hides HUD
        raw.nextStratum();
        raw.blurBeforeThisStratum();
    }


    public void gaussianBlur(float x0, float y0, float x1, float y1, final float radius) {
        if(x0 > x1) { final float tmp = x0; x0 = x1; x1 = tmp; }
        if(y0 > y1) { final float tmp = y0; y0 = y1; y1 = tmp; }
        UiBlur.request(radius);

        final @NotNull Vector2f p0 = raw.pose().transformPosition(x0, y0, new Vector2f());
        final @NotNull Vector2f p1 = raw.pose().transformPosition(x1, y1, new Vector2f());
        final @NotNull Window window = Minecraft.getInstance().getWindow();
        final float scale = MinecraftUtils.getVanillaGuiScale();
        final float w = window.getWidth()  / scale;
        final float h = window.getHeight() / scale;

        raw.guiRenderState.addGuiElement(new AaBlitRenderState(
            UiRenderPipelines.AA_BLIT, UiBlur.textureSetup(), new Matrix3x2f(raw.pose()),
            x0, y0, x1, y1,
            p0.x / w, 1f - p0.y / h, p1.x / w, 1f - p1.y / h,
            1f, scissor.getStack().peek()
        ));
    }
}
