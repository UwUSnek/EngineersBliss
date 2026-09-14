package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class BlueWallBannerPartProvider extends __base_WallBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.BLUE_WALL_BANNER;
        *///? } else {
            return Blocks.WALL_BANNER.blue();
        //? }
    }

    @Override
    protected String getColorName() {
        return "blue";
    }
}
