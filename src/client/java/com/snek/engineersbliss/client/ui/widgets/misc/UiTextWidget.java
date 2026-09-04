package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;




/**
 * A widget capable of rendering wrapped text lines.
 */
public class UiTextWidget extends __base_UiWidget {
    private List<UiTxt> cachedLines; //! Wrapped lines
    private final boolean wrapLines;
    private int color;




    public UiTextWidget(final Screen screen, final UiTxt label, final TextAlignment alignment, final int color) {
        this(screen, label, alignment, false, color);
    }
    public UiTextWidget(final Screen screen, final UiTxt label, final TextAlignment alignment, final int color, final int bgColor) {
        this(screen, label, alignment, false, color, bgColor);
    }
    public UiTextWidget(final Screen screen, final UiTxt label, final TextAlignment alignment, final boolean wrapLines, final int color) {
        this(screen, label, alignment, wrapLines, color, 0x0);
    }
    public UiTextWidget(final Screen screen, final UiTxt label, final TextAlignment alignment, final boolean wrapLines, final int color, final int bgColor) {
        super(screen, label, alignment);
        this.wrapLines = wrapLines;
        this.color = color;
        setBgColor(bgColor);
        recalculateLines();
    }








    @Override
    public void relayoutSelf() {
        if(wrapLines) {
            recalculateLines();
        }
    }






    //! Recalculate lines when the width changes
    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        recalculateLines();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        recalculateLines();
    }


    protected void recalculateLines() {
        final int innerWidth = (int)getInnerWidth();
        cachedLines = RenderingUtils.wrapLines(getLabel(), innerWidth);
    }


    @Override
    public void setLabel(final UiTxt newLabel) {
        super.setLabel(newLabel);
        recalculateLines();
    }







    @Override
    protected void extractLabel(UiGraphics graphics, float mouseX, float mouseY, float a) {
        if(!wrapLines) {
            super.extractLabel(graphics, mouseX, mouseY, a);
        }
        else {

            // Calculate position
            int curLineNum = 0;
            final @NotNull ScaledFont scaledFont = getLabel().getScaledFont();
            final int lineHeight = scaledFont.getLineHeight();
            final int textHeight = lineHeight * cachedLines.size();
            final int y = switch(getVerticalAlignment()) {
                case TOP    -> (int)(getYF() + Layout.textMarginPx);
                case CENTER -> (int)(getYF() + (height - textHeight) / 2);
                case BOTTOM -> (int)(getBottom() - textHeight);
            };


            // Draw text lines
            graphics.enableScissor((int)getInnerX(), getY(), (int)getInnerRight(), (int)getBottom());
            for(final UiTxt l : cachedLines) {
                graphics.extractTxt(l, (int)getInnerX(), y + lineHeight * curLineNum, color, getAlignment(), (int)getInnerWidth());
                ++curLineNum;
            }
            graphics.disableScissor();
        }
    }







    // Text widgets reject clicks by default
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return false;
    }

    public void setColor(final int newColor) {
        color = newColor;
    }
}