package com.snek.engineersbliss.client.screens.rendering.widgets;

import com.snek.engineersbliss.client.ui.font.FontFamily;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiContainer;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiLayoutElm;
import com.snek.engineersbliss.client.utils.UiTxt;




public class BlockEntryHeader extends __base_UiContainer<__base_UiLayoutElm> {
    private final RenderingScreenBlockListWidget list;

    public BlockEntryHeader(final RenderingScreenBlockListWidget list) {
        super(list.getScreen());
        this.list = list;
    }

    @Override
    public void relayoutSelf() {
        // Empty
    }

    @Override
    public void extractSelf(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        super.extractSelf(graphics, mouseX, mouseY, a);

        final FontFamily fontFamily = Fonts.ui.regular;
        final int rowLeft = (int)list.getRowLeft();
        final int rowRight = (int)list.getRowRight();
        final int checkboxAreaWidth = RenderingScreenBlockListWidget.CHECKBOX_AREA_WIDTH;
        final int textY = (int)(getYF() + (getHeightF() - fontFamily.get(1f).getLineHeight()) / 2f); //FIXME check actual line-height accessor for FontFamily

        graphics.text(new UiTxt("Block",   fontFamily), rowLeft,                              textY, 0xFFAAAAAA);
        graphics.text(new UiTxt("Enable",  fontFamily), rowRight - checkboxAreaWidth * 2,     textY, 0xFFAAAAAA);
        graphics.text(new UiTxt("Isolate", fontFamily), rowRight - checkboxAreaWidth * 1,     textY, 0xFFAAAAAA);
    }
}