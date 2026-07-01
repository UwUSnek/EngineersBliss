package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import org.jspecify.annotations.NonNull;

import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.network.creative_tweaks.payloads.InteractionRadiusChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.payloads.ReachDistanceChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.payloads.CreativeTweaksToggleFeaturesUpdateRequestPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;





public class CreativeTweaksHandler {
    private CreativeTweaksHandler() { }
    private static final float DEFAULT_FLYING_SPEED = new Abilities().getFlyingSpeed();
    private static long clientFeatureMask = CreativeTweakFeature.DEFAULT_FLAGS;





    public static void setFeature(final CreativeTweakFeature feature, boolean value) {

        // Update feature bit
        final long featureBit = feature.getFlagBit();
        if(value) clientFeatureMask |= featureBit; else clientFeatureMask &= ~featureBit;

        // Send an update packet to the server
        // ! Checking for changes here is pointless as any setFeature call is triggered by a feature change.
        if(NetworkUtils.serverHasMod()) {
            ClientPlayNetworking.send(new CreativeTweaksToggleFeaturesUpdateRequestPayload(clientFeatureMask));
        }
    }




    /**
     * Checks if a player has the specified feature toggled ON.
     * ! This cannot be called by the server. Use CreativeTweaksServerHandler.serverPlayerHasFeature(Entity, CreativeTweakFeature) instead.
     */
    public static boolean clientPlayerHasFeature(final Object entity, final CreativeTweakFeature feature) {
        if(entity instanceof Player player) {
            if(feature.hasFlagBit(clientFeatureMask)) {
                if(player.getAbilities().instabuild) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean shouldPlayerPhaseThroughBlocks(final Object entity) {
        return clientPlayerHasFeature(entity, CreativeTweakFeature.PHASE_THROUGH_BLOCKS_FLY) && Minecraft.getInstance().player.getAbilities().flying;
    }





    public static void onFlyingSpeedChange(final Float value) {
        final @NonNull Player player = Minecraft.getInstance().player;
        if(!player.getAbilities().instabuild) return;
        player.getAbilities().setFlyingSpeed(value * DEFAULT_FLYING_SPEED);
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
