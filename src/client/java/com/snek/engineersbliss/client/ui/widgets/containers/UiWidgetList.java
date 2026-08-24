package com.snek.engineersbliss.client.ui.widgets.containers;


import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.ui.widgets.base.UiWidgetBase;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.cursor.CursorTypes;








/**
 * A scrollable vertical list capable of containing other widgets.
 */

public class UiWidgetList extends __base_UiContainer<UiWidgetList.Entry> {


    private final Minecraft minecraft;
    private final int defaultEntryHeight;
    private final float rowMargin;

    private double scrollAmount;
    private boolean scrolling;
    private final int scrollRateBase;




    public UiWidgetList(final Screen screen, final int itemHeight) {
        this(screen, itemHeight, 0f);
    }
    public UiWidgetList(final Screen screen, final int itemHeight, final float rowMargin) {
        super(screen, new UiTxt(CommonComponents.EMPTY));
        this.minecraft = Minecraft.getInstance();
        this.defaultEntryHeight = itemHeight;
        this.scrollRateBase = itemHeight;
        this.rowMargin = rowMargin;
    }








    private void repositionEntries() {
        int y = getY() - (int)scrollAmount();
        for(final @NotNull Entry child : children) {
            child.setY(y);
            y += child.getHeight();
            child.setX(getRowLeft());
            child.setWidth(getRowWidth());
        }
        relayoutContent();
    }

    @Override
    public void relayoutSelf() {
        //! Nothing to call from parent classes, this is the first implementation

        repositionEntries();
        if(getSelected() != null) {
            scrollToEntry(getSelected()); //! This calls setScrollAmount -> layoutWidgets on entries.
        }
        this.refreshScrollAmount();

        //! This is required to reposition the entries and their children in case recalculating the main layout made them go out of scroll bounds.
        repositionEntries();
    }

    public int getNextY() {
        int y = getY() - (int) scrollAmount();
        for(final Entry child : children) {
            y += child.getHeight();
        }
        return y;
    }

    protected int contentHeight() {
        int totalHeight = 0;
        for(final Entry child : children) {
            totalHeight += child.getHeight();
        }
        return totalHeight + 4;
    }

    public int getRowLeft() {
        return getX() + (int)(this.width * rowMargin);
    }

    public int getRowRight() {
        return getRowLeft() + getRowWidth();
    }

    public int getRowWidth() {
        final int marginPx = (int)(width * rowMargin);
        final int scrollbarEncroachment = Math.max(0, scrollbarWidth() - marginPx);
        return this.width - marginPx * 2 - scrollbarEncroachment;
    }

    public int getRowTop(final int row) {
        return children.get(row).getY();
    }

    public int getRowBottom(final int row) {
        final Entry child = children.get(row);
        return child.getY() + child.getHeight();
    }

    public int scrollbarWidth() {
        return 2;
    }

    protected int scrollBarX() {
        return getRowRight();
    }













    protected void clearEntries() {
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

    public int maxScrollAmount() {
        return Math.max(0, contentHeight() - height);
    }

    protected boolean scrollable() {
        return maxScrollAmount() > 0;
    }

    public boolean updateScrolling(final MouseButtonEvent event) {
        scrolling = scrollable() && isValidClickButton(event.buttonInfo()) && isOverScrollbar(event.x(), event.y());
        return scrolling;
    }

    protected boolean isOverScrollbar(final double x, final double y) {
        return x >= scrollBarX() && x <= scrollBarX() + scrollbarWidth() && y >= getY() && y < getBottom();
    }

    protected int scrollerHeight() {
        return Mth.clamp((int) ((float) (height * height) / contentHeight()), 32, height - 8);
    }

    public int scrollBarY() {
        return maxScrollAmount() == 0
            ? getY()
            : Math.max(getY(), (int) scrollAmount * (height - scrollerHeight()) / maxScrollAmount() + getY())
        ;
    }

    protected double scrollRate() {
        return scrollRateBase;
    }

    private void scroll(final int amount) {
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
            if(event.y() < getY()) {
                setScrollAmount(0.0);
            }
            else if(event.y() > getBottom()) {
                setScrollAmount(maxScrollAmount());
            }
            else {
                final double max = Math.max(1, maxScrollAmount());
                final int barHeight = scrollerHeight();
                final double yDragScale = Math.max(1.0, max / (height - barHeight));
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
    protected int __internal_addWidget(final Entry entry, final int height) {
        entry.parentList = this;
        entry.setX(getRowLeft());
        entry.setWidth(getRowWidth());
        entry.setY(getNextY());
        entry.setHeight(height);
        final int r = super.addChild(entry);
        repositionEntries();
        return r;
    }
    public void addWidget(final AbstractWidget widget) {
        __internal_addWidget(new Entry(widget));
    }
    public void addWidget(final AbstractWidget widget, final int height) {
        __internal_addWidget(new Entry(widget), height);
    }


    public void addWidgetAndSpacer(final AbstractWidget widget, final int marginBottom) {
        __internal_addWidget(new Entry(widget));
        addWidget(new UiSpacer(), marginBottom);
    }
    public void addWidgetAndSpacer(final AbstractWidget widget, final int height, final int marginBottom) {
        __internal_addWidget(new Entry(widget), height);
        addWidget(new UiSpacer(), marginBottom);
    }


    public void addWidgetAndSpacers(final AbstractWidget widget, final int marginTop, final int marginBottom) {
        addWidget(new UiSpacer(), marginTop);
        addWidgetAndSpacer(widget, marginBottom);
    }
    public void addWidgetAndSpacers(final AbstractWidget widget, final int height, final int marginTop, final int marginBottom) {
        addWidget(new UiSpacer(), marginTop);
        addWidgetAndSpacer(widget, height, marginBottom);
    }




    @Override
    protected void onSelected(final Entry selectedEntry) {
        final boolean topClipped = selectedEntry.getContentY() < getY();
        final boolean bottomClipped = selectedEntry.getContentBottom() > getBottom();
        if(minecraft.getLastInputType().isKeyboard() || topClipped || bottomClipped) {
            scrollToEntry(selectedEntry);
        }
    }

    protected void scrollToEntry(final Entry entry) {
        final int topDelta = entry.getY() - getY() - 2;
        if(topDelta < 0) {
            scroll(topDelta);
        }
        final int bottomDelta = getBottom() - entry.getY() - entry.getHeight() - 2;
        if(bottomDelta < 0) {
            scroll(-bottomDelta);
        }
    }

    protected void centerScrollOn(final Entry entry) {
        int y = 0;
        for(final Entry child : children) {
            if(child == entry) {
                y += child.getHeight() / 2;
                break;
            }
            y += child.getHeight();
        }
        setScrollAmount(y - height / 2.0);
    }

    //! Let clicks through if they don't hit a sub element.
    @Override
    public boolean isMouseOver(final double mouseX, final double mouseY) {
        if(super.isMouseOver(mouseX, mouseY)) {
            if(isOverScrollbar(mouseX, mouseY)) return true;
            else for(final var c : children) {
                if(c.isMouseOver(mouseX, mouseY)) return true;
            }
        }
        return false;
    }





    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        extractBackground(graphics, mouseX, mouseY, a);

        hovered = (Entry)getChildAt(mouseX, mouseY).orElseGet(() -> null);
        graphics.enableScissor(getX(), getY(), getRight(), getBottom());
        for(final @NotNull Entry child : children) {
            if(child.getY() + child.getHeight() >= getY() && child.getY() <= getBottom()) {
                child.extractContent(graphics, mouseX, mouseY, Objects.equals(hovered, child), a);
            }
        }
        graphics.disableScissor();

        extractListSeparators(graphics);
        extractScrollbar(graphics, mouseX, mouseY);
    }


    protected void extractListSeparators(final GuiGraphicsExtractor graphics) {
        final Identifier headerSeparator = minecraft.level == null ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
        final Identifier footerSeparator = minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
        graphics.blit(RenderPipelines.GUI_TEXTURED, headerSeparator, getX(), getY() - 2, 0.0F, 0.0F, getWidth(), 2, 32, 2);
        graphics.blit(RenderPipelines.GUI_TEXTURED, footerSeparator, getX(), getBottom(), 0.0F, 0.0F, getWidth(), 2, 32, 2);
    }


    protected void extractScrollbar(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY) {
        final int scrollBarX = scrollBarX();
        final int scrollerHeight = scrollerHeight();
        final int scrollerY = scrollBarY();
        final int barWidth = scrollbarWidth();

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













    public static class Entry implements LayoutElement, GuiEventListener, UiWidgetBase {
        public static final int CONTENT_PADDING = 2;

        private int x = 0;
        private int y = 0;
        private int width = 0;
        private int height;

        @Override public int getX() { return x; }
        @Override public int getY() { return y; }
        @Override public int getWidth() { return width; }
        @Override public int getHeight() { return height; }


        private UiWidgetList parentList;
        private final @Nullable List<AbstractWidget> children;
        private final AbstractWidget widget;




        //! For subclasses that manage their own content
        protected Entry() {
            this.widget = null;
            this.children = null;
        }
        public Entry(final AbstractWidget widget) {
            this.widget = widget;
            this.children = List.of(widget);
        }




        @Override
        public @Nullable List<?> children() {
            return children;
        }
        @Override
        public void relayoutSelf() {
            widget.setSize(getWidth(), getHeight());
            widget.setPosition(getX(), getY());
        }
        @Override
        public Screen getScreen() {
            return parentList.getScreen();
        }




        public @Nullable AbstractWidget getWidget() {
            return widget;
        }

        @Override
        public void setFocused(final boolean focused) {
            // Empty
        }

        @Override
        public boolean isFocused() {
            return parentList.getFocused() == this;
        }

        public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
            widget.extractRenderState(graphics, mouseX, mouseY, a);
        }

        @Override
        public boolean isMouseOver(final double mx, final double my) {
            return getRectangle().containsPoint((int) mx, (int) my);
        }

        @Override
        public void setX(final int newX) {
            x = newX;
        }

        @Override
        public void setY(final int newY) {
            y = newY;
        }

        public void setWidth(final int newWidth) {
            width = newWidth;
        }

        public void setHeight(final int newHeight) {
            height = newHeight;
        }

        public int getContentX() {
            return getX() + CONTENT_PADDING;
        }

        public int getContentY() {
            return getY() + CONTENT_PADDING;
        }

        public int getContentHeight() {
            return getHeight() - CONTENT_PADDING * 2;
        }

        public int getContentYMiddle() {
            return getContentY() + getContentHeight() / 2;
        }

        public int getContentBottom() {
            return getContentY() + getContentHeight();
        }

        public int getContentWidth() {
            return getWidth() - CONTENT_PADDING * 2;
        }

        public int getContentXMiddle() {
            return getContentX() + getContentWidth() / 2;
        }

        public int getContentRight() {
            return getContentX() + getContentWidth();
        }

        @Override
        public void visitWidgets(final java.util.function.Consumer<AbstractWidget> widgetVisitor) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ScreenRectangle getRectangle() {
            return LayoutElement.super.getRectangle();
        }




        @Override
        public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
            return widget != null && widget.mouseClicked(event, doubleClick);
        }

        @Override
        public boolean mouseReleased(final MouseButtonEvent event) {
            return widget != null && widget.mouseReleased(event);
        }

        @Override
        public boolean mouseDragged(final MouseButtonEvent event, final double dx, final double dy) {
            return widget != null && widget.mouseDragged(event, dx, dy);
        }

        @Override
        public boolean mouseScrolled(final double x, final double y, final double scrollX, final double scrollY) {
            return widget != null && widget.mouseScrolled(x, y, scrollX, scrollY);
        }

        @Override
        public boolean keyPressed(final KeyEvent event) {
            return widget != null && widget.keyPressed(event);
        }

        @Override
        public boolean keyReleased(final KeyEvent event) {
            return widget != null && widget.keyReleased(event);
        }

        @Override
        public boolean charTyped(final CharacterEvent event) {
            return widget != null && widget.charTyped(event);
        }
    }





    @Override
    protected void updateWidgetNarration(final NarrationElementOutput output) {
        // Empty
    }
}