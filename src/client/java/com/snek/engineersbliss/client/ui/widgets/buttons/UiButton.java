package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedColor;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Easings;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;








public class UiButton extends __base_UiWidget {
    private static final int KEYBIND_ICON_WIDTH = 16;

    private char key;
    private final AnimatedColor overlayColor;
    private final @Nullable Consumer<UiButton> pressCallback;

    // Sprite
    private @Nullable Identifier bgSpriteId;
    private float bgSpriteWidth; // Sprite width compared to the height. 1 means square.

    // Mouse handling
    private boolean dragged = false;




    public UiButton(final Screen screen, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key, final TextAlignment alignment) {
        super(screen, label, alignment);
        setBgColor(Layout.bgColor);
        this.pressCallback = pressCallback;
        this.key = Character.toLowerCase(key);
        this.bgSpriteId = null;
        this.overlayColor = new AnimatedColor(0x0, Layout.hoverTransitionDuration, Easings.quadInOut);
    }
    public UiButton(final Screen screen, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final TextAlignment alignment) {
        this(screen, label, pressCallback, '\0', alignment);
    }
    public UiButton(final Screen screen, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key) {
        this(screen, label, pressCallback, key, TextAlignment.LEFT);
    }
    public UiButton(final Screen screen, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback) {
        this(screen, label, pressCallback, TextAlignment.LEFT);
    }


    public UiButton withSpriteBg(final Identifier id) {
        return withSpriteBg(id, 0);
    }
    public UiButton withSpriteBg(final Identifier id, final float width) {
        return withSpriteBg(id, width, getLeftLabelMargin());
    }
    public UiButton withSpriteBg(final Identifier id, final float width, final float leftLabelMargin) {
        this.bgSpriteId = id;
        this.bgSpriteWidth = width;
        setLeftLabelMargin(leftLabelMargin);
        return this;
    }








    @Override
    public void relayoutSelf() {
        // Empty
    }







    @Override
    public void onClick(final MouseButtonEvent event, final boolean doubleClick) {
        if(pressCallback != null) pressCallback.accept(this);
    }

    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        final boolean r = super.mouseClicked(event, doubleClick);
        dragged = true;
        return r;
    }

    @Override
    public boolean mouseReleased(final MouseButtonEvent event) {
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
    protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);


        // Draw keybind if present
        if(key != '\0') {
            final FontFamily fontFamily = Fonts.mono.medium;
            final ScaledFont scaledFont = fontFamily.get(1f);
            final int keybindY = getY() + (height - scaledFont.getLineHeight()) / 2;
            final UiTxt keybindText = new UiTxt(String.valueOf(key), fontFamily);
            final int keybindX = getRight() - Layout.textMarginPx - KEYBIND_ICON_WIDTH / 2;
            RenderingUtils.extractTxt(graphics, keybindText, keybindX, keybindY, Layout.fgColorHint, TextAlignment.CENTER_ANCHORED, width);
        }


        // Recalculate and draw hover highlight
        //! Minecraft doesn't provide any onMouseEnter/onMouseLeave callback so this must be recalculated by the rendering loop.
        //! This isn't bad, identical values don't update the animated target and computing time is negligible. It just feels unorthodox.
        final boolean shouldShowOverlay = isHoveredOrBeingDragged();
        overlayColor.startNewTransition(shouldShowOverlay ? Layout.highlightOverlay : 0x0);
        graphics.fill(getX(), getY(), getRight(), getBottom(), overlayColor.compute());

        handleCursor(graphics);
    }




    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

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
            if(pressCallback != null) pressCallback.accept(this);
            return true;
        }
        else {
            return super.charTyped(event);
        }
    }
}