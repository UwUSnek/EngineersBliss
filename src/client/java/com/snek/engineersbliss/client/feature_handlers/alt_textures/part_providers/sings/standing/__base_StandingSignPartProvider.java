package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_StandingSignPartProvider extends __base_SignPartProvider {

    @Override
    public List<String> calcPartNames(final BlockState state, final boolean suffix) {
        final String materialName = getSignMaterialName();
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(StandingSignBlock.ROTATION), suffix);
        return List.of(String.format("%s/standing/%s%s", ROOT, materialName, rotName));
    }
}