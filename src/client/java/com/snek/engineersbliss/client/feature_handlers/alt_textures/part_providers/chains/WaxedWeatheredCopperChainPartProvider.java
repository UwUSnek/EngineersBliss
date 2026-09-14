package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedWeatheredCopperChainPartProvider extends __base_ChainPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.COPPER_CHAIN.waxedWeathered();
        //? } else {
            /*return Blocks.COPPER_CHAIN.waxed().weathered();
        *///? }
    }

    @Override
    protected String getChainName() {
        return "weathered_copper";
    }
}