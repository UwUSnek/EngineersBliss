package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class YellowBannerPartProvider extends __base_StandingBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.YELLOW_BANNER;
        *///? } else {
            return Blocks.BANNER.yellow();
        //? }
    }

    @Override
    protected String getColorName() {
        return "yellow";
    }
}
