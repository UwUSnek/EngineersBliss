package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.standing;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.__base_BannerPartProvider;

import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_StandingBannerPartProvider extends __base_BannerPartProvider {


    @Override
    public List<String> calcPartNames(final BlockState state) {
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(BannerBlock.ROTATION));
        return List.of(
            String.format("banners/vanilla/standing/support%s",            rotName),
            String.format("banners/vanilla/standing/%s%s", getColorName(), rotName)
        );
    }
}