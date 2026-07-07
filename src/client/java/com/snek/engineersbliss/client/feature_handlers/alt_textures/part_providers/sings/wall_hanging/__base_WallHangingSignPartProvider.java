package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.wall_hanging;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.sings.__base_SignPartProvider;

import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;







public abstract class __base_WallHangingSignPartProvider extends __base_SignPartProvider {

    @Override
    public List<String> calcPartNames(final BlockState state) {
        final String materialName = getSignMaterialName();
        final String dirName = getVariantSuffixFromDirection(state.getValue(WallHangingSignBlock.FACING));
        return List.of(
            String.format("%s/hanging_board/%s_0%s",         ROOT, materialName, dirName),
            String.format("%s/hanging_wall_attachment/%s%s", ROOT, materialName, dirName)
        );
    }
}