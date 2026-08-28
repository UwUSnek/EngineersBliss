package com.snek.engineersbliss.client.ui.widgets.base;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.TextAlignmentY;
import com.snek.engineersbliss.client.ui.data_types.UiSize;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.misc.BgCacheWidget;
import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;








public abstract class __base_UiWidget extends __base_UiLayoutElm implements BgCacheWidget {
    private static final List<AbstractWidget> emptyChildList = new ArrayList<>();


    // Cached background
    private TextureCache bgCache;
    private int bgColor;
    public void setBgColor(final int newColor) {
        bgColor = newColor;
        markBgDirty();
    }
    @Override public int getBgBaseColor() { return bgColor; }
    public @Nullable TextureCache getBgTextureCache() {
        if(bgCache == null) {
            if((bgColor & 0xFF000000) != 0) {
                bgCache = new TextureCache(getScreen());
                return bgCache;
            }
            else {
                return null;
            }
        }
        else {
            return bgCache;
        }
    }


    // Label
    private UiTxt label;
    private int labelWidth;
    public UiTxt getLabel() { return label; }
    public void setLabel(final Component label) { setLabel(new UiTxt(label)); }
    public void setLabel(final UiTxt     label) {
        this.label = (UiTxt)label.copy();
        labelWidth = this.label.getScaledFont().calcWidth(this.label.get());
    }


    // Label layout
    private TextAlignment alignment;
    private TextAlignmentY verticalAlignment;
    public TextAlignment getAlignment() { return alignment; }
    public TextAlignmentY getVerticalAlignment() { return verticalAlignment; }
    public void setAlignment(final TextAlignment alignment) { this.alignment = alignment; }
    public void setVerticalAlignment(final TextAlignmentY verticalAlignment) { this.verticalAlignment = verticalAlignment; }
    private UiSize leftLabelMargin;
    private UiSize rightLabelMargin;
    public UiSize getLeftLabelMargin() { return leftLabelMargin; }
    public UiSize getRightLabelMargin() { return rightLabelMargin; }

    public int getInnerWidth() {
        return getWidth() - leftLabelMargin.getPx() - rightLabelMargin.getPx();
    }
    public int getInnerLeftShift() {
        return leftLabelMargin.getPx();
    }
    public int getInnerX() {
        return getX() + getInnerLeftShift();
    }
    public int getInnerRightShift() {
        return rightLabelMargin.getPx();
    }
    public int getInnerRight() {
        return getRight() - getInnerRightShift();
    }







    protected __base_UiWidget(final Screen screen, final UiTxt label, final TextAlignment alignment) {
        super(screen);
        bgColor = 0x0; //! Default to no background, this also improves performance
        this.leftLabelMargin  = new UiSize(this);  leftLabelMargin.setPx(Layout.textMarginPx);
        this.rightLabelMargin = new UiSize(this); rightLabelMargin.setPx(Layout.textMarginPx);
        setLabel(label); //! Sets label and label width
        this.alignment = alignment;
        this.verticalAlignment = TextAlignmentY.CENTER;
        this.bgCache = null;
    }
    protected __base_UiWidget(final Screen screen, final UiTxt label) {
        this(screen, label, TextAlignment.LEFT);
    }
    protected __base_UiWidget(final Screen screen, final TextAlignment alignment) {
        this(screen, new UiTxt(), alignment);
    }
    protected __base_UiWidget(final Screen screen) {
        this(screen, new UiTxt());
    }

    @Override
    public @NotNull List<?> children() {
        return emptyChildList;
    }








    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        extractBackground  (graphics, mouseX, mouseY, a);
        extractLabel       (graphics, mouseX, mouseY, a);
        extractDebugOverlay(graphics, mouseX, mouseY, a);
    }


    protected void extractLabel(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if(label != null && label.length() > 0) {
            final @NotNull ScaledFont scaledFont = label.getScaledFont();
            final float scale = scaledFont.getScale();
            final int lineHeight = scaledFont.getLineHeight();

            final int pauseMs = 1000;
            final int pxPerSecond = 20;
            final int overflow = labelWidth - getInnerWidth();

            int shift = 0;
            if(overflow > 0) {
                final int scrollMs = (int)(overflow * 1000L / pxPerSecond);
                final int cycleMs  = pauseMs * 2 + scrollMs;
                final long t = System.currentTimeMillis() % cycleMs;

                if(t < pauseMs) {
                    shift = 0;
                } else if(t < pauseMs + scrollMs) {
                    shift = (int)((t - pauseMs) * pxPerSecond / 1000);
                } else {
                    shift = overflow;
                }
            }

            final TextAlignment drawAlignment = overflow > 0 ? TextAlignment.LEFT : getAlignment();
            final int textX = getInnerX();
            final int y = switch(getVerticalAlignment()) {
                case TOP    -> getY() + Layout.textMarginPx;
                case CENTER -> getY() + (height - lineHeight) / 2;
                case BOTTOM -> getBottom() - lineHeight;
            };

            graphics.enableScissor(getInnerX(), getY(), getInnerRight(), getBottom());
            RenderingUtils.extractTxt(graphics, label, textX, y, Layout.fgColor, drawAlignment, getInnerWidth(), false, -shift, 0f);
            graphics.disableScissor();
        }
    }


    protected void extractDebugOverlay(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if(ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.DEBUG_OVERLAYS)) {
            graphics.outline(getX(), getY(), getWidth(), getHeight(), 0xFFFF0000);
        }
    }






    // @Override //TODO remove
    // protected void handleCursor(final GuiGraphicsExtractor graphics) {
    //     // Empty
    // }
}