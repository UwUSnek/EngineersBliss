package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class OxidizedCopperChainPartProvider extends __base_ChainPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.COPPER_CHAIN.oxidized();
        //? } else {
            /*return Blocks.COPPER_CHAIN.weathering().oxidized();
        *///? }
    }

    @Override
    protected String getChainName() {
        return "oxidized_copper";
    }
}