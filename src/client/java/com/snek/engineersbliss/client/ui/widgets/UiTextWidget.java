package com.snek.engineersbliss.client.ui.widgets;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.TextAlignmentY;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;




/**
 * A custom widget capable of rendering text with the specified scale, alignment, background color and text color.
 * It also supports line word wrap.
 */
public class UiTextWidget extends AbstractWidget {
    private UiTxt label;
    private List<UiTxt> cachedLines; //! Wrapped lines
    private final TextAlignment alignment;
    private TextAlignmentY verticalAlignment;
    private int color;
    private int bgColor;
    private final boolean wrapLines;




    public UiTextWidget(final UiTxt label, final TextAlignment alignment, final int color) {
        this(label, alignment, color, 0x0);
    }
    public UiTextWidget(final int x, final int y, final int w, final int h, final UiTxt label, final TextAlignment alignment, final int color) {
        this(x, y, w, h, label, alignment, color, 0x0);
    }
    public UiTextWidget(final UiTxt label, final TextAlignment alignment, final int color, final boolean wrapLines) {
        this(label, alignment, color, wrapLines, 0x0);
    }
    public UiTextWidget(final int x, final int y, final int w, final int h, final UiTxt label, final TextAlignment alignment, final int color, final boolean wrapLines) {
        this(x, y, w, h, label, alignment, color, wrapLines, 0x0);
    }


    public UiTextWidget(final UiTxt label, final TextAlignment alignment, final int color, final int bgColor) {
        this(0, 0, 0, 0, label, alignment, color, false, bgColor);
    }
    public UiTextWidget(final int x, final int y, final int w, final int h, final UiTxt label, final TextAlignment alignment, final int color, final int bgColor) {
        this(x, y, w, h, label, alignment, color, false, bgColor);
    }
    public UiTextWidget(final UiTxt label, final TextAlignment alignment, final int color, final boolean wrapLines, final int bgColor) {
        this(0, 0, 0, 0, label, alignment, color, wrapLines, bgColor);
    }
    public UiTextWidget(final int x, final int y, final int w, final int h, final UiTxt label, final TextAlignment alignment, final int color, final boolean wrapLines, final int bgColor) {
        super(x, y, w, h, new Txt().get());
        this.alignment = alignment;
        this.color = color;
        this.bgColor = bgColor;
        this.wrapLines = wrapLines;
        verticalAlignment = TextAlignmentY.CENTER;
        setLabel(label); //! Call setLabel to initialized cachedLines
    }


    public UiTextWidget withVerticalAlignment(final TextAlignmentY newVerticalAlignment) {
        verticalAlignment = newVerticalAlignment;
        return this;
    }







    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        // Draw background color if needed
        if((bgColor & 0xFF000000) != 0) {
            graphics.fill(getX(), getY(), getRight(), getBottom(), bgColor);
        }


        // Fetch ScaledFont and calculate position
        int curLineNum = 0;
        final @NotNull ScaledFont scaledFont = (label instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont();
        final int textHeight = scaledFont.getLineHeight() * cachedLines.size();
        final int x = alignment == TextAlignment.LEFT ? getX() + Layout.textMarginPx : getX();
        final int y = switch(verticalAlignment) {
            case TOP    -> getY() + Layout.textMarginPx;
            case CENTER -> getY() + (height - textHeight) / 2;
            case BOTTOM -> getBottom() - textHeight;
        };
        final int wrapWidth = width - Layout.textMarginPx * 2;


        // Draw text lines
        for(final UiTxt l : cachedLines) {
            RenderingUtils.extractTxt(graphics, l, x, y + scaledFont.getLineHeight() * curLineNum, color, alignment, wrapWidth);
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

    public void setLabel(final UiTxt newLabel) {
        label = newLabel;
        recalculateLines();
    }

    public void setColor(final int newColor) {
        color = newColor;
    }

    public void setBgColor(final int newBgColor) {
        bgColor = newBgColor;
    }







    //! Recalculate lines when the width changes
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        recalculateLines();
    }


    protected void recalculateLines() {
        if(wrapLines) {
            final int wrapWidth = width - Layout.textMarginPx * 2;
            cachedLines = RenderingUtils.wrapLines(label, wrapWidth);
        }
        else {
            cachedLines = List.of(label);
        }
    }
}
