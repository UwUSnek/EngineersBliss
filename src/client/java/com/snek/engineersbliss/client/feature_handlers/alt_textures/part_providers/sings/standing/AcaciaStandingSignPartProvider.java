package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class AcaciaStandingSignPartProvider extends __base_StandingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "acacia";
    }

    @Override
    public Block getBlock() {
        return BlockSets.SIGN.standing().floor().acacia();
    }
}
