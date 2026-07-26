package com.snek.engineersbliss.client.ui.widgets.containers;

import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;

import com.snek.engineersbliss.client.ui.widgets.misc.BgCacheWidget;
import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.snek.engineersbliss.client.mixin.accessors.AbstractScrollAreaAccessor;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.utils.Layout;








/**
 * A scrollable vertical list capable of containing other widgets.
 */
public class UiWidgetList extends AbstractSelectionList<UiWidgetList.Entry> implements BgCacheWidget {

    // Cached textures
    private final TextureCache bgCache;
	@Override public TextureCache getBgTextureCache() { return bgCache; }


    public UiWidgetList(final Screen screen, int width, int height, int x, int y, int itemHeight) {
        super(Minecraft.getInstance(), width, height, y, itemHeight);
        bgCache = new TextureCache(screen);
        setX(x);
    }




    @Override
    protected boolean entriesCanBeSelected() {
        return false;
    }

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
        // Empty
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


    //! For whatever reason, AbstractSelectionList's getHovered() is PROTECTED but also FINAL???
    //! So the hovered entry cannot be accessed by external classes.
    //! This lets external code access it without iterating all the children.
    public Entry getHoveredEntry() {
        return super.getHovered();
    }


    //! Override lets clicks through when they don't hit a sub element.
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






    //! Vanilla's getFirstEntryY removes 2px for absolutely no reason and it cannot be changed bc its PRIVATE omfg why.
    //! In Vanilla, getFirstEntryY is only used for setY, so this override changes setY to remove the 2px padding added by getFirstEntryY.
    @Override
    public void setY(final int y) {
        super.setY(y - 2);
    }

    //! Fix Vanilla getRowLeft to account for the scrollbar's width.
    @Override
    public int getRowLeft() {
        return getX();
    }

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








    //! Disable default background
    @Override
    protected void extractListBackground(final GuiGraphicsExtractor graphics) {
        // Empty
    }


    //! Draw custom background, then draw the rest
    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        BgCacheWidget.super.extractBackground(graphics, mouseX, mouseY, a);
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
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
}