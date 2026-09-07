package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;








public class UiCheckbox extends UiToggleButton {


    public UiCheckbox(final Screen screen, final boolean initialValue, final @Nullable Consumer<UiCheckbox> pressCallback) {
		super(screen, initialValue, new UiTxt(""), pressCallback == null ? null : b -> pressCallback.accept((UiCheckbox)b), '\0');
		indicatorWidth.clear().setWF(1f);
	}
    public UiCheckbox(final Screen screen, final boolean initialValue) {
		this(screen, initialValue, null);
	}
    public UiCheckbox(final Screen screen, final @Nullable Consumer<UiCheckbox> pressCallback) {
		this(screen, false, pressCallback);
	}
    public UiCheckbox(final Screen screen) {
        this(screen, null);
	}
}
