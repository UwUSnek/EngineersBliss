package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.bars;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class ExposedCopperBarsPartProvider extends __base_BarsPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.COPPER_BARS.exposed();
    }

    protected String getMaterialName() {
        return "exposed_copper";
    }
}
