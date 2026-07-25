package com.snek.engineersbliss.client.ui.widgets.containers;

import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.snek.engineersbliss.client.mixin.accessors.AbstractScrollAreaAccessor;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.utils.Layout;








public class UiWidgetList extends AbstractSelectionList<UiWidgetList.Entry> {

    public UiWidgetList(int width, int height, int x, int y, int itemHeight) {
        super(Minecraft.getInstance(), width, height, y, itemHeight);
        setX(x);
    }




    @Override
    protected boolean entriesCanBeSelected() {
        return false;
    }

    @Override
    public boolean keyPressed(final KeyEvent event) {
        boolean r = false;
        for(final Entry c : children()) r = r || c.keyPressed(event);
        return r;
    }
    @Override
    public boolean charTyped(CharacterEvent event) {
        boolean r = false;
        for(final Entry c : children()) r = r || c.charTyped(event);
        return r;
    }

    @Override
    protected double scrollRate() {
        return super.scrollRate() * 2d;
    }

    //! For whatever reason, AbstractSelectionList's getHovered is PROTECTED but also FINAL??? so it cannot be called by external classes.
    //! This lets external code access the hovered entry without iterating all the children.
    public Entry getHoveredEntry() {
        return super.getHovered();
    }




    public void addWidget(AbstractWidget widget) {
        this.addEntry(new Entry(widget));
    }
    public void addWidget(AbstractWidget widget, final int height) {
        this.addEntry(new Entry(widget), height);
    }


    public void addWidgetAndSpacer(AbstractWidget widget, final int marginBottom) {
        this.addEntry(new Entry(widget));
        this.addWidget(new UiSpacer(), marginBottom);
    }
    public void addWidgetAndSpacer(AbstractWidget widget, final int height, final int marginBottom) {
        this.addEntry(new Entry(widget), height);
        this.addWidget(new UiSpacer(), marginBottom);
    }


    public void addWidgetAndSpacers(AbstractWidget widget, final int marginTop, final int marginBottom) {
        this.addWidget(new UiSpacer(), marginTop);
        this.addWidgetAndSpacer(widget, marginBottom);
    }
    public void addWidgetAndSpacers(AbstractWidget widget, final int height, final int marginTop, final int marginBottom) {
        this.addWidget(new UiSpacer(), marginTop);
        this.addWidgetAndSpacer(widget, height, marginBottom);
    }







    @Override
    protected void extractListBackground(final GuiGraphicsExtractor graphics) {
        graphics.fill(getX(), getY(), getRight(), getBottom(), Layout.bgColorSolid);
    }





    //! Vanilla's getFirstEntryY removes 2px for absolutely no reason and it cannot be changed bc its private.
    //! So scrollAmount add 2px from to re-align the elemtns.
    //! In Vanilla, getFirstEntryY is always used with scrollAmount.
    //! setScrollAmount compensates for scrollAmount so scrolling down doesn't get messed up.
    @Override
    public double scrollAmount() {
        return super.scrollAmount() + 2.0;
    }
    @Override
    public void setScrollAmount(double scrollAmount) {
        super.setScrollAmount(scrollAmount - 2.0);
    }



    @Override
    protected void extractScrollbar(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY) {
        int scrollBarX     = this.scrollBarX();
        int scrollerHeight = this.scrollerHeight();
        int scrollerY      = this.scrollBarY();
        int barWidth       = this.scrollbarWidth();


        // If there are hidden elements
        if(scrollable()) {

            // Draw track
            graphics.fill(scrollBarX, getY(), scrollBarX + barWidth, getBottom(), Layout.bgColorSolid);

            // Draw thumb
            final boolean hovered = isOverScrollbar(mouseX, mouseY);
            final int thumbColor = hovered ?  Layout.handleColorActive : Layout.handleColor;
            graphics.fill(scrollBarX, scrollerY, scrollBarX + barWidth, scrollerY + scrollerHeight, thumbColor);
            if(isOverScrollbar(mouseX, mouseY)) {
                graphics.requestCursor(((AbstractScrollAreaAccessor)this).isScrolling() ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);

                // Draw hover overlay
                graphics.fill(scrollBarX, scrollerY, scrollBarX + barWidth, scrollerY + scrollerHeight, Layout.highlightOverlay);
            }
        }
    }




    //TODO something's wrong here, it seems like the scrollbar is wider than it should be and the row widths is also a few pixels more than it should be.
    //TODO the elements get cut by a few pixels on the right side when the scroll bar is visible
    @Override
    public int getRowWidth() {
        return this.width - this.scrollbarWidth();
    }

    @Override
    protected int scrollBarX() {
        return getX() + width - scrollbarWidth();
    }

    @Override
    public int scrollbarWidth() {
        return 2;
    }

    //! Override lets clicks through when they don't hit a sub element
    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(super.isMouseOver(mouseX, mouseY)) {
            if(this.isOverScrollbar(mouseX, mouseY)) return true;
            else for(final var c : children()) {
                if(c.isMouseOver(mouseX, mouseY)) return true;
            }
        }
        return false;
    }





    public static class Entry extends AbstractSelectionList.Entry<Entry> {
        private final AbstractWidget widget;

        public Entry(AbstractWidget widget) {
            this.widget = widget;
        }

        public AbstractWidget getWidget() {
            return widget;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            widget.setX(getX());
            widget.setY(getY());
            widget.setWidth(getWidth());
            widget.setHeight(getHeight());
            widget.extractRenderState(graphics, mouseX, mouseY, a);
        }

        @Override
        public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
            return widget.mouseClicked(event, doubleClick);
        }

        @Override
        public boolean mouseReleased(final MouseButtonEvent event) {
            return widget.mouseReleased(event);
        }

        @Override
        public boolean mouseDragged(final MouseButtonEvent event, final double dx, final double dy) {
            return widget.mouseDragged(event, dx, dy);
        }

        @Override
        public boolean mouseScrolled(final double x, final double y, final double scrollX, final double scrollY) {
            return widget.mouseScrolled(x, y, scrollX, scrollY);
        }

        @Override
        public boolean keyPressed(final KeyEvent event) {
            return widget.keyPressed(event);
        }

        @Override
        public boolean keyReleased(final KeyEvent event) {
            return widget.keyReleased(event);
        }

        @Override
        public boolean charTyped(final CharacterEvent event) {
            return widget.charTyped(event);
        }
    }

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
        //TODO idk what this does
	}
}