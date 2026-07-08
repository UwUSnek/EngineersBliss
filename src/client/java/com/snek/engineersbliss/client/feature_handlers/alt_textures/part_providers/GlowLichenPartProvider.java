package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.ArrayList;
import java.util.List;

import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTextureFeature;
import com.snek.engineersbliss.client.feature_handlers.alt_textures.AltTexturesHandler;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;




public class GlowLichenPartProvider extends __base_PartProvider {

    @Override
    public Block getBlock() {
        return Blocks.GLOW_LICHEN;
    }


    @Override
    public List<String> calcPartNames(final BlockState state, final boolean suffix) {
        final List<String> r = new ArrayList<>();
        final String root = "glow_lichen/3d/block";
        if(state.getValue(MultifaceBlock.getFaceProperty(Direction.NORTH)).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_n" : ""));
        if(state.getValue(MultifaceBlock.getFaceProperty(Direction.EAST )).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_e" : ""));
        if(state.getValue(MultifaceBlock.getFaceProperty(Direction.SOUTH)).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_s" : ""));
        if(state.getValue(MultifaceBlock.getFaceProperty(Direction.WEST )).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_w" : ""));
        if(state.getValue(MultifaceBlock.getFaceProperty(Direction.UP   )).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_u" : ""));
        if(state.getValue(MultifaceBlock.getFaceProperty(Direction.DOWN )).booleanValue()) r.add(String.format("%s%s", root, suffix ? "_d" : ""));
        return r;
    }




    @Override
    public boolean shouldUseCustom(final BlockState state) {
        return AltTexturesHandler.getFeature(AltTextureFeature.GLOW_LICHEN_3D);
    }
    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !shouldUseCustom(state);
    }
}