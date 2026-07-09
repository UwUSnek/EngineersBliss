package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class BlackBannerPartProvider extends __base_StandingBannerPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.BLACK_BANNER;
    }

    @Override
    protected String getColorName() {
        return "black";
    }
}
