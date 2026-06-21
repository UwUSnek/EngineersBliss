package com.snek.engineersbliss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.engineersbliss.network.overlay_data.payloads.ComparatorUpdatePayload;
import com.snek.engineersbliss.network.overlay_data.payloads.RailUpdatePayload;




public class EngineerSBliss implements ModInitializer {
    public static final String MOD_ID = "engineers-bliss";
    public static final Logger LOGGER = LoggerFactory.getLogger("Engineer's Bliss");

    @Override
    public void onInitialize() {

        // Register network payloads
        PayloadTypeRegistry.clientboundPlay().register(ComparatorUpdatePayload.TYPE, ComparatorUpdatePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(      RailUpdatePayload.TYPE,       RailUpdatePayload.CODEC);

        // Log library loading
        LOGGER.info("Engineer's Bliss server loaded :3");
    }
}