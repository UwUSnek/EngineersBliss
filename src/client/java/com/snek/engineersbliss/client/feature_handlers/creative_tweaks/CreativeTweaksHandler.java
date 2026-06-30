package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakFeature;
import com.snek.engineersbliss.network.creative_tweaks.payloads.InteractionRadiusChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.payloads.ReachDistanceChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.payloads.CreativeTweaksToggleFeaturesUpdateRequestPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;





public class CreativeTweaksHandler {
    private CreativeTweaksHandler() { }
    private static final float DEFAULT_FLYING_SPEED = new Abilities().getFlyingSpeed();
    private static long clientFeatureMask = CreativeTweakFeature.DEFAULT_FLAGS; //TODO use this same system for the other toggle features





    public static void setFeature(final CreativeTweakFeature feature, boolean value) {
        final long lastMask = clientFeatureMask;
        final long featureBit = feature.getFlagBit();
        if(value) clientFeatureMask |= featureBit; else clientFeatureMask &= ~featureBit;
        if(clientFeatureMask != lastMask) {
        if(NetworkUtils.serverHasMod()) {
            ClientPlayNetworking.send(new CreativeTweaksToggleFeaturesUpdateRequestPayload(clientFeatureMask));
            //FIXME REMOVE THE WHOLE SERVER COORDINATION/PACKET THING FOR TOGGLE FEATURES IF NOT NEEDED
            //FIXME movement packets are client only, server just validates speed or something, idk. Slime block and sliding are already fully client side
            }
        }
    }

    /**
     * Checks if a player has the specified feature toggled ON.
     * ! This cannot be called by the server. Use CreativeTweaksServerHandler.serverPlayerHasFeature(Entity, CreativeTweakFeature) instead.
     */
    public static boolean clientPlayerHasFeature(final Entity entity, final CreativeTweakFeature feature) {
        if(entity instanceof Player player) {
            if(feature.hasFlagBit(clientFeatureMask)) {
                if(player.getAbilities().instabuild) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Checks if a player has the specified feature toggled ON.
     * ! This cannot be called by the server. Use CreativeTweaksServerHandler.serverPlayerHasFeature(Entity, CreativeTweakFeature) instead.
     */
    public static boolean clientPlayerHasFeature(final CreativeTweakFeature feature) {
        return clientPlayerHasFeature(Minecraft.getInstance().player, feature);
    }
    public static boolean shouldPlayerPhaseThroughBlocks() {
        return clientPlayerHasFeature(CreativeTweakFeature.PHASE_THROUGH_BLOCKS_FLY) && Minecraft.getInstance().player.getAbilities().flying;
    }





    public static void onFlyingSpeedChange(final Float value) {
        final Player player = Minecraft.getInstance().player;
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
