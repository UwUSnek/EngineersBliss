package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;




public class LanternPartProvider extends __base_BlockSetPartProvider {
    public LanternPartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String lanternName = getBlockResourceName();
        final String lanternTypeName = state.getValue(LanternBlock.HANGING).booleanValue() ? "_hanging" : "";
        return List.of(String.format("lanterns/3d/%s%s%s", lanternName, lanternTypeName, getSingleVariantSuffix()));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String lanternName = getBlockResourceName();
        return List.of(
            String.format("lanterns/3d/%s",         lanternName),
            String.format("lanterns/3d/%s_hanging", lanternName)
        );
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