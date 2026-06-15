package com.snek.engineersbliss.client.feature_handlers.alt_textures;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class AltTexturesHandler {
    private AltTexturesHandler() {}


    private static Map<Block, Boolean> features = new HashMap<>();


    public static void init(){
        features.put(Blocks.SLIME_BLOCK, true);
        features.put(Blocks.HONEY_BLOCK, true);
        features.put(Blocks.MANGROVE_ROOTS, true);
        features.put(Blocks.SCAFFOLDING, true);
        features.put(Blocks.REDSTONE_WIRE, true);
    }


    public static void setFeature(final Block block, boolean value) {
        features.put(block, value);
    }

    public static boolean getFeature(final Block block) {
        return features.get(block);
    }
}
