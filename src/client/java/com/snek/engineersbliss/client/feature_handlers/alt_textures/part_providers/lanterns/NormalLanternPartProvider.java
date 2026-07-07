package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.lanterns;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class NormalLanternPartProvider extends __base_LanternPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.LANTERN;
    }

    @Override
    protected String getLanternName() {
        return "normal";
    }
}