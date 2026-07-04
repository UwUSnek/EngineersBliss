package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.ceiling_hanging;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_CeilingHangingSignPartProvider extends __base_SignPartProvider {
//TODO CeilingHangingSignBlock.ATTACHED
//TODO idk what this is

    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_SIGNS)) {
            final String materialName = getSignMaterialName();
            final String rotName = getVariantSuffixFromRotationIndex(state.getValue(CeilingHangingSignBlock.ROTATION));
            return List.of(
                ROOT + "/hanging_board/" + materialName                         + rotName,
                ROOT + "/hanging_ceiling_attachment/hanging_ceiling_attachment" + rotName
            );
        }
        else {
            return null;
        }
    }
}