package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedExposedCopperChainPartProvider extends __base_ChainPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.COPPER_CHAIN.waxedExposed();
    }

    @Override
    protected String getChainName() {
        return "exposed_copper";
    }
}