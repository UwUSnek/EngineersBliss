package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chains;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_ChainPartProvider extends __base_PartProvider {

    protected abstract String getChainName();




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String chainName = getChainName();
        final String axisName = getVariantSuffixFromAxis(state.getValue(ChainBlock.AXIS));
        return List.of(String.format("chains/3d/%s%s", chainName, axisName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String chainName = getChainName();
        return List.of(String.format("chains/3d/%s", chainName));
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.CHAINS_3D);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}