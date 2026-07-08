package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.ceiling_hanging;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_CeilingHangingSignPartProvider extends __base_SignPartProvider {

    @Override
    public List<String> calcPartNames(final BlockState state, final boolean suffix) {
        final String materialName = getSignMaterialName();
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(CeilingHangingSignBlock.ROTATION), suffix);

        final String chainType = AltTexturesHandler.getFeature(AltTextureFeature.CHAINS_3D) ? "3d" : "vanilla";
        final String attachmentName =
            (state.getValue(CeilingHangingSignBlock.ATTACHED).booleanValue() ? "narrow" : "wide")
            //! Rotation is always 0 when not "ATTACHED" as wide supports only snap to cardinal directions
        ;
        return List.of(
            String.format("hanging_signs/vanilla/board/%s%s",                          materialName, rotName),
            String.format("hanging_signs/%s/ceiling_attachment_%s/all%s", chainType, attachmentName, rotName)
        );
    }
}