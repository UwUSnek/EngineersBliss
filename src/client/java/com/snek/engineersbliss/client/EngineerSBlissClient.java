package com.snek.engineersbliss.client;

import javax.imageio.spi.IIORegistry;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.network.overlays.AttachedDataNetworkReceiver;
import com.snek.engineersbliss.client.screens.Layout;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesModelPlugin;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.renderer.OverlayRenderer;
import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;




public class EngineerSBlissClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {


        // Register scheduler
        ClientTickEvents.END_CLIENT_TICK.register(_server -> {
            ClientScheduler.tick();
        });


        // Initialize utility classes
        NetworkUtils.init();


        // Register WebP ImageIO reader
        IIORegistry.getDefaultInstance().registerServiceProvider(
            new com.luciad.imageio.webp.WebPImageReaderSpi()
        );


        // Initialize resource plugin for alt textures handler
        PreparableModelLoadingPlugin.register(
            AltTexturesModelPlugin::discoverModels,
            new AltTexturesModelPlugin()
        );


        // Initialize handlers
        RenderFilterHandler.init(false, true, true, true, true); //TODO add to filter presets
        OverlaysHandler.init();


        // Register overlay renderers
        OverlayRenderer.register();


        // Register network receivers
        AttachedDataNetworkReceiver.register();


        // Log library loading
        EngineerSBliss.LOGGER.info(EngineerSBliss.MOD_NAME + " client loaded :3");
    }
}