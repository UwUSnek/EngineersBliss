package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.copper_golem_statues;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.CopperGolemStatueBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_CopperGolemStatuePartProvider extends __base_PartProvider {

    protected abstract String getGolemName();




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String golemName = getGolemName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(CopperGolemStatueBlock.FACING));
        final String poseName = state.getValue(CopperGolemStatueBlock.POSE).getSerializedName();
        return List.of(String.format("copper_golem_statues/vanilla/%s/%s%s", poseName, golemName, dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String golemName = getGolemName();
        return List.of(
            String.format("copper_golem_statues/vanilla/standing/%s", golemName),
            String.format("copper_golem_statues/vanilla/sitting/%s", golemName),
            String.format("copper_golem_statues/vanilla/running/%s", golemName),
            String.format("copper_golem_statues/vanilla/star/%s", golemName)
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