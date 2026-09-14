package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class LimeWallBannerPartProvider extends __base_WallBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.LIME_WALL_BANNER;
        //? } else {
            /*return Blocks.WALL_BANNER.lime();
        *///? }
    }

    @Override
    protected String getColorName() {
        return "lime";
    }
}
