package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class BlackBannerPartProvider extends __base_StandingBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.BLACK_BANNER;
        *///? } else {
            return Blocks.BANNER.black();
        //? }
    }

    @Override
    protected String getColorName() {
        return "black";
    }
}
