package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class GrayWallBannerPartProvider extends __base_WallBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            // return Blocks.GRAY_WALL_BANNER;
        //? } else {
            return Blocks.WALL_BANNER.gray();
        //? }
    }

    @Override
    protected String getColorName() {
        return "gray";
    }
}
