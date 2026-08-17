package com.snek.engineersbliss.client.feature_handlers.status_bar;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.feature_handlers.settings.SettingsServerFeatureSet;

import net.minecraft.client.Minecraft;




/**
 * Handles status bar positioning and display logic
 */
public class StatusBarHandler {
    private StatusBarHandler() {}


    public static boolean shouldRender() {
        return !(
            Minecraft.getInstance().level == null ||
            !MinecraftUtils.isCreativeMode() ||
            (ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.CHAT_HIDES_STATUS_BAR) && MinecraftUtils.isChatOpen())
        );
    }


    public static boolean isBottom() {
        return !ClientFeatureSync.getFeatureB(SettingsServerFeatureSet.STATUS_BAR_POSITION);
    }


    public static int getHeight() {
        return SettingsServerFeatureSet.STATUS_BAR_HEIGHT.getValues().get(ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.STATUS_BAR_HEIGHT));
    }




    public static int calcTop() {
        final @NotNull Minecraft mc = Minecraft.getInstance();
        final int height = mc.getWindow().getGuiScaledHeight();
        return isBottom() ? height - getHeight() : 0;
    }
    public static int calcBottom() {
        final @NotNull Minecraft mc = Minecraft.getInstance();
        final int height = mc.getWindow().getGuiScaledHeight();
        return isBottom() ? height : getHeight();
    }
    public static int getWidth() {
        final @NotNull Minecraft mc = Minecraft.getInstance();
        return mc.getWindow().getGuiScaledWidth();
    }
}
