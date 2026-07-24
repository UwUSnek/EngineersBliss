package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

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
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.UNOBSTRUCTIVE_MANGROVE_ROOTS);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}
