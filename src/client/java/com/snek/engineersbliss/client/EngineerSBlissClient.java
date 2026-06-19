package com.snek.engineersbliss.client;

import javax.imageio.spi.IIORegistry;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesModelPlugin;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.attached_data.AttachedDataNetworkReceiver;
import com.snek.engineersbliss.client.feature_handlers.overlays.renderer.OverlayRenderer;
import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.client.utils.scheduler.Scheduler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;




public class EngineerSBlissClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {


        // Initialize utility classes
        NetworkUtils.init();


        // Register WebP ImageIO reader
        IIORegistry.getDefaultInstance().registerServiceProvider(
            new com.luciad.imageio.webp.WebPImageReaderSpi()
        );


        // Initialize handlers
        RenderFilterHandler.init(false, true, true, true, true); //TODO add to filter presets
        AltTexturesHandler.init();
        ModelLoadingPlugin.register(new AltTexturesModelPlugin());
        OverlaysHandler.init();


        // Register overlay renderers
        OverlayRenderer.register();


        // Register network receivers
        AttachedDataNetworkReceiver.register();


        // Register scheduler
        ClientTickEvents.END_CLIENT_TICK.register(_server -> {
            Scheduler.tick();
        });


        // Log library loading
        EngineerSBliss.LOGGER.info("Engineer's Bliss client loaded :3");
    }
}