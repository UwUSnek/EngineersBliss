package com.snek.engineersbliss.client.screens.parts;

import org.lwjgl.glfw.GLFW;

import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.CharacterEvent;




public class UiButton extends Button {
    private char key;

    public UiButton(final int x, final int y, final int width, final int height, final Txt message, final Button.OnPress onPress, final char key) {
        super(x, y, width, height, message.get(), onPress, DEFAULT_NARRATION);
        this.key = key;
    }
    public UiButton(final Txt message, final Button.OnPress onPress, final char key) {
        this(50, 50, 50, 50, message, onPress, key);
    }
    public UiButton(final int x, final int y, final int width, final int height, final Txt message, final Button.OnPress onPress) {
        this(x, y, width, height, message, onPress, '\0');
    }
    public UiButton(final Txt message, final Button.OnPress onPress) {
        this(50, 50, 50, 50, message, onPress);
    }


    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        final Font font = Minecraft.getInstance().font;

        final int textX = getX() + Layout.textMarginPx;
        final int textY = getY() + (height - font.lineHeight) / 2;
        final int bgColor = isHovered() ? Layout.bgColorActive : Layout.bgColor;
        final int fgColor = isHovered() ? Layout.fgColorActive : Layout.fgColor;
        graphics.fill(getX(), getY(), getX() + width, getY() + height, bgColor);
        graphics.text(font, this.message, textX, textY, fgColor);
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
