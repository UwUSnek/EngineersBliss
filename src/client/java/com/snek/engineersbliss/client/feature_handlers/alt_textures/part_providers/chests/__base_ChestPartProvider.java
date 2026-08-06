package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_ChestPartProvider extends __base_PartProvider {

    protected abstract String getChestName();




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String chestName = getChestName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(ChestBlock.FACING));
        return List.of(String.format("chests/static/single/%s%s", chestName, dirName));
        //! __base_DoublableChestPartProvider fully replaces this logic to account for right and left parts
    }
    @Override
    public List<String> calcDependencyNames() {
        final String chestName = getChestName();
        return List.of(
            String.format("chests/static/single/%s", chestName)
            //! Left and right are added by __base_DoublableChestPartProvider
        );
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_CHESTS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}