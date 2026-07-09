package com.snek.engineersbliss.client.mixin.screens;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.InputConstants;
import com.snek.engineersbliss.client.screens.alt_textures.AltTexturesScreen;
import com.snek.engineersbliss.client.screens.creative_tweaks.CreativeTweaksScreen;
import com.snek.engineersbliss.client.screens.julia_set.JuliaSetScreen;
import com.snek.engineersbliss.client.screens.overlays.OverlaysScreen;
import com.snek.engineersbliss.client.utils.UiTxt;
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

    private static Button blockPropertiesButton;
    private static Button groupsButton;
    private static Button containerToolsButton;
    private static Button gameplayTweaksButton;
    private static Button creativeTweaksButton;

    private static Button renderingButton;
    private static Button overlaysButton;
    private static Button altTexturesButton;
    private static Button mufflerButton;
    private static Button actionHistory;


    protected PauseScreenMixin(final Component title) {
        super(title);
    }





    @Override
    public boolean keyPressed(final KeyEvent event) {
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
        if(event.key() == InputConstants.KEY_Y) {
            creativeTweaksButton.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        if(event.key() == InputConstants.KEY_U) {
            actionHistory.onClick(new MouseButtonEvent(0, 0, new MouseButtonInfo(0, 0)), false);
            return true;
        }
        return super.keyPressed(event);
    }




    @Inject(method = "init", at = @At("TAIL"), cancellable = false, require = 1)
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

                blockPropertiesButton = addButton("[P] Block Properties", RenderingScreen::new, x1, y + BUTTON_SPACING * 0, buttonWidth); //FIXME
                groupsButton          = addButton("[G] Groups",           RenderingScreen::new, x1, y + BUTTON_SPACING * 1, buttonWidth); //FIXME
                containerToolsButton  = addButton("[C] Container tools",  RenderingScreen::new, x1, y + BUTTON_SPACING * 2, buttonWidth); //FIXME
                gameplayTweaksButton  = addButton("[X] Gameplay tweaks",  RenderingScreen::new, x1, y + BUTTON_SPACING * 3, buttonWidth); //FIXME
                creativeTweaksButton  = addButton("[Y] Creative tweaks",  CreativeTweaksScreen::new, x1, y + BUTTON_SPACING * 4, buttonWidth);
                //FIXME add a BIG disclaimer to "gameplay tweaks" screen that says it changes game mechanics
                //FIXME anything that changes game mechanics for anything that isn't the creative player is in there (write this too)
                //FIXME move no particles to alternative texture maybe?
                //FIXME move visible block overlays to alternative texture maybe?

                renderingButton       = addButton("[R] Rendering",        RenderingScreen  ::new, x2, y + BUTTON_SPACING * 0, buttonWidth);
                overlaysButton        = addButton("[O] Overlays",         OverlaysScreen   ::new, x2, y + BUTTON_SPACING * 1, buttonWidth);
                altTexturesButton     = addButton("[T] Alt textures",     AltTexturesScreen::new, x2, y + BUTTON_SPACING * 2, buttonWidth);
                mufflerButton         = addButton("[M] Muffler",          AltTexturesScreen::new, x2, y + BUTTON_SPACING * 3, buttonWidth); //FIXME
                actionHistory         = addButton("[U] Action History",   AltTexturesScreen::new, x2, y + BUTTON_SPACING * 4, buttonWidth); //FIXME

                addButton("??", JuliaSetScreen::new, width - BUTTON_HEIGHT - BUTTON_MARGIN, height - BUTTON_HEIGHT - BUTTON_MARGIN, BUTTON_HEIGHT);
            })
        ;
    }




    private Button addButton(final String label, final Supplier<Screen> screenFactory, final int x, final int y, final int width) {
        final Button btn = Button.builder(
            new UiTxt(label).get(),
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