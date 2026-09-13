package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class LightGrayBannerPartProvider extends __base_StandingBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            // return Blocks.LIGHT_GRAY_BANNER;
        //? } else {
            return Blocks.BANNER.lightGray();
        //? }
    }

    @Override
    protected String getColorName() {
        return "light_gray";
    }
}
