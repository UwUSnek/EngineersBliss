package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CopperGolemStatueBlock;
import net.minecraft.world.level.block.state.BlockState;




public class CopperGolemStatuePartProvider extends __base_BlockSetPartProvider {
    public CopperGolemStatuePartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String golemName = getBlockResourceName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(CopperGolemStatueBlock.FACING));
        final String poseName = state.getValue(CopperGolemStatueBlock.POSE).getSerializedName();
        return List.of(String.format("copper_golem_statues/static/%s/%s%s", poseName, golemName, dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String golemName = getBlockResourceName();
        return List.of(
            String.format("copper_golem_statues/static/standing/%s", golemName),
            String.format("copper_golem_statues/static/sitting/%s", golemName),
            String.format("copper_golem_statues/static/running/%s", golemName),
            String.format("copper_golem_statues/static/star/%s", golemName)
        );
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_COPPER_GOLEM_STATUES);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}