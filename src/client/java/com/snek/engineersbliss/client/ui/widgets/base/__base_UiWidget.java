package com.snek.engineersbliss.client.ui.widgets.base;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.TextAlignmentY;
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
    private int bgColor = Layout.bgColor;
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
    public UiTxt getLabel() { return label; }
    public void setLabel(final UiTxt     label) { this.label = (UiTxt) label.copy(); }
    public void setLabel(final Component label) { this.label = new UiTxt(label); }


    // Label layout
    private TextAlignment alignment;
    private TextAlignmentY verticalAlignment;
    public TextAlignment getAlignment() { return alignment; }
    public TextAlignmentY getVerticalAlignment() { return verticalAlignment; }
    public void setAlignment(final TextAlignment alignment) { this.alignment = alignment; }
    public void setVerticalAlignment(final TextAlignmentY verticalAlignment) { this.verticalAlignment = verticalAlignment; }
    private float labelOffset;
    public float getLabelOffset() { return labelOffset; }
    public void setLabelOffset(final float labelOffset) { this.labelOffset = labelOffset; }
    private float leftLabelMargin;
    public float getLeftLabelMargin() { return leftLabelMargin; }
    public void setLeftLabelMargin(final float leftLabelMargin) { this.leftLabelMargin = leftLabelMargin; }
    private float rightLabelMargin;
    public float getRightLabelMargin() { return rightLabelMargin; }
    public void setRightLabelMargin(final float rightLabelMargin) { this.rightLabelMargin = rightLabelMargin; }







    protected __base_UiWidget(final Screen screen, final UiTxt label, final TextAlignment alignment) {
        super(screen);
        this.label = label;
        this.alignment = alignment;
        this.verticalAlignment = TextAlignmentY.CENTER;
        this.labelOffset = 0;
        this.leftLabelMargin = 0;
        this.rightLabelMargin = 0;
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

        // Render children recursively
        for(final var child : children()) {
            if(child instanceof @NotNull AbstractWidget w) {
                if(w.getY() + w.getHeight() >= getY() && w.getY() <= getBottom()) {
                    w.extractRenderState(graphics, mouseX, mouseY, a);
                }
            }
        }
    }


    protected void extractLabel(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if(label != null && label.length() > 0) {
            final @NotNull ScaledFont scaledFont = label.getScaledFont();
            final int lineHeight = scaledFont.getLineHeight();
            final int x = getX() + (int)(height * getLabelOffset()) + Layout.textMarginPx;
            final int y = switch(getVerticalAlignment()) {
                case TOP    -> getY() + Layout.textMarginPx;
                case CENTER -> getY() + (height - lineHeight) / 2;
                case BOTTOM -> getBottom() - lineHeight;
            };
            final int scissorLeft =      getX() + (int)(height * leftLabelMargin);
            final int scissorRight = getRight() - (int)(height * rightLabelMargin);
            graphics.enableScissor(scissorLeft, getY(), scissorRight, getBottom());
            RenderingUtils.extractTxt(graphics, label, x, y, Layout.fgColor, getAlignment(), width, false);
            graphics.disableScissor();
        }
    }


    protected void extractDebugOverlay(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if(ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.DEBUG_OVERLAYS)) {
            graphics.outline(getX(), getY(), getWidth(), getHeight(), 0xFFFF0000);
        }
    }
}