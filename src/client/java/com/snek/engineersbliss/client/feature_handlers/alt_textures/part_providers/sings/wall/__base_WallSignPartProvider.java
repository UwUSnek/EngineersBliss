package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_WallSignPartProvider extends __base_SignPartProvider {

    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_SIGNS)) {
            final String materialName = getSignMaterialName();
            final String dirName = getVariantSuffixFromDirection(state.getValue(WallSignBlock.FACING));
            return List.of(ROOT + "/wall/" + materialName + dirName);
        }
        else {
            return null;
        }
    }
}