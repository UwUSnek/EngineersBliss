package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import com.snek.engineersbliss.utils.block_groups.BlockSets;

import net.minecraft.world.level.block.Block;




public class MagentaWallBannerPartProvider extends __base_WallBannerPartProvider {

    @Override
    public Block getBlock() {
        return BlockSets.BANNER.wall().magenta();
    }

    @Override
    protected String getColorName() {
        return "magenta";
    }
}
