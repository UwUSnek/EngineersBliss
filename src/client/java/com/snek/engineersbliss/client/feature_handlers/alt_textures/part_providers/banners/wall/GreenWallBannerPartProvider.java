package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class GreenWallBannerPartProvider extends __base_WallBannerPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.GREEN_WALL_BANNER;
    }

    @Override
    protected String getColorName() {
        return "green";
    }
}
