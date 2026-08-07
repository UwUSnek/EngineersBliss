package com.snek.engineersbliss.client.screens.status_bar;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.feature_handlers.status_bar.StatusBarHandler;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
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
        if(StatusBarHandler.shouldRender()) {
            final int top    = StatusBarHandler.calcTop();
            final int bottom = StatusBarHandler.calcBottom();
            final int width  = StatusBarHandler.getWidth();
            graphics.fill(0, top, width, bottom, Layout.statusBarBgColor);
        }
    }
}

