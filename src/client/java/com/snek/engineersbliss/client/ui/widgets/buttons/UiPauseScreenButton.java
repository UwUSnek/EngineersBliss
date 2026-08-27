package com.snek.engineersbliss.client.ui.widgets.buttons;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.ui.data_types.TextAlignment;
import com.snek.engineersbliss.client.utils.UiTxt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;








public class UiPauseScreenButton extends UiButton {
    private static final Consumer<UiButton> createPressCallback(final Supplier<Screen> screenFactory) {
        return sf -> {
            Minecraft.getInstance().setScreen(screenFactory.get());
            sf.setFocused(false);
        };
    }


    public UiPauseScreenButton(final Screen screen, final UiTxt label, final @Nullable String spriteName, final Supplier<Screen> screenFactory, final char key) {
        super(screen, label, createPressCallback(screenFactory), key, TextAlignment.LEFT);
        finalizeInit(spriteName);
    }
    public UiPauseScreenButton(final Screen screen, final UiTxt label, final @Nullable String spriteName, final Supplier<Screen> screenFactory) {
        super(screen, label, createPressCallback(screenFactory), TextAlignment.LEFT);
        finalizeInit(spriteName);
    }


    private void finalizeInit(final @Nullable String spriteName) {
        final Identifier bgSpriteId = spriteName == null ? null : Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "pause_screen/" + spriteName);
        withSpriteBg(bgSpriteId, 4f, 1f + getLeftLabelMargin());
    }
}
