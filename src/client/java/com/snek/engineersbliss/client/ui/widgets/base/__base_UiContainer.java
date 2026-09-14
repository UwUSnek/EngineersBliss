package com.snek.engineersbliss.client.ui.widgets.base;



import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.utils.UiTxt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;








/**
 * Base class for widgets that act as containers holding a list of children of type T.
 * This doesn't provide scrolling capabilities.
 */

public abstract class __base_UiContainer<T extends GuiEventListener> extends __base_UiWidget implements ContainerEventHandler {
    protected final List<T> children = new ArrayList<>();



    protected __base_UiContainer(final UiScreen screen) {
        super(screen);
    }
    protected __base_UiContainer(final UiScreen screen, final UiTxt label) {
        super(screen, label);
    }




    //! Recursive relayout is handled by __base_UiLayoutElm
    //! Recursive rendering is handled by __base_UiLayoutElm







    @Override
    public final @NotNull List<T> children() {
        return children;
    }

    protected void clearChildren() {
        children.clear();
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

    /**
     * Returns the first child that matches the condition specified by the provided predicate.
     * @param predicate The predicate used to check each element.
     * @return The first child that makes the predicate return true, or an empty optional otherwise.
     */
    public Optional<T> getFirstChildMatching(final Predicate<T> predicate) {
        for(final T c : children()) {
            if(predicate.test(c)) return Optional.of(c);
        }
        return Optional.empty();
    }








    //! Hook for subclasses that need to react to a new selection.
    protected void onSelected(final T selected) {
        // Empty
    }


    //! Abstract stuff
    private boolean __isDragging = false;
    private GuiEventListener __focused = null;
    @Override
    public boolean isDragging() {
        return __isDragging;
    }
    @Override
    public void setDragging(boolean dragging) {
        __isDragging = dragging;
    }
    @Override
    public GuiEventListener getFocused() {
        return __focused;
    }
    @Override
    public void setFocused(GuiEventListener focused) {
        __focused = focused;
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
}