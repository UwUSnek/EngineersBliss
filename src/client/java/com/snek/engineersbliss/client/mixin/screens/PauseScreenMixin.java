package com.snek.engineersbliss.client.mixin.screens;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.client.screens.alt_textures.AltTexturesScreen;
import com.snek.engineersbliss.client.screens.julia_set.JuliaSetScreen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.screens.rendering.RenderingScreen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;




@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_MARGIN = 4;
    private static final int BUTTON_SPACING = BUTTON_HEIGHT + BUTTON_MARGIN;

    private static Button renderingButton;
    private static Button overlaysButton;
    private static Button groupsButton;
    private static Button altTexturesButton;

    private static Button blockPropertiesButton;
    private static Button mufflerButton;
    private static Button containerToolsButton;
    private static Button gameplayTweaksButton;


    protected PauseScreenMixin(final Component title) {
        super(title);
    }





    @Override
    public boolean keyPressed(KeyEvent event) {
        if(event.key() == InputConstants.KEY_R) {
            renderingButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_O) {
            overlaysButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_G) {
            groupsButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_T) {
            altTexturesButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_P) {
            blockPropertiesButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_M) {
            mufflerButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_C) {
            containerToolsButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_X) {
            gameplayTweaksButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        return super.keyPressed(event);
    }




    @Inject(method = "init", at = @At("TAIL"))
    public void init(final CallbackInfo ci) {
        final int buttonWidth = 100;
        final int gap = 16;

        // find the first vanilla button's Y and add custom buttons based on that value
        this.children().stream()
            .filter(Button.class::isInstance)
            .map(w -> (Button) w)
            .findFirst()
            .ifPresent(first -> {
                final int x1 = first.getX() - buttonWidth - gap;
                final int x2 = x1 - buttonWidth - gap;
                final int y = first.getY();

                renderingButton       = addButton("[R] Rendering",        RenderingScreen  ::new, x1, y + BUTTON_SPACING * 0, buttonWidth);
                overlaysButton        = addButton("[O] Overlays",         OverlaysScreen   ::new, x1, y + BUTTON_SPACING * 1, buttonWidth);
                groupsButton          = addButton("[G] Groups",           RenderingScreen  ::new, x1, y + BUTTON_SPACING * 2, buttonWidth);
                altTexturesButton     = addButton("[T] Alt textures",     AltTexturesScreen::new, x1, y + BUTTON_SPACING * 3, buttonWidth);

                blockPropertiesButton = addButton("[P] Block Properties", RenderingScreen::new, x2, y + BUTTON_SPACING * 0, buttonWidth);
                mufflerButton         = addButton("[M] Muffler",          RenderingScreen::new, x2, y + BUTTON_SPACING * 1, buttonWidth);
                containerToolsButton  = addButton("[C] Container tools",  RenderingScreen::new, x2, y + BUTTON_SPACING * 2, buttonWidth);
                gameplayTweaksButton  = addButton("[X] Gameplay tweaks",  RenderingScreen::new, x2, y + BUTTON_SPACING * 3, buttonWidth);

                addButton("??",               JuliaSetScreen::new, width - BUTTON_HEIGHT - BUTTON_MARGIN, height - BUTTON_HEIGHT - BUTTON_MARGIN, BUTTON_HEIGHT);
            })
        ;
    }




    private Button addButton(String label, Supplier<Screen> screenFactory, int x, int y, int width) {
        Button btn = Button.builder(
            Component.literal(label),
                b -> {
                minecraft.setScreen(screenFactory.get());
                b.setFocused(false);
            })
            .bounds(x, y, width, BUTTON_HEIGHT)
            .build()
        ;
        return this.addRenderableWidget(btn);
    }
}