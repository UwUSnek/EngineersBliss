package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall_hanging;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_WallHangingSignPartProvider extends __base_SignPartProvider {

    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_SIGNS)) {
            final String materialName = getSignMaterialName();
            final String dirName = getVariantSuffixFromDirection(state.getValue(WallHangingSignBlock.FACING));
            return List.of(
                ROOT + "/hanging_board/"           + materialName + "_0" + dirName,
                ROOT + "/hanging_wall_attachment/" + materialName +        dirName
            );
        }
        else {
            return null;
        }
    }
}