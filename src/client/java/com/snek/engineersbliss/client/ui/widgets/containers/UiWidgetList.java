package com.snek.engineersbliss.client.ui.widgets.containers;

import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;

import com.snek.engineersbliss.client.ui.widgets.misc.BgCacheWidget;
import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;
import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.widgets.base.UiWidgetBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.snek.engineersbliss.client.mixin.accessors.AbstractScrollAreaAccessor;
import com.snek.engineersbliss.client.ui.widgets.misc.UiSpacer;
import com.snek.engineersbliss.client.utils.Layout;








/**
 * A scrollable vertical list capable of containing other widgets.
 */

public class UiWidgetList extends AbstractContainerWidget implements BgCacheWidget, UiWidgetBase {


    // Screen reference
    private final Screen screen;
    public Screen getScreen() { return screen; }

    private final Minecraft minecraft;
    private final int defaultEntryHeight;
    private final List<Entry> children = new ArrayList<>();
    @Nullable private Entry hovered;
    @Nullable private Entry selected;

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




    public UiWidgetList(final Screen screen, final int itemHeight) {
        super(50, 50, 50, 50, CommonComponents.EMPTY, AbstractScrollArea.defaultSettings(itemHeight / 2));
        this.screen = screen;
        this.minecraft = Minecraft.getInstance();
        this.defaultEntryHeight = itemHeight;
        this.bgCache = new TextureCache(screen);
    }





    @Override
    public void layoutWidgets() {
        repositionEntries();
        if(getSelected() != null) {
            scrollToEntry(getSelected()); //! This calls setScrollAmount -> layoutWidgets on entries.
        }
        this.refreshScrollAmount();
        layoutEntries(); //! This also calls layoutWidget on entries but there isnt really a way to avoid that.
    }

    public void layoutEntries() {
        for(final var c : children) {
            if(c instanceof UiWidgetBase w) {
                w.layoutWidgets();
            }
        }
    }

    //! layoutEntries() must be called manually after adding or removing entries.
    //! This helps with performance. It forces the caller to batch operations.
    private void repositionEntries() {
        int y = getY() - (int) scrollAmount();
        for(final Entry child : children) {
            child.setY(y);
            y += child.getHeight();
            child.setX(getRowLeft());
            child.setWidth(getRowWidth());
        }
    }

    public int getNextY() {
        int y = getY() - (int) scrollAmount();
        for(final Entry child : children) {
            y += child.getHeight();
        }
        return y;
    }

    @Override
    protected int contentHeight() {
        int totalHeight = 0;
        for(final Entry child : children) {
            totalHeight += child.getHeight();
        }
        return totalHeight + 4;
    }

    public void updateSize(final int width, final HeaderAndFooterLayout layout) {
        this.updateSizeAndPosition(width, layout.getContentHeight(), layout.getHeaderHeight());
    }

    public void updateSizeAndPosition(final int width, final int height, final int y) {
        this.updateSizeAndPosition(width, height, 0, y);
    }

    public void updateSizeAndPosition(final int width, final int height, final int x, final int y) {
        this.setSize(width, height);
        this.setPosition(x, y);
        this.repositionEntries();
        if(this.getSelected() != null) {
            this.scrollToEntry(this.getSelected());
        }
        this.refreshScrollAmount();
    }

    public int getRowLeft() {
        return getX();
    }

    public int getRowRight() {
        return getRowLeft() + getRowWidth();
    }

    public int getRowWidth() {
        return this.width - scrollbarWidth();
    }

    public int getRowTop(final int row) {
        return children.get(row).getY();
    }

    public int getRowBottom(final int row) {
        final Entry child = children.get(row);
        return child.getY() + child.getHeight();
    }

    @Override
    public int scrollbarWidth() {
        return 2;
    }

    @Override
    protected int scrollBarX() {
        return getX() + this.width - scrollbarWidth();
    }





    public final List<Entry> children() {
        return children;
    }

    protected int getItemCount() {
        return children.size();
    }

    protected void sort(final Comparator<Entry> comparator) {
        children.sort(comparator);
        repositionEntries();
        layoutEntries();
    }

    protected void swap(final int firstIndex, final int secondIndex) {
        Collections.swap(children, firstIndex, secondIndex);
        repositionEntries();
        scrollToEntry(children.get(secondIndex));
        layoutEntries();
    }

    protected void clearEntries() {
        children.clear();
        selected = null;
    }

    protected void clearEntriesExcept(final Entry exception) {
        children.removeIf(entry -> entry != exception);
        if(selected != exception) {
            setSelected(null);
        }
    }

    public void replaceEntries(final List<Entry> newChildren) {
        clearEntries();
        for(final Entry newChild : newChildren) {
            addEntry(newChild);
        }
    }

    protected int addEntry(final Entry entry) {
        return addEntry(entry, defaultEntryHeight);
    }

    protected int addEntry(final Entry entry, final int height) {
        entry.list = this;
        entry.setX(getRowLeft());
        entry.setWidth(getRowWidth());
        entry.setY(getNextY());
        entry.setHeight(height);
        children.add(entry);
        return children.size() - 1;
    }

    protected void addEntryToTop(final Entry entry) {
        addEntryToTop(entry, defaultEntryHeight);
    }

    protected void addEntryToTop(final Entry entry, final int height) {
        final double scrollFromBottom = maxScrollAmount() - scrollAmount();
        entry.list = this;
        entry.setHeight(height);
        children.addFirst(entry);
        repositionEntries();
        setScrollAmount(maxScrollAmount() - scrollFromBottom);
    }

    protected void removeEntryFromTop(final Entry entry) {
        final double scrollFromBottom = maxScrollAmount() - scrollAmount();
        removeEntry(entry);
        setScrollAmount(maxScrollAmount() - scrollFromBottom);
    }

    protected void removeEntries(final List<Entry> entries) {
        entries.forEach(this::removeEntry);
    }

    protected void removeEntry(final Entry entry) {
        final boolean removed = children.remove(entry);
        if(removed) {
            repositionEntries();
            if(entry == selected) {
                setSelected(null);
            }
        }
    }

    @Nullable
    protected final Entry getEntryAtPosition(final double posX, final double posY) {
        for(final Entry child : children) {
            if(child.isMouseOver(posX, posY)) {
                return child;
            }
        }
        return null;
    }

    public void addWidget(final AbstractWidget widget) {
        this.addEntry(new Entry(widget));
    }
    public void addWidget(final AbstractWidget widget, final int height) {
        this.addEntry(new Entry(widget), height);
    }

    public void addWidgetAndSpacer(final AbstractWidget widget, final int marginBottom) {
        this.addEntry(new Entry(widget));
        this.addWidget(new UiSpacer(), marginBottom);
    }
    public void addWidgetAndSpacer(final AbstractWidget widget, final int height, final int marginBottom) {
        this.addEntry(new Entry(widget), height);
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




    @Nullable
    public Entry getSelected() {
        return selected;
    }

    public void setSelected(@Nullable final Entry selected) {
        this.selected = selected;
        if(selected != null) {
            final boolean topClipped = selected.getContentY() < this.getY();
            final boolean bottomClipped = selected.getContentBottom() > this.getBottom();
            if(this.minecraft.getLastInputType().isKeyboard() || topClipped || bottomClipped) {
                this.scrollToEntry(selected);
            }
        }
    }

    @Nullable
    @Override
    public Entry getFocused() {
        return (Entry) super.getFocused();
    }

    @Override
    public void setFocused(@Nullable final GuiEventListener focused) {
        final GuiEventListener oldFocus = this.getFocused();
        if(oldFocus != focused && oldFocus instanceof ContainerEventHandler oldFocusContainer) {
            oldFocusContainer.setFocused(null);
        }
        super.setFocused(focused);
        final int index = children.indexOf(focused);
        if(index >= 0) {
            setSelected(children.get(index));
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

    private void scroll(final int amount) {
        this.setScrollAmount(this.scrollAmount() + amount);
    }

    @Override
    public void setScrollAmount(final double scrollAmount) {
        super.setScrollAmount(scrollAmount);
        repositionEntries();
        layoutEntries();
    }

    @Nullable
    protected Entry nextEntry(final ScreenDirection dir) {
        return this.nextEntry(dir, entry -> true);
    }

    @Nullable
    protected Entry nextEntry(final ScreenDirection dir, final Predicate<Entry> canSelect) {
        return this.nextEntry(dir, canSelect, this.getSelected());
    }

    @Nullable
    protected Entry nextEntry(final ScreenDirection dir, final Predicate<Entry> canSelect, @Nullable final Entry startEntry) {
        final int delta = switch (dir) {
            case RIGHT, LEFT -> 0;
            case UP -> -1;
            case DOWN -> 1;
        };
        if(!children.isEmpty() && delta != 0) {
            int index;
            if(startEntry == null) {
                index = delta > 0 ? 0 : children.size() - 1;
            } else {
                index = children.indexOf(startEntry) + delta;
            }
            for(int i = index; i >= 0 && i < children.size(); i += delta) {
                final Entry candidate = children.get(i);
                if(canSelect.test(candidate)) {
                    return candidate;
                }
            }
        }
        return null;
    }

    @Override
    public Optional<GuiEventListener> getChildAt(final double x, final double y) {
        return Optional.ofNullable(this.getEntryAtPosition(x, y));
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

    public Entry getHoveredEntry() {
        return hovered;
    }

    protected boolean entriesCanBeSelected() {
        return false;
    }





    @Override
    public boolean keyPressed(final KeyEvent event) {
        boolean r = false;
        for(final Entry c : children) {
            if(c.keyPressed(event)) r = true;
        }
        return r;
    }

    @Override
    public boolean charTyped(final CharacterEvent event) {
        boolean r = false;
        for(final Entry c : children) {
            if(c.charTyped(event)) r = true;
        }
        return r;
    }

    @Override
    protected double scrollRate() {
        return super.scrollRate() * 2d;
    }





    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        BgCacheWidget.super.extractBackground(graphics, mouseX, mouseY, a);

        this.hovered = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;

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

    @Override
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
                graphics.requestCursor(((AbstractScrollAreaAccessor) this).isScrolling() ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
                graphics.fill(scrollBarX, scrollerY, scrollBarX + barWidth, scrollerY + scrollerHeight, Layout.highlightOverlay);
            }
        }
    }





    public static class Entry implements LayoutElement, GuiEventListener {
        public static final int CONTENT_PADDING = 2;

        private int x = 0;
        private int y = 0;
        private int width = 0;
        private int height;
        private UiWidgetList list;

        private final AbstractWidget widget;

        //! For subclasses that manage their own content
        protected Entry() {
            this.widget = null;
        }

        public Entry(final AbstractWidget widget) {
            this.widget = widget;
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
            widget.setX(getX());
            widget.setY(getY());
            widget.setWidth(getWidth());
            widget.setHeight(getHeight());
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
        public void visitWidgets(final Consumer<AbstractWidget> widgetVisitor) {
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
	protected void updateWidgetNarration(NarrationElementOutput output) {
        // Empty
	}
}