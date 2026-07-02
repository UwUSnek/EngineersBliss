package com.snek.engineersbliss.client.screens;

import com.snek.engineersbliss.EngineerSBliss;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;




public class Layout {
    private Layout() { }

    public static final int BORDER_WIDTH = 10;
    public static final int BORDER_HEIGHT = 4;
    public static final int LIST_TOP = 32;
    public static final int BUTTON_HEIGHT = 20;




    public static Identifier getFontIdForScale(final String baseName) {
        int scale = Minecraft.getInstance().getWindow().getGuiScale();
        return Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, String.format("%s_%dx", baseName, scale));
    }

    public static final Style textStyleForScale() {
        final FontDescription textFont = new FontDescription.Resource(getFontIdForScale("ui"));
        return Style.EMPTY.withoutShadow().withFont(textFont);
    }
}
