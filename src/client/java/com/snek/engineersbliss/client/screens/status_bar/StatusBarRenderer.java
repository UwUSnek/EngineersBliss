package com.snek.engineersbliss.client.screens.status_bar;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.platform.Window;
import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.feature_handlers.status_bar.StatusBarHandler;
import com.snek.engineersbliss.client.ui.renderer.UiGraphics;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;





public class StatusBarRenderer {
    private StatusBarRenderer() {}


    public static void register() {
        HudElementRegistry.addLast(
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "status_bar"),
            StatusBarRenderer::render
        );
    }


    private static void render(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        final @NotNull Window window = Minecraft.getInstance().getWindow();
        final int width  =  window.getWidth();
        final int height = window.getHeight();
        graphics.pose().pushMatrix();
        graphics.pose().scale(1f / window.getGuiScale());
        __internal_render(new UiGraphics(graphics, () -> width, () -> height), tickCounter);
        graphics.pose().popMatrix();
    }


    private static void __internal_render(UiGraphics graphics, DeltaTracker tickCounter) {
        if(StatusBarHandler.shouldRender()) {
            final float top    = StatusBarHandler.calcTop();
            final float bottom = StatusBarHandler.calcBottom();
            final float width  = StatusBarHandler.getWidth();
            graphics.fill(0, top, width, bottom, Layout.statusBarBgColor);
        }
    }
}

