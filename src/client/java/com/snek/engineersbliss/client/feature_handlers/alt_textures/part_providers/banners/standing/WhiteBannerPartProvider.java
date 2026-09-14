package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WhiteBannerPartProvider extends __base_StandingBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            /*return Blocks.WHITE_BANNER;
        *///? } else {
            return Blocks.BANNER.white();
        //? }
    }

    @Override
    protected String getColorName() {
        return "white";
    }
}
