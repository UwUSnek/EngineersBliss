package com.snek.engineersbliss.client.screens.parts;

import java.util.List;

import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;




/**
 * A custom widget capable of rendering text with the specified scale, alignment, and color, and wrap lines based on the element's width.
 */
public class UiTextWidget extends AbstractWidget {
    private Txt label;
    private List<Txt> cachedLines; //! Wrapped lines
    private final TextAlignment alignment;
    private final int color;
    private final boolean wrapLines;




    public UiTextWidget(final Txt label, final TextAlignment alignment, final int color) {
        this(0, 0, 0, 0, label, alignment, color, false);
    }

    public UiTextWidget(final int x, final int y, final int w, final int h, final Txt label, final TextAlignment alignment, final int color) {
        this(x, y, w, h, label, alignment, color, false);
    }

    public UiTextWidget(final Txt label, final TextAlignment alignment, final int color, final boolean wrapLines) {
        this(0, 0, 0, 0, label, alignment, color, wrapLines);
    }

    public UiTextWidget(final int x, final int y, final int w, final int h, final Txt label, final TextAlignment alignment, final int color, final boolean wrapLines) {
        super(x, y, w, h, new Txt().get());
        this.alignment = alignment;
        this.color = color;
        this.wrapLines = wrapLines;
        setLabel(label);; //! Call setLabel to initialized cachedLines
    }



    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        final Font font = Minecraft.getInstance().font;
        final int x = getX() + Layout.textMarginPx;
        final int y = getY() + (height - font.lineHeight) / 2;

        int curLineNum = 0;
        for(final Txt l : cachedLines) {
            RenderingUtils.extractTxt(graphics, l, x, y + font.lineHeight * curLineNum, color, alignment, width);
            ++curLineNum;
        }
    }




    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        // Empty
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return false;
    }

    public void setLabel(final Txt newLabel) {
        label = newLabel;
        recalculateLines();
    }



    //! Recalculate lines when the width changes
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        recalculateLines();
    }


    protected void recalculateLines() {
        if(wrapLines) {
            cachedLines = RenderingUtils.wrapLines(label, width);
        }
        else {
            cachedLines = List.of(label);
        }
    }
}
