package com.snek.engineersbliss.client.ui.widgets.base;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.screens.Screen;





/**
 * Contains methods shared by any custom Ui Widget.
 * Fake multiple inheritance for Java.
 */
public interface UiWidgetBase {
    public Screen getScreen();
    public @Nullable List<?> children();


    public void relayoutSelf();
    public default void relayoutContent() {
        final @Nullable List<?> children = children();
        if(children != null) for(final var e : children) {
            if(e instanceof final @NotNull UiWidgetBase w) {
                w.relayout();
            }
        }
    }
    public default void relayout() {
        relayoutSelf();
        relayoutContent();
    }
}
