package com.snek.engineersbliss.client;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderFilterHandler;
import com.snek.engineersbliss.client.feature_handlers.rendering.ShadingFixModelPlugin;
import com.snek.engineersbliss.client.network.overlays.AttachedDataNetworkReceiver;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesModelPlugin;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
import com.snek.engineersbliss.client.feature_handlers.custom_items.UnshadedBlockModelPlugin;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.renderer.OverlayRenderer;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemHandler;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.loader.api.FabricLoader;




public class EngineerSBlissClient implements ClientModInitializer {
    private static String modVersion = "";
    public  static String getModVersion() { return modVersion; }



    @Override
    public void onInitializeClient() {

        // Set mod version string
        modVersion = FabricLoader.getInstance()
            .getModContainer(EngineerSBliss.MOD_ID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("")
        ;


        // Register scheduler
        ClientTickEvents.END_CLIENT_TICK.register(_server -> {
            ClientScheduler.tick();
        });


        // Initialize utility classes
        NetworkUtils.register();
        MinecraftUtils.register();


        // Initialize block model shading fix plugin
        ModelLoadingPlugin.register(new ShadingFixModelPlugin());


        // Initialize custom block renderer plugin (for GREEN_SCREEN and BLUE_SCREEN blocks)
        ModelLoadingPlugin.register(new UnshadedBlockModelPlugin());
        //! Item and block registration is done on the server side


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


        // Register CreativeTweaksClientHandler
        CreativeTweaksClientHandler.register();


        // Register network receivers
        AttachedDataNetworkReceiver.register();
net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
    System.out.println("FINAL full_bee_nest: " + CustomItemHandler.FULL_BEE_NEST.components().get(net.minecraft.core.component.DataComponents.ITEM_MODEL));
});
        // Log library loading
        EngineerSBliss.LOGGER.info(EngineerSBliss.MOD_NAME + " client loaded :3");
    }
}