package com.snek.engineersbliss.client.screens.rendering.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.level.block.Block;

import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.client.screens.rendering.BlockRenderer;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;








public class BlockEntryContents extends __base_UiContainer {
    private final RenderingScreenBlockListWidget list;
    private final Block block;
    private final Checkbox enableBox;
    private final Checkbox isolateBox;

    public Block getBlock() { return block; }




    public BlockEntryContents(final RenderingScreenBlockListWidget list, final Block block) {
        super(list.getScreen());
        this.list = list;
        this.block = block;

        final Font font = Fonts.ui.regular.get(1f).getFont();
        addChild(this.enableBox  = Checkbox.builder(new UiTxt().get(), font).selected(RenderingFilterHandler.getEnabled(block)).build());
        addChild(this.isolateBox = Checkbox.builder(new UiTxt().get(), font).selected(RenderingFilterHandler.getIsolated(block)).build());
    }



    @Override
    public void relayoutSelf() {
        final float rowWidth = list.getRowWidth();
        final float checkboxY = getYF() + (getHeightF() - 20) / 2;

        enableBox.setX((int)(getXF() + rowWidth - RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH * 2 + (RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH - enableBox.getWidth()) / 2));
        enableBox.setY((int)(checkboxY));

        isolateBox.setX((int)(getXF() + rowWidth - RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH + (RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH - isolateBox.getWidth()) / 2));
        isolateBox.setY((int)(checkboxY));
    }


    @Override
    public void extractWidgetRenderState(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);

        final float midY = getHeightCenter();
        BlockRenderer.extractBlockIcon(graphics, block, getXF(), midY - 8);
        BlockRenderer.extractBlockName(graphics, block, (int)getXF() + 20, (int)midY - 4, 0xFFFFFFFF);
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
        return false;
    }
}