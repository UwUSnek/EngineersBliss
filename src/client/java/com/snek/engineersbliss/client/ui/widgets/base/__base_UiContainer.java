package com.snek.engineersbliss.client.ui.widgets.base;



import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

import com.snek.engineersbliss.client.utils.UiTxt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;








/**
 * Base class for widgets that act as containers holding a list of children of type T.
 * This doesn't provide scrolling capabilities.
 */

public abstract class __base_UiContainer<T extends GuiEventListener> extends __base_UiWidget implements ContainerEventHandler {


    protected final List<T> children = new ArrayList<>();
    @Nullable protected T focused;
    @Nullable protected T selected;
    @Nullable protected T hovered;
    private boolean isDragging;




    protected __base_UiContainer(final Screen screen, final UiTxt label) {
        super(screen, label);
    }







    public final List<T> children() {
        return children;
    }

    protected void clearChildren() {
        children.clear();
        selected = null;
    }

    protected int addChild(final T e) {
        children.add(e);
        return children.size() - 1;
    }

    @Override
    public Optional<GuiEventListener> getChildAt(final double x, final double y) {
        for(final @NotNull T child : children) {
            if(child.isMouseOver(x, y)) {
                return Optional.of(child);
            }
        }
        return Optional.empty();
    }

    protected boolean entriesCanBeSelected() {
        return false;
    }

    @Nullable
    public T getHoveredEntry() {
        return hovered;
    }








    @Nullable
    public T getSelected() {
        return selected;
    }

    public void setSelected(@Nullable final T selected) {
        this.selected = selected;
        if(selected != null) {
            onSelected(selected);
        }
    }

    //! Hook for subclasses that need to react to a new selection.
    protected void onSelected(final T selected) {
        // Empty
    }








    @Override
    public final boolean isDragging() {
        return this.isDragging;
    }

    @Override
    public final void setDragging(final boolean dragging) {
        this.isDragging = dragging;
    }

    @Nullable
    @Override
    public T getFocused() {
        return this.focused;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setFocused(@Nullable final GuiEventListener focused) {
        final GuiEventListener oldFocus = this.focused;
        if(oldFocus != focused && oldFocus instanceof ContainerEventHandler oldFocusContainer) {
            oldFocusContainer.setFocused(null);
        }
        if(this.focused != null) {
            this.focused.setFocused(false);
        }
        if(focused != null) {
            focused.setFocused(true);
        }
        this.focused = (T)focused;

        final int index = children.indexOf(this.focused);
        if(index >= 0) {
            setSelected(children.get(index));
        }
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(final FocusNavigationEvent navigationEvent) {
        // Empty. Tab focusing is disabled.
        return null;
    }








    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        return ContainerEventHandler.super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(final MouseButtonEvent event) {
        super.mouseReleased(event);
        return ContainerEventHandler.super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(final MouseButtonEvent event, final double dx, final double dy) {
        super.mouseDragged(event, dx, dy);
        return ContainerEventHandler.super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean isFocused() {
        return ContainerEventHandler.super.isFocused();
    }

    @Override
    public void setFocused(final boolean focused) {
        ContainerEventHandler.super.setFocused(focused);
    }

    @Override
    public boolean keyPressed(final KeyEvent event) {
        boolean r = false;
        for(final @NotNull T c : children) {
            if(c.keyPressed(event)) r = true;
        }
        return r;
    }

    @Override
    public boolean charTyped(final CharacterEvent event) {
        boolean r = false;
        for(final @NotNull T c : children) {
            if(c.charTyped(event)) r = true;
        }
        return r;
    }
}