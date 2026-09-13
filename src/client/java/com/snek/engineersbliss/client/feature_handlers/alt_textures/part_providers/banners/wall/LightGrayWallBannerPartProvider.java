package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class LightGrayWallBannerPartProvider extends __base_WallBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            // return Blocks.LIGHT_GRAY_WALL_BANNER;
        //? } else {
            return Blocks.WALL_BANNER.lightGray();
        //? }
    }

    @Override
    protected String getColorName() {
        return "light_gray";
    }
}
