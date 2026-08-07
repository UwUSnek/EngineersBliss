package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;




public class LecternPartProvider extends __base_PartProvider {

    @Override
    public Block getBlock() {
        return Blocks.LECTERN;
    }




    //! Only the book part needs to be made static. Wooden base is already static.
    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final String dirName = getVariantSuffixFromDirection(state.getValue(LecternBlock.FACING));
        return List.of(String.format("lectern/static/book%s", dirName));
    }
    @Override
    public List<String> calcDependencyNames() {
        return List.of("lectern/static/book");
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return
            ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.STATIC_LECTERNS) &&
            state.getValue(LecternBlock.HAS_BOOK).booleanValue()
        ;
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return true;
        //! Always keep Vanilla's static base
    }
}