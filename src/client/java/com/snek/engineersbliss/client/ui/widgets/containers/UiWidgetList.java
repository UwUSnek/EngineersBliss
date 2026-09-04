package com.snek.engineersbliss.client.ui.widgets.containers;


import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;

import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.ui.UiGraphics;
import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.cursor.CursorTypes;








/**
 * A scrollable vertical list capable of containing other widgets.
 */

public class UiWidgetList extends __base_UiContainer<UiWidgetList.Entry> {


    private final float defaultEntryHeight;
    private final float rowMargin;

    private boolean isScrollable;
    private double scrollAmount;
    private boolean scrolling;




    public UiWidgetList(final Screen screen, final float defaultEntryHeight) {
        this(screen, defaultEntryHeight, 0f);
    }
    public UiWidgetList(final Screen screen, final float defaultEntryHeight, final float rowMargin) {
        super(screen, new UiTxt(CommonComponents.EMPTY));
        setBgColor(Layout.bgColor);
        this.isScrollable = true;
        this.defaultEntryHeight = defaultEntryHeight;
        this.rowMargin = rowMargin;
    }








    private void repositionEntries() {
        if(!isRelayoutDisabled()) {
            float y = getYF() - (float)scrollAmount();
            for(final @NotNull Entry child : children) {
                child.setYF(y);
                y += child.getHeightF();
                child.setXF(getRowLeft());
                child.setWidth(getRowWidth());
            }
            relayoutContent();
        }
    }

    @Override
    public void relayoutSelf() {
        if(!isRelayoutDisabled()) {
            repositionEntries();
            this.refreshScrollAmount();

            //! This is required to reposition the entries and their children in case recalculating the main layout made them go out of scroll bounds.
            repositionEntries();
        }
    }

    public float getNextY() {
        float y = getYF() - (float)scrollAmount();
        for(final Entry child : children) {
            y += child.getHeightF();
        }
        return y;
    }

    protected int contentHeight() {
        int totalHeight = 0;
        for(final Entry child : children) {
            totalHeight += child.getHeightF();
        }
        return totalHeight + 4;
    }

    public float getRowLeft() {
        return getXF() + getWidthF() * rowMargin;
    }

    public float getRowRight() {
        return getRowLeft() + getRowWidth();
    }

    public float getRowWidth() {
        final float marginPx = getWidthF() * rowMargin;
        final float scrollbarEncroachment = Math.max(0, scrollbarWidth() - marginPx);
        return getWidthF() - marginPx * 2 - scrollbarEncroachment;
    }

    public float getRowTop(final int row) {
        return children.get(row).getY();
    }

    public int getRowBottom(final int row) {
        final Entry child = children.get(row);
        return (int)(child.getYF() + child.getHeightF());
    }

    public int scrollbarWidth() {
        return 2;
    }

    protected float scrollBarX() {
        return getRowRight();
    }

    public void setIsScrollable(final boolean newIsScrollable) {
        isScrollable = newIsScrollable;
    }













    public void clearEntries() {
        super.clearChildren();
        setScrollAmount(0);
    }

    public double scrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(final double newScrollAmount) {
        scrollAmount = Mth.clamp(newScrollAmount, 0.0, maxScrollAmount());
        repositionEntries();
    }

    public void refreshScrollAmount() {
        setScrollAmount(scrollAmount);
    }

    public float maxScrollAmount() {
        return Math.max(0, contentHeight() - height);
    }

    protected boolean scrollable() {
        return isScrollable && maxScrollAmount() > 0;
    }

    public boolean updateScrolling(final MouseButtonEvent event) {
        scrolling = scrollable() && isLeftClick(event) && isOverScrollbar(event.x(), event.y());
        return scrolling;
    }

    protected boolean isOverScrollbar(final double x, final double y) {
        return x >= scrollBarX() && x <= scrollBarX() + scrollbarWidth() && y >= getYF() && y < getBottom();
    }

    protected float scrollerHeight() {
        return Mth.clamp((float)(height * height) / contentHeight(), 32, height - 8);
    }

    public float scrollBarY() {
        return maxScrollAmount() == 0
            ? getYF()
            : Math.max(getYF(), (float)scrollAmount * (height - scrollerHeight()) / maxScrollAmount() + getYF())
        ;
    }

    protected double scrollRate() {
        return defaultEntryHeight * SettingsFeatureHandler.getCurrentGuiScale();
    }

    private void scroll(final float amount) {
        setScrollAmount(scrollAmount() + amount);
    }

    @Override
    public boolean mouseScrolled(final double mx, final double my, final double scrollX, final double scrollY) {
        if(!visible) {
            return false;
        }
        setScrollAmount(scrollAmount() - scrollY * scrollRate());
        return true;
    }

    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        final boolean scrollClicked = updateScrolling(event);
        return super.mouseClicked(event, doubleClick) || scrollClicked;
    }

    @Override
    public boolean mouseDragged(final MouseButtonEvent event, final double dx, final double dy) {
        if(scrolling) {
            if(event.y() < getYF()) {
                setScrollAmount(0.0);
            }
            else if(event.y() > getBottom()) {
                setScrollAmount(maxScrollAmount());
            }
            else {
                final double max = Math.max(1, maxScrollAmount());
                final float barHeight = scrollerHeight();
                final double yDragScale = Math.max(1.0, max / (getHeightF() - barHeight));
                setScrollAmount(scrollAmount() + dy * yDragScale);
            }
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public void onRelease(final MouseButtonEvent event) {
        scrolling = false;
    }








    protected int __internal_addWidget(final Entry entry) {
        return __internal_addWidget(entry, defaultEntryHeight);
    }
    protected int __internal_addWidget(final Entry entry, final float height) {
        entry.parentList = this;
        entry.setXF(getRowLeft());
        entry.setWidth(getRowWidth());
        entry.setYF(getNextY());
        entry.setHeight(height);
        final int r = super.addChild(entry);
        repositionEntries();
        return r;
    }
    public void addWidget(final __base_UiLayoutElm widget) {
        __internal_addWidget(new Entry(getScreen(), widget));
    }
    public void addWidget(final __base_UiLayoutElm widget, final float height) {
        __internal_addWidget(new Entry(getScreen(), widget), height);
    }


    public void addWidgetAndSpacer(final __base_UiLayoutElm widget, final float marginBottom) {
        __internal_addWidget(new Entry(getScreen(), widget));
        addWidget(new UiSpacer(getScreen()), marginBottom);
    }
    public void addWidgetAndSpacer(final __base_UiLayoutElm widget, final float height, final float marginBottom) {
        __internal_addWidget(new Entry(getScreen(), widget), height);
        addWidget(new UiSpacer(getScreen()), marginBottom);
    }


    public void addWidgetAndSpacers(final __base_UiLayoutElm widget, final float marginTop, final float marginBottom) {
        addWidget(new UiSpacer(getScreen()), marginTop);
        addWidgetAndSpacer(widget, marginBottom);
    }
    public void addWidgetAndSpacers(final __base_UiLayoutElm widget, final float height, final float marginTop, final float marginBottom) {
        addWidget(new UiSpacer(getScreen()), marginTop);
        addWidgetAndSpacer(widget, height, marginBottom);
    }




    @Override
    protected void onSelected(final Entry selectedEntry) {
        final boolean topClipped    = selectedEntry.getYF()      < getYF();
        final boolean bottomClipped = selectedEntry.getBottom() > getBottom();
        if(Minecraft.getInstance().getLastInputType().isKeyboard() || topClipped || bottomClipped) {
            scrollToEntry(selectedEntry);
        }
    }

    protected void scrollToEntry(final Entry entry) {
        final float topDelta = entry.getYF() - getYF() - 2;
        if(topDelta < 0) {
            scroll(topDelta);
        }
        final float bottomDelta = getBottom() - entry.getYF() - entry.getHeightF() - 2;
        if(bottomDelta < 0) {
            scroll(-bottomDelta);
        }
    }

    protected void centerScrollOn(final Entry entry) {
        int y = 0;
        for(final Entry child : children) {
            if(child == entry) {
                y += child.getHeightF() / 2;
                break;
            }
            y += child.getHeightF();
        }
        setScrollAmount(y - height / 2.0);
    }





    @Override
    public void extractWidgetRenderState(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        graphics.enableScissor(getX(), getY(), (int)getRight(), (int)getBottom());
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        graphics.disableScissor();
        extractScrollbar(graphics, mouseX, mouseY);
    }


    protected void extractScrollbar(final UiGraphics graphics, final float mouseX, final float mouseY) {
        final float scrollBarX     = scrollBarX();
        final float scrollerHeight = scrollerHeight();
        final float scrollerY      = scrollBarY();
        final float barWidth       = scrollbarWidth();

        // If there are hidden elements
        if(scrollable()) {

            // Draw handle
            final boolean hoveredBar = isOverScrollbar(mouseX, mouseY);
            final int handleColor = hoveredBar ? Layout.handleColorActive : Layout.handleColor;
            graphics.fill(scrollBarX, scrollerY, scrollBarX + barWidth, scrollerY + scrollerHeight, handleColor);
            if(hoveredBar) {
                graphics.requestCursor(scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
                graphics.fill(scrollBarX, scrollerY, scrollBarX + barWidth, scrollerY + scrollerHeight, Layout.highlightOverlay);
            }
        }
    }













    public static class Entry extends __base_UiContainer implements GuiEventListener {
        private UiWidgetList parentList;
        private final __base_UiLayoutElm widget;
        @Override public boolean scaleHeightWithGui() {
            return true;
        }



        //! For subclasses that manage their own content
        protected Entry(final Screen screen) {
            super(screen);
            setBgColor(0x0);
            this.widget = null;
        }
        public Entry(final Screen screen, final __base_UiLayoutElm widget) {
            super(screen);
            setBgColor(0x0);
            this.widget = widget;
            addChild(widget);
        }




        @Override
        public void relayoutSelf() {
            widget.setSize(getWidthF(), getHeightF());
            widget.setPos(getXF(), getYF());
        }




        public @Nullable __base_UiLayoutElm getWidget() {
            return widget;
        }

        @Override
        public void setFocused(final boolean focused) {
            // Empty. Entry elements cannot be focused. Focus state is forwarded to the containe widget.
        }

        @Override
        public boolean isFocused() {
            return parentList.getFocused() == this;
        }
    }
}