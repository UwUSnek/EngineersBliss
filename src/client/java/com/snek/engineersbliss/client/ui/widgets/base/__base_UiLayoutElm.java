package com.snek.engineersbliss.client.ui.widgets.base;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;

import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;








public abstract class __base_UiLayoutElm implements LayoutElement, Renderable, GuiEventListener, NarratableEntry {

    // Basic data
    protected float width;
    protected float height;
    private float x;
    private float y;
    public boolean active = true;
    public boolean visible = true;
    protected float alpha = 1.0F;
    private int tabOrderGroup;

    // Basic data - getters
    @Override public final int getHeight() { return (int)getHeightF(); }
    @Override public final int  getWidth() { return (int)getWidthF(); }
    @Override public final int      getX() { return (int)getXF(); }
    @Override public final int      getY() { return (int)getYF(); }
    public float      getHeightF() { return scaleHeightWithGui() ? getGuiScale() * height : height; }
    public float       getWidthF() { return  scaleWidthWithGui() ? getGuiScale() * width  : width;  }
    public float           getXF() { return x; }
    public float           getYF() { return y; }
    public float        getRight() { return getXF() + getWidthF(); }
    public float       getBottom() { return getYF() + getHeightF(); }
    public float  getWidthCenter() { return getXF() + getWidthF() / 2; }
    public float getHeightCenter() { return getYF() + getHeightF() / 2; }
    public boolean scaleWidthWithGui() { return false; }
    public boolean scaleHeightWithGui() { return false; }

    // Basic data - setters
    @Override public void setX(final int x) { setXF(x); }
    @Override public void setY(final int y) { setYF(y); }
    public void  setWidth(final float  width) { this.width  = width; }
    public void setHeight(final float height) { this.height = height; }
    public void     setXF(final float      x) { this.x      = x; }
    public void     setYF(final float      y) { this.y      = y; }

    // Basic data - more setters
    public void setSize(final float width, final float height) {
        setWidth(width);
        setHeight(height);
    }
    public void setPos(final float x, final float y) {
        setXF(x);
        setYF(y);
    }


    // Input handling
    private boolean focused;
    private boolean isHovered = false;
    private boolean dragged;
    private boolean wasHovered = false;


    // Screen reference
    private final Screen screen;
    public Screen getScreen() { return screen; }
    public boolean isGuiScaleTransitioning() {
        return (screen instanceof @NotNull __base_UiScreen uiScreen) && uiScreen.isGuiScaleTransitioning();
    }
    public float getGuiScale() {
        if(getScreen() instanceof __base_UiScreen uiScreen) {
            return uiScreen.getGuiScale();
        }
        else {
            return 1f;
        }
    }


    // Relayout handling
    private static boolean relayoutDisabled = false;
    public static void    disableRelayout() { relayoutDisabled = true;  }
    public static void     enableRelayout() { relayoutDisabled = false; }
    public static boolean isRelayoutDisabled() { return relayoutDisabled; }








    protected __base_UiLayoutElm(final Screen screen) {
        this.screen = screen;
        this.x = 50;
        this.y = 50;
        this.width = 50;
        this.height = 50;
        this.dragged = false;
    }
    public abstract @NotNull List<?> children();








    // Input handling

    public void onClick(final MouseButtonEvent event, final boolean doubleClick) {
        // Empty by default
    }
    public void onRelease(final MouseButtonEvent event) {
        // Empty by default
     }
    protected void onDrag(final MouseButtonEvent event, final double dx, final double dy) {
        // Empty by default
    }
    protected boolean isLeftClick(final MouseButtonEvent event) {
        return event.button() == 0;
    }
    protected boolean isRightClick(final MouseButtonEvent event) {
        return event.button() == 1;
    }

    @Override
    public boolean mouseDragged(final MouseButtonEvent event, final double dx, final double dy) {
        if(isActive() && isLeftClick(event)) {
            onDrag(event, dx, dy);
            return true;
        }
        else {
            return false;
        }
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(final FocusNavigationEvent navigationEvent) {
        // Disable tab cycling
        return null;
    }

    //! Override used by the screen and Vanilla.
    //! This class and its users should call .isHovered()
    @Override
    public boolean isMouseOver(final double mouseX, final double mouseY) {
        return this.isActive() && this.areCoordinatesInRectangle(mouseX, mouseY);
    }

    public void setAlpha(final float newAlpha) { alpha = newAlpha; }
    public float getAlpha() { return alpha; }

    @Override
    public boolean isFocused() { return focused; }
    public boolean isHoveredOrFocused() { return isHovered() || isFocused(); }

    @Override
    public boolean isActive() {
        return visible && active;
    }
    @Override
    public void setFocused(final boolean newFocused) {
        focused = newFocused;
    }


    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if(isActive() && isLeftClick(event) && isHovered()) {
            onClick(event, doubleClick);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if(isActive() && isLeftClick(event)) {
            onRelease(event);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isBeingDragged() {
        return isActive() && dragged;
    }

    public boolean isHoveredOrBeingDragged() {
        return isBeingDragged() || isHovered();
    }

    public boolean isHovered() {
        return isActive() && isHovered;
    }


    protected void onHoverStart() {
        // Empty by default
    }
    protected void onHoverEnd() {
        // Empty by default
    }
    private void checkHoverTransition() {
        boolean hovered = isHoveredOrBeingDragged();
        if(hovered && !wasHovered) onHoverStart();
        if(!hovered && wasHovered) onHoverEnd();
        wasHovered = hovered;
    }








    // Rendering and layout

    public abstract void relayoutSelf();

    public void relayoutContent() {
        if(!isRelayoutDisabled()) for(final var e : children()) {
            if(e instanceof final @NotNull __base_UiLayoutElm w) {
                w.relayout();
            }
        }
    }

    public void relayout() {
        if(!isRelayoutDisabled()) {
            relayoutSelf();
            relayoutContent();
        }
    }


    @Override
    public final void extractRenderState(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, final float a) {
        // Empty.
        //! Block Vanilla's extractRenderState so the __base_UiScreen can call extractWidgetRenderState directly using its UiGraphics.
    }
    public void extractWidgetRenderState(UiGraphics graphics, float mouseX, float mouseY, float a) {
        dragged = ((__base_UiScreen)getScreen()).getDraggedElm() == this;
        isHovered = ((__base_UiScreen)getScreen()).getHoveredElm() == this;
        checkHoverTransition();

        // Handle cursor
        if(isHoveredOrBeingDragged()) {
            if(!isActive()) graphics.requestCursor(CursorTypes.NOT_ALLOWED);
            else            graphics.requestCursor(selectCursor(graphics));
        }
    }


    /**
     * This function lets widgets change the displayed cursor sprite.
     * This is only called on active widgets that are being hovered or dragged.
     * @return The chosen cursor type.
     */
    protected CursorType selectCursor(UiGraphics graphics) {
        return CursorTypes.ARROW;
    }










    // Narration and stuff

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public final void updateNarration(final NarrationElementOutput output) {
        // Empty
    }

    @Override
    public void visitWidgets(final Consumer<AbstractWidget> widgetVisitor) {
        //  Empty
        //TODO idk what this does but it wants an AbstractWidget
    }

    @Override
    public ScreenRectangle getRectangle() {
        return LayoutElement.super.getRectangle();
    }

    private boolean areCoordinatesInRectangle(final double x, final double y) {
        return x >= getXF() && y >= getYF() && x < getRight() && y < getBottom();
    }

    @Override
    public int getTabOrderGroup() {
        return this.tabOrderGroup;
    }

    public void setTabOrderGroup(final int tabOrderGroup) {
        this.tabOrderGroup = tabOrderGroup;
    }
}
