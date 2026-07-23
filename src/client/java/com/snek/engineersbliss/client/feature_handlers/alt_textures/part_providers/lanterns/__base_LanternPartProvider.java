package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.lanterns;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_LanternPartProvider extends __base_PartProvider {

    protected abstract String getLanternName();




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String lanternName = getLanternName();
        final String lanternTypeName = state.getValue(LanternBlock.HANGING).booleanValue() ? "_hanging" : "";
        return List.of(String.format("lanterns/3d/%s%s%s", lanternName, lanternTypeName, getSingleVariantSuffix()));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String lanternName = getLanternName();
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