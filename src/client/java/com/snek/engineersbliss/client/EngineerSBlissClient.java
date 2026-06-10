package com.snek.engineersbliss.client;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;




public class EngineerSBlissClient implements ClientModInitializer {



    @Override
    public void onInitializeClient() {
        RenderFilterHandler.init(false);
    }
}