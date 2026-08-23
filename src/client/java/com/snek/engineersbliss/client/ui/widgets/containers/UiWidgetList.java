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
import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.widgets.base.UiWidgetBase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.snek.engineersbliss.client.mixin.accessors.AbstractScrollAreaAccessor;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;








/**
 * A scrollable vertical list capable of containing other widgets.
 */
public class UiWidgetList extends AbstractSelectionList<UiWidgetList.Entry> implements BgCacheWidget, UiWidgetBase {

    // Screen reference
    private final Screen screen;
    public Screen getScreen() { return screen; }

    // Cached textures
    private final TextureCache bgCache;
    private int bgColor = Layout.bgColor;
    public void setBgColor(final int newColor) {
        bgColor = newColor; markBgDirty();
    }
    @Override public TextureCache getBgTextureCache() {
        return bgCache;
    }
    @Override public int getBgBaseColor() {
        return bgColor;
    }
    @Override public boolean isGuiScaleTransitioning() {
        return (screen instanceof @NotNull __base_UiScreen uiScreen) && uiScreen.isGuiScaleTransitioning();
    }




    public UiWidgetList(final Screen screen, int itemHeight) {
        super(Minecraft.getInstance(), 50, 50, 50, itemHeight);
        this.screen = screen;
        bgCache = new TextureCache(screen);
    }





    /**
     * Verbatim copy of Vanilla's getFirstEntryY bc for some reason they made it private.
     */
    protected int _getFirstEntryY() {
        return getY() + 2;
    }
    /**
     * Verbatim copy of Vanilla's repositionEntries bc for some reason they made it private.
     */
    protected void _repositionEntries() {
        int y = _getFirstEntryY() - (int)scrollAmount();

        for(var child : children()) {
            child.setY(y);
            y += child.getHeight();
            child.setX(getRowLeft());
            child.setWidth(getRowWidth());
        }
    }

    @Override
    public void layoutWidgets() {
        _repositionEntries();
        if(getSelected() != null) {
            scrollToEntry(getSelected()); //! This  calls setScrollAmount -> layoutWidgets on entries.
        }
        this.refreshScrollAmount();
        layoutWidgets(); //! This also calls layoutWidgets on entries but there isnt rly a way to avoid that.
    }


    public void layoutEntries() {
        for(final var c : children()) {
            if(c instanceof UiWidgetBase w) {
                w.layoutWidgets();
            }
        }
    }



    //! layoutEntries() must be called manually after adding or removing entries.
    //! This helps with performance. It forces the caller to batch operations.

    @Override
    public void setScrollAmount(final double scrollAmount) {
        //! Reposition entries doenst call layoutWidgets and is also private idfk why.
        super.setScrollAmount(scrollAmount);
        layoutEntries();
    }

    @Override
    protected void sort(final Comparator<Entry> comparator) {
        //! Reposition entries doenst call layoutWidgets and is also private idfk why.
        super.sort(comparator);
        layoutEntries();
    }

    @Override
    protected void swap(final int firstIndex, final int secondIndex) {
        //! Reposition entries doenst call layoutWidgets and is also private idfk why.
        super.swap(firstIndex, secondIndex);
        layoutEntries();
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
        for(final Entry c : children()) {
            if(c.keyPressed(event)) r = true;
        }
        return r;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        boolean r = false;
        for(final Entry c : children()) {
            if(c.charTyped(event)) r = true;
        }
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

            // Draw handle
            final boolean hovered = isOverScrollbar(mouseX, mouseY);
            final int handleColor = hovered ?  Layout.handleColorActive : Layout.handleColor;
            graphics.fill(scrollBarX, scrollerY, scrollBarX + barWidth, scrollerY + scrollerHeight, handleColor);
            if(isOverScrollbar(mouseX, mouseY)) {
                graphics.requestCursor(((AbstractScrollAreaAccessor)this).isScrolling() ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);

                // Draw hover overlay
                graphics.fill(scrollBarX, scrollerY, scrollBarX + barWidth, scrollerY + scrollerHeight, Layout.highlightOverlay);
            }
        }
    }








    public static class Entry extends AbstractSelectionList.Entry<Entry> {
        private final AbstractWidget widget;

        //! For subclasses that manage their own content
        protected Entry() {
            this.widget = null;
        }

        public Entry(AbstractWidget widget) {
            this.widget = widget;
        }

        public @Nullable AbstractWidget getWidget() {
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
}