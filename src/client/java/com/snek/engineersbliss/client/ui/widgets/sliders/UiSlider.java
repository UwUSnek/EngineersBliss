package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.UiSize;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedColor;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedDouble;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Easings;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.resources.Identifier;








public class UiSlider extends __base_UiWidget {
    private static final double HANDLE_MAX_WIDTH_SCALE = 2;
    private static final double HANDLE_SPEED_SENSITIVITY = 0.6;


	public final UiSize baseHandleWidth;
    private final UiTxt baseLabel;
    private final @Nullable Consumer<Double> onChange;
    private final @Nullable Function<UiSlider, UiTxt> valueFormatter;
    private AnimatedDouble visualValue; //! The visual interpolated value, 0 to 1
    private AnimatedColor overlayColor;
    private AnimatedColor handleColor;

    // Slider value
    protected double value;

    // Sprite
    private @Nullable Identifier bgSpriteId;
    private UiSize bgSpriteWidth;
    private UiSize bgSpriteMargin;
    public UiSize getBgSpriteMargin() { return bgSpriteMargin; }


    // Mouse handling
    private double virtualX = 0;

    // Cached textures
    private int bgColorAlt = Layout.bgColorAlt;
    public void setBgColorAlt(final int newColor) {
        bgColorAlt = newColor;
    }
    public int getBgBaseColorAlt() {
        return bgColorAlt;
    }




    public UiSlider(
        final UiScreen screen,
        final UiTxt baseLabel, final double initialValue,
        final @Nullable Consumer<Double> onChange,
        final @Nullable Function<UiSlider, UiTxt> valueFormatter
    ) {
        //! Pass empty text to super and store a custom UiTxt instance locally
        super(screen, new UiTxt(new Txt().get()), TextAlignment.CENTER);
        setBgColor(Layout.bgColor);
        this.baseHandleWidth = new UiSize(this); baseHandleWidth.setPx(8).setScaleWithUi(true);
        this.value = initialValue;
        this.bgSpriteId = null;
        this.bgSpriteWidth = new UiSize(this);
        this.bgSpriteMargin = new UiSize(this); this.bgSpriteMargin.clear().setHF(0.25f);
        this.baseLabel = baseLabel;
        this.onChange = onChange;
        this.valueFormatter = valueFormatter == null ? s -> new UiTxt(String.valueOf((int)(s.value * 100)) + "%") : valueFormatter;
        this.visualValue = new AnimatedDouble(initialValue,       Layout.slideTransitionDuration, Easings.cubicInOut);
        this.overlayColor = new AnimatedColor(0x0,                Layout.hoverTransitionDuration, Easings.quadIn);
        this.handleColor  = new AnimatedColor(Layout.handleColor, Layout.hoverTransitionDuration, Easings.quadIn);
        updateMessage();
    }

    public UiSlider withSpriteBg(final Identifier id, final float width_heightFraction) {
        this.bgSpriteId = id;
        this.bgSpriteWidth.clear().setHF(width_heightFraction);
        return this;
    }





    @Override
    public void relayoutSelf() {
        // Empty
    }







    //! Disable the cursor so it doesn't wander off screen or out of bounds while dragging.
    // Also recalculate the slider's handle position based on the click position. //! onClick disabled that.
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
        boolean r = super.mouseClicked(event, doubled);
        if(r) {
            final long handle = Minecraft.getInstance().getWindow().handle();
            virtualX = event.x();
            GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            updateValueFromVirtualX();
        }
        return r;
    }


    //! Calculate a virtual X position by accumulating deltas.
    //! This allows for instant bound clamping. Simply moving the cursor back to the right position looks very jittery and delayed.
    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        boolean r = super.mouseDragged(event, dx, dy);
        if(r) {
            virtualX = Math.clamp(virtualX + dx, getXF(), getRight());
            updateValueFromVirtualX();
        }
        return r;
    }


    //! Reactivate the cursor (mouseClicked disabled it).
    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        final boolean r = super.mouseReleased(event);
        if(r) {
            long handle = Minecraft.getInstance().getWindow().handle();
            GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
            GLFW.glfwSetCursorPos(handle, getInnerX() + getInnerWidth() * value, getHeightCenter());
        }
        return r;
    }

    protected void updateMessage() {
        setLabel(((UiTxt)new UiTxt(baseLabel.get()).cat(" : ")).cat(valueFormatter.apply(this)));
    }

    private void updateValueFromVirtualX() {
        final double newValue = (virtualX - getInnerX()) / getInnerWidth();
        if(value != newValue) {
            this.setValue(newValue);
        }
    }

    protected void setValue(final double newValue) {
        double oldValue = this.value;
        this.value = Math.clamp(newValue, 0.0, 1.0);
        if(oldValue != this.value) {
            this.applyValue();
        }
        this.updateMessage();
    }

    protected void applyValue() {
        if(onChange != null) onChange.accept(value);
        visualValue.startNewTransition(value);
        playDragSound(value);
    }

    @Override
    protected void onHoverStart() {
        super.onHoverStart();
        playHoverSound();
    }








    @Override
    public void extractSelf(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {

        // Draw background and label
        super.extractSelf(graphics, mouseX, mouseY, a);
        final float alpha = isActive() ? 1f : Layout.disabledAlpha;


        // Draw slider handle //! Clamp to slider inner width
        final float handleX = calcHandleX();
        final float handleWidth = calcHandleWidth();
        final float handleL = handleX - handleWidth / 2;
        final float handleR = handleX + handleWidth / 2;
        final float innerL = getInnerX();
        final float innerR = getInnerRight();
        handleColor.startNewTransition(isHoveredOrBeingDragged() ? Layout.handleColorActive : Layout.handleColor);
        graphics.fill(Math.max(innerL, handleL), getYF(), Math.min(innerR, handleR), getBottom(), handleColor.compute(), alpha);
        if(handleL <  innerL) graphics.fill(handleL, getYF(), innerL,  getBottom(), Layout.handleColorTransparent, alpha);
        if(handleR >= innerR) graphics.fill(innerR,  getYF(), handleR, getBottom(), Layout.handleColorTransparent, alpha);


        // Recalculate and draw hover highlight
        //! Minecraft doesn't provide any onMouseEnter/onMouseLeave callback so this must be recalculated by the rendering loop.
        //! This isn't bad, identical values don't update the animated target and computing time is negligible. It just feels unorthodox.
        final boolean shouldShowOverlay = isHoveredOrBeingDragged();
        overlayColor.startNewTransition(shouldShowOverlay ? Layout.highlightOverlay : 0x0);
        graphics.fill(getXF(), getYF(), getRight(), getBottom(), overlayColor.compute(), alpha);
    }

    @Override
    protected CursorType selectCursor(final UiGraphics graphics) {
        return CursorTypes.RESIZE_EW;
    }




    @Override
    public void extractBackground(UiGraphics graphics, float mouseX, float mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        final float alpha = isActive() ? 1f : 0.25f;

        // Draw background sprite if present, on top of the default background so the shape of the button is preserved
        if(bgSpriteId != null) {
            final float marginPx = getBgSpriteMargin().getPx();
            // graphics.blitSprite(bgSpriteId, getXF() + marginPx, getYF() + marginPx, bgSpriteWidth.getPx() - 2f * marginPx, getHeightF() - 2f * marginPx, alpha);
            graphics.blit.wh(
                bgSpriteId,
                getXF() + marginPx, getYF() + marginPx,
                bgSpriteWidth.getPx() - 2f * marginPx, getHeightF() - 2f * marginPx,
                alpha
            );
        }
    }




    public float calcHandleWidth() {
        final double magnitude = Math.abs(value - visualValue.getLast());
        final double speed = Math.abs(visualValue.calcSpeed()) * magnitude;
        final double widthFactor = Math.clamp(1.0 + speed * HANDLE_SPEED_SENSITIVITY, 1.0, HANDLE_MAX_WIDTH_SCALE);
        return baseHandleWidth.getPx() * (float)widthFactor;
    }

    public float calcHandleX() {
        return getInnerX() + visualValue.compute().floatValue() * getInnerWidth();
    }
}