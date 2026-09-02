package com.snek.engineersbliss.client.ui.widgets.sliders;

import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.snek.engineersbliss.client.feature_handlers.settings.SettingsFeatureHandler;
import com.snek.engineersbliss.client.ui.UiGraphics;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.UiSize;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedColor;
import com.snek.engineersbliss.client.ui.data_types.animated.AnimatedDouble;
import com.snek.engineersbliss.client.ui.widgets.base.__base_UiWidget;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Easings;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;








public class UiSlider extends __base_UiWidget {
	public static final int HANDLE_BASE_WIDTH = 8;
    private static final double HANDLE_MAX_WIDTH_SCALE = 2;
    private static final double HANDLE_SPEED_SENSITIVITY = 0.6;


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

    // Mouse handling
    private double virtualX = 0;

    // Cached textures
    private int bgColorAlt = Layout.bgColorAlt;
    public void setBgColorAlt(final int newColor) {
        bgColorAlt = newColor; markBgDirty();
    }
    public int getBgBaseColorAlt() {
        return bgColorAlt;
    }




    public UiSlider(
        final Screen screen,
        final UiTxt baseLabel, final double initialValue,
        final @Nullable Consumer<Double> onChange,
        final @Nullable Function<UiSlider, UiTxt> valueFormatter
    ) {
        //! Pass empty text to super and store a custom UiTxt instance locally
        super(screen, new UiTxt(new Txt().get()), TextAlignment.CENTER);
        setBgColor(Layout.bgColor);
        this.value = initialValue;
        this.bgSpriteId = null;
        this.bgSpriteWidth = new UiSize(this);
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
        boolean result = super.mouseClicked(event, doubled);
        GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().handle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        virtualX = event.x();
        updateValueFromVirtualX();
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
        float guiScale = SettingsFeatureHandler.getCurrentGuiScale();
        GLFW.glfwSetCursorPos(handle, virtualX * guiScale, (getY() + getHeight() / 2d) * guiScale);
        return super.mouseReleased(event);
    }


    //! Default keyPressed moves the handle when the left or right arrow key is pressed.
    //! This stops that behaviour.
    @Override
    public boolean keyPressed(KeyEvent event) {
        return true;
    }

    protected void updateMessage() {
        setLabel(((UiTxt)new UiTxt(baseLabel.get()).cat(" : ")).cat(valueFormatter.apply(this)));
    }

    private void updateValueFromVirtualX() {
        final double newValue = (virtualX - (this.getX() + height)) / (getWidth() - 2d * height);
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
        markBgDirty();
    }

    @Override
    protected void onHoverStart() {
        super.onHoverStart();
        playHoverSound();
    }








    @Override
    public void extractWidgetRenderState(final UiGraphics graphics, final int mouseX, final int mouseY, final float a) {

        // Draw background and label
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);


        // Draw slider handle //! Clamp to slider inner width
        final int handleX = calcHandleX();
        final int handleWidth = calcHandleWidth();
        final int handleL = handleX - handleWidth / 2;
        final int handleR = handleX + handleWidth / 2;
        final int innerL = calcInnerLeft();
        final int innerR = calcInnerRight();
        handleColor.startNewTransition(isHoveredOrBeingDragged() ? Layout.handleColorActive : Layout.handleColor);
        graphics.fill(Math.max(innerL, handleL), getY(), Math.min(innerR, handleR), getBottom(), handleColor.compute());
        if(handleL <  innerL) graphics.fill(handleL, getY(), innerL,  getBottom(), Layout.handleColorTransparent);
        if(handleR >= innerR) graphics.fill(innerR,  getY(), handleR, getBottom(), Layout.handleColorTransparent);


        // Recalculate and draw hover highlight
        //! Minecraft doesn't provide any onMouseEnter/onMouseLeave callback so this must be recalculated by the rendering loop.
        //! This isn't bad, identical values don't update the animated target and computing time is negligible. It just feels unorthodox.
        final boolean shouldShowOverlay = isHoveredOrBeingDragged();
        overlayColor.startNewTransition(shouldShowOverlay ? Layout.highlightOverlay : 0x0);
        graphics.fill(getX(), getY(), getRight(), getBottom(), overlayColor.compute());
    }

    @Override
    protected void handleCursor(final UiGraphics graphics) {
        if(isHoveredOrBeingDragged()) {
            graphics.requestCursor(isActive() ? CursorTypes.RESIZE_EW : CursorTypes.NOT_ALLOWED);
        }
    }




    @Override
    public void extractBackground(UiGraphics graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        // Draw background sprite if present, on top of the default background so the shape of the button is preserved
        if(bgSpriteId != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, bgSpriteId, getX(), getY(), bgSpriteWidth.getPx(), getHeight());
        }
    }




    public int calcHandleWidth() {
        final double magnitude = Math.abs(value - visualValue.getLast());
        final double speed = Math.abs(visualValue.calcSpeed()) * magnitude;
        final double widthFactor = Math.clamp(1.0 + speed * HANDLE_SPEED_SENSITIVITY, 1.0, HANDLE_MAX_WIDTH_SCALE);
        return (int)Math.round(HANDLE_BASE_WIDTH * widthFactor);
    }

    public int calcHandleX() {
        return calcInnerLeft() + (int)(visualValue.compute() * calcInnerWidth());
    }

    public int calcInnerLeft() {
        return getX() + height;
    }

    public int calcInnerRight() {
        return getRight() - height;
    }

    public int calcInnerWidth() {
        return getWidth() - 2 * height;
    }
}



//TODO add a sound when the value changes. The pitch changes based on the %