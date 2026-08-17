package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.__base_BannerPartProvider;

import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_StandingBannerPartProvider extends __base_BannerPartProvider {


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String colorName = getColorName();
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(BannerBlock.ROTATION));
        return List.of(
            String.format("banners/static/standing/support%s",       rotName),
            String.format("banners/static/standing/%s%s", colorName, rotName)
        );
    }
    @Override
    public List<String> calcDependencyNames() {
        final String colorName = getColorName();
        return List.of(
            "banners/static/standing/support_0",
            "banners/static/standing/support_1",
            "banners/static/standing/support_2",
            "banners/static/standing/support_3",
            String.format("banners/static/standing/%s_0", colorName),
            String.format("banners/static/standing/%s_1", colorName),
            String.format("banners/static/standing/%s_2", colorName),
            String.format("banners/static/standing/%s_3", colorName)
        );
    }
}