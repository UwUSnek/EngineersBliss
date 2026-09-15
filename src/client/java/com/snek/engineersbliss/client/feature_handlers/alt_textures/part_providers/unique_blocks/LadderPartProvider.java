package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.unique_blocks;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_PartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;




public class LadderPartProvider extends __base_PartProvider {
    public LadderPartProvider(Block targetBlock) {
        super(targetBlock);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String dirName = getVariantSuffixFromDirection(state.getValue(LadderBlock.FACING));
        return List.of(String.format("ladder/3d/block%s", dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        return List.of("ladder/3d/block");
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.LADDERS_3D);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}