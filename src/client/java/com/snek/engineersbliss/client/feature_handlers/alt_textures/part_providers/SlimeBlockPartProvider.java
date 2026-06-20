package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




public class SlimeBlockPartProvider extends __base_PartProvider {

    @Override
    public Block getBlock() {
        return Blocks.SLIME_BLOCK;
    }


    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_SLIME_BLOCK) ?
            List.of("slime_block/transparent/block") :
            null
        ;
    }

    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !AltTexturesHandler.getFeature(AltTextureFeature.TRANSPARENT_SLIME_BLOCK);
    }
}