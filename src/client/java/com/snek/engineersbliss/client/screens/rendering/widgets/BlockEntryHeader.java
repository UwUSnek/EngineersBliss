package com.snek.engineersbliss.client.screens.rendering.widgets;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.UiSize;
import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.utils.UiTxt;




public class BlockEntryHeader extends __base_UiContainer<__base_UiLayoutElm> {
    private final RenderingScreenBlockListWidget list;
    public final UiSize checkboxSize;
    public final UiSize checkboxMarginX;


    public BlockEntryHeader(final RenderingScreenBlockListWidget list) {
        super(list.getScreen());
        this.list = list;
        this.checkboxSize    = new UiSize(this);    checkboxSize.setHF(BlockEntryContents.CHECKBOX_SIZE_HF);
        this.checkboxMarginX = new UiSize(this); checkboxMarginX.setHF(BlockEntryContents.CHECKBOX_MARGIN_HF);
    }


    @Override
    public void relayoutSelf() {
        // Empty
    }


    @Override
    public void extractSelf(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        super.extractSelf(graphics, mouseX, mouseY, a);
        final @NotNull FontFamily fontFamily = Fonts.ui.regular;

        // Compute text position
        final float rowLeft  = list.getRowLeft();
        final float checkboxSizePx = checkboxSize.getPx();
        final float checkboxMarginPx = checkboxMarginX.getPx();
        final float isolateTextL = list.getRowRight() - checkboxMarginPx - checkboxSizePx;
        final float  enableTextR = isolateTextL       - checkboxMarginPx;
        final int textY = (int)(getYF() + (getHeightF() - fontFamily.get(1f).getLineHeight()) / 2f);

        // Draw text
        final float selfWidth = getWidthF();
        graphics.text(new UiTxt("Block",   fontFamily), (int)rowLeft,      textY, 0xFFAAAAAA, TextAlignment.LEFT,  selfWidth);
        graphics.text(new UiTxt("Enable",  fontFamily), (int)getXF(),      textY, 0xFFAAAAAA, TextAlignment.RIGHT, enableTextR - getXF());
        graphics.text(new UiTxt("Isolate", fontFamily), (int)isolateTextL, textY, 0xFFAAAAAA, TextAlignment.LEFT,  0);
    }
}