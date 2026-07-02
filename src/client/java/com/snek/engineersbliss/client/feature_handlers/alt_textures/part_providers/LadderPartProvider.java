package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

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
    public @Nullable List<String> calcPartNames(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.LADDERS_3D) ?
            List.of("ladder/3d/block" + getVariationSuffixFromDirection(state.getValue(LadderBlock.FACING))) :
            null
        ;
    }

    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !AltTexturesHandler.getFeature(AltTextureFeature.LADDERS_3D);
    }
}