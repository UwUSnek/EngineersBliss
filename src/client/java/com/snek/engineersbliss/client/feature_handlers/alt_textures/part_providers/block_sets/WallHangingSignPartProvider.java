package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public class WallHangingSignPartProvider extends __base_BlockSetPartProvider {
    public WallHangingSignPartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    private static final List<String> chainPathForSet = List.of("static", "3d");
    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String materialName = getBlockResourceName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(WallHangingSignBlock.FACING));
        return List.of(
            String.format("hanging_signs/static/board/%s_0%s",                                         materialName, dirName),
            String.format("hanging_signs/%s/wall_attachment/%s%s", chainPathForSet.get(modelSetIndex), materialName, dirName)
        );
    }
    @Override
    public List<String> calcDependencyNames() {
        final String materialName = getBlockResourceName();
        return List.of(
            String.format("hanging_signs/static/board/%s_0",         materialName),
            String.format("hanging_signs/static/wall_attachment/%s", materialName),
            String.format("hanging_signs/3d/wall_attachment/%s",     materialName)
        );
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_SIGNS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
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