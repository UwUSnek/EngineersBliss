package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.bars;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedWeatheredCopperBarsPartProvider extends __base_BarsPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.COPPER_BARS.waxedWeathered();
    }

    protected String getMaterialName() {
        return "weathered_copper";
    }
}
