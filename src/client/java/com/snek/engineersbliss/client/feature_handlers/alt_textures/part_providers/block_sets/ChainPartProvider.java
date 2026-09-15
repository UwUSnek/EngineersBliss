package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.BlockState;




public class ChainPartProvider extends __base_BlockSetPartProvider {
    public ChainPartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String chainName = getBlockResourceName();
        final String axisName = getVariantSuffixFromAxis(state.getValue(ChainBlock.AXIS));
        return List.of(String.format("chains/3d/%s%s", chainName, axisName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String chainName = getBlockResourceName();
        return List.of(String.format("chains/3d/%s", chainName));
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.CHAINS_3D);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}
