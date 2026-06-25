package com.snek.engineersbliss.network.creative_tweaks;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;
import com.snek.engineersbliss.network.creative_tweaks.payloads.InteractionRadiusChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.payloads.ReachDistanceChangeRequestPayload;
import com.snek.engineersbliss.utils.scheduler.ServerScheduler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;




public class CreativeTweakRequestReceiver {
    private CreativeTweakRequestReceiver() {}



    public static void register() {
        //! Flying speed is set by the client

        ServerPlayNetworking.registerGlobalReceiver(ReachDistanceChangeRequestPayload.TYPE, (payload, context) -> {
            ServerScheduler.run(() -> {
                CreativeTweaksServerHandler.updateReachDistance(context.player(), payload.reach());
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(InteractionRadiusChangeRequestPayload.TYPE, (payload, context) -> {
            ServerScheduler.run(() -> {
                CreativeTweaksServerHandler.updateInteractionRadius(context.player(), payload.radius());
            });
        });
    }
}
