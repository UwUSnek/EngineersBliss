package com.snek.engineersbliss.client.screens.parts;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.screens.Layout;
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


    @Override
    public @NotNull Component get() {
        rawText.setStyle(style.withFont(new FontDescription.Resource(Layout.getFontIdForScale(Layout.FONT_NAME_UI_MEDIUM, textScale))));
        return rawText;
    }
}
