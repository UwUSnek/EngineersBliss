package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_WallSignPartProvider extends __base_SignPartProvider {

    @Override
    public List<String> calcPartNames(final BlockState state) {
        final String materialName = getSignMaterialName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(WallSignBlock.FACING));
        return List.of(String.format("signs/vanilla/wall/%s%s", materialName, dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String materialName = getSignMaterialName();
        return List.of("signs/vanilla/wall/" + materialName);
    }
}