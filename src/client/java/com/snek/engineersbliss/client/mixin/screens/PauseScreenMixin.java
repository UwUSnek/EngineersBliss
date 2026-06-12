package com.snek.engineersbliss.client.mixin.screens;

import java.util.function.Consumer;

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
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = BUTTON_HEIGHT + 4;


    protected PauseScreenMixin(final Component title) {
        super(title);
    }




    @Inject(method = "init", at = @At("TAIL"))
    public void addButton(final CallbackInfo ci) {
        final int buttonWidth = 100;
        final int gap = 16;

        // find the first vanilla button's Y and add custom buttons based on that value
        this.children().stream()
            .filter(Button.class::isInstance)
            .map(w -> (Button) w)
            .findFirst()
            .ifPresent(first -> {
                final int x = first.getX() - buttonWidth - gap;
                final int y = first.getY();
                addButton("Rendering",        b -> this.minecraft.setScreen(new RenderingScreen(this)), x, y + BUTTON_SPACING * 0, buttonWidth);
                addButton("Groups",           b -> {},                                                  x, y + BUTTON_SPACING * 1, buttonWidth);
                addButton("Block Properties", b -> {},                                                  x, y + BUTTON_SPACING * 2, buttonWidth);
                addButton("Muffler",          b -> {},                                                  x, y + BUTTON_SPACING * 3, buttonWidth);
                addButton("Container tools",  b -> {},                                                  x, y + BUTTON_SPACING * 4, buttonWidth);
            })
        ;
    }


    private Button addButton(String label, Consumer<Button> action, int x, int y, int width) {
        Button btn = Button.builder(Component.literal(label), b -> { action.accept(b); b.setFocused(false); }).bounds(x, y, width, BUTTON_HEIGHT).build();
        return this.addRenderableWidget(btn);
    }
}