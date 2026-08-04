package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.beds;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;




public abstract class __base_BedPartProvider extends __base_PartProvider {
    protected abstract String getColorName();




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String colorName = getColorName();
        final String partName = state.getValue(BedBlock.PART) == BedPart.FOOT ? "foot" : "head";
        final String dirName = getVariantSuffixFromDirection(state.getValue(BedBlock.FACING));
        return List.of(String.format("beds/vanilla/%s/%s_bed_%s%s", partName, colorName, partName, dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String colorName = getColorName();
        return List.of(
            String.format("beds/vanilla/foot/%s_bed_foot", colorName),
            String.format("beds/vanilla/head/%s_bed_head", colorName)
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
