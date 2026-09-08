package com.snek.engineersbliss.client.screens.rendering.widgets;

import net.minecraft.world.level.block.Block;

import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.client.screens.rendering.BlockRenderer;
import com.snek.engineersbliss.client.ui.data_types.UiSize;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiCheckbox;
import com.snek.engineersbliss.client.utils.MinecraftUtils;








public class BlockEntryContents extends __base_UiContainer<__base_UiLayoutElm> {
    public final UiSize checkboxSize;
    private final RenderingScreenBlockListWidget list;
    private final Block block;
    private final UiCheckbox enableBox;
    private final UiCheckbox isolateBox;
    public Block getBlock() { return block; }




    public BlockEntryContents(final RenderingScreenBlockListWidget list, final Block block) {
        super(list.getScreen());
        this.list = list;
        this.block = block;
        this.checkboxSize = new UiSize(this); checkboxSize.setHF(0.75f);

        addChild(this.enableBox  = new UiCheckbox(getScreen(), RenderingFilterHandler.getEnabled(block),  this::onToggleEnable));
        addChild(this.isolateBox = new UiCheckbox(getScreen(), RenderingFilterHandler.getIsolated(block), this::onToggleIsolate));
    }




    @Override
    public void relayoutSelf() {
        final float checkboxSizePx = checkboxSize.getPx();
        enableBox .setSize(checkboxSizePx, checkboxSizePx);
        isolateBox.setSize(checkboxSizePx, checkboxSizePx);

        final float checkboxY = getYF() + (getHeightF() - checkboxSizePx) / 2f;
        final int checkboxAreaWidth = RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH;
        enableBox .setPos(list.getRowRight() - checkboxAreaWidth * 2 + (checkboxAreaWidth -  enableBox.getWidthF()) / 2f, checkboxY);
        isolateBox.setPos(list.getRowRight() - checkboxAreaWidth * 1 + (checkboxAreaWidth - isolateBox.getWidthF()) / 2f, checkboxY);
    }




    @Override
    public void extractSelf(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        super.extractSelf(graphics, mouseX, mouseY, a);
        final float midY = getHeightCenter();
        BlockRenderer.extractBlockIcon(graphics, block, getXF(), midY - 8); //FIXME replace with proper graphics. call
        BlockRenderer.extractBlockName(graphics, block, (int)getXF() + 20, (int)midY - 4, 0xFFFFFFFF); //FIXME replace with proper graphics. call
    }




    //FIXME for some reason this doesn't register double-or-more-clicks properly.
    //FIXME Only the first one goes through when a lot of sections are refreshed.
    //FIXME It works properly when the game doesn't freeze because of refreshes.
    public void onToggleEnable(final UiCheckbox checkbox) {
        RenderingFilterHandler.resetStateCache();
        RenderingFilterHandler.setEnabled(block, checkbox.getValue());
        MinecraftUtils.refreshSectionsContaining(block);
    }
    //FIXME ^ this one instead always works? even on double clicks? idfk
    public void onToggleIsolate(final UiCheckbox checkbox) {
        RenderingFilterHandler.resetStateCache();
        RenderingFilterHandler.setIsolated(block, checkbox.getValue());
        MinecraftUtils.refreshRendering();
    }
}