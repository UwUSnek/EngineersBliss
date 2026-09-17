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

    //! Reference bar height in screen pixels at GUI scale 1. STATUS_BAR_HEIGHT values are multipliers of this base size.
    public static final int DEFAULT_BAR_HEIGHT_PX = 24;


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




    //! Return value depends on the current Vanilla GUI Scale
    public static int getVanillaHeight () { return Math.round(getHeight () / MinecraftUtils.getVanillaGuiScale());}
    public static int calcVanillaTop   () { return Math.round(calcTop   () / MinecraftUtils.getVanillaGuiScale());}
    public static int calcVanillaBottom() { return Math.round(calcBottom() / MinecraftUtils.getVanillaGuiScale());}
    public static int getVanillaWidth  () { return Math.round(getWidth  () / MinecraftUtils.getVanillaGuiScale());}




    public static float getHeight() {
        final float heightMultiplier = SettingsServerFeatureSet.STATUS_BAR_HEIGHT.getValues().get(ClientFeatureSync.getFeatureI(SettingsServerFeatureSet.STATUS_BAR_HEIGHT));
        final float realPixelHeight = DEFAULT_BAR_HEIGHT_PX * heightMultiplier;
        return Math.max(1, Math.round(realPixelHeight));
    }
    public static float calcTop() {
        final @NotNull Minecraft mc = Minecraft.getInstance();
        final int height = mc.getWindow().getHeight();
        return isBottom() ? height - getHeight() : 0;
    }
    public static float calcBottom() {
        final @NotNull Minecraft mc = Minecraft.getInstance();
        final int height = mc.getWindow().getHeight();
        return isBottom() ? height : getHeight();
    }
    public static float getWidth() {
        final @NotNull Minecraft mc = Minecraft.getInstance();
        return mc.getWindow().getWidth();
    }
}