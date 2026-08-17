package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_StandingSignPartProvider extends __base_SignPartProvider {

    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String materialName = getSignMaterialName();
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(StandingSignBlock.ROTATION));
        return List.of(String.format("signs/static/standing/%s%s", materialName, rotName));
    }
    @Override
    public List<String> calcDependencyNames() {
        final String materialName = getSignMaterialName();
        return List.of(
            String.format("signs/static/standing/%s_0", materialName),
            String.format("signs/static/standing/%s_1", materialName),
            String.format("signs/static/standing/%s_2", materialName),
            String.format("signs/static/standing/%s_3", materialName)
        );
    }
}