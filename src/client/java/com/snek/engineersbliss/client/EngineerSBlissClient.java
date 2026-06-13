package com.snek.engineersbliss.client;

import javax.imageio.spi.IIORegistry;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.RenderFilterHandler;
import com.snek.engineersbliss.client.utils.scheduler.Scheduler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;




public class EngineerSBlissClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {


        // Register WebP ImageIO reader
        IIORegistry.getDefaultInstance().registerServiceProvider(
            new com.luciad.imageio.webp.WebPImageReaderSpi()
        );


        // Initialize filter handler
        RenderFilterHandler.init(false, true, true, true, true); //TODO add to filter presets


        // Register scheduler
        ClientTickEvents.END_CLIENT_TICK.register(_server -> {
            Scheduler.tick();
        });


        // Log library loading
        EngineerSBliss.LOGGER.info("Engineer's Bliss client loaded :3");
    }
}