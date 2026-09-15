package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.block_sets;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_BlockSetPartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;
import com.snek.engineersbliss.utils.block_groups.base.__base_BlockSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public class CeilingHangingSignPartProvider extends __base_BlockSetPartProvider {
    public CeilingHangingSignPartProvider(Block targetBlock, __base_BlockSet blockSet) {
        super(targetBlock, blockSet);
    }


    private static final List<String> chainPathForSet = List.of("static", "3d");
    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String materialName = getBlockResourceName();
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(CeilingHangingSignBlock.ROTATION));
        final String attachmentName =
            (state.getValue(CeilingHangingSignBlock.ATTACHED).booleanValue() ? "narrow" : "wide")
            //! Rotation is always 0 when not "ATTACHED" as wide supports only snap to cardinal directions
        ;
        return List.of(
            String.format("hanging_signs/static/board/%s%s",                                                    materialName, rotName),
            String.format("hanging_signs/%s/ceiling_attachment_%s/all%s", chainPathForSet.get(modelSetIndex), attachmentName, rotName)
        );
    }
    @Override
    public List<String> calcDependencyNames() {
        final String materialName = getBlockResourceName();
        return List.of(
            String.format("hanging_signs/static/board/%s_0", materialName),
            String.format("hanging_signs/static/board/%s_1", materialName),
            String.format("hanging_signs/static/board/%s_2", materialName),
            String.format("hanging_signs/static/board/%s_3", materialName),
            "hanging_signs/static/ceiling_attachment_narrow/all_0",
            "hanging_signs/static/ceiling_attachment_narrow/all_1",
            "hanging_signs/static/ceiling_attachment_narrow/all_2",
            "hanging_signs/static/ceiling_attachment_narrow/all_3",
            "hanging_signs/static/ceiling_attachment_wide/all_0",
            "hanging_signs/3d/ceiling_attachment_narrow/all_0",
            "hanging_signs/3d/ceiling_attachment_narrow/all_1",
            "hanging_signs/3d/ceiling_attachment_narrow/all_2",
            "hanging_signs/3d/ceiling_attachment_narrow/all_3",
            "hanging_signs/3d/ceiling_attachment_wide/all_0"
        );
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_SIGNS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }




    @Override
    public int getModelSetNumber() {
        return 2;
    }
    @Override
    public int calcCurrentModelSetIndex() {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.CHAINS_3D) ? 1 : 0;
    }
}