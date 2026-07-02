package com.snek.engineersbliss.client.feature_handlers.alt_textures.part_providers;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

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
    public @Nullable List<String> calcPartNames(final BlockState state) {
        if(AltTexturesHandler.getFeature(AltTextureFeature.GLOW_LICHEN_3D)) {
            final List r = new ArrayList<>();
            final String root = "glow_lichen/3d/block_";
            if(state.getValue(MultifaceBlock.getFaceProperty(Direction.NORTH)).booleanValue()) r.add(root + "n");
            if(state.getValue(MultifaceBlock.getFaceProperty(Direction.EAST )).booleanValue()) r.add(root + "e");
            if(state.getValue(MultifaceBlock.getFaceProperty(Direction.SOUTH)).booleanValue()) r.add(root + "s");
            if(state.getValue(MultifaceBlock.getFaceProperty(Direction.WEST )).booleanValue()) r.add(root + "w");
            if(state.getValue(MultifaceBlock.getFaceProperty(Direction.UP   )).booleanValue()) r.add(root + "u");
            if(state.getValue(MultifaceBlock.getFaceProperty(Direction.DOWN )).booleanValue()) r.add(root + "d");
            return r;
        }
        else return null;
    }


    @Override
    public boolean shouldKeepVanilla(final BlockState state) {
        return !AltTexturesHandler.getFeature(AltTextureFeature.GLOW_LICHEN_3D);
    }
}