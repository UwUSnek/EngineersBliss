package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.bars;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedWeatheredCopperBarsPartProvider extends __base_BarsPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.COPPER_BARS.waxedWeathered();
        //? } else {
            /*return Blocks.COPPER_BARS.waxed().weathered();
        *///? }
    }

    protected String getMaterialName() {
        return "weathered_copper";
    }
}
