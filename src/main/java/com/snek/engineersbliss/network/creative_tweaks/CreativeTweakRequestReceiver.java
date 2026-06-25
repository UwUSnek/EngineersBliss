package com.snek.engineersbliss.network.creative_tweaks;

import com.snek.engineersbliss.network.creative_tweaks.payloads.ReachDistanceChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.request_handlers.ReachDistanceRequestHandler;
import com.snek.engineersbliss.utils.scheduler.ServerScheduler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;




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
