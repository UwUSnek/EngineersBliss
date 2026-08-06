package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




public class BellBlockPartProvider extends __base_PartProvider {

    @Override
    public Block getBlock() {
        return Blocks.BELL;
    }




    //! Only the bell part needs to be made static. Supports are already static.
    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String dirName = getVariantSuffixFromDirection(state.getValue(BellBlock.FACING));
        return List.of(String.format("bell/static/bell%s", dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        return List.of("bell/static/bell");
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_BELLS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return true;
        //! Always keep Vanilla's static supports
    }
}