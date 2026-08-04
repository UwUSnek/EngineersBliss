package com.snek.engineersbliss.client.network.overlays;

import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.ComparatorAttachedData;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.RailAttachedData;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;
import com.snek.engineersbliss.network.overlay_data.payloads.ComparatorUpdatePayload;
import com.snek.engineersbliss.network.overlay_data.payloads.RailUpdatePayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;








public class AttachedDataNetworkReceiver {
    private AttachedDataNetworkReceiver() {}



    /**
     * Registers all network receivers for overlay attached data.
     * ! Use the scheduler to run the logic on the main thread. Network Receiver runs on the network thread.
     */
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ComparatorUpdatePayload.TYPE, (payload, context) -> {
            com.snek.engineersbliss.utils.scheduler.ClientScheduler.run(() -> {
                OverlaysHandler.updateAttachedData(payload.pos(), new ComparatorAttachedData(payload.back(), payload.side(), payload.out(), payload.mode()));
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(RailUpdatePayload.TYPE, (payload, context) -> {
            ClientScheduler.run(() -> {
                OverlaysHandler.updateAttachedData(payload.pos(), new RailAttachedData(payload.input()));
            });
        });
    }
}

//TODO this might need batching. batching requires a different receiver thats able to handle batches. idk yet