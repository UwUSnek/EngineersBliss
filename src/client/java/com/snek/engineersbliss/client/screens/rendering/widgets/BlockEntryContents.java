package com.snek.engineersbliss.client.screens.rendering.widgets;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.level.block.Block;

import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.client.screens.rendering.BlockRenderer;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;








public class BlockEntryContents extends __base_UiWidget {
    private static final int CONTENT_PADDING = 2;

    private final RenderingScreenBlockListWidget list;
    private final Block block;
    private final Checkbox enableBox;
    private final Checkbox isolateBox;

    public Block getBlock() { return block; }


    private final List<Object> children;
    @Override
    public @Nullable List<?> children() {
        return children;
    }




    public BlockEntryContents(final RenderingScreenBlockListWidget list, final Block block) {
        super(list.getScreen(), new UiTxt());
        this.list = list;
        this.block = block;

        final Font font = Fonts.ui.regular.get(1f).getFont();
        children = new ArrayList<>();
        children.add(this.enableBox  = Checkbox.builder(new UiTxt().get(), font).selected(RenderingFilterHandler.getEnabled(block)).build());
        children.add(this.isolateBox = Checkbox.builder(new UiTxt().get(), font).selected(RenderingFilterHandler.getIsolated(block)).build());
    }


    private int getContentX() {
        return this.getX() + CONTENT_PADDING;
    }


    @Override
    public void layoutWidgets() {
        final int rowWidth = list.getRowWidth();
        final int checkboxY = this.getY() + (this.getHeight() - 20) / 2;

        enableBox.setX(this.getX() + rowWidth - RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH * 2 + (RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH - enableBox.getWidth()) / 2);
        enableBox.setY(checkboxY);

        isolateBox.setX(this.getX() + rowWidth - RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH + (RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH - isolateBox.getWidth()) / 2);
        isolateBox.setY(checkboxY);

        super.layoutWidgets();
    }


    @Override
    protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        final int midY = this.getY() + this.getHeight() / 2;

        BlockRenderer.extractBlockIcon(graphics, block, this.getContentX(), midY - 8);
        BlockRenderer.extractBlockName(graphics, block, this.getContentX() + 20, midY - 4, 0xFFFFFFFF);

        enableBox. extractRenderState(graphics, mouseX, mouseY, a);
        isolateBox.extractRenderState(graphics, mouseX, mouseY, a);
    }


    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        if(enableBox.mouseClicked(event, doubleClick)) {
            RenderingFilterHandler.resetStateCache();
            RenderingFilterHandler.setEnabled(block, enableBox.selected());
            MinecraftUtils.refreshSectionsContaining(block);
            return true;
        }
        if(isolateBox.mouseClicked(event, doubleClick)) {
            RenderingFilterHandler.resetStateCache();
            RenderingFilterHandler.setIsolated(block, isolateBox.selected());
            MinecraftUtils.refreshRendering();
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }


    @Override
    protected void updateWidgetNarration(final NarrationElementOutput output) {
        // Empty
    }
}