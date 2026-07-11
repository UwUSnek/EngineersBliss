package com.snek.engineersbliss.client.screens.parts;

import org.lwjgl.glfw.GLFW;

import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.CharacterEvent;




public class UiButton extends Button {
    private final Txt label;
    private char key;
    private final TextAlignment alignment;




    public UiButton(final int x, final int y, final int width, final int height, final Txt label, final Button.OnPress onPress, final char key, final TextAlignment alignment) {
        //! Pass empty text to super and store a custom Txt isntance locally
        super(x, y, width, height, new Txt().get(), onPress, DEFAULT_NARRATION);
        this.key = key;
        this.alignment = alignment;
        this.label = label;
    }
    public UiButton(final Txt label, final Button.OnPress onPress, final char key, final TextAlignment alignment) {
        this(50, 50, 50, 50, label, onPress, key);
    }
    public UiButton(final int x, final int y, final int width, final int height, final Txt label, final Button.OnPress onPress, final TextAlignment alignment) {
        this(x, y, width, height, label, onPress, '\0');
    }
    public UiButton(final Txt label, final Button.OnPress onPress, final TextAlignment alignment) {
        this(50, 50, 50, 50, label, onPress);
    }


    public UiButton(final int x, final int y, final int width, final int height, final Txt label, final Button.OnPress onPress, final char key) {
        this(x, y, width, height, label, onPress, key, TextAlignment.LEFT);
    }
    public UiButton(final Txt label, final Button.OnPress onPress, final char key) {
        this(50, 50, 50, 50, label, onPress, key, TextAlignment.LEFT);
    }
    public UiButton(final int x, final int y, final int width, final int height, final Txt label, final Button.OnPress onPress) {
        this(x, y, width, height, label, onPress, '\0', TextAlignment.LEFT);
    }
    public UiButton(final Txt label, final Button.OnPress onPress) {
        this(50, 50, 50, 50, label, onPress, TextAlignment.LEFT);
    }




    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        final Font font = Minecraft.getInstance().font;

        final int textX = getX() + Layout.textMarginPx;
        final int textY = getY() + (height - font.lineHeight) / 2;
        final int bgColor = isHovered() ? Layout.bgColorActive : Layout.bgColor;
        final int fgColor = isHovered() ? Layout.fgColorActive : Layout.fgColor;
        graphics.fill(getX(), getY(), getRight(), getBottom(), bgColor);
        RenderingUtils.extractTxt(graphics, label, textX, textY, fgColor, alignment);
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
}
