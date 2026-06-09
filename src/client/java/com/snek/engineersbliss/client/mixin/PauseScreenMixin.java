package com.snek.engineersbliss.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.engineersbliss.client.screens.rendering.RenderingScreen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;




@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addButton(CallbackInfo ci) {
        int buttonWidth = 100;
        int gap = 16;

        // find the first vanilla button's Y
        this.children().stream()
            .filter(Button.class::isInstance)
            .map(w -> (Button) w)
            .findFirst()
            .ifPresent(first -> {
                int vanillaX = first.getX();
                int startY = first.getY();

                this.addRenderableWidget(Button.builder(
                    Component.literal("Rendering"),
                    btn -> this.minecraft.setScreen(new RenderingScreen(this)))
                    .bounds(vanillaX - buttonWidth - gap, startY, buttonWidth, 20)
                    .build()
                );
                this.addRenderableWidget(Button.builder(
                    Component.literal("Groups"),
                    btn -> { /* TODO */ })
                    .bounds(vanillaX - buttonWidth - gap, startY + 24, buttonWidth, 20)
                    .build()
                );
                this.addRenderableWidget(Button.builder(
                    Component.literal("Block Properties"),
                    btn -> { /* TODO */ })
                    .bounds(vanillaX - buttonWidth - gap, startY + 48, buttonWidth, 20)
                    .build()
                );
            })
        ;
    }
}