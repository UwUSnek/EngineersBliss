package com.snek.engineersbliss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.engineersbliss.feature_handlers.ServerFeaturePlayerData;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTextureServerFeatureSet;
import com.snek.engineersbliss.feature_handlers.base.__base_ServerFeature;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweakServerFeatureSet;
import com.snek.engineersbliss.feature_handlers.creative_tweaks.CreativeTweaksServerHandler;
import com.snek.engineersbliss.feature_handlers.custom_items.CustomItemHandler;
import com.snek.engineersbliss.feature_handlers.custom_items.ModCreativeTab;
import com.snek.engineersbliss.feature_handlers.overlays.OverlayServerFeatureSet;
import com.snek.engineersbliss.network.features.ServerFeatureSync;
import com.snek.engineersbliss.network.features.payloads.BoolFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.DoubleFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.FloatFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.IntFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.features.payloads.LongFeatureUpdateRequestPayload;
import com.snek.engineersbliss.network.overlay_data.payloads.ComparatorUpdatePayload;
import com.snek.engineersbliss.network.overlay_data.payloads.RailUpdatePayload;
import com.snek.engineersbliss.utils.scheduler.ServerScheduler;




public class EngineerSBliss implements ModInitializer {
    public static final String MOD_ID   = "engineers-bliss";
    public static final String MOD_NAME = "Engineer's Bliss";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);


    @Override
    public void onInitialize() {


        // Register scheduler
        ServerTickEvents.END_SERVER_TICK.register(_server -> {
            ServerScheduler.tick();
        });


        // Initialize custom items
        CustomItemHandler.init();
        ModCreativeTab.register();


        // Register feature sets and initialize the feature system
        CreativeTweakServerFeatureSet.INSTANCE.init();
        AltTextureServerFeatureSet.INSTANCE.init();
        OverlayServerFeatureSet.INSTANCE.init();
        __base_ServerFeature.finalizeSetInits();


        // Register server feature handlers
        CreativeTweaksServerHandler.register();


        // Register server->client network payloads
        PayloadTypeRegistry.clientboundPlay().register(ComparatorUpdatePayload.TYPE, ComparatorUpdatePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(      RailUpdatePayload.TYPE,       RailUpdatePayload.CODEC);


        // Register client->server network payloads
        PayloadTypeRegistry.serverboundPlay().register(  BoolFeatureUpdateRequestPayload.TYPE,   BoolFeatureUpdateRequestPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register( FloatFeatureUpdateRequestPayload.TYPE,  FloatFeatureUpdateRequestPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(DoubleFeatureUpdateRequestPayload.TYPE, DoubleFeatureUpdateRequestPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(   IntFeatureUpdateRequestPayload.TYPE,    IntFeatureUpdateRequestPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(  LongFeatureUpdateRequestPayload.TYPE,   LongFeatureUpdateRequestPayload.CODEC);
        ServerFeatureSync.register();


        // Log library loading
        LOGGER.info(MOD_NAME + " server loaded :3");
    }
}