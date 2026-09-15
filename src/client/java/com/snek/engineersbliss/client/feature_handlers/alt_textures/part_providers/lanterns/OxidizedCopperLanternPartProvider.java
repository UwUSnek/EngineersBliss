package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.lanterns;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class OxidizedCopperLanternPartProvider extends __base_LanternPartProvider {

    @Override
    public Block getBlock() {
        return BlockSets.LANTERN.weathering().oxidized();
    }

    @Override
    protected String getLanternName() {
        return "oxidized_copper";
    }
}