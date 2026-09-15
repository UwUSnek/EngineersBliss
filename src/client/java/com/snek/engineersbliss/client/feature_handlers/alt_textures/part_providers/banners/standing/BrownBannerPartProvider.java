package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class BrownBannerPartProvider extends __base_StandingBannerPartProvider {

    @Override
    public Block getBlock() {
        return BlockSets.BANNER.floor().brown();
    }

    @Override
    protected String getColorName() {
        return "brown";
    }
}
