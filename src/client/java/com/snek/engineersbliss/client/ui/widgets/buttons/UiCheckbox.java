package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;








public class UiCheckbox extends UiButton {
	private boolean selected;
	public boolean isSelected() { return this.selected; }




    public UiCheckbox(final Screen screen, final @Nullable Consumer<UiCheckbox> pressCallback, final boolean selected) {
        super(screen, new UiTxt(), pressCallback == null ? null : b -> pressCallback.accept((UiCheckbox)b));
		this.selected = selected;
	}
    public UiCheckbox(final Screen screen, final boolean selected) {
		this(screen, null, selected);
	}
    public UiCheckbox(final Screen screen, final @Nullable Consumer<UiCheckbox> pressCallback) {
		this(screen, pressCallback, false);
	}
    public UiCheckbox(final Screen screen) {
        this(screen, null);
	}

	@Override
	public void onClick(MouseButtonEvent event, boolean doubleClick) {
		this.selected = !this.selected;
		super.onClick(event, doubleClick);
	}
}
