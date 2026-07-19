package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import org.jetbrains.annotations.NotNull;

import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerFeatureSet;
import com.snek.engineersbliss.network.creative_tweaks.payloads.InteractionRadiusChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.payloads.ReachDistanceChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.payloads.CreativeTweaksToggleFeaturesUpdateRequestPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;





public class CreativeTweaksClientHandler {
    private CreativeTweaksClientHandler() { }
    private static final float DEFAULT_FLYING_SPEED = new Abilities().getFlyingSpeed();





    public static void onFlyingSpeedChange(final Float value) {
        final @NotNull Player player = Minecraft.getInstance().player;
        if(player != null) {
            if(!player.getAbilities().instabuild) return;
            player.getAbilities().setFlyingSpeed(value * DEFAULT_FLYING_SPEED);
        }
    }


    //TODO add a disclaimer that says this needs the mod installed on the server
    // public static void onReachDistanceChange(final Float value) {


    //TODO add a disclaimer that says this needs the mod installed on the server
    // public static void onInteractionRadiusChanged(final Integer value) {
}
