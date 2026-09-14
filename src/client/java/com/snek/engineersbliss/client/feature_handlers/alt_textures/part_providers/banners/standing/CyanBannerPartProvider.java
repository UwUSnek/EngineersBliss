package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class CyanBannerPartProvider extends __base_StandingBannerPartProvider {

    @Override
    public Block getBlock() {
        //? if <=26.1.2 {
            return Blocks.CYAN_BANNER;
        //? } else {
            /*return Blocks.BANNER.cyan();
        *///? }
    }

    @Override
    protected String getColorName() {
        return "cyan";
    }
}
