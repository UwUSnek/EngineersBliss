package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.banners;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_BannerPartProvider extends __base_PartProvider {
    protected abstract String getColorName();


    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_BANNERS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}
