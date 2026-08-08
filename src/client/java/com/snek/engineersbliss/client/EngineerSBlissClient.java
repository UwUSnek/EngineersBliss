package com.snek.engineersbliss.client;

import com.snek.engineersbliss.EngineerSBliss;
import com.snek.engineersbliss.client.feature_handlers.rendering.RenderingFilterHandler;
import com.snek.engineersbliss.client.feature_handlers.rendering.ShadingFixModelPlugin;
import com.snek.engineersbliss.client.network.overlays.AttachedDataNetworkReceiver;
import com.snek.engineersbliss.client.screens.status_bar.StatusBarRenderer;
import com.snek.engineersbliss.client.custom.block_entities.renderers.ItemSinkBlockEntityRenderer;
import com.snek.engineersbliss.client.custom.block_entities.renderers.SceneSnapshot;
import com.snek.engineersbliss.client.custom.block_entities.renderers.SceneSnapshotTexture;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesModelPlugin;
import com.snek.engineersbliss.client.feature_handlers.creative_tweaks.CreativeTweaksClientHandler;
import com.snek.engineersbliss.client.feature_handlers.custom_items.UnshadedBlockModelPlugin;
import com.snek.engineersbliss.client.feature_handlers.overlays.OverlaysHandler;
import com.snek.engineersbliss.client.feature_handlers.overlays.renderer.OverlayRenderer;
import com.snek.engineersbliss.client.utils.MinecraftUtils;
import com.snek.engineersbliss.client.utils.NetworkUtils;
import com.snek.engineersbliss.custom.block_entities.CustomBlockEntityHandler;
import com.snek.engineersbliss.utils.scheduler.ClientScheduler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.Identifier;




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


        // Initialize custom block renderer plugin and custom block entity renderers
        ModelLoadingPlugin.register(new UnshadedBlockModelPlugin());
        // CustomBlockEntityHandler.initClient();
//FIXME move to ClientCustomBlockEntityHandler
BlockEntityRenderers.register(CustomBlockEntityHandler.ITEM_SINK, ItemSinkBlockEntityRenderer::new);
        //! Item and block registration is done on the server side
//TODO move this somewhere else. texture registration
ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
    SceneSnapshot.init(client.getWindow().getWidth(), client.getWindow().getHeight());
    Minecraft.getInstance().getTextureManager().register(ItemSinkBlockEntityRenderer.SCENE_COLOR_ID, new SceneSnapshotTexture(SceneSnapshot.getColor()));
    Minecraft.getInstance().getTextureManager().register(ItemSinkBlockEntityRenderer.SCENE_DEPTH_ID, new SceneSnapshotTexture(SceneSnapshot.getDepth()));
});

        // Initialize resource plugin for alt textures handler
        PreparableModelLoadingPlugin.register(
            AltTexturesModelPlugin::discoverModels,
            new AltTexturesModelPlugin()
        );


        // Initialize handlers
        RenderingFilterHandler.init(false, true, true, true, true); //TODO add to filter presets
        OverlaysHandler.init();


        // Register overlay renderers
        OverlayRenderer.register();


        // Register CreativeTweaksClientHandler
        CreativeTweaksClientHandler.register();


        // Register network receivers
        AttachedDataNetworkReceiver.register();


        // Register status bar GUI renderer
        StatusBarRenderer.register();


        // Log library loading
        EngineerSBliss.LOGGER.info(EngineerSBliss.MOD_NAME + " client loaded :3");
    }
}