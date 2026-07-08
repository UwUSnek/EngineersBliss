package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.__base_BannerPartProvider;

import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_WallBannerPartProvider extends __base_BannerPartProvider {


    @Override
    public List<String> calcPartNames(final BlockState state) {
        final String colorName = getColorName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(WallBannerBlock.FACING));
        return List.of(
            String.format("banners/vanilla/wall/support%s",       dirName),
            String.format("banners/vanilla/wall/%s%s", colorName, dirName)
        );
    }
    @Override
    public List<String> calcDependencyNames() {
        final String colorName = getColorName();
        return List.of(
            "banners/vanilla/wall/support",
            String.format("banners/vanilla/wall/%s", colorName)
        );
    }
}