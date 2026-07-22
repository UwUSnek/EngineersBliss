package com.snek.engineersbliss.client.utils;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;








public class UiTxt extends Txt {

    // Font family and scale
    private final FontFamily fontFamily;
    private final float scale;

    // Getters
    public @NotNull FontFamily getFontFamily() { return fontFamily; }
    public @NotNull float      getScale     () { return scale; }
    public @NotNull ScaledFont getScaledFont() { return new ScaledFont(fontFamily.get(scale), scale); }




    public UiTxt()                                  { this(   1f); }
    public UiTxt(final @NotNull String           s) { this(s, 1f); }
    public UiTxt(final @NotNull MutableComponent s) { this(s, 1f); }
    public UiTxt(final @NotNull Component        s) { this(s, 1f); }

    public UiTxt(                                   final FontFamily fontFamily) { this(   fontFamily, 1f); }
    public UiTxt(final @NotNull String           s, final FontFamily fontFamily) { this(s, fontFamily, 1f); }
    public UiTxt(final @NotNull MutableComponent s, final FontFamily fontFamily) { this(s, fontFamily, 1f); }
    public UiTxt(final @NotNull Component        s, final FontFamily fontFamily) { this(s, fontFamily, 1f); }

    public UiTxt(                                   final float scale) { this(   Fonts.ui.light, scale); }
    public UiTxt(final @NotNull String           s, final float scale) { this(s, Fonts.ui.light, scale); }
    public UiTxt(final @NotNull MutableComponent s, final float scale) { this(s, Fonts.ui.light, scale); }
    public UiTxt(final @NotNull Component        s, final float scale) { this(s, Fonts.ui.light, scale); }

    public UiTxt(                                   final FontFamily fontFamily, final float scale) { super( ); this.fontFamily = fontFamily; this.scale = scale; }
    public UiTxt(final @NotNull String           s, final FontFamily fontFamily, final float scale) { super(s); this.fontFamily = fontFamily; this.scale = scale; }
    public UiTxt(final @NotNull MutableComponent s, final FontFamily fontFamily, final float scale) { super(s); this.fontFamily = fontFamily; this.scale = scale; }
    public UiTxt(final @NotNull Component        s, final FontFamily fontFamily, final float scale) { super(s); this.fontFamily = fontFamily; this.scale = scale; }




    @Override
    public @NotNull Txt copy() {
        rawText.setStyle(style);
        return new UiTxt(rawText.copy(), getFontFamily(), getScale());
    }

    @Override
    public @NotNull Component get() {
        rawText.setStyle(style.withFont(fontFamily.get(scale).getDescription()));
        return rawText.copy();
    }

    @Override
    public Txt substring(int start, int end) {
        return new UiTxt(super.substring(start, end).get(), getFontFamily(), getScale());
    }

    /** Wrapper for Txt.cat that returns a UiTxt instead of a Txt. */
    public UiTxt cat(final @NotNull UiTxt s) {
        return (UiTxt)super.cat(s);
    }
}
