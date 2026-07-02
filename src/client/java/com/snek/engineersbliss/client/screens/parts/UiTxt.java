package com.snek.engineersbliss.client.screens.parts;

import com.snek.engineersbliss.utils.Txt;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.screens.Layout;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;



public class UiTxt extends Txt {

    public UiTxt() { super(); }
    public UiTxt(final @NotNull String s) { super(s); }
    public UiTxt(final @NotNull MutableComponent s) { super(s); }
    public UiTxt(final @NotNull Component s) { super(s); }


    @Override
    public @NotNull Component get() {
        rawText.setStyle(Layout.textStyleForScale(Layout.FONT_NAME_UI_MEDIUM));
        return rawText;
    }
}
