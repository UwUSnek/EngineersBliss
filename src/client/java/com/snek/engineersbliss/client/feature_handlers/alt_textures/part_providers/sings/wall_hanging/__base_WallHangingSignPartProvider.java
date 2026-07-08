package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall_hanging;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_WallHangingSignPartProvider extends __base_SignPartProvider {

    @Override
    public List<String> calcPartNames(final BlockState state, final boolean suffix) {
        final String materialName = getSignMaterialName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(WallHangingSignBlock.FACING), suffix);
        final String chainType = AltTexturesHandler.getFeature(AltTextureFeature.CHAINS_3D) ? "3d" : "vanilla";
        return List.of(
            String.format("hanging_signs/vanilla/board/%s_0%s",               materialName, dirName),
            String.format("hanging_signs/%s/wall_attachment/%s%s", chainType, materialName, dirName)
        );
    }
}