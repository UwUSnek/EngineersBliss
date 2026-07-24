package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;








public class UiSlider extends AbstractSliderButton {
    private final UiTxt baseLabel;
    private UiTxt label;
    private final @Nullable Consumer<Double> onChange;




    public UiSlider(final int x, final int y, final int width, final int height, final UiTxt label, final double initialValue, final @Nullable Consumer<Double> onChange) {
        //! Pass empty text to super and store a custom UiTxt instance locally
        super(x, y, width, height, new Txt().get(), initialValue);
        this.baseLabel = label;
        this.onChange = onChange;
        updateMessage();
    }
    public UiSlider(final UiTxt label, final double initialValue, final @Nullable Consumer<Double> onChange) {
        this(50, 50, 150, DEFAULT_HEIGHT, label, initialValue, onChange);
    }




    @Override
    protected void updateMessage() {
        this.label = new UiTxt(baseLabel.get()).cat(buildValueText());
    }
    public UiTxt buildValueText() {
        return new UiTxt(String.valueOf((int)(value * 100)) + "%");
    }
    public void setLabel(final Component label) {
        super.setMessage(label);
        this.label = new UiTxt(label);
    }
    public void setLabel(final UiTxt label) {
        super.setMessage(label.get());
        this.label = (UiTxt)label.copy();
    }
    @Override
    public void setMessage(Component message) {
        throw new UnsupportedOperationException("Use .setLabel(label) instead.");
    }




    @Override
    protected void applyValue() {
        if(onChange != null) onChange.accept(value);
    }




    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {

        // Draw black background //! Always drawn
        graphics.fill(getX(), getY(), getRight(), getBottom(), Layout.bgColor);


        // Draw hover highlight
        if(isHovered) graphics.fill(getX(), getY(), getRight(), getBottom(), Layout.bgColorActive);


        // Draw label
        final ScaledFont scaledFont = (label instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont();
        final int textX = getX() + Layout.textMarginPx;
        final int textY = getY() + (height - scaledFont.getLineHeight()) / 2;
        final int fgColor = isHovered() ? Layout.fgColorActive : Layout.fgColor;
        RenderingUtils.extractTxt(graphics, label, textX, textY, fgColor, TextAlignment.CENTER, width, false);

        this.handleCursor(graphics);
    }
}