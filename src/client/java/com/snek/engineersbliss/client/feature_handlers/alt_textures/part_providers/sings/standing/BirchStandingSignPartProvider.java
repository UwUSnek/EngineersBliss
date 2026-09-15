package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class BirchStandingSignPartProvider extends __base_StandingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "birch";
    }

    @Override
    public Block getBlock() {
        return BlockSets.SIGN.standing().floor().birch();
    }
}
