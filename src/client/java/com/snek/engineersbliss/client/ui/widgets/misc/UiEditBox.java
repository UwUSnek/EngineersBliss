package com.snek.engineersbliss.client.ui.widgets.misc;

import com.snek.engineersbliss.client.ui.font.Fonts;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;








public class UiEditBox extends EditBox implements BgCacheWidget {

    // Cached textures
    private final TextureCache bgCache;
    @Override public TextureCache getBgTextureCache() { return bgCache; }




    public UiEditBox(final Screen screen, final int x, final int y, final int width, final int height, final Component narration) {
        super(Fonts.ui.regular.get(1f).getFont(), x, y, width, height, narration);
        this.setTextShadow(false);
        this.addFormatter((text, offset) ->
            FormattedCharSequence.forward(text, Style.EMPTY.withFont(Fonts.ui.regular.get(1f).getDescription()))
        );
        bgCache = new TextureCache(screen);
    }




    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        BgCacheWidget.super.extractBackground(graphics, mouseX, mouseY, a);
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
    }
}