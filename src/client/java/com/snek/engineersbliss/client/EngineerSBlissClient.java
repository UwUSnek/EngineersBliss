package com.snek.engineersbliss.client;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.utils.scheduler.Scheduler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;




public class EngineerSBlissClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Initialize filter handler
        RenderFilterHandler.init(false);


        // Register scheduler
        ClientTickEvents.END_CLIENT_TICK.register(_server -> {
            Scheduler.tick();
        });


        // Log library loading
        EngineerSBliss.LOGGER.info("Engineer's Bliss client loaded :3");
    }
}