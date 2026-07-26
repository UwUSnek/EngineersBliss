package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.misc.BgCacheWidget;
import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;








public class UiSlider extends AbstractSliderButton implements BgCacheWidget {
    private final UiTxt baseLabel;
    private UiTxt label;
    private final @Nullable Consumer<Double> onChange;

    // Mouse handling
    private boolean dragged = false;
    private double virtualX = 0;

    // Cached textures
    private final TextureCache bgCache;
	@Override public TextureCache getBgTextureCache() { return bgCache; }




    public UiSlider(final Screen screen, final int x, final int y, final int width, final int height, final UiTxt label, final double initialValue, final @Nullable Consumer<Double> onChange) {
        //! Pass empty text to super and store a custom UiTxt instance locally
        super(x, y, width, height, new Txt().get(), initialValue);
        this.baseLabel = label;
        this.onChange = onChange;
        updateMessage();
        bgCache = new TextureCache(screen);
    }
    public UiSlider(final Screen screen, final UiTxt label, final double initialValue, final @Nullable Consumer<Double> onChange) {
        this(screen, 50, 50, 150, DEFAULT_HEIGHT, label, initialValue, onChange);
    }




    @Override
    protected void updateMessage() {
        this.label = ((UiTxt)new UiTxt(baseLabel.get()).cat(" : ")).cat(buildValueText());
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
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        //! Override with no-op to skip the default setValueFromMouse call.
        //! setValueFromMouse is private and cannot be changed.
    }


    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        //! Override with no-op to skip the default setValueFromMouse call.
        //! setValueFromMouse is private and cannot be changed.
    }


    //! Disable the cursor so it doesn't wander off screen or out of bounds while dragging.
    // Also recalculate the slider's handle position based on the click position. //! onClick disabled that.
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
        boolean result = super.mouseClicked(event, doubled);
        dragged = true;
        if(result) {
            GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().handle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            virtualX = event.x();
            updateValueFromVirtualX();
        }
        return result;
    }


    //! Calculate a virtual X position by accumulating deltas.
    //! This allows for instant bound clamping. Simply moving the cursor back to the right position looks very jittery and delayed.
    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        boolean result = super.mouseDragged(event, dx, dy);
        virtualX = Math.clamp(virtualX + dx, getX(), (double)getX() + getWidth());
        updateValueFromVirtualX();
        return result;
    }


    //! Reactivate the cursor (mouseClicked disabled it).
    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        long handle = Minecraft.getInstance().getWindow().handle();
        GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
        GLFW.glfwSetCursorPos(handle, virtualX * guiScale, (getY() + getHeight() / 2d) * guiScale);
        dragged = false;
        return super.mouseReleased(event);
    }


    private void updateValueFromVirtualX() {
        this.setValue((virtualX - (this.getX() + HANDLE_WIDTH / 2d)) / (getWidth() - HANDLE_WIDTH));
    }


    @Override
    protected void applyValue() {
        if(onChange != null) onChange.accept(value);
    }

    public boolean isBeingDragged() {
        return dragged;
    }

    public boolean isHoveredOrBeingDragged() {
        return isHovered() || isBeingDragged();
    }










    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {

        // Draw background
        extractBackground(graphics, mouseX, mouseY, a);

        // Draw slider handle
        final int handleX = getX() + (int)(this.value * (width - HANDLE_WIDTH));
        final int handleColor = isHoveredOrBeingDragged() ? Layout.handleColorActive : Layout.handleColor;
        graphics.fill(handleX, getY(), handleX + HANDLE_WIDTH, getBottom(), handleColor);

        // Draw label
        final ScaledFont scaledFont = (label instanceof final @NotNull UiTxt uiTxt) ? uiTxt.getScaledFont() : new ScaledFont();
        final int textX = getX() + Layout.textMarginPx;
        final int textY = getY() + (height - scaledFont.getLineHeight()) / 2;
        RenderingUtils.extractTxt(graphics, label, textX, textY, Layout.fgColor, TextAlignment.CENTER, width, false);

        // Draw hover highlight
        if(isHoveredOrBeingDragged()) {
            graphics.fill(getX(), getY(), getRight(), getBottom(), Layout.highlightOverlay);
        }

        // Handle cursor shape and position
        this.handleCursor(graphics);
    }
}