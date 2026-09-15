package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.ArrayList;
import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.state.BlockState;




public class WallBannerPartProvider extends __base_BlockSetPartProvider {
    public WallBannerPartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String colorName = getBlockResourceName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(WallBannerBlock.FACING));
        return List.of(
            String.format("banners/static/wall/support%s",       dirName),
            String.format("banners/static/wall/%s%s", colorName, dirName)
        );
    }
    @Override
    public List<String> calcDependencyNames() {
        final String colorName = getBlockResourceName();
        return List.of(
            "banners/static/wall/support",
            String.format("banners/static/wall/%s", colorName)
        );
    }


    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_BANNERS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}