package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;




public class WhiteBannerPartProvider extends __base_StandingBannerPartProvider {

    @Override
    public Block getBlock() {
        BannerRenderer
        return Blocks.WHITE_BANNER;
    }

    @Override
    protected String getColorName() {
        return "white";
    }
}