package com.snek.engineersbliss.client.utils;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;








public class UiTxt extends Txt {
    private final float textScale;
    public float getTextScale() { return textScale; }
    public FontDescription getFont() { return style.getFont(); }


    public UiTxt()                                  { this(   1); }
    public UiTxt(final @NotNull String           s) { this(s, 1); }
    public UiTxt(final @NotNull MutableComponent s) { this(s, 1); }
    public UiTxt(final @NotNull Component        s) { this(s, 1); }


    public UiTxt(final float textScale) {
        super();
        this.textScale = textScale;
        withRegularFont();
    }
    public UiTxt(final @NotNull String s, final float textScale) {
        super(s);
        this.textScale = textScale;
        withRegularFont();
    }
    public UiTxt(final @NotNull MutableComponent s, final float textScale) {
        super(s);
        this.textScale = textScale;
        withFont(s.getStyle().getFont()); //FIXME this keeps the wrong glyph size when a different textScale is used
    }
    public UiTxt(final @NotNull Component s, final float textScale) {
        super(s);
        this.textScale = textScale;
        withFont(s.getStyle().getFont()); //FIXME this keeps the wrong glyph size when a different textScale is used
    }







    public @NotNull UiTxt withRegularFont() {
        style = style.withFont(new FontDescription.Resource(Layout.getFontIdForScale(Layout.FONT_NAME_UI_REGULAR, textScale)));
        return this;
    }
    public @NotNull UiTxt withBoldFont() {
        style = style.withFont(new FontDescription.Resource(Layout.getFontIdForScale(Layout.FONT_NAME_UI_BOLD, textScale)));
        return this;
    }
    public @NotNull UiTxt withLightFont() {
        style = style.withFont(new FontDescription.Resource(Layout.getFontIdForScale(Layout.FONT_NAME_UI_LIGHT, textScale)));
        return this;
    }
    public @NotNull UiTxt withMonoFont() {
        style = style.withFont(new FontDescription.Resource(Layout.getFontIdForScale(Layout.FONT_NAME_MONO_MEDIUM, textScale)));
        return this;
    }
    public @NotNull UiTxt withFont(final FontDescription font) {
        style = style.withFont(font);
        return this;
    }




    @Override
    public @NotNull Txt copy() {
        rawText.setStyle(style);
        return new UiTxt(rawText.copy(), textScale).withFont(style.getFont());
    }
}
