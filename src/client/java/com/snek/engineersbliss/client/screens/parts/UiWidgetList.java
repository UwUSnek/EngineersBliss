package com.snek.engineersbliss.client.screens.parts;

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
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected void extractListBackground(final GuiGraphicsExtractor graphics) {
        graphics.fill(getX(), getY(), getRight(), getBottom(), Layout.bgColorSolid);
    }

    @Override
    public double scrollAmount() {
        return super.scrollAmount() + 2.0;
        //! Vanilla's getFirstEntryY removes 2px for absolutely no reason and it cannot be changed bc its private.
        //! So scrollAmount add 2px from to re-align the elemtns.
        //! In Vanilla, getFirstEntryY is always used with scrollAmount.
    }

    @Override
    protected int scrollBarX() {
        return width;
    }

    @Override
    public int scrollbarWidth() {
        return 2;
    }





    public static class Entry extends AbstractSelectionList.Entry<Entry> {
        private final AbstractWidget widget;

        public Entry(AbstractWidget widget) {
            this.widget = widget;
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