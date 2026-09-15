package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class CrimsonStandingSignPartProvider extends __base_StandingSignPartProvider {

    @Override
    protected String getSignMaterialName() {
        return "crimson";
    }

    @Override
    public Block getBlock() {
        return BlockSets.SIGN.standing().floor().crimson();
    }
}
