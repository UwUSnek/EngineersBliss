package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.ceiling_hanging;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_CeilingHangingSignPartProvider extends __base_SignPartProvider {

    @Override
    public List<String> calcPartNames(final BlockState state) {
        final String materialName = getSignMaterialName();
        final String rotName = getVariantSuffixFromRotationIndex(state.getValue(CeilingHangingSignBlock.ROTATION));

        final String attachmentName =
            (state.getValue(CeilingHangingSignBlock.ATTACHED).booleanValue() ? "narrow" : "wide") + "/all" + rotName
            //! Rotation is always 0 when not "ATTACHED" as wide supports only snap to cardinal directions
        ;
        return List.of(
            ROOT + "/hanging_board/" + materialName  + rotName,
            ROOT + "/hanging_ceiling_attachment_" + attachmentName
        );
    }
}