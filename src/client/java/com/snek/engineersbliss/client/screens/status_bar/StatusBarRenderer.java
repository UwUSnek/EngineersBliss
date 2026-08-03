package com.snek.engineersbliss.client.screens.status_bar;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.utils.Layout;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;

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

        // Skip rendering if the player is not in Creative mode
        // Also skip rendering if the player has the chat open and the "Chat hides Status Bar" setting ON
        if(
            !MinecraftUtils.isCreativeMode() ||
            (ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.CHAT_HIDES_STATUS_BAR) && MinecraftUtils.isChatOpen())
        ) {
            return;
        }

        final @NotNull Minecraft mc = Minecraft.getInstance();
        final int width     = mc.getWindow().getGuiScaledWidth();
        final int height    = mc.getWindow().getGuiScaledHeight();
        final int barHeight = ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.STATUS_BAR_HEIGHT);
        final boolean isPositionTop = ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.STATUS_BAR_POSITION);
        final int barTop    = isPositionTop ? 0 : height - barHeight;
        final int barBottom = isPositionTop ? barHeight : height;

        graphics.fill(0, barTop, width, barBottom, Layout.statusBarBgColor);
    }
}

