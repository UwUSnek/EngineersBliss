package com.snek.engineersbliss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;
import com.snek.engineersbliss.network.creative_tweaks.CreativeTweakRequestReceiver;
import com.snek.engineersbliss.network.creative_tweaks.payloads.InteractionRadiusChangeRequestPayload;
import com.snek.engineersbliss.network.creative_tweaks.payloads.ReachDistanceChangeRequestPayload;
import com.snek.engineersbliss.network.overlay_data.payloads.ComparatorUpdatePayload;
import com.snek.engineersbliss.network.overlay_data.payloads.RailUpdatePayload;
import com.snek.engineersbliss.utils.scheduler.ServerScheduler;




public class EngineerSBliss implements ModInitializer {
    public static final String MOD_ID = "engineers-bliss";
    public static final Logger LOGGER = LoggerFactory.getLogger("Engineer's Bliss");


    @Override
    public void onInitialize() {


        // Register scheduler
        ServerTickEvents.END_SERVER_TICK.register(_server -> {
            ServerScheduler.tick();
        });


        // Register server feature handlers
        CreativeTweaksServerHandler.register();


        // Register server->client network payloads
        PayloadTypeRegistry.clientboundPlay().register(ComparatorUpdatePayload.TYPE, ComparatorUpdatePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(      RailUpdatePayload.TYPE,       RailUpdatePayload.CODEC);


        // Register client->server network payloads
        PayloadTypeRegistry.serverboundPlay().register(    ReachDistanceChangeRequestPayload.TYPE,     ReachDistanceChangeRequestPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(InteractionRadiusChangeRequestPayload.TYPE, InteractionRadiusChangeRequestPayload.CODEC);
        CreativeTweakRequestReceiver.register();


        // Log library loading
        LOGGER.info("Engineer's Bliss server loaded :3");
    }
}