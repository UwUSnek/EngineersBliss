package com.snek.engineersbliss.client.ui.widgets.base;



import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.gui.components.AbstractWidget;

import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
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




    protected __base_UiContainer(final Screen screen) {
        super(screen);
    }
    protected __base_UiContainer(final Screen screen, final UiTxt label) {
        super(screen, label);
    }







    @Override
    public final @NotNull List<T> children() {
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

    public @Nullable T getHoveredChild() {
        return hovered;
    }







    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        // Update hovered widget reference
        hovered = isMouseOver(mouseX, mouseY) ? (T)getChildAt(mouseX, mouseY).orElse(null) : null;

        // Normal rendering
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);

        // Render children recursively
        for(final var child : children()) {
            if(child instanceof @NotNull AbstractWidget w) {
                if(w.getY() + w.getHeight() >= getY() && w.getY() <= getBottom()) {
                    w.extractRenderState(graphics, mouseX, mouseY, a);
                }
            }
        }
    }








    public @Nullable T getSelected() {
        return selected;
    }

    public void setSelected(@Nullable final T _selected) {
        selected = _selected;
        if(_selected != null) {
            onSelected(_selected);
        }
    }

    //! Hook for subclasses that need to react to a new selection.
    protected void onSelected(final T selected) {
        // Empty
    }








    @Override
    public final boolean isDragging() {
        return isDragging;
    }

    @Override
    public final void setDragging(final boolean dragging) {
        isDragging = dragging;
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return focused;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setFocused(@Nullable final GuiEventListener _focused) {
        final GuiEventListener oldFocus = focused;
        if(oldFocus != _focused && oldFocus instanceof ContainerEventHandler oldFocusContainer) {
            oldFocusContainer.setFocused(null);
        }
        if(focused != null) {
            focused.setFocused(false);
        }
        if(_focused != null) {
            _focused.setFocused(true);
        }
        focused = (T)_focused;

        final int index = children.indexOf(focused);
        if(index >= 0) {
            setSelected(children.get(index));
        }
    }








    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        super.mouseClicked(event, doubleClick);
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

    @Override
    public void playDownSound(final SoundManager soundManager) {
        // Empty. Stop containers from playing the click sound.
    }
}