package com.snek.engineersbliss.client.ui.widgets.base;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.data_types.TextAlignmentY;
import com.snek.engineersbliss.client.ui.data_types.UiSize;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;
import com.snek.engineersbliss.client.ui.widgets.misc.BgCacheWidget;
import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;








public abstract class __base_UiWidget extends __base_UiLayoutElm implements BgCacheWidget {
    private static final int SCROLL_PAUSE_MS = 1000;
    private static final int SCROLL_SPEED    = 20;  // The scroll speed, in pixels/s




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
        playUiSound(CUSTOM_TYPE_SOUND, 1f, 0.75f);
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
            bgCache = new TextureCache(getScreen());
            //! Creating a texture for each element is slow but not that important.
            //! Subclasses expect a texture to be available. Allocating the texture selectively would only create issues.
        }
        return bgCache;
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
    private int borderTop    = 0;
    private int borderRight  = 0;
    private int borderBottom = 0;
    private int borderLeft   = 0;
    private int borderTopColor    = Layout.borderColor;
    private int borderRightColor  = Layout.borderColor;
    private int borderBottomColor = Layout.borderColor;
    private int borderLeftColor   = Layout.borderColor;
    public void setBorderTopPx      (final int    px) { borderTop         = px;    }
    public void setBorderRightPx    (final int    px) { borderRight       = px;    }
    public void setBorderBottomPx   (final int    px) { borderBottom      = px;    }
    public void setBorderLeftPx     (final int    px) { borderLeft        = px;    }
    public void setBorderTopColor   (final int color) { borderTopColor    = color; }
    public void setBorderRightColor (final int color) { borderRightColor  = color; }
    public void setBorderBottomColor(final int color) { borderBottomColor = color; }
    public void setBorderLeftColor  (final int color) { borderLeftColor   = color; }
    public void setBorderTop   (final int px, final int color) { setBorderTopPx   (px); setBorderTopColor   (color); }
    public void setBorderRight (final int px, final int color) { setBorderRightPx (px); setBorderRightColor (color); }
    public void setBorderBottom(final int px, final int color) { setBorderBottomPx(px); setBorderBottomColor(color); }
    public void setBorderLeft  (final int px, final int color) { setBorderLeftPx  (px); setBorderLeftColor  (color); }







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
    public void extractWidgetRenderState(UiGraphics graphics, float mouseX, float mouseY, float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        extractBackground  (graphics, mouseX, mouseY, a);
        extractLabel       (graphics, mouseX, mouseY, a);
        extractBorders     (graphics, mouseX, mouseY, a);
        extractDebugOverlay(graphics, mouseX, mouseY, a);
    }


    protected void extractLabel(UiGraphics graphics, float mouseX, float mouseY, float a) {
        if(label != null && label.length() > 0) {
            final @NotNull ScaledFont scaledFont = label.getScaledFont();
            final int lineHeight = scaledFont.getLineHeight();

            final float overflow = label.getWidth() - getInnerWidth();

            int shift = 0;
            if(overflow > 0) {
                final int scrollMs = (int)(overflow * 1000L / SCROLL_SPEED);
                final int cycleMs  = SCROLL_PAUSE_MS * 2 + scrollMs;
                final long t = System.currentTimeMillis() % cycleMs;

                if(t < SCROLL_PAUSE_MS) {
                    shift = 0;
                }
                else if(t < SCROLL_PAUSE_MS + scrollMs) {
                    shift = (int)((t - SCROLL_PAUSE_MS) * SCROLL_SPEED / 1000);
                }
                else {
                    shift = (int)overflow;
                }
            }

            final TextAlignment drawAlignment = overflow > 0 ? TextAlignment.LEFT : getAlignment();
            final int textX = (int)getInnerX();
            final int textY = switch(getVerticalAlignment()) {
                case TOP    -> (int)(getYF() + Layout.textMarginPx);
                case CENTER -> (int)(getYF() + (getHeightF() - lineHeight) / 2);
                case BOTTOM -> (int)getBottom() - lineHeight;
            };

            graphics.enableScissor((int)getInnerX(), getY(), (int)getInnerRight(), (int)getBottom());
            graphics.text(label, textX, textY, Layout.fgColor, drawAlignment, (int)getInnerWidth(), false, -shift, 0f);
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
            if(hasTop   ) extractBorderTop   (graphics, x, y, r, b, borderTop,    borderTopColor);
            if(hasRight ) extractBorderRight (graphics, x, y, r, b, borderRight,  borderRightColor);
            if(hasBottom) extractBorderBottom(graphics, x, y, r, b, borderBottom, borderBottomColor);
            if(hasLeft  ) extractBorderLeft  (graphics, x, y, r, b, borderLeft,   borderLeftColor);
        }
    }


    protected void extractDebugOverlay(UiGraphics graphics, float mouseX, float mouseY, float a) {
        if(ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.DEBUG_OVERLAYS)) {
            final int outlineColor = 0xFFFF0000;
            final float ox = getX();
            final float oy = getY();
            final float or = getRight();
            final float ob = getBottom();
            extractBorderTop   (graphics, ox, oy, or, ob, 1, outlineColor);
            extractBorderRight (graphics, ox, oy, or, ob, 1, outlineColor);
            extractBorderBottom(graphics, ox, oy, or, ob, 1, outlineColor);
            extractBorderLeft  (graphics, ox, oy, or, ob, 1, outlineColor);
        }
    }


    protected void extractBorderTop(final UiGraphics g, final float x, final float y, final float r, final float b, final float thickness, final int color) {
        g.fill(x, y, r, y + thickness, color);
    }
    protected void extractBorderRight(final UiGraphics g, final float x, final float y, final float r, final float b, final float thickness, final int color) {
        g.fill(r, y, r - thickness, b, color);
    }
    protected void extractBorderBottom(final UiGraphics g, final float x, final float y, final float r, final float b, final float thickness, final int color) {
        g.fill(x, b, r, b - thickness, color);
    }
    protected void extractBorderLeft(final UiGraphics g, final float x, final float y, final float r, final float b, final float thickness, final int color) {
        g.fill(x, y, x + thickness, b, color);
    }
}