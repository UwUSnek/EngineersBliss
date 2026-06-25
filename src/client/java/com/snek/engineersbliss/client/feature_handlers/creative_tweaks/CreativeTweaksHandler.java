package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.network.creative_tweaks.payloads.InteractionRadiusChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.payloads.ReachDistanceChangeRequestPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;





public class CreativeTweaksHandler {
    private CreativeTweaksHandler() { }

    private static final float DEFAULT_FLYING_SPEED = new Abilities().getFlyingSpeed();




    public static void onFlyingSpeedChange(final Float value) {
        final Player player = Minecraft.getInstance().player;
        if(player != null && player.getAbilities().instabuild) {
            player.getAbilities().setFlyingSpeed(value * DEFAULT_FLYING_SPEED);
        }
    }


    //TODO add a disclaimer that says this needs the mod installed on the server
    public static void onReachDistanceChange(final Float value) {
        if(NetworkUtils.serverHasMod()) {
            ClientPlayNetworking.send(new ReachDistanceChangeRequestPayload(value));
        }
    }


    //TODO add a disclaimer that says this needs the mod installed on the server
    public static void onInteractionRadiusChanged(final Integer value) {
        if(NetworkUtils.serverHasMod()) {
            ClientPlayNetworking.send(new InteractionRadiusChangeRequestPayload(value));
        }
    }
}
