package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedColor;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;
import com.snek.engineersbliss.client.ui.widgets.misc.BgCacheWidget;
import com.snek.engineersbliss.client.ui.widgets.base.UiWidgetBase;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Easings;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;




public class UiButton extends Button implements BgCacheWidget, UiWidgetBase {
    private static final int KEYBIND_BADGE_WIDTH = 16;

    private UiTxt label;
    private char key;
    private final TextAlignment alignment;
    private float labelOffset;
    private final AnimatedColor overlayColor;

    // Screen reference
    private final Screen screen;
    public Screen getScreen() { return screen; }

    // Sprite
    private @Nullable Identifier bgSpriteId;
    private float bgSpriteWidth; // Sprite width compared to the height. 1 means square.

    // Mouse handling
    private boolean dragged = false;

    // Cached textures
    private final TextureCache bgCache;
    private int bgColor = Layout.bgColor;
    public void setBgColor(final int newColor) {
        bgColor = newColor; markBgDirty();
    }
	@Override public TextureCache getBgTextureCache() {
        return bgCache;
    }
    @Override public int getBgBaseColor() {
        return bgColor;
    }
    @Override public boolean isGuiScaleTransitioning() {
        return (screen instanceof @NotNull __base_UiScreen uiScreen) && uiScreen.isGuiScaleTransitioning();
    }




    public UiButton(final Screen screen, final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key, final TextAlignment alignment) {
        //! Pass empty text to super and store a custom UiTxt isntance locally
        super(x, y, width, height, new Txt().get(), b -> { if(pressCallback != null) pressCallback.accept((UiButton)b); }, DEFAULT_NARRATION);
        this.screen = screen;
        this.key = Character.toLowerCase(key);
        this.alignment = alignment;
        this.label = label;
        this.bgSpriteId = null;
        this.labelOffset = 0;
        this.bgCache = new TextureCache(screen);
        this.overlayColor = new AnimatedColor(0x0, Layout.hoverTransitionDuration, Easings.quadIn);
    }
    public UiButton(final Screen screen, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key, final TextAlignment alignment) {
        this(screen, 50, 50, 50, 50, label, pressCallback, key, alignment);
    }
    public UiButton(final Screen screen, final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final TextAlignment alignment) {
        this(screen, x, y, width, height, label, pressCallback, '\0', alignment);
    }
    public UiButton(final Screen screen, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final TextAlignment alignment) {
        this(screen, 50, 50, 50, 50, label, pressCallback, alignment);
    }


    public UiButton(final Screen screen, final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key) {
        this(screen, x, y, width, height, label, pressCallback, key, TextAlignment.LEFT);
    }
    public UiButton(final Screen screen, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key) {
        this(screen, 50, 50, 50, 50, label, pressCallback, key, TextAlignment.LEFT);
    }
    public UiButton(final Screen screen, final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback) {
        this(screen, x, y, width, height, label, pressCallback, '\0', TextAlignment.LEFT);
    }
    public UiButton(final Screen screen, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback) {
        this(screen, 50, 50, 50, 50, label, pressCallback, TextAlignment.LEFT);
    }


    public UiButton withSpriteBg(final Identifier id) {
        return withSpriteBg(id, 0);
    }
    public UiButton withSpriteBg(final Identifier id, final float width) {
        return withSpriteBg(id, width, labelOffset);
    }
    public UiButton withSpriteBg(final Identifier id, final float width, final float labelOffset) {
        this.bgSpriteId = id;
        this.bgSpriteWidth = width;
        this.labelOffset = labelOffset;
        return this;
    }
    // public UiButton withSpriteBg(final Identifier id, final float width, final int labelOffsetPx) { //TODO remove
    //     this.bgSpriteId = id;
    //     this.bgSpriteWidth = width;
    //     this.labelOffset = labelOffsetPx;
    //     return this;
    // }








    @Override
    public @Nullable List<?> children() {
        return null;
    }







    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        final boolean r = super.mouseClicked(event, doubleClick);
        dragged = true;
        return r;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        dragged = false;
        return super.mouseReleased(event);
    }

    public boolean isBeingDragged() {
        return dragged;
    }

    public boolean isHoveredOrBeingDragged() {
        return isHovered() || isBeingDragged();
    }








    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        // Draw background
        extractBackground(graphics, mouseX, mouseY, a);


        // Draw label
        final ScaledFont scaledFont = (label instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont();
        final int textX = getX() + (int)(height * labelOffset) + Layout.textMarginPx;
        final int textY = getY() + (height - scaledFont.getLineHeight()) / 2;
        RenderingUtils.extractTxt(graphics, label, textX, textY, Layout.fgColor, alignment, width, false);


        // Draw keybind if present
        if(key != '\0') {
            final UiTxt keybindText = new UiTxt(String.valueOf(key), Fonts.mono.medium);
            final int keybindX = getRight() - Layout.textMarginPx - KEYBIND_BADGE_WIDTH / 2;
            RenderingUtils.extractTxt(graphics, keybindText, keybindX, textY, Layout.fgColorHint, TextAlignment.CENTER_ANCHORED, width);
        }


        // Recalculate and draw hover highlight
        //! Minecraft doesn't provide any onMouseEnter/onMouseLeave callback so this must be recalculated by the rendering loop.
        //! This isn't bad, identical values don't update the animated target and computing time is negligible. It just feels unorthodox.
        final boolean shouldShowOverlay = isHoveredOrBeingDragged();
        overlayColor.startNewTransition(shouldShowOverlay ? Layout.highlightOverlay : 0x0);
        graphics.fill(getX(), getY(), getRight(), getBottom(), overlayColor.compute());
    }




    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        BgCacheWidget.super.extractBackground(graphics, mouseX, mouseY, a);

        // Draw background sprite if present, on top of the default background so the shape of the button is preserved
        final boolean usingSprite = bgSpriteId != null;
        if(usingSprite) {
            final int spriteWidth = (int)(getHeight() * bgSpriteWidth);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, bgSpriteId, getX(), getY(), spriteWidth, getHeight());
        }
    }








    @Override
    public boolean charTyped(CharacterEvent event) {
        if(key != '\0' && Character.toLowerCase((char)event.codepoint()) == Character.toLowerCase(key)) {
            onPress.onPress(this);
            return true;
        }
        else {
            return super.charTyped(event);
        }
    }


    @Override
    public void setMessage(Component message) {
        throw new UnsupportedOperationException("Use .setLabel(label) instead.");
    }
    public void setLabel(Component label) {
        this.label = new UiTxt(label);
    }
    public void setLabel(UiTxt label) {
        this.label = (UiTxt)label.copy();
    }
}
