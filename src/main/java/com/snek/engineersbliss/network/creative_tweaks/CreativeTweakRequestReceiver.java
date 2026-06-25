package com.snek.engineersbliss.network.creative_tweaks;

import com.snek.engineersbliss.network.creative_tweaks.payloads.ReachDistanceChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.request_handlers.ReachDistanceRequestHandler;
import com.snek.engineersbliss.utils.scheduler.ServerScheduler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;




public class CreativeTweakRequestReceiver {
    private CreativeTweakRequestReceiver() {}



    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ReachDistanceChangeRequestPayload.TYPE, (payload, context) -> {
            ServerScheduler.run(() -> {
                ReachDistanceRequestHandler.handle(payload, context.player());
            });
        });
    }
}
