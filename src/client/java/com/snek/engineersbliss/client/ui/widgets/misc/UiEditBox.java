package com.snek.engineersbliss.client.ui.widgets.misc;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.ui.base.__base_UiScreen;
import com.snek.engineersbliss.client.ui.font.Fonts;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.ui.widgets.base.UiWidgetBase;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;








public class UiEditBox extends EditBox implements BgCacheWidget, UiWidgetBase {

    // Cached textures
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

    // Screen reference
    private final Screen screen;
    public Screen getScreen() { return screen; }




    public UiEditBox(final Screen screen, final Component narration) {
        super(Fonts.ui.regular.get(1f).getFont(), 50, 50, 50, 50, narration);
        this.screen = screen;
        this.setTextShadow(false);
        this.addFormatter((text, offset) ->
            FormattedCharSequence.forward(text, Style.EMPTY.withFont(Fonts.ui.regular.get(1f).getDescription()))
        );
        bgCache = new TextureCache(screen);
    }




    @Override
    public @Nullable List<?> children() {
        return null;
    }




    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        BgCacheWidget.super.extractBackground(graphics, mouseX, mouseY, a);
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
    }
}