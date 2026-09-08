package com.snek.engineersbliss.client.ui.widgets.containers;


import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;

import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedFloat;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Easings;
import com.snek.engineersbliss.utils.data_types.Pair;

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
    private float scrollAmount;
    private final AnimatedFloat animatedScrollAmount;
    private float lastGuiScale;
    private boolean scrolling;
    private int lockedRows;




    public UiWidgetList(final Screen screen, final float defaultEntryHeight) {
        this(screen, defaultEntryHeight, 0f);
    }
    public UiWidgetList(final Screen screen, final float defaultEntryHeight, final float rowMargin) {
        super(screen, new UiTxt(CommonComponents.EMPTY));
        setBgColor(Layout.bgColor);
        this.isScrollable = true;
        this.scrollAmount = 0f;
        this.animatedScrollAmount = new AnimatedFloat(scrollAmount, 80, Easings.quadInOut);
        this.lastGuiScale = -1f;
        this.scrolling = false;
        this.lockedRows = 0;
        this.defaultEntryHeight = defaultEntryHeight;
        this.rowMargin = rowMargin;
    }









    public void setLockedRows(final int newLockedRows) {
        lockedRows = newLockedRows;
        // repositionEntries(); //TODO remove
    }

    // private void repositionEntries() { //TODO remove
    //     if(!isRelayoutDisabled()) {
    //         // float y = getYF() - animatedScrollAmount.compute();
    //         // float lockedY = getYF();
    //         // int i = 0;
    //         for(final @NotNull Entry child : children) {
    //             // if(i < lockedRows) {
    //             //     child.setYF(lockedY);
    //             //     lockedY += child.getHeightF();
    //             // }
    //             // else {
    //             //     child.setYF(y);
    //             // }
    //             // y += child.getHeightF();
    //             // child.setXF(getRowLeft());
    //             child.setWidth(getRowWidth());
    //             // i++;
    //         }
    //         relayoutContent();
    //     }
    // }

    @Override
    public void relayoutContent() {
        if(!isRelayoutDisabled()) {
            float lockedEntryY = getYF();
            for(int i = 0; i < children.size(); ++i) {
                final @NotNull Entry child = children.get(i);
                child.setWidth(getRowWidth());
                child.setXF(getRowLeft());
                if(i < lockedRows) {
                    child.setYF(lockedEntryY);
                    lockedEntryY += child.getHeightF();
                }
                //! Y positioning of unlocked entries is done in the render loop for performance reasons.
                //! Only visible entries are moved and drawn.
            }
        }
        super.relayoutContent();
    }

    @Override
    public void relayoutSelf() {
    //     if(!isRelayoutDisabled()) {
    //         // repositionEntries(); //TODO remove
            // this.refreshScrollAmount();

    //         // // //! This is required to reposition the entries and their children in case recalculating the main layout made them go out of scroll bounds. //TODO remove
    //         // repositionEntries(); //TODO remove
    //     }
        if(!isRelayoutDisabled()) {
            tickGuiScale();
            this.refreshScrollAmount();
        }
    }


    public float getNextY() { //FIXME remove, new system doesnt need that.
        float y = getYF() - animatedScrollAmount.compute();
        for(final @NotNull Entry child : children) {
            y += child.getHeightF();
        }
        return y;
    }

    protected float contentHeight() {
        float totalHeight = 0;
        for(final @NotNull Entry child : children) {
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

    //FIXME replace with a UiSize member
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

    public void setScrollAmount(final double newScrollAmount) {
        setScrollAmount(newScrollAmount, false);
    }
    public void setScrollAmount(final double newScrollAmount, final boolean snap) {
        scrollAmount = (float)Mth.clamp(newScrollAmount, 0.0, maxScrollAmount());
        if(snap) animatedScrollAmount.snapTo            (scrollAmount);
        else     animatedScrollAmount.startNewTransition(scrollAmount);
        // repositionEntries(); //TODO remove
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
        return x >= scrollBarX() && x <= scrollBarX() + scrollbarWidth() && y >= scrollTrackY() && y < getBottom();
    }

    protected float scrollTrackY() {
        return getYF() + calcLockedHeight();
    }

    protected float scrollTrackHeight() {
        return height - calcLockedHeight();
    }

    protected float scrollerHeight() {
        return Mth.clamp(scrollTrackHeight() * scrollTrackHeight() / contentHeight(), 32, scrollTrackHeight() - 8);
    }

    public float scrollBarY() {
        return maxScrollAmount() == 0
            ? scrollTrackY()
            : Math.max(scrollTrackY(), animatedScrollAmount.compute() * (scrollTrackHeight() - scrollerHeight()) / maxScrollAmount() + scrollTrackY())
        ;
    }

    protected double scrollRate() {
        return defaultEntryHeight * getGuiScale();
    }

    private void scroll(final float amount) {
        setScrollAmount(scrollAmount + amount);
    }

    @Override
    public boolean mouseScrolled(final double mx, final double my, final double scrollX, final double scrollY) {
        if(!visible) {
            return false;
        }
        setScrollAmount(scrollAmount - scrollY * scrollRate());
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
                setScrollAmount(scrollAmount + dy * yDragScale);
            }
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public void onRelease(final MouseButtonEvent event) {
        super.onRelease(event);
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
        // repositionEntries();  //TODO remove
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
        float y = 0;
        for(final @NotNull Entry child : children) {
            if(child == entry) {
                y += child.getHeightF() / 2f;
                break;
            }
            y += child.getHeightF();
        }
        setScrollAmount(y - height / 2f);
    }








    private float calcLockedHeight() {
        float h = 0;
        int i = 0;
        for(final @NotNull Entry c : children) {
            if(i >= lockedRows) break;
            h += c.getHeightF();
            i++;
        }
        return h;
    }


    /**
     * Finds the first visible unlocked entry based on the current scroll amount.
     * @return A Pair containing the index and Y position of the element, or null if no unlocked element is visible.
     */
    public @Nullable Pair<Integer, Float> findFirstVisibleUnlockedEntry() {
        float y = getYF();
        float curScrollAmount = animatedScrollAmount.compute();
        float entryY = y - curScrollAmount;
        for(int i = lockedRows; i < children.size(); ++i) {
            final @NotNull Entry child = children.get(i);
            final float h = child.getHeightF();
            if(entryY + h > y) return Pair.from(i, entryY);
            entryY += h;
        }
        return null;
    }


    @Override
    public void extractContent(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        //! Skip default extractContent. This class handles draw recursion on its own.


    // repositionEntries(); //TODO remove & optimize  //TODO remove
        // Draw unlocked entries
        final @Nullable Pair<Integer, Float> firstVisible = findFirstVisibleUnlockedEntry();
        if(firstVisible != null) {
            final float lockedBottom = getYF() + calcLockedHeight();
            float entryY = firstVisible.getSecond();
            graphics.enableScissor(getX(), Math.round(lockedBottom), Math.round(getRight()) + 1, Math.round(getBottom()) + 1);
            for(int i = firstVisible.getFirst(); i < children.size(); ++i) {
                final @NotNull Entry child = children.get(i);
                final float oldEntryY = child.getYF();
                if(oldEntryY != entryY) { //! Optimize relayout to run only when the y is actually updated
                    child.setYF(entryY);
                    child.relayout();
                }
                if(!elmIsInBounds(child)) break; //! Stop rendering entries if the current entry's new Y position is out of bounds
                child.extractWidgetRenderState(graphics, mouseX, mouseY, a);
                entryY += child.getHeightF();
            }
            graphics.disableScissor();
        }


        // Draw locked entries
        int i = 0;
        graphics.enableScissor(getX(), getY(), Math.round(getRight()) + 1, Math.round(getBottom()) + 1);
        for(final @NotNull Entry child : children) {
            if(i < lockedRows && elmIsInBounds(child)) {
                child.extractWidgetRenderState(graphics, mouseX, mouseY, a);
            }
            i++;
        }
        graphics.disableScissor();
    }




    @Override
    public void extractSelf(UiGraphics graphics, float mouseX, float mouseY, float a) {
        super.extractSelf(graphics, mouseX, mouseY, a);
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

    private void tickGuiScale() {
        final float scale = getGuiScale();
        if(lastGuiScale < 0) {
            lastGuiScale = scale;
            return;
        }
        if(scale != lastGuiScale) {
            final float ratio = scale / lastGuiScale;
            setScrollAmount(scrollAmount * ratio, true);
            lastGuiScale = scale;
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