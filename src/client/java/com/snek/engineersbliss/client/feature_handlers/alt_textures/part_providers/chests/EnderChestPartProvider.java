package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.chests;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.__base_PartProvider;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.state.BlockState;




public class EnderChestPartProvider extends __base_PartProvider {


    @Override
    public @Nullable List<String> calcPartNames(final BlockState state) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.STATIC_CHESTS)) {
            final String dirName = getVariantSuffixFromDirection(state.getValue(EnderChestBlock.FACING));
            return List.of("chests/vanilla/single/ender" + dirName);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !AltTexturesHandler.getFeature(AltTextureFeature.STATIC_CHESTS);
    }

    @Override
    public Block getBlock() {
        return Blocks.ENDER_CHEST;
    }
}