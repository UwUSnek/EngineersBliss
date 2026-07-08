package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;




public class LadderPartProvider extends __base_PartProvider {

    @Override
    public Block getBlock() {
        return Blocks.LADDER;
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final boolean suffix) {
        final String dirName = getVariantSuffixFromDirection(state.getValue(LadderBlock.FACING), suffix);
        return List.of(String.format("ladder/3d/block%s", dirName));
    }


    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.LADDERS_3D);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}