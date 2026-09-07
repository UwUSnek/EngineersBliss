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


    private static void render(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) { //FIXME this might need the custom UiGraphics
        if(StatusBarHandler.shouldRender()) {
            final int top    = StatusBarHandler.calcTop();    //FIXME this is prob fine as int bc the in game bar is always int and doesnt change with GUI scale
            final int bottom = StatusBarHandler.calcBottom(); //FIXME this is prob fine as int bc the in game bar is always int and doesnt change with GUI scale
            final int width  = StatusBarHandler.getWidth();   //FIXME this is prob fine as int bc the in game bar is always int and doesnt change with GUI scale
            graphics.fill(0, top, width, bottom, Layout.statusBarBgColor);
        }
    }
}

