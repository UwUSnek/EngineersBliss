package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.wall;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners.__base_BannerPartProvider;

import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_WallBannerPartProvider extends __base_BannerPartProvider {


    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_BANNERS)) {
            final String dirName = getVariantSuffixFromDirection(state.getValue(WallBannerBlock.FACING));
            return List.of("banners/vanilla/wall/" + getColorName() + dirName);
        }
        else {
            return null;
        }
    }
}