package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;




public class MangroveRootsPartProvider extends __base_PartProvider {


    @Override
    public Block getBlock() {
        return Blocks.MANGROVE_ROOTS;
    }




    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        return List.of("mangrove_roots/unobstructive/block" + getSingleVariantSuffix());
    }
    @Override
    public List<String> calcDependencyNames() {
        return List.of("mangrove_roots/unobstructive/block");
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.UNOBSTRUCTIVE_MANGROVE_ROOTS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}
