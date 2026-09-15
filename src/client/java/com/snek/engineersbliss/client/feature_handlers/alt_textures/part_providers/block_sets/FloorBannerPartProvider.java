package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;




public class FloorBannerPartProvider extends __base_BlockSetPartProvider {
    public FloorBannerPartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String colorName = getBlockResourceName();
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(BannerBlock.ROTATION));
        return List.of(
            String.format("banners/static/floor/support%s",       rotName),
            String.format("banners/static/floor/%s%s", colorName, rotName)
        );
    }
    @Override
    public List<String> calcDependencyNames() {
        final String colorName = getBlockResourceName();
        return List.of(
            "banners/static/floor/support_0",
            "banners/static/floor/support_1",
            "banners/static/floor/support_2",
            "banners/static/floor/support_3",
            String.format("banners/static/floor/%s_0", colorName),
            String.format("banners/static/floor/%s_1", colorName),
            String.format("banners/static/floor/%s_2", colorName),
            String.format("banners/static/floor/%s_3", colorName)
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