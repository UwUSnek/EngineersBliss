package com.snek.engineersbliss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.engineersbliss.client.utils.scheduler.Scheduler;




public class EngineerSBliss implements ModInitializer {
    public static final String MOD_ID = "engineers-bliss";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {


        // Register scheduler
        ServerTickEvents.END_SERVER_TICK.register(_server -> {
            Scheduler.tick();
        });

        // Log library loading
        LOGGER.info("Engineer's Bliss loaded :3");
    }
}