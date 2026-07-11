package com.snek.engineersbliss.client.utils;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;



public class UiTxt extends Txt {
    private final float textScale;

    public UiTxt()                                  { super();  textScale = 1; }
    public UiTxt(final @NotNull String           s) { super(s); textScale = 1; }
    public UiTxt(final @NotNull MutableComponent s) { super(s); textScale = 1; }
    public UiTxt(final @NotNull Component        s) { super(s); textScale = 1; }

    public UiTxt(                                   final float textScale) { super();  this.textScale = textScale; }
    public UiTxt(final @NotNull String           s, final float textScale) { super(s); this.textScale = textScale; }
    public UiTxt(final @NotNull MutableComponent s, final float textScale) { super(s); this.textScale = textScale; }
    public UiTxt(final @NotNull Component        s, final float textScale) { super(s); this.textScale = textScale; }




    public @NotNull Component get(final String fontName) {
        rawText.setStyle(style.withFont(new FontDescription.Resource(Layout.getFontIdForScale(fontName, textScale))));
        return rawText.copy();
    }
    @Override
    public @NotNull Component get() {
        return get(Layout.FONT_NAME_UI_MEDIUM);
    }
    public @NotNull Component getBold() {
        return get(Layout.FONT_NAME_UI_BOLD);
    }
    public @NotNull Component getLight() {
        return get(Layout.FONT_NAME_UI_LIGHT);
    }




    @Override
    public @NotNull Txt copy() {
        rawText.setStyle(style);
        return new UiTxt(rawText.copy(), textScale);
    }
}
