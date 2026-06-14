package com.snek.engineersbliss;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class EngineerSBliss implements ModInitializer {
    public static final String MOD_ID = "engineers-bliss";
    public static final Logger LOGGER = LoggerFactory.getLogger("Engineer's Bliss");

    @Override
    public void onInitialize() {

        // Log library loading
        LOGGER.info("Engineer's Bliss server loaded :3");
    }
}