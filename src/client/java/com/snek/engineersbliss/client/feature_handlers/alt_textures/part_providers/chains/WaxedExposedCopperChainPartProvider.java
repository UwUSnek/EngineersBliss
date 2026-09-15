package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class WaxedExposedCopperChainPartProvider extends __base_ChainPartProvider {

    @Override
    public Block getBlock() {
        return BlockSets.CHAIN.waxed().exposed();
    }

    @Override
    protected String getChainName() {
        return "exposed_copper";
    }
}