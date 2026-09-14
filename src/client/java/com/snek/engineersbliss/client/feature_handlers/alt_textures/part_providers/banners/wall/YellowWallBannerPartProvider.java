package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class YellowWallBannerPartProvider extends __base_WallBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.YELLOW_WALL_BANNER;
        *///? } else {
            return Blocks.WALL_BANNER.yellow();
        //? }
    }

    @Override
    protected String getColorName() {
        return "yellow";
    }
}
