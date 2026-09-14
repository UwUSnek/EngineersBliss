package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedExposedCopperChainPartProvider extends __base_ChainPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.COPPER_CHAIN.waxedExposed();
        *///? } else {
            return Blocks.COPPER_CHAIN.waxed().exposed();
        //? }
    }

    @Override
    protected String getChainName() {
        return "exposed_copper";
    }
}