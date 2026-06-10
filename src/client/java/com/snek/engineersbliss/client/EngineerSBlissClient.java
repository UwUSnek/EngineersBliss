package com.snek.engineersbliss.client;

import com.snek.engineersbliss.client.rendering.RenderFilterHandler;

import net.fabricmc.api.ClientModInitializer;




public class EngineerSBlissClient implements ClientModInitializer {



    @Override
    public void onInitializeClient() {
        RenderFilterHandler.init();
    }
}