package com.snek.engineersbliss.client.ui.widgets.base;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.ui.font.ScaledFont;
import com.snek.engineersbliss.client.ui.widgets.misc.BgCacheWidget;
import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.RenderingUtils;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;








public abstract class __base_UiWidget extends AbstractWidget implements BgCacheWidget, UiWidgetBase {

    // Screen reference
    private final Screen screen;
    public Screen getScreen() { return screen; }
    public boolean isGuiScaleTransitioning() {
        return (screen instanceof @NotNull __base_UiScreen uiScreen) && uiScreen.isGuiScaleTransitioning();
    }


    // Cached background
    private final TextureCache bgCache;
    private int bgColor = Layout.bgColor;
    public void setBgColor(final int newColor) {
        bgColor = newColor;
        markBgDirty();
    }
    @Override public TextureCache getBgTextureCache() { return bgCache; }
    @Override public int getBgBaseColor() { return bgColor; }


    // Label
    private UiTxt label;
    public UiTxt getLabel() { return label; }
    public void setLabel(final UiTxt     label) { this.label = (UiTxt) label.copy(); }
    public void setLabel(final Component label) { this.label = new UiTxt(label); }


    // Label layout
    private TextAlignment alignment;
    public TextAlignment getAlignment() { return alignment; }
    public void setAlignment(final TextAlignment alignment) { this.alignment = alignment; }
    private float labelOffset;
    public float getLabelOffset() { return labelOffset; }
    public void setLabelOffset(final float labelOffset) { this.labelOffset = labelOffset; }


    // Relayout handling
    private boolean selfRelayoutDisabled;
    private boolean contentRelayoutDisabled;
    private boolean relayoutDisabled;
    public void    disableSelfRelayout() {    selfRelayoutDisabled = true;  }
    public void     enableSelfRelayout() {    selfRelayoutDisabled = false; }
    public void disableContentRelayout() { contentRelayoutDisabled = true;  }
    public void  enableContentRelayout() { contentRelayoutDisabled = false; }
    public void        disableRelayout() {        relayoutDisabled = true;  }
    public void         enableRelayout() {        relayoutDisabled = false; }







    protected __base_UiWidget(final Screen screen, final UiTxt label, final TextAlignment alignment) {
        super(50, 50, 50, 50, new Txt().get());
        this.screen = screen;
        this.label = label;
        this.alignment = alignment;
        this.labelOffset = 0;
        this.bgCache = new TextureCache(screen);
        this.selfRelayoutDisabled = false;
        this.contentRelayoutDisabled = false;
        this.relayoutDisabled = false;
    }

    protected __base_UiWidget(final Screen screen, final UiTxt label) {
        this(screen, label, TextAlignment.LEFT);
    }



    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        // Draw background
        extractBackground(graphics, mouseX, mouseY, a);


        // Draw label
        final UiTxt label = getLabel();
        if(label != null && label.length() > 0) {
            final ScaledFont scaledFont = label.getScaledFont();
            final int textX = getX() + (int)(height * getLabelOffset()) + Layout.textMarginPx;
            final int textY = getY() + (height - scaledFont.getLineHeight()) / 2;
            RenderingUtils.extractTxt(graphics, label, textX, textY, Layout.fgColor, getAlignment(), width, false);
        }
    }




    // Stop vanilla's key handling from doing stupid random stuff on custom widgets.
    @Override
    public boolean keyPressed(final KeyEvent event) {
        return false;
    }

    // Forbid vanilla setMessage() in favor of setLabel()
    @Override
    public void setMessage(final Component message) {
        throw new UnsupportedOperationException("Use .setLabel(label) instead.");
    }

    @Override
    protected void updateWidgetNarration(final NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }



    //! Vanilla's hovering system checks for scissors. Unlike clicks, which don't do that, for whatever reason.
    //! Scissors always use screen coordinates because Minecraft only ever manages 1 coordinate space.
    //! They end up reporting an incorrect boundary when the custom GUI Scale doesn't match Vanilla's, making hover detection very unrealiable.
    //! This override fixes that by changing isHovered's behaviour for widgets that are children of __base_UiScreen
    //! (the only screen that can use custom scale), making it convert from screen to virtual coordinates before checking boundaries.

    @Override
    public boolean isHovered() {
        if(!isActive()) return false;
        if(screen instanceof @NotNull __base_UiScreen s) {
            return !(
                s.getMirrorHoverMouseX() <  getX()      ||
                s.getMirrorHoverMouseX() >= getRight()  ||
                s.getMirrorHoverMouseY() <  getY()      ||
                s.getMirrorHoverMouseY() >= getBottom() ||
                s.getMirrorHoverGraphics() == null      ||
                !s.getMirrorHoverGraphics().containsPointInScissor(
                    s.getMirrorHoverScreenMouseX(),
                    s.getMirrorHoverScreenMouseY()
                )
            );
        }
        else {
            return super.isHovered();
        }
    }




    @Override
    public void relayout() {
        if(!relayoutDisabled) {
            if(   !selfRelayoutDisabled) relayoutSelf();
            if(!contentRelayoutDisabled) relayoutContent();
        }
    }
}