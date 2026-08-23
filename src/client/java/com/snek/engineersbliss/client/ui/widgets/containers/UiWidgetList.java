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
    }

    @Override
    public void layoutWidgets() {
        repositionEntries();
        if(getSelected() != null) {
            scrollToEntry(getSelected()); //! This calls setScrollAmount -> layoutWidgets on entries.
        }
        this.refreshScrollAmount();

        //! This is required to reposition the entries and their children in case recalculating the main layout made them go out of scroll bounds.
        repositionEntries();
        super.layoutWidgets();
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













    @Override
    protected void clearEntries() {
        super.clearEntries();
        setScrollAmount(0);
    }

    public double scrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(final double scrollAmount) {
        this.scrollAmount = Mth.clamp(scrollAmount, 0.0, this.maxScrollAmount());
        repositionEntries();
        super.layoutWidgets(); //! This is fine. Caller is expected to not call setScrollAmount() more than once at a time.
    }

    public void refreshScrollAmount() {
        this.setScrollAmount(this.scrollAmount);
    }

    public int maxScrollAmount() {
        return Math.max(0, this.contentHeight() - this.height);
    }

    protected boolean scrollable() {
        return this.maxScrollAmount() > 0;
    }

    public boolean updateScrolling(final MouseButtonEvent event) {
        this.scrolling = this.scrollable() && this.isValidClickButton(event.buttonInfo()) && this.isOverScrollbar(event.x(), event.y());
        return this.scrolling;
    }

    protected boolean isOverScrollbar(final double x, final double y) {
        return x >= this.scrollBarX() && x <= this.scrollBarX() + this.scrollbarWidth() && y >= this.getY() && y < this.getBottom();
    }

    protected int scrollerHeight() {
        return Mth.clamp((int) ((float) (this.height * this.height) / this.contentHeight()), 32, this.height - 8);
    }

    public int scrollBarY() {
        return this.maxScrollAmount() == 0
            ? this.getY()
            : Math.max(this.getY(), (int) this.scrollAmount * (this.height - this.scrollerHeight()) / this.maxScrollAmount() + this.getY())
        ;
    }

    protected double scrollRate() {
        return this.scrollRateBase;
    }

    private void scroll(final int amount) {
        this.setScrollAmount(this.scrollAmount() + amount);
    }

    @Override
    public boolean mouseScrolled(final double mx, final double my, final double scrollX, final double scrollY) {
        if(!this.visible) {
            return false;
        }
        this.setScrollAmount(this.scrollAmount() - scrollY * this.scrollRate());
        return true;
    }

    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        final boolean scrollClicked = this.updateScrolling(event);
        return super.mouseClicked(event, doubleClick) || scrollClicked;
    }

    @Override
    public boolean mouseDragged(final MouseButtonEvent event, final double dx, final double dy) {
        if(this.scrolling) {
            if(event.y() < this.getY()) {
                this.setScrollAmount(0.0);
            }
            else if(event.y() > this.getBottom()) {
                this.setScrollAmount(this.maxScrollAmount());
            }
            else {
                final double max = Math.max(1, this.maxScrollAmount());
                final int barHeight = this.scrollerHeight();
                final double yDragScale = Math.max(1.0, max / (this.height - barHeight));
                this.setScrollAmount(this.scrollAmount() + dy * yDragScale);
            }
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public void onRelease(final MouseButtonEvent event) {
        this.scrolling = false;
    }








    protected int __internal_addWidget(final Entry entry) {
        return __internal_addWidget(entry, defaultEntryHeight);
    }
    protected int __internal_addWidget(final Entry entry, final int height) {
        entry.list = this;
        entry.setX(getRowLeft());
        entry.setWidth(getRowWidth());
        entry.setY(getNextY());
        entry.setHeight(height);
        final int r = super.addChild(entry);
        repositionEntries();
        return r;
    }
    public void addWidget(final AbstractWidget widget) {
        this.__internal_addWidget(new Entry(widget));
    }
    public void addWidget(final AbstractWidget widget, final int height) {
        this.__internal_addWidget(new Entry(widget), height);
    }


    public void addWidgetAndSpacer(final AbstractWidget widget, final int marginBottom) {
        this.__internal_addWidget(new Entry(widget));
        this.addWidget(new UiSpacer(), marginBottom);
    }
    public void addWidgetAndSpacer(final AbstractWidget widget, final int height, final int marginBottom) {
        this.__internal_addWidget(new Entry(widget), height);
        this.addWidget(new UiSpacer(), marginBottom);
    }


    public void addWidgetAndSpacers(final AbstractWidget widget, final int marginTop, final int marginBottom) {
        this.addWidget(new UiSpacer(), marginTop);
        this.addWidgetAndSpacer(widget, marginBottom);
    }
    public void addWidgetAndSpacers(final AbstractWidget widget, final int height, final int marginTop, final int marginBottom) {
        this.addWidget(new UiSpacer(), marginTop);
        this.addWidgetAndSpacer(widget, height, marginBottom);
    }




    @Override
    protected void onSelected(final Entry selected) {
        final boolean topClipped = selected.getContentY() < this.getY();
        final boolean bottomClipped = selected.getContentBottom() > this.getBottom();
        if(this.minecraft.getLastInputType().isKeyboard() || topClipped || bottomClipped) {
            this.scrollToEntry(selected);
        }
    }

    protected void scrollToEntry(final Entry entry) {
        final int topDelta = entry.getY() - this.getY() - 2;
        if(topDelta < 0) {
            this.scroll(topDelta);
        }
        final int bottomDelta = this.getBottom() - entry.getY() - entry.getHeight() - 2;
        if(bottomDelta < 0) {
            this.scroll(-bottomDelta);
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
        this.setScrollAmount(y - this.height / 2.0);
    }

    //! Let clicks through if they don't hit a sub element.
    @Override
    public boolean isMouseOver(final double mouseX, final double mouseY) {
        if(super.isMouseOver(mouseX, mouseY)) {
            if(this.isOverScrollbar(mouseX, mouseY)) return true;
            else for(final var c : children) {
                if(c.isMouseOver(mouseX, mouseY)) return true;
            }
        }
        return false;
    }





    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        extractBackground(graphics, mouseX, mouseY, a);

        this.hovered = this.isMouseOver(mouseX, mouseY) ? this.getChildAtPosition(mouseX, mouseY) : null;

        graphics.enableScissor(this.getX(), this.getY(), this.getRight(), this.getBottom());
        for(final Entry child : children) {
            if(child.getY() + child.getHeight() >= this.getY() && child.getY() <= this.getBottom()) {
                child.extractContent(graphics, mouseX, mouseY, Objects.equals(this.hovered, child), a);
            }
        }
        graphics.disableScissor();

        extractListSeparators(graphics);
        extractScrollbar(graphics, mouseX, mouseY);
    }

    protected void extractListSeparators(final GuiGraphicsExtractor graphics) {
        final Identifier headerSeparator = this.minecraft.level == null ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
        final Identifier footerSeparator = this.minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
        graphics.blit(RenderPipelines.GUI_TEXTURED, headerSeparator, this.getX(), this.getY() - 2, 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
        graphics.blit(RenderPipelines.GUI_TEXTURED, footerSeparator, this.getX(), this.getBottom(), 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
    }

    protected void extractScrollbar(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY) {
        final int scrollBarX = this.scrollBarX();
        final int scrollerHeight = this.scrollerHeight();
        final int scrollerY = this.scrollBarY();
        final int barWidth = this.scrollbarWidth();

        // If there are hidden elements
        if(scrollable()) {

            // Draw handle
            final boolean hoveredBar = isOverScrollbar(mouseX, mouseY);
            final int handleColor = hoveredBar ? Layout.handleColorActive : Layout.handleColor;
            graphics.fill(scrollBarX, scrollerY, scrollBarX + barWidth, scrollerY + scrollerHeight, handleColor);
            if(hoveredBar) {
                graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
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
        private UiWidgetList list;
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
        public void layoutWidgets() {
            widget.setSize(getWidth(), getHeight());
            widget.setPosition(getX(), getY());
            UiWidgetBase.super.layoutWidgets();
        }
        @Override
        public Screen getScreen() {
            return list.getScreen();
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
            return this.list.getFocused() == this;
        }

        public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
            widget.extractRenderState(graphics, mouseX, mouseY, a);
        }

        @Override
        public boolean isMouseOver(final double mx, final double my) {
            return this.getRectangle().containsPoint((int) mx, (int) my);
        }

        @Override
        public void setX(final int x) {
            this.x = x;
        }

        @Override
        public void setY(final int y) {
            this.y = y;
        }

        public void setWidth(final int width) {
            this.width = width;
        }

        public void setHeight(final int height) {
            this.height = height;
        }

        public int getContentX() {
            return this.getX() + CONTENT_PADDING;
        }

        public int getContentY() {
            return this.getY() + CONTENT_PADDING;
        }

        public int getContentHeight() {
            return this.getHeight() - CONTENT_PADDING * 2;
        }

        public int getContentYMiddle() {
            return this.getContentY() + this.getContentHeight() / 2;
        }

        public int getContentBottom() {
            return this.getContentY() + this.getContentHeight();
        }

        public int getContentWidth() {
            return this.getWidth() - CONTENT_PADDING * 2;
        }

        public int getContentXMiddle() {
            return this.getContentX() + this.getContentWidth() / 2;
        }

        public int getContentRight() {
            return this.getContentX() + this.getContentWidth();
        }

        @Override
        public int getX() {
            return this.x;
        }

        @Override
        public int getY() {
            return this.y;
        }

        @Override
        public int getWidth() {
            return this.width;
        }

        @Override
        public int getHeight() {
            return this.height;
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