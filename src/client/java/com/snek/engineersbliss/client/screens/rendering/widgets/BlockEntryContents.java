package com.snek.engineersbliss.client.screens.rendering.widgets;

import net.minecraft.world.level.block.Block;

import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.client.ui.data_types.UiSize;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.ui.widgets.buttons.UiCheckbox;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.UiTxt;








public class BlockEntryContents extends __base_UiContainer<__base_UiLayoutElm> {
    public static final float CHECKBOX_SIZE_HF   = 0.75f;
    public static final float CHECKBOX_MARGIN_HF = 0.25f;

    public final UiSize checkboxSize;
    public final UiSize checkboxMarginX;
    public final UiSize blockSpriteSize;
    private final RenderingScreenBlockListWidget list;
    private final Block block;
    private final UiCheckbox enableBox;
    private final UiCheckbox isolateBox;
    public Block getBlock() { return block; }




    public BlockEntryContents(final RenderingScreenBlockListWidget list, final Block block) {
        super(list.getScreen(), new UiTxt(block.getName()));
        this.list = list;
        this.block = block;
        this.checkboxSize    = new UiSize(this);    checkboxSize.setHF(CHECKBOX_SIZE_HF);
        this.checkboxMarginX = new UiSize(this); checkboxMarginX.setHF(CHECKBOX_MARGIN_HF);
        this.blockSpriteSize = new UiSize(this); blockSpriteSize.setHF(0.75f);
        setAlignment(TextAlignment.LEFT);
        getLeftLabelMargin().clear().addHF(1f);
        getRightLabelMargin().clear().add(checkboxMarginX, 3).add(checkboxSize, 2);

        addChild(this.enableBox  = new UiCheckbox(getScreen(), RenderingFilterHandler.getEnabled(block),  this::onToggleEnable));
        addChild(this.isolateBox = new UiCheckbox(getScreen(), RenderingFilterHandler.getIsolated(block), this::onToggleIsolate));
    }




    @Override
    public void relayoutSelf() {
        final float checkboxSizePx = checkboxSize.getPx();
        enableBox .setSize(checkboxSizePx, checkboxSizePx);
        isolateBox.setSize(checkboxSizePx, checkboxSizePx);

        final float checkboxMarginPx = checkboxMarginX.getPx();
        final float checkboxY = getYF() + (getHeightF() - checkboxSizePx) / 2f;
        isolateBox.setPos(list.getRowRight() - checkboxMarginPx - isolateBox.getWidthF(), checkboxY);
        enableBox .setPos(isolateBox.getXF() - checkboxMarginPx -  enableBox.getWidthF(), checkboxY);
    }




    @Override
    public void extractSelf(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        super.extractSelf(graphics, mouseX, mouseY, a);
        final float blockSpriteSizePx = blockSpriteSize.getPx();
        final float blockSpriteOffset = (getHeightF() - blockSpriteSizePx) / 2f;
        graphics.blockIcon(block, blockSpriteOffset + getXF(), blockSpriteOffset + getYF(), blockSpriteSizePx);
        //! Block name is dislayed by the default widget label
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