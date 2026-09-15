package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.unique_blocks;

import java.util.ArrayList;
import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.ClientFeatureSync;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers.base.__base_PartProvider;
import com.snek.engineersbliss.feature_handlers.alt_textures.AltTexturesServerFeatureSet;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;




public class VinesPartProvider extends __base_PartProvider {
    public VinesPartProvider(Block targetBlock) {
        super(targetBlock);
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final int modelSetIndex) {
        final List<String> r = new ArrayList<>();
        final String root = "vines/3d/block";
        if(state.getValue(VineBlock.NORTH).booleanValue()) r.add(root + "_n");
        if(state.getValue(VineBlock.EAST ).booleanValue()) r.add(root + "_e");
        if(state.getValue(VineBlock.SOUTH).booleanValue()) r.add(root + "_s");
        if(state.getValue(VineBlock.WEST ).booleanValue()) r.add(root + "_w");
        if(state.getValue(VineBlock.UP   ).booleanValue()) r.add(root + "_u");
        //! Vines can't have faces on top sufaces of blocks. No Down part needed.
        return r;
    }
    @Override
    public List<String> calcDependencyNames() {
        return List.of("vines/3d/block");
    }



    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return ClientFeatureSync.getFeatureB(AltTexturesServerFeatureSet.VINES_3D);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}