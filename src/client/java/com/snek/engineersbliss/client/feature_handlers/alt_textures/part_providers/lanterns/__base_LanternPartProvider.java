package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.lanterns;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;




public abstract class __base_LanternPartProvider extends __base_PartProvider {

    protected abstract String getLanternName();


    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.CHAINS_3D)) {
            final String lanternTypeName = state.getValue(LanternBlock.HANGING).booleanValue() ? "_hanging" : "";
            return List.of("chains/3d/lanterns/" + getLanternName() + lanternTypeName + "_n");
        }
        else {
            return null;
        }
    }

    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !AltTexturesHandler.getFeature(AltTextureFeature.CHAINS_3D);
    }
}