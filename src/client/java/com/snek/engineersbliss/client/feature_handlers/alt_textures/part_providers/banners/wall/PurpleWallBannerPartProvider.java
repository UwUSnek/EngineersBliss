package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class PurpleWallBannerPartProvider extends __base_WallBannerPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.PURPLE_WALL_BANNER;
    }

    @Override
    protected String getColorName() {
        return "purple";
    }
}
