package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class YellowBannerPartProvider extends __base_StandingBannerPartProvider {

    @Override
    public Block getBlock() {
        return Blocks.YELLOW_BANNER;
    }

    @Override
    protected String getColorName() {
        return "yellow";
    }
}
