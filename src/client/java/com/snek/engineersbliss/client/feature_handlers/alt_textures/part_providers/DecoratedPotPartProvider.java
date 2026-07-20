package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.state.BlockState;




public class DecoratedPotPartProvider extends __base_PartProvider {


    @Override
    public Block getBlock() {
        return Blocks.DECORATED_POT;
    }




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String dirName = getVariantSuffixFromDirection(state.getValue(DecoratedPotBlock.HORIZONTAL_FACING));
        return List.of(String.format("decorated_pot/vanilla/block%s", dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        return List.of("decorated_pot/vanilla/block");
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_DECORATED_POTS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}