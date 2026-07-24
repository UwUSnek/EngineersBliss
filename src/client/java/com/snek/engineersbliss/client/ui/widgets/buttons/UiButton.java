package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;




public class UiButton extends Button {
    private static final int KEYBIND_BADGE_WIDTH = 16;

    private UiTxt label;
    private char key;
    private final TextAlignment alignment;
    private @Nullable Identifier bgSpriteId;
    private float bgSpriteWidth; // Sprite width compared to the height. 1 means square.
    private int labelOffset;   // Label offset from the left edge, in pixels.




    public UiButton(final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key, final TextAlignment alignment) {
        //! Pass empty text to super and store a custom UiTxt isntance locally
        super(x, y, width, height, new Txt().get(), b -> { if(pressCallback != null) pressCallback.accept((UiButton)b); }, DEFAULT_NARRATION);
        this.key = Character.toLowerCase(key);
        this.alignment = alignment;
        this.label = label;
        this.bgSpriteId = null;
        this.labelOffset = Layout.textMarginPx;
    }
    public UiButton(final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key, final TextAlignment alignment) {
        this(50, 50, 50, 50, label, pressCallback, key, alignment);
    }
    public UiButton(final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final TextAlignment alignment) {
        this(x, y, width, height, label, pressCallback, '\0', alignment);
    }
    public UiButton(final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final TextAlignment alignment) {
        this(50, 50, 50, 50, label, pressCallback, alignment);
    }


    public UiButton(final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key) {
        this(x, y, width, height, label, pressCallback, key, TextAlignment.LEFT);
    }
    public UiButton(final UiTxt label, final @Nullable Consumer<UiButton> pressCallback, final char key) {
        this(50, 50, 50, 50, label, pressCallback, key, TextAlignment.LEFT);
    }
    public UiButton(final int x, final int y, final int width, final int height, final UiTxt label, final @Nullable Consumer<UiButton> pressCallback) {
        this(x, y, width, height, label, pressCallback, '\0', TextAlignment.LEFT);
    }
    public UiButton(final UiTxt label, final @Nullable Consumer<UiButton> pressCallback) {
        this(50, 50, 50, 50, label, pressCallback, TextAlignment.LEFT);
    }


    public UiButton withSpriteBg(final Identifier id) {
        return withSpriteBg(id, 0);
    }
    public UiButton withSpriteBg(final Identifier id, final float width) {
        return withSpriteBg(id, width, labelOffset);
    }
    public UiButton withSpriteBg(final Identifier id, final float width, final float labelOffsetUnit) {
        this.bgSpriteId = id;
        this.bgSpriteWidth = width;
        this.labelOffset = (int)(height * labelOffsetUnit);
        return this;
    }
    public UiButton withSpriteBg(final Identifier id, final float width, final int labelOffsetPx) {
        this.bgSpriteId = id;
        this.bgSpriteWidth = width;
        this.labelOffset = labelOffsetPx;
        return this;
    }




    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        final boolean usingSprite = bgSpriteId != null;


        // Draw black background //! Always drawn
        graphics.fill(getX(), getY(), getRight(), getBottom(), Layout.bgColor);

        // Draw background sprite if present, on top of the default background so the shape of the button is preserved
        if(usingSprite) {
            final int spriteWidth = (int)(height * bgSpriteWidth);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, bgSpriteId, getX(), getY(), spriteWidth, height);
        }

        // Draw hover highlight
        if(isHovered) graphics.fill(getX(), getY(), getRight(), getBottom(), Layout.bgColorActive);


        // Draw label
        final ScaledFont scaledFont = (label instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont();
        final int textX = getX() + labelOffset;
        final int textY = getY() + (height - scaledFont.getLineHeight()) / 2;
        final int fgColor = isHovered() ? Layout.fgColorActive : Layout.fgColor;
        RenderingUtils.extractTxt(graphics, label, textX, textY, fgColor, alignment, width, usingSprite);


        // Draw keybind if present
        if(key != '\0') {
            final UiTxt keybindText = new UiTxt(String.valueOf(key), Fonts.mono.medium);
            final int keybindX = getRight() - Layout.textMarginPx - KEYBIND_BADGE_WIDTH / 2;
            RenderingUtils.extractTxt(graphics, keybindText, keybindX, textY, Layout.fgColorHint, TextAlignment.CENTER_ANCHORED, width);
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
