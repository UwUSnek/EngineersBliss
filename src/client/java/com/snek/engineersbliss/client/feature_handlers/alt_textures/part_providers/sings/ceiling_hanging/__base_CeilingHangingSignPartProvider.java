package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.ceiling_hanging;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_CeilingHangingSignPartProvider extends __base_SignPartProvider {
    private static final List<String> chainPathForSet = List.of("vanilla", "3d");




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String materialName = getSignMaterialName();
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(CeilingHangingSignBlock.ROTATION));
        final String attachmentName =
            (state.getValue(CeilingHangingSignBlock.ATTACHED).booleanValue() ? "narrow" : "wide")
            //! Rotation is always 0 when not "ATTACHED" as wide supports only snap to cardinal directions
        ;
        return List.of(
            String.format("hanging_signs/vanilla/board/%s%s",                                                   materialName, rotName),
            String.format("hanging_signs/%s/ceiling_attachment_%s/all%s", chainPathForSet.get(modelSetIndex), attachmentName, rotName)
        );
    }
    @Override
    public List<String> calcDependencyNames() {
        final String materialName = getSignMaterialName();
        return List.of(
            String.format("hanging_signs/vanilla/board/%s_0", materialName),
            String.format("hanging_signs/vanilla/board/%s_1", materialName),
            String.format("hanging_signs/vanilla/board/%s_2", materialName),
            String.format("hanging_signs/vanilla/board/%s_3", materialName),
            "hanging_signs/vanilla/ceiling_attachment_narrow/all_0",
            "hanging_signs/vanilla/ceiling_attachment_narrow/all_1",
            "hanging_signs/vanilla/ceiling_attachment_narrow/all_2",
            "hanging_signs/vanilla/ceiling_attachment_narrow/all_3",
            "hanging_signs/vanilla/ceiling_attachment_wide/all_0",
            "hanging_signs/3d/ceiling_attachment_narrow/all_0",
            "hanging_signs/3d/ceiling_attachment_narrow/all_1",
            "hanging_signs/3d/ceiling_attachment_narrow/all_2",
            "hanging_signs/3d/ceiling_attachment_narrow/all_3",
            "hanging_signs/3d/ceiling_attachment_wide/all_0"
        );
    }




    @Override
    public int getModelSetNumber() {
        return 2;
    }
    @Override
    public int calcCurrentModelSetIndex() {
        return AltTexturesHandler.getFeature(AltTextureFeature.CHAINS_3D) ? 1 : 0;
    }
}