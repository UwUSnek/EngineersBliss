package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WaxedOxidizedCopperChainPartProvider extends __base_ChainPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.COPPER_CHAIN.waxedOxidized();
    }

    @Override
    protected String getChainName() {
        return "oxidized_copper";
    }
}