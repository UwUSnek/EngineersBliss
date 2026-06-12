package com.snek.engineersbliss.client;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.utils.scheduler.Scheduler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;




public class EngineerSBlissClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Initialize filter handler
        RenderFilterHandler.init(false);


        // Register scheduler
        ServerTickEvents.END_SERVER_TICK.register(_server -> {
            Scheduler.tick();
        });


        // Log library loading
        EngineerSBliss.LOGGER.info("Engineer's Bliss client loaded :3");
    }
}