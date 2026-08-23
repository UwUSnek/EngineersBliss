package com.snek.engineersbliss.client.ui.widgets.base;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.widgets.misc.BgCacheWidget;
import com.snek.engineersbliss.client.ui.widgets.misc.TextureCache;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.UiTxt;
import com.snek.engineersbliss.utils.Txt;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;








public abstract class __base_UiWidget extends AbstractWidget implements BgCacheWidget, UiWidgetBase {


    // Screen reference
    private final Screen screen;
    public Screen getScreen() { return screen; }


    // Cached background
    private final TextureCache bgCache;
    private int bgColor = Layout.bgColor;
    public void setBgColor(final int newColor) {
        bgColor = newColor; markBgDirty();
    }
    @Override public TextureCache getBgTextureCache() {
        return bgCache;
    }
    @Override public int getBgBaseColor() {
        return bgColor;
    }
    @Override public boolean isGuiScaleTransitioning() {
        return (screen instanceof @NotNull __base_UiScreen uiScreen) && uiScreen.isGuiScaleTransitioning();
    }


    // Label
    private UiTxt label;
    public UiTxt getLabel() { return label; }
    public void setLabel(final UiTxt label) { this.label = (UiTxt)label.copy(); }
    public void setLabel(final Component label) { this.label = new UiTxt(label); }




    protected __base_UiWidget(final Screen screen, final UiTxt label) {
        super(50, 50, 50, 50, new Txt().get());
        this.screen = screen;
        this.label = label;
        this.bgCache = new TextureCache(screen);
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
}