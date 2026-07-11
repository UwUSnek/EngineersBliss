package com.snek.engineersbliss.client.utils;

import com.snek.engineersbliss.client.screens.parts.TextAlignment;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;








public class RenderingUtils {
    private RenderingUtils() {}




    public static void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color, final TextAlignment textAlignment, final boolean dropShadow) {
        final Font font = Minecraft.getInstance().font;
        final float textScale = (text instanceof UiTxt uiTxt) ? uiTxt.getTextScale() : 1f;


        graphics.pose().pushMatrix();
        graphics.pose().scale(textScale, textScale);
        switch(textAlignment) {
            case LEFT:   { graphics.        text(font, (dropShadow ? text.copy().noShadow() : text).get(), (int)(x / textScale), (int)(y / textScale), color); break; }
            case CENTER: { graphics.centeredText(font, (dropShadow ? text.copy().noShadow() : text).get(), (int)(x / textScale), (int)(y / textScale), color); break; }
        }
        graphics.pose().popMatrix();
    }
    public void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color, final boolean dropShadow) {
        extractTxt(graphics, text, x, y, color, TextAlignment.LEFT);
    }
    public static void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color, final TextAlignment textAlignment) {
        extractTxt(graphics, text, x, y, color, textAlignment, true);
    }
    public void extractTxt(GuiGraphicsExtractor graphics, final Txt text, final int x, final int y, final int color) {
        extractTxt(graphics, text, x, y, color, TextAlignment.LEFT, true);
    }
}
