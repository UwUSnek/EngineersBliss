package com.snek.engineersbliss.client.feature_handlers.creative_tweaks;

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


    private static long clientFeatureMask = 0
    |   CreativeTweakFeature.PHASE_THROUGH_BLOCKS_FLY     .getFlagBit()
    |   CreativeTweakFeature.PHASE_THROUGH_ENTITIES       .getFlagBit()
    |   CreativeTweakFeature.DISABLE_HONEY_JUMP           .getFlagBit()
    |   CreativeTweakFeature.DISABLE_HONEY_SLIDING        .getFlagBit()
    |   CreativeTweakFeature.DISABLE_HONEY_SLOWDOWN       .getFlagBit()
    |   CreativeTweakFeature.DISABLE_SLIME_BOUNCE         .getFlagBit()
    |   CreativeTweakFeature.DISABLE_SLIME_SLOWDOWN       .getFlagBit()
    |   CreativeTweakFeature.DISABLE_SOULSAND_SLOWDOWN    .getFlagBit()
    |   CreativeTweakFeature.DISABLE_ICE_SLIDING          .getFlagBit()
    |   CreativeTweakFeature.DISABLE_CURRENT_DRAG         .getFlagBit()

    |   CreativeTweakFeature.DISABLE_ITEM_CHANGE_ANIMATION.getFlagBit() &0
    |   CreativeTweakFeature.DISABLE_HAND_SWING_ANIMATION .getFlagBit() &0
    |   CreativeTweakFeature.DISABLE_FIRE_EFFECT          .getFlagBit()
    |   CreativeTweakFeature.DISABLE_FREEZING_EFFECT      .getFlagBit()
    |   CreativeTweakFeature.DISABLE_NETHER_PORTAL_OVERLAY.getFlagBit()
    |   CreativeTweakFeature.DISABLE_WATER_FOV_CHANGE     .getFlagBit()
    |   CreativeTweakFeature.DISABLE_WATER_OVERLAY        .getFlagBit()
    |   CreativeTweakFeature.DISABLE_LAVA_OVERLAY         .getFlagBit()
    //TODO maybe add a disable snow overlay? idk if freezing is linked to the overlay or if they are separate things
    ;





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


    //TODO add to feature enum
    //TODO public boolean hasFlagBit(final long mask) { return (mask & flagBit) != 0; }
    public static boolean hasFeature(final CreativeTweakFeature feature) {
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
