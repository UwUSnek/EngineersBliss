package com.snek.engineersbliss.client.ui.widgets.containers;


import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;

import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.data_types.UiSize;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedFloat;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Easings;

import java.util.Optional;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.cursor.CursorTypes;








/**
 * A scrollable vertical list capable of containing other widgets.
 */

public class UiWidgetList extends __base_UiContainer<UiWidgetList.Entry> {


    private final UiSize scrollbarWidth;
    private final float defaultEntryHeight;
    private final float rowMargin;

    private boolean isScrollable;
    private float scrollAmount;
    private final AnimatedFloat animatedScrollAmount;
    private float lastGuiScale;
    private boolean scrolling;
    private int lockedRows;




    public UiWidgetList(final UiScreen screen, final float defaultEntryHeight) {
        this(screen, defaultEntryHeight, 0f);
    }
    public UiWidgetList(final UiScreen screen, final float defaultEntryHeight, final float rowMargin) {
        super(screen, new UiTxt(CommonComponents.EMPTY));
        setBgColor(Layout.bgColor);
        this.scrollbarWidth = new UiSize(this); scrollbarWidth.setPx(2);
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
    }

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
        if(!isRelayoutDisabled()) {
            tickGuiScale();
            this.refreshScrollAmount();
        }
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
        final float scrollbarEncroachment = Math.max(0, scrollbarWidth.getPx() - marginPx);
        return getWidthF() - marginPx * 2 - scrollbarEncroachment;
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
        return x >= scrollBarX() && x <= scrollBarX() + scrollbarWidth.getPx() && y >= scrollTrackY() && y < getBottom();
    }

    protected float scrollTrackY() {
        return getYF() + calcLockedHeight();
    }

    protected float scrollTrackHeight() {
        return height - calcLockedHeight();
    }

    protected float scrollerHeight() {
        return Mth.clamp(scrollTrackHeight() * scrollTrackHeight() / contentHeight(), 32, scrollTrackHeight() - 8); //FIXME replace 32 and 8 magic numbers
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
        entry.setHeight(height);
        final int r = super.addChild(entry);
        relayoutContent();
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
        __internal_addWidget(new SpacerEntry(getScreen()), marginBottom);
    }
    public void addWidgetAndSpacer(final __base_UiLayoutElm widget, final float height, final float marginBottom) {
        __internal_addWidget(new Entry(getScreen(), widget), height);
        __internal_addWidget(new SpacerEntry(getScreen()), marginBottom);
    }


    public void addWidgetAndSpacers(final __base_UiLayoutElm widget, final float marginTop, final float marginBottom) {
        __internal_addWidget(new SpacerEntry(getScreen()), marginTop);
        addWidgetAndSpacer(widget, marginBottom);
    }
    public void addWidgetAndSpacers(final __base_UiLayoutElm widget, final float height, final float marginTop, final float marginBottom) {
        __internal_addWidget(new SpacerEntry(getScreen()), marginTop);
        addWidgetAndSpacer(widget, height, marginBottom);
    }


    public void addSpacer(final float height) {
        __internal_addWidget(new SpacerEntry(getScreen()), height);
    }

    /**
     * Returns the first widget that matches the condition specified by the provided predicate.
     * @param predicate The predicate used to check each widget.
     * @return The first widget that makes the predicate return true, or an empty optional otherwise.
     */
    public Optional<__base_UiLayoutElm> getFirstWidgetMatching(final Predicate<__base_UiLayoutElm> predicate) {
        for(final Entry entry : children()) {
            final __base_UiLayoutElm widget = entry.getWidget();
            if(predicate.test(widget)) return Optional.of(widget);
        }
        return Optional.empty();
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


    @Override
    public void extractContent(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        //! Skip default extractContent. This class handles draw recursion on its own.


        // Draw unlocked entries
        final float outOfBoundsY = getScreen().height + 9999f;
        final float lockedBottom = getYF() + calcLockedHeight();
        float entryY = lockedBottom - animatedScrollAmount.compute();
        float selfBottom = getBottom();
        graphics.enableScissor(getX(), Math.round(lockedBottom), Math.round(getRight()) + 1, Math.round(getBottom()) + 1);
        for(int i = lockedRows; i < children.size(); ++i) {
            final @NotNull Entry child = children.get(i);
            final float oldEntryY = child.getYF();
            final boolean oldPosInBounds = oldEntryY != outOfBoundsY;
            final boolean newPosInBounds = entryY < selfBottom && entryY + child.getHeightF() > lockedBottom;

            //! Optimize relayout to run only when the y is actually updated.
            //! Stash out of bounds elements in a single place under the screen so they don't interfere with input detection.
            if(oldEntryY != entryY) {
                if(newPosInBounds) {
                    child.setYF(entryY);
                    child.relayout();
                }
                else if(oldPosInBounds) {
                    child.setYF(outOfBoundsY);
                    child.relayout();
                }
            }

            //! Only draw the element if the new position is not ouf of bounds
            if(newPosInBounds) {
                child.extract(graphics, mouseX, mouseY, a);
            }
            entryY += child.getHeightF();
        }
        graphics.disableScissor();


        // Draw locked entries
        int i = 0;
        graphics.enableScissor(getX(), getY(), Math.round(getRight()) + 1, Math.round(getBottom()) + 1);
        for(final @NotNull Entry child : children) {
            if(i < lockedRows && elmIsInBounds(child)) {
                child.extract(graphics, mouseX, mouseY, a);
            }
            i++;
        }
        graphics.disableScissor();


        // Draw locked entries shadow
        if(lockedRows > 0) {
            extractShadowBottom(graphics, getRowLeft(), getRowRight(), lockedBottom, Layout.shadowSizePx, Layout.shadowColor, 1f);
        }
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
        final float barWidth       = scrollbarWidth.getPx();

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
        protected Entry(final UiScreen screen) {
            super(screen);
            setBgColor(0x0);
            this.widget = null;
        }
        public Entry(final UiScreen screen, final __base_UiLayoutElm widget) {
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



    public static class SpacerEntry extends Entry {
        @Override public boolean scaleHeightWithGui() {
            return false;
        }
        protected SpacerEntry(final UiScreen screen) {
            super(screen);
        }
        @Override
        public void relayoutSelf() {
            // Empty
        }
        @Override
        public boolean isFocused() {
            return false;
        }
    }
}