package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.bars;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedExposedCopperBarsPartProvider extends __base_BarsPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.COPPER_BARS.waxedExposed();
        *///? } else {
            return Blocks.COPPER_BARS.waxed().exposed();
        //? }
    }

    protected String getMaterialName() {
        return "exposed_copper";
    }
}
