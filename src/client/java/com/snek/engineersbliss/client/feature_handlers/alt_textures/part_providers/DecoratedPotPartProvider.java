package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.state.BlockState;




public class DecoratedPotPartProvider extends __base_PartProvider {


    @Override
    public Block getBlock() {
        return Blocks.DECORATED_POT;
    }


    @Override
    public List<String> calcPartNames(final BlockState state) {
        final String dirName = getVariantSuffixFromDirection(state.getValue(DecoratedPotBlock.HORIZONTAL_FACING));
        return List.of(String.format("decorated_pot/vanilla/block%s", dirName));
    }


    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.STATIC_DECORATED_POTS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}