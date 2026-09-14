package com.snek.engineersbliss.client.ui.widgets.base;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.ui.base.UiScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.TextAlignmentY;
import com.snek.engineersbliss.client.ui.data_types.UiSize;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;








public abstract class __base_UiWidget extends __base_UiLayoutElm {
    private static final int SCROLL_PAUSE_MS = 1000;
    private static final int BASE_SCROLL_SPEED = 10;  // The base scroll speed, in pixels/s. Multiplied by the text's visual size




    // Custom sounds
    public static SoundEvent CUSTOM_TYPE_SOUND;
    public static SoundEvent CUSTOM_CLICK_SOUND;
    public static SoundEvent CUSTOM_DRAG_SOUND;
    public static SoundEvent CUSTOM_HOVER_SOUND;
    public static SoundEvent METAL_PIPE_SOUND;


    private static SoundEvent registerSound(final String id) {
        final Identifier identifier = Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }
    public static void register() {
        CUSTOM_TYPE_SOUND  = registerSound("ui.type");
        CUSTOM_CLICK_SOUND = registerSound("ui.click");
        CUSTOM_DRAG_SOUND  = registerSound("ui.drag");
        CUSTOM_HOVER_SOUND = registerSound("ui.hover");
        METAL_PIPE_SOUND   = registerSound("ui.metal_pipe");
    }


    private static void playUiSound(final SoundEvent sound, final float pitch, final float volume) {
        final SoundEvent actualSound = ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.METAL_PIPE_SOUNDS) ? METAL_PIPE_SOUND : sound;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(actualSound, pitch, volume));
    }
    public static void playTypeSound() {
        playUiSound(CUSTOM_TYPE_SOUND, 1f, 0.25f);
    }
    public static void playClickSound() {
        playUiSound(CUSTOM_CLICK_SOUND, 1f, 1.2f);
    }
    public static void playDragSound() {
        playDragSound(0.5);
    }
    public static void playDragSound(final double position) {
        playUiSound(CUSTOM_DRAG_SOUND, 1f + ((float)position * 0.2f - 0.1f), 0.75f);
    }
    public static void playHoverSound() {
        playUiSound(CUSTOM_HOVER_SOUND, 1f, 0.5f);
    }




    private static final List<__base_UiLayoutElm> emptyChildList = new ArrayList<>();


    // Background color
    private int bgColor;
    public void setBgColor(final int newColor) {
        bgColor = newColor;
    }
    public int getBgColor() {
        return bgColor;
    }


    // Label
    private UiTxt label;
    public UiTxt getLabel() { return label; }
    public void setLabel(final Component label) { setLabel(new UiTxt(label)); }
    public void setLabel(final UiTxt     label) {
        this.label = (UiTxt)label.copy();
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

    public float getInnerWidth() {
        return getWidthF() - leftLabelMargin.getPx() - rightLabelMargin.getPx();
    }
    public float getInnerLeftShift() {
        return leftLabelMargin.getPx();
    }
    public float getInnerX() {
        return getXF() + getInnerLeftShift();
    }
    public float getInnerRightShift() {
        return rightLabelMargin.getPx();
    }
    public float getInnerRight() {
        return getRight() - getInnerRightShift();
    }




    // Borders
    private float borderTop    = 0;
    private float borderRight  = 0;
    private float borderBottom = 0;
    private float borderLeft   = 0;
    private int borderTopColor    = Layout.borderColor;
    private int borderRightColor  = Layout.borderColor;
    private int borderBottomColor = Layout.borderColor;
    private int borderLeftColor   = Layout.borderColor;
    public void setBorderTopPx      (final float  px) { borderTop         =    px; }
    public void setBorderRightPx    (final float  px) { borderRight       =    px; }
    public void setBorderBottomPx   (final float  px) { borderBottom      =    px; }
    public void setBorderLeftPx     (final float  px) { borderLeft        =    px; }
    public void setBorderTopColor   (final int color) { borderTopColor    = color; }
    public void setBorderRightColor (final int color) { borderRightColor  = color; }
    public void setBorderBottomColor(final int color) { borderBottomColor = color; }
    public void setBorderLeftColor  (final int color) { borderLeftColor   = color; }
    public void setBorderTop   (final float px, final int color) { setBorderTopPx   (px); setBorderTopColor   (color); }
    public void setBorderRight (final float px, final int color) { setBorderRightPx (px); setBorderRightColor (color); }
    public void setBorderBottom(final float px, final int color) { setBorderBottomPx(px); setBorderBottomColor(color); }
    public void setBorderLeft  (final float px, final int color) { setBorderLeftPx  (px); setBorderLeftColor  (color); }







    protected __base_UiWidget(final UiScreen screen, final UiTxt label, final TextAlignment alignment) {
        super(screen);
        bgColor = 0x0; //! Default to no background, this also improves performance
        this.leftLabelMargin  = new UiSize(this);  leftLabelMargin.setPx(Layout.textLargeMarginPx);
        this.rightLabelMargin = new UiSize(this); rightLabelMargin.setPx(Layout.textLargeMarginPx);
        setLabel(label); //! Sets label and label width
        this.alignment = alignment;
        this.verticalAlignment = TextAlignmentY.CENTER;
    }
    protected __base_UiWidget(final UiScreen screen, final UiTxt label) {
        this(screen, label, TextAlignment.LEFT);
    }
    protected __base_UiWidget(final UiScreen screen, final TextAlignment alignment) {
        this(screen, new UiTxt(), alignment);
    }
    protected __base_UiWidget(final UiScreen screen) {
        this(screen, new UiTxt());
    }

    @Override
    public @NotNull List<?> children() {
        return emptyChildList;
    }






    @Override
    public void extractSelf(UiGraphics graphics, float mouseX, float mouseY, float a) {
        extractBackground  (graphics, mouseX, mouseY, a);
        extractLabel       (graphics, mouseX, mouseY, a);
        extractBorders     (graphics, mouseX, mouseY, a);
        extractDebugOverlay(graphics, mouseX, mouseY, a);
    }


    public void extractBackground(final UiGraphics graphics, final float mouseX, final float mouseY, final float a) {
        if((bgColor & 0xFF000000) != 0) {
            graphics.fill(getXF(), getYF(), getRight(), getBottom(), bgColor);
        }
    }


    protected void extractLabel(UiGraphics graphics, float mouseX, float mouseY, float a) {
        if(label != null && label.length() > 0) {
            final @NotNull ScaledFont scaledFont = label.getScaledFont();
            final int lineHeight = scaledFont.getLineHeight();
            final float overflow = label.getWidth() - getInnerWidth();

            // Calculate optimal scroll speed
            float scrollSpeed = BASE_SCROLL_SPEED * scaledFont.getSize();
            if(!scaledFont.isScaleInvariant()) scrollSpeed *= getGuiScale();

            // Calculate current horizontal shift
            float shift = 0;
            if(overflow > 0) {
                final float scrollMs = overflow * 1000 / scrollSpeed;
                final float cycleMs  = SCROLL_PAUSE_MS * 2 + scrollMs;
                final float t = (float)((double)System.currentTimeMillis() % cycleMs);

                if(t < SCROLL_PAUSE_MS) {
                    shift = 0;
                }
                else if(t < SCROLL_PAUSE_MS + scrollMs) {
                    shift = (t - SCROLL_PAUSE_MS) * scrollSpeed / 1000;
                }
                else {
                    shift = overflow;
                }
            }

            final TextAlignment drawAlignment = overflow > 0 ? TextAlignment.LEFT : getAlignment();
            final int textX = (int)getInnerX();
            final int textY = (int)switch(getVerticalAlignment()) {
                case TRUE_TOP    -> getYF();
                case TOP         -> getYF() + Layout.textMarginPx;
                case CENTER      -> getYF() + (getHeightF() - lineHeight) / 2f;
                case BOTTOM      -> getBottom() - lineHeight - Layout.textMarginPx;
                case TRUE_BOTTOM -> getBottom() - lineHeight;
            };

            final float alpha = isActive() ? 1f : Layout.disabledAlpha;
            graphics.enableScissor(Math.round(getInnerX()), getY(), Math.round(getInnerRight()) + 1, Math.round(getBottom()) + 1);
            graphics.text(label, textX, textY, Layout.fgColor, alpha, drawAlignment, getInnerWidth(), false, -shift, 0f);
            graphics.disableScissor();
        }
    }


    protected void extractBorders(UiGraphics graphics, float mouseX, float mouseY, float a) {
        final boolean hasTop    = borderTop    > 0;
        final boolean hasRight  = borderRight  > 0;
        final boolean hasBottom = borderBottom > 0;
        final boolean hasLeft   = borderLeft   > 0;
        if(hasTop || hasRight || hasBottom || hasLeft) {
            final float x = getX();
            final float y = getY();
            final float r = getRight();
            final float b = getBottom();
            final float alpha = isActive() ? 1f : Layout.disabledAlpha;
            if(hasTop   ) extractBorderTop   (graphics, x, y, r, b, borderTop,    borderTopColor, alpha);
            if(hasRight ) extractBorderRight (graphics, x, y, r, b, borderRight,  borderRightColor, alpha);
            if(hasBottom) extractBorderBottom(graphics, x, y, r, b, borderBottom, borderBottomColor, alpha);
            if(hasLeft  ) extractBorderLeft  (graphics, x, y, r, b, borderLeft,   borderLeftColor, alpha);
        }
    }


    protected void extractDebugOverlay(UiGraphics graphics, float mouseX, float mouseY, float a) {
        if(ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.DEBUG_OVERLAYS)) {
            final int outlineColor = 0xFFFF0000;
            final float ox = getX();
            final float oy = getY();
            final float or = getRight();
            final float ob = getBottom();
            extractBorderTop   (graphics, ox, oy, or, ob, 1, outlineColor, 1f);
            extractBorderRight (graphics, ox, oy, or, ob, 1, outlineColor, 1f);
            extractBorderBottom(graphics, ox, oy, or, ob, 1, outlineColor, 1f);
            extractBorderLeft  (graphics, ox, oy, or, ob, 1, outlineColor, 1f);
        }
    }


    protected void extractBorderTop(final UiGraphics g, final float x, final float y, final float r, final float b, final float thickness, final int color, final float alpha) {
        g.fill(x, y, r, y + thickness, color);
    }
    protected void extractBorderRight(final UiGraphics g, final float x, final float y, final float r, final float b, final float thickness, final int color, final float alpha) {
        g.fill(r, y, r - thickness, b, color);
    }
    protected void extractBorderBottom(final UiGraphics g, final float x, final float y, final float r, final float b, final float thickness, final int color, final float alpha) {
        g.fill(x, b, r, b - thickness, color);
    }
    protected void extractBorderLeft(final UiGraphics g, final float x, final float y, final float r, final float b, final float thickness, final int color, final float alpha) {
        g.fill(x, y, x + thickness, b, color);
    }
}