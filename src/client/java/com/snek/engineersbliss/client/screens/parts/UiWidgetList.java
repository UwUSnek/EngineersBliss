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

    public void addWidget(AbstractWidget widget) {
        this.addEntry(new Entry(widget));
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected void extractListBackground(final GuiGraphicsExtractor graphics) {
        graphics.fill(getX(), getY(), getX() + width, getY() + height, Layout.bgColor);
    }

    @Override
    public int getNextY() {
        int y = this.__custom_getFirstEntryY() - (int)this.scrollAmount();
        for(final Entry child : this.children()) {
            y += child.getHeight();
        }
        return y;
    }
    private int __custom_getFirstEntryY() {
        return this.getY();
        //! Vanilla's getFirstEntryY adds 2px for absolutely no reason
        //! And it cannot be changed bc its private
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
            return keyReleased(event);
        }

        @Override
        public boolean charTyped(final CharacterEvent event) {
            return charTyped(event);
        }
    }

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
        //TODO idk what this does
	}
}