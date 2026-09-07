package com.snek.engineersbliss.client.screens.rendering.widgets;

import net.minecraft.world.level.block.Block;

import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.client.screens.rendering.BlockRenderer;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiCheckbox;
import com.snek.engineersbliss.client.utils.MinecraftUtils;








public class BlockEntryContents extends __base_UiContainer<__base_UiLayoutElm> {
    private final RenderingScreenBlockListWidget list;
    private final Block block;
    private final UiCheckbox enableBox;
    private final UiCheckbox isolateBox;

    public Block getBlock() { return block; }




    public BlockEntryContents(final RenderingScreenBlockListWidget list, final Block block) {
        super(list.getScreen());
        this.list = list;
        this.block = block;

        addChild(this.enableBox  = new UiCheckbox(getScreen(), this::onToggleEnable,  RenderingFilterHandler.getEnabled(block)));
        addChild(this.isolateBox = new UiCheckbox(getScreen(), this::onToggleIsolate, RenderingFilterHandler.getIsolated(block)));
    }



    @Override
    public void relayoutSelf() {
        final float checkboxY = getYF() + (getHeightF() - 20) / 2;
        final int checkboxAreaWidth = RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH;
        enableBox .setPos(list.getRowRight() - checkboxAreaWidth * 2 + (checkboxAreaWidth -  enableBox.getWidthF()) / 2f, checkboxY);
        isolateBox.setPos(list.getRowRight() - checkboxAreaWidth * 1 + (checkboxAreaWidth - isolateBox.getWidthF()) / 2f, checkboxY);
    }


    @Override
    public void extractWidgetRenderState(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);

        final float midY = getHeightCenter();
        BlockRenderer.extractBlockIcon(graphics, block, getXF(), midY - 8); //FIXME replace with proper graphics. call
        BlockRenderer.extractBlockName(graphics, block, (int)getXF() + 20, (int)midY - 4, 0xFFFFFFFF); //FIXME replace with proper graphics. call
    }


    public void onToggleEnable(final UiCheckbox checkbox) {
        RenderingFilterHandler.resetStateCache();
        RenderingFilterHandler.setEnabled(block, checkbox.isSelected());
        MinecraftUtils.refreshSectionsContaining(block);
    }
    public void onToggleIsolate(final UiCheckbox checkbox) {
        RenderingFilterHandler.resetStateCache();
        RenderingFilterHandler.setIsolated(block, checkbox.isSelected());
        MinecraftUtils.refreshRendering();
    }
}