package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall_hanging;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_WallHangingSignPartProvider extends __base_SignPartProvider {
    private static final List<String> chainPathForSet = List.of("vanilla", "3d");



    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String materialName = getSignMaterialName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(WallHangingSignBlock.FACING));
        return List.of(
            String.format("hanging_signs/static/board/%s_0%s",                                        materialName, dirName),
            String.format("hanging_signs/%s/wall_attachment/%s%s", chainPathForSet.get(modelSetIndex), materialName, dirName)
        );
    }
    @Override
    public List<String> calcDependencyNames() {
        final String materialName = getSignMaterialName();
        return List.of(
            String.format("hanging_signs/static/board/%s_0",         materialName),
            String.format("hanging_signs/static/wall_attachment/%s", materialName),
            String.format("hanging_signs/3d/wall_attachment/%s",      materialName)
        );
    }




    @Override
    public int getModelSetNumber() {
        return 2;
    }
    @Override
    public int calcCurrentModelSetIndex() {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.CHAINS_3D) ? 1 : 0;
    }
}