package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.bars;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class OxidizedCopperBarsPartProvider extends __base_BarsPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.COPPER_BARS.oxidized();
        *///? } else {
            return Blocks.COPPER_BARS.weathering().oxidized();
        //? }
    }

    protected String getMaterialName() {
        return "oxidized_copper";
    }
}
