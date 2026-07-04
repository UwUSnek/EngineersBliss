package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.standing;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_StandingSignPartProvider extends __base_SignPartProvider {
//TODO StandingSignBlock.ATTACHED
//TODO idk what this is

    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_SIGNS)) {
            final String materialName = getSignMaterialName();
            final String rotName = getVariantSuffixFromRotationIndex(state.getValue(StandingSignBlock.ROTATION));
            return List.of(ROOT + "/standing/" + materialName + rotName);
        }
        else {
            return null;
        }
    }
}