package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.copper_golem_statues;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.CopperGolemStatueBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_CopperGolemStatuePartProvider extends __base_PartProvider {

    protected abstract String getGolemName();


    @Override
    public List<String> calcPartNames(final BlockState state) {
        final String dirName = getVariantSuffixFromDirection(state.getValue(CopperGolemStatueBlock.FACING));
        final String poseName = state.getValue(CopperGolemStatueBlock.POSE).getSerializedName();
        return List.of("copper_golem_statues/vanilla/" + poseName + "/" + getGolemName() + dirName);
    }


    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.STATIC_COPPER_GOLEM_STATUES);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}