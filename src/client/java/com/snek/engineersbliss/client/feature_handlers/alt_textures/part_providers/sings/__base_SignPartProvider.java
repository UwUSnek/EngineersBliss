package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_SignPartProvider extends __base_PartProvider {
    protected static String ROOT = "signs/vanilla";


    protected abstract String getSignMaterialName();


    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.STATIC_SIGNS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}
