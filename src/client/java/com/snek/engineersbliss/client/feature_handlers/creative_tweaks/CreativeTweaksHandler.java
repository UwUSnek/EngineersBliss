package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

import java.util.EnumMap;
import java.util.Map;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
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


    // private static Map<CreativeTweakFeature, Boolean> features = new EnumMap<>(CreativeTweakFeature.class); //TODO remove
    private static long clientFeatureMask = 0;
    // public static void init(){
    //     for(CreativeTweakFeature feature : CreativeTweakFeature.values()) {
    //         features.put(feature, false);
    //     }
    // }
    // public long createClientFeatureMask() {
    //     final long r = 0;
    //     for(var entry : features.entrySet()) {
    //         if(entry.getValue().booleanValue()) {
    //             r |=
    //         }
    //     }
    // }





    //TODO add a disclaimer that says all of these toggle features require the mod to be installed on the server
    public static void setFeature(final CreativeTweakFeature feature, boolean value) {
        // features.put(feature, value);
        final long lastMask = clientFeatureMask;
        final long featureBit = feature.getFlagBit();
        if(value) clientFeatureMask |= featureBit; else clientFeatureMask &= ~featureBit;
        if(clientFeatureMask != lastMask) {
        if(NetworkUtils.serverHasMod()) {
            ClientPlayNetworking.send(new CreativeTweaksToggleFeaturesUpdateRequestPayload(clientFeatureMask));
        }
        }
    }

    //TODO add to feature enum
    //TODO public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }
    public static boolean hasFeature(final CreativeTweakFeature feature) {
        // return features.get(feature);
        return feature.hasFlagBit(clientFeatureMask);
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
