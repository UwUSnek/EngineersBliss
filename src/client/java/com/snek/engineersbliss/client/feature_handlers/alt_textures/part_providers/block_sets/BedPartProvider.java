package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;




public class BedPartProvider extends __base_BlockSetPartProvider {
    public BedPartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String colorName = getBlockResourceName();
        final String partName = state.getValue(BedBlock.PART) == BedPart.FOOT ? "foot" : "head";
        final String dirName = getVariantSuffixFromDirection(state.getValue(BedBlock.FACING));
        return List.of(String.format("beds/static/%s/%s_bed_%s%s", partName, colorName, partName, dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String colorName = getBlockResourceName();
        return List.of(
            String.format("beds/static/foot/%s_bed_foot", colorName),
            String.format("beds/static/head/%s_bed_head", colorName)
        );
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_BEDS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}
