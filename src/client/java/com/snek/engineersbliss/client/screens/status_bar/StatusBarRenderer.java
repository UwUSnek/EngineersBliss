package com.snek.engineersbliss.client.screens.status_bar;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.Layout;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;





public class StatusBarRenderer {
    private StatusBarRenderer() {}
    public static final int STATUS_BAR_HEIGHT = 10;



    public static void register() {
        HudElementRegistry.addLast(
            Identifier.fromNamespaceAndPath(EngineerSBliss.MOD_ID, "status_bar"),
            StatusBarRenderer::render
        );
    }


    private static void render(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        graphics.fill(0, height - STATUS_BAR_HEIGHT, width, height, Layout.statusBarBgColor);
    }
}

