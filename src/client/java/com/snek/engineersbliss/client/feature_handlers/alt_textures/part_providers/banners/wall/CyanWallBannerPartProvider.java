package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class CyanWallBannerPartProvider extends __base_WallBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.CYAN_WALL_BANNER;
        *///? } else {
            return Blocks.WALL_BANNER.cyan();
        //? }
    }

    @Override
    protected String getColorName() {
        return "cyan";
    }
}
