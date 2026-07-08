package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.ArrayList;
import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;




public class VinesPartProvider extends __base_PartProvider {

    @Override
    public Block getBlock() {
        return Blocks.VINE;
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final boolean suffix) {
        final List<String> r = new ArrayList<>();
        final String root = "vines/3d/block";
        if(state.getValue(VineBlock.NORTH).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_n" : ""));
        if(state.getValue(VineBlock.EAST ).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_e" : ""));
        if(state.getValue(VineBlock.SOUTH).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_s" : ""));
        if(state.getValue(VineBlock.WEST ).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_w" : ""));
        if(state.getValue(VineBlock.UP   ).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_u" : ""));
        //! Vines can't have faces on top sufaces of blocks. No Down part needed.
        return r;
    }


    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.VINES_3D);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}