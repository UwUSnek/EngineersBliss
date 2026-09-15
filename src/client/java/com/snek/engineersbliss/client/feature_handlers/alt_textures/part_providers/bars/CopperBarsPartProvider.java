package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.bars;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class CopperBarsPartProvider extends __base_BarsPartProvider {

    @Override
    public Block getBlock() {
        return BlockSets.METAL_BARS.weathering().unaffected();
    }

    protected String getMaterialName() {
        return "copper";
    }
}
