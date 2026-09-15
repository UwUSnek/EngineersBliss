package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public class FloorStandingSignPartProvider extends __base_BlockSetPartProvider {
    public FloorStandingSignPartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String materialName = getBlockResourceName();
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(StandingSignBlock.ROTATION));
        return List.of(String.format("standing_signs/static/floor/%s%s", materialName, rotName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String materialName = getBlockResourceName();
        return List.of(
            String.format("standing_signs/static/floor/%s_0", materialName),
            String.format("standing_signs/static/floor/%s_1", materialName),
            String.format("standing_signs/static/floor/%s_2", materialName),
            String.format("standing_signs/static/floor/%s_3", materialName)
        );
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_SIGNS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}