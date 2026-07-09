package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.lanterns;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class OxidizedCopperLanternPartProvider extends __base_LanternPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.COPPER_LANTERN.oxidized();
    }

    @Override
    protected String getLanternName() {
        return "oxidized_copper";
    }
}